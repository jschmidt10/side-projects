package com.github.jschmidt10.neo4j.javaapi

import com.github.jschmidt10.neo4j.message.{GeneralMessage, MessageSerialization}
import com.github.jschmidt10.neo4j.{KafkaGraphProducer => ScalaKafkaGraphProducer}
import java.util.{List => JList}
import scala.collection.JavaConverters._

/**
  * Java friendly version of {@link ScalaKafkaGraphProducer}
  */
class KafkaGraphProducer(serialization: MessageSerialization) extends ScalaKafkaGraphProducer(serialization) {

  /**
    * Java friendly command to send messages to kafka.
    *
    * @param components
    */
  def send(components: JList[GeneralMessage]): Unit = this.send(components.asScala.toList)
}
