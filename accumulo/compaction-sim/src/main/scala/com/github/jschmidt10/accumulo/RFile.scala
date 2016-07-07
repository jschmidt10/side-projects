package com.github.jschmidt10.accumulo

import java.util.Comparator

/**
 * An Accumulo RFile.
 */
case class RFile(val timestamp: Long, val size: Long) {

  /**
   * Combines this RFile with another.
   */
  def combine(timestamp: Long, other: RFile): RFile = RFile(timestamp, size + other.size)
  
  override def toString() = s"RFile(timestamp=$timestamp,size=$size)"
}