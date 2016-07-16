package com.github.jschmidt10.neo4j.javaapi

import com.github.jschmidt10.neo4j.message.{RelationshipMessage => ScalaRelationshipMessage}
import java.util.{Map => JMap}

import scala.collection.JavaConverters._

/**
  * Java friendly version of {@link ScalaRelationshipMessage}
  */
class RelationshipMessage(fromId: String,
                          fromLabel: String,
                          toId: String,
                          toLabel: String,
                          relType: String,
                          properties: JMap[String, Any]) extends ScalaRelationshipMessage(fromId, fromLabel, toId, toLabel, relType, properties.asScala.toMap) {

}
