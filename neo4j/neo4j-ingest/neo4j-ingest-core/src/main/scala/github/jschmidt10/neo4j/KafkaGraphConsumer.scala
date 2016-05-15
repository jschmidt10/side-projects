package github.jschmidt10.neo4j

import java.io.ByteArrayInputStream
import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.Future

import scala.collection.JavaConversions._
import scala.util.Try

import org.apache.kafka.clients.consumer.KafkaConsumer

import github.jschmidt10.neo4j.message.GeneralMessage
import github.jschmidt10.neo4j.message.MessageSerialization
import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.Await
import org.apache.log4j.Logger

/**
 * A graph consumer that reads messages out of kafka.
 */
class KafkaGraphConsumer(serialization: MessageSerialization) extends GraphConsumer {

  private val Log = Logger.getLogger(this.getClass)

  val TopicProp = "kafka.topic"
  val PollProp = "kafka.pollTimeout"

  private var receiverTask: ReceiverTask = null
  private var future: Future[_] = null
  private val executor = Executors.newFixedThreadPool(1)

  private var topic: String = null
  private var pollTimeout: Long = 0L

  private var consumer: KafkaConsumer[String, Array[Byte]] = null
  private var initialized = false

  override def init(properties: Properties) {
    require(properties.containsKey(TopicProp), s"$TopicProp is required for a Kafka consumer")
    require(properties.containsKey(PollProp), s"$PollProp is required for a Kafka consumer")

    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

    topic = properties.getProperty(TopicProp)
    pollTimeout = properties.getProperty(PollProp).toLong

    Log.info(s"Initializing KafkaConsumer for topic ${topic} with poll timeout ${pollTimeout}.")

    consumer = new KafkaConsumer(properties)
    consumer.subscribe(List(topic))

    println("================ Metrics ================")
    consumer.metrics().mapValues(m => println(s"${m.metricName} = ${m.value}"))

    initialized = true
  }

  override def receive(handler: List[GeneralMessage] ⇒ Unit) {
    if (!initialized)
      throw new IllegalStateException("Must initialize the GraphConsumer before using.")

    Log.info("Launching background receiver thread.")
    receiverTask = new ReceiverTask(handler)
    future = executor.submit(receiverTask)
  }

  override def close() {
    Log.info("Stopping background threads and terminating Kafka connection.")
    receiverTask.stop
    Try(executor.shutdown)
    Try(consumer.close)
  }

  /**
   * The Runnable who is responsible for looping and invoking the consumer.
   */
  class ReceiverTask(handler: List[GeneralMessage] ⇒ Unit) extends Runnable {
    private val keepRunning = new AtomicBoolean(true)

    override def run() {
      while (keepRunning.get) {
        consumer
          .poll(pollTimeout)
          .iterator()
          .map(rec => new ByteArrayInputStream(rec.value()))
          .map(input => serialization.read(input))
          .foreach(handler)
      }
    }

    /**
     * Stops this running task.
     */
    def stop() = keepRunning.set(false)
  }
}