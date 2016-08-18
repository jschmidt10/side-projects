package com.github.jschmidt10.accumulo

/**
 * The profile is used to determine how often and how much data should be ingested.
 */
trait IngestProfile {
   
  /**
   * Gets the file at the given timestamp.
   */
  def getFile(timestamp: Long): Option[RFile]
}