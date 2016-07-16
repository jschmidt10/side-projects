package com.github.jschmidt10.neo4j.message

import com.github.jschmidt10.neo4j.PreConditions._

/**
 * An node creation request that will be sent to the ingest service.
 *
 * Each node must have a surrogateId and one or more labels.
 */
case class NodeMessage(
  val id: String,
  val labels: List[String],
  override val properties: Map[String, Any])
    extends GeneralMessage("node", id, labels, null, null, null, null, null, properties) {

  requireNonEmpty(id, "ID")
  requireAtLeastOne(labels, "Labels")
  require(properties != null, "Properties cannot be null.")
}
object NodeMessage {
  def apply(id: String, label: String, properties: Map[String, Any]): NodeMessage = NodeMessage(id, List(label), properties)
}