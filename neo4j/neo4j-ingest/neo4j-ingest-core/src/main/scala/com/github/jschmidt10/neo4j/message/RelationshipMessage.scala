package com.github.jschmidt10.neo4j.message

import com.github.jschmidt10.neo4j.PreConditions._

/**
 * A relationship creation request. Must include a relationship type and information
 * for locating the source and destination nodes (id's and one label).
 */
case class RelationshipMessage(
  val fromId: String,
  val fromLabel: String,
  val toId: String,
  val toLabel: String,
  override val relType: String,
  override val properties: Map[String, Any])
    extends GeneralMessage("relationship", null, null, fromId, fromLabel, toId, toLabel, relType, properties) {

  requireNonEmpty(fromId, "From ID")
  requireNonEmpty(fromLabel, "From Label")
  requireNonEmpty(toId, "To ID")
  requireNonEmpty(toLabel, "To Label")
  requireNonEmpty(relType, "Relationship Type")

  require(properties != null, "Properties cannot be null.")
}
