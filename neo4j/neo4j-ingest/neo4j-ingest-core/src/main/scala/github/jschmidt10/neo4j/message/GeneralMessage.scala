package github.jschmidt10.neo4j.message

import github.jschmidt10.neo4j.message.GeneralMessage._

/**
 * A general graph message that represents a node or a relationship.
 */
class GeneralMessage(
    val msgType: String,
    val nodeId: String,
    val nodeLabels: List[String],
    val relFromId: String,
    val relFromLabel: String,
    val relToId: String,
    val relToLabel: String,
    val relType: String,
    val properties: Map[String, Any]) {

  require(NodeType.equals(msgType) || RelType.equals(msgType), s"Message type must be either ${NodeType} or ${RelType}.")
  
  def isNode() = NodeType.equals(msgType)
  
  def isRelationship() = RelType.equals(msgType)
}

object GeneralMessage {
  val NodeType = "node"
  val RelType = "relationship"
}