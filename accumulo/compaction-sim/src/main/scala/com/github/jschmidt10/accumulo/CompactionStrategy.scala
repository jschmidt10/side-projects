package com.github.jschmidt10.accumulo

/**
 * A slightly lighter weight interface than the Accumulo counterpart.
 *
 * Determines if we should compact and if so, how we compact.
 */
trait CompactionStrategy {

  /**
   * Determine if compaction should be attempted.
   */
  def shouldCompact(timestamp: Long, files: List[RFile]): Boolean

  /**
   * Runs compaction.
   */
  def compact(timestamp: Long, files: List[RFile]): List[RFile]
}