package github.jschmidt10.neo4j

import java.io.ByteArrayOutputStream
import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

import github.jschmidt10.neo4j.message.GeneralMessage
import github.jschmidt10.neo4j.message.MessageSerialization

/**
 * A GraphProducer that is backed by Kafka.
 */
class KafkaGraphProducer(serialization: MessageSerialization) extends GraphProducer {

  val TopicProp = "kafka.topic"
  
  private val Logger = LoggerFactory.getLogger(this.getClass)

  private var producer: KafkaProducer[String, Array[Byte]] = null
  private var topic: String = null
  private var initialized = false

  override def init(properties: Properties) {
    require(properties.containsKey(TopicProp))
    
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")

    producer = new KafkaProducer(properties)
    topic = properties.getProperty(TopicProp)
    initialized = true
  }

  // TODO: Update to only send one graph message per kafka message
  // This will enable us to track how many messages we've ingested
  // vs how many we are processing
  override def send(components: List[GeneralMessage]) {
    if (!initialized)
      throw new IllegalStateException(s"Cannot use ${this.getClass.getSimpleName} without calling init() first.")

    producer.send(
      new ProducerRecord(
        topic,
        toBytes(components)))
  }

  private def toBytes(components: List[GeneralMessage]) = {
    val out = new ByteArrayOutputStream

    try {
      serialization.write(out, components)
      out.toByteArray
    } finally {
      out.close
    }
  }

  override def close() {
    try {
      producer.close
    } catch {
      case ex: Exception => Logger.error("Could not close Kafka producer", ex)
    }
  }
}