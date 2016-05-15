package github.jschmidt10.neo4j

import github.jschmidt10.neo4j.KafkaProducerTest._
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.util.Properties
import org.junit.Assert._
import org.hamcrest.Matchers._
import kafka.utils.TestUtils
import org.apache.kafka.clients.producer.KafkaProducer
import scala.collection.JavaConversions._
import org.apache.kafka.clients.producer.ProducerRecord
import github.jschmidt10.neo4j.message.NodeMessage
import github.jschmidt10.neo4j.message.GeneralMessage
import github.jschmidt10.neo4j.message.RelationshipMessage
import org.junit.Ignore

class KafkaProducerTest {

  val props = new Properties

  props.put("kafka.topic", Topic)
  props.put("bootstrap.servers", Kafka.brokerList)

  val messages = List[GeneralMessage](
    NodeMessage("s1", List("Person", "Boy"), Map("age" -> 19, "name" -> "Tommy")),
    NodeMessage("s2", List("Person", "Girl"), Map("age" -> 21, "name" -> "Tina")),
    RelationshipMessage("s1", "Person", "s2", "Person", "KNOWS", Map("since" -> "2016/01/01")))

  @Test
  @Ignore
  def shouldSendAllMessages() {

    //    val producer = new KafkaGraphProducer(new KryoGraphSerialization)
    //
    //    producer.init(props)
    //    producer.send(messages)
    //    producer.close

    val producer = Kafka.newProducer()
    producer.send(new ProducerRecord(Topic, "Message 1".getBytes))
    producer.send(new ProducerRecord(Topic, "Message 2".getBytes))
    producer.send(new ProducerRecord(Topic, "Message 3".getBytes))

    producer.close()

    val consumer = Kafka.newConsumer("unit-test-1", Topic)

    consumer.commitSync()

    println("Starting Poll")
    val records = consumer
      .poll(100L)
      .records(Topic)
      .iterator
      .toList
    println("Ending Poll")

    consumer.close

    //    assertThat(records.size, is(3))
  }
}

object KafkaProducerTest {

  val Topic = "test-topic"
  val Partitions = 2

  var Kafka: TestKafkaCluster = null

  @BeforeClass
  def setupAll() {
    Kafka = new TestKafkaCluster()
    Kafka.createTopic(Topic, Partitions)
  }

  @AfterClass
  def tearDownAll() {
    Kafka.stop()
  }
}