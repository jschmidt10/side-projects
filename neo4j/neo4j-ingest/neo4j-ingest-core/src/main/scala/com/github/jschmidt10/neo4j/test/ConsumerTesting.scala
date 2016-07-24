package com.github.jschmidt10.neo4j.test

import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConversions._
import com.github.jschmidt10.neo4j.message.RelationshipMessage
import com.github.jschmidt10.neo4j.message.NodeMessage
import com.github.jschmidt10.neo4j.message.GeneralMessage
import com.github.jschmidt10.neo4j.message.KryoMessageSerialization
import com.github.jschmidt10.neo4j.KafkaGraphProducer

object ConsumerTesting {
  def main(args: Array[String]) {
    val props = new Properties()

    props.put("kafka.topic", "testing")
    props.put("bootstrap.servers", "192.168.99.100:32770")
    props.put("group.id", "test-client-26")
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    props.put("auto.offset.reset", "earliest")

    val consumer = new KafkaConsumer[String, Array[Byte]](props)
    consumer.subscribe(List("testing"))
    println("Reading from: " + consumer.listTopics())

    val records = consumer.poll(100L)

    records.foreach(r => println(new String(r.value)))
    consumer.close
  }
}