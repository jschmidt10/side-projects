package com.github.jschmidt10.neo4j.javaapi

import com.github.jschmidt10.neo4j.message.{NodeMessage => ScalaNodeMessage}
import java.util.{Collections, List => JList, Map => JMap}

import scala.collection.JavaConverters._

/**
  * Java version of {@link com.github.jschmidt10.message.NodeMessage}.
  */
class NodeMessage(id: String, labels: JList[String], properties: JMap[String, Any]) extends ScalaNodeMessage(id, labels.asScala.toList, properties.asScala.toMap) {
  def this(id: String, labels: JList[String]) = this(id, labels, Collections.emptyMap())
}
