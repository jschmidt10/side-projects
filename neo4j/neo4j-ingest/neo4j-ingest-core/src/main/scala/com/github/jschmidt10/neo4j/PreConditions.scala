package com.github.jschmidt10.neo4j

/**
 * Utilities for checking preconditions.
 */
object PreConditions {
  
  /**
   * Requires a String is non-empty.
   */
  def requireNonEmpty(s: String, name: String) {
    require(s != null && s.length > 0, s"${name} must be non-empty.")
  }
  
  /**
   * Requires a list is defined and contains at least one element.
   */
  def requireAtLeastOne(list: List[_], name: String) {
    require(list != null && list.length > 0, s"${name} must contain at least one element.")
  }
}