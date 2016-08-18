package com.github.jschmidt10.accumulo

import scala.concurrent.duration.Duration

/**
 * An IngestProfile where the same volume of data is generated at a given rate.
 */
class ConstantIngestProfile(bytes: Long, timestep: Long) extends IngestProfile {

  // ensure that we write a file at time 0
  private var lastWrittenTime = -2 * timestep

  override def getFile(timestamp: Long) = {
    if (timestamp - lastWrittenTime >= timestep) {
      lastWrittenTime = timestamp
      Some(RFile(timestamp, bytes))
    } else {
      None
    }
  }
}