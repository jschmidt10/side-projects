package github.jschmidt10.neo4j.cypher

import github.jschmidt10.neo4j.message.GeneralMessage
import scala.util.Try

/**
 * Converts a message to a cypher creation statement.
 *
 * Currently uses MERGE with the given nodeId.
 */
object MessageCypher {

  def apply(msg: GeneralMessage): String = if (msg.isNode) node(msg) else relationship(msg)

  private def node(msg: GeneralMessage): String = {
    s"""
      MERGE (n:${msg.nodeLabels.head} {id: "${msg.nodeId}"}) 
      """ + updateStatement(msg)
  }

  private def relationship(msg: GeneralMessage): String = {
    s"""
        MERGE (f:${msg.relFromLabel} {id: "${msg.relFromId}"}) 
        MERGE (t:${msg.relToLabel} {id: "${msg.relToId}"}) 
        MERGE (f)-[n:${msg.relType}]->(t)
      """ + updateStatement(msg)
  }

  private def updateStatement(msg: GeneralMessage): String =
    if (msg.properties.isEmpty) ""
    else
      "SET" + msg.properties.map(e => s" n.${e._1} = ${valueString(e._2)}").mkString(",")

  private def valueString(value: Any): String =
    tryConversions(value,
      List(
        (s) => s.toString.toLong.toString,
        (s) => s.toString.toDouble.toString,
        (s) => s""""${s}""""))

  private def tryConversions(value: Any, conversions: List[(Any => String)]): String = {
    conversions
      .map(c => Try(c(value)))
      .find(ts => ts.isSuccess)
      .getOrElse(throw new IllegalArgumentException(s"Could not convert ${value} into a Cypher value."))
      .get
  }
}