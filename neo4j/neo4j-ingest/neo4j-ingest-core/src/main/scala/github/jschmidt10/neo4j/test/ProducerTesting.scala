package github.jschmidt10.neo4j.test

import github.jschmidt10.neo4j.message.KryoMessageSerialization
import github.jschmidt10.neo4j.message.RelationshipMessage
import github.jschmidt10.neo4j.message.NodeMessage
import github.jschmidt10.neo4j.KafkaGraphProducer
import java.util.Properties

object ProducerTesting {
  def main(args: Array[String]) {
    val broker = "192.168.99.100:32770"
    val groupId = "testGroup1"

    val properties = new Properties
    
    properties.put("kafka.topic", "graph-testing-1")
    properties.put("bootstrap.servers", broker)
    properties.put("group.id", groupId)
    properties.put("enable.auto.commit", "true")
    properties.put("auto.commit.interval.ms", "1000")
    properties.put("auto.offset.reset", "earliest")

    val serialization = new KryoMessageSerialization()
    val producer = new KafkaGraphProducer(serialization)
    producer.init(properties)

    producer.send(List(
      NodeMessage("s3", List("Person"), Map("name" -> "Johnny")),
      RelationshipMessage("s1", "Person", "s3", "Person", "KNOWS", Map("since" -> "2016/02/01"))))

    producer.close
    println("Done")
  }
}