package com.github.jschmidt10.accumulo

import scala.collection.mutable.ListBuffer

/**
 * The simulator for testing a compaction strategy. Requires an ingest profile and the strategy to test.
 */
class CompactionSim(profile: IngestProfile, strategy: CompactionStrategy) {

  /**
   * Runs the simulator for the given time. The result handler will be called
   * after each compaction (requires that the files are actually modified).
   */
  def run(timeToRun: Long, resultHandler: ((Long, List[RFile]) => Unit)) = {
    var rfiles = List[RFile]()

    for {
      timestamp <- 0L to timeToRun
      rfile <- profile.getFile(timestamp)
    } {
      rfiles = rfile :: rfiles

      if (strategy.shouldCompact(timestamp, rfiles)) {
        val newFiles = strategy.compact(timestamp, rfiles)
        if (rfiles != newFiles) {
          rfiles = newFiles
          resultHandler(timestamp, newFiles)
        }
      }
    }
  }
}