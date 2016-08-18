package com.github.jschmidt10.accumulo

import java.text.SimpleDateFormat

import scala.collection.mutable.ListBuffer

/**
  * The simulator for testing a compaction strategy. Requires an ingest profile and the strategy to test.
  */
class CompactionSim(profile: IngestProfile, strategy: CompactionStrategy) {

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  private val compactionObservers = ListBuffer[CompactionObserver]()

  /**
    * Adds a new observer to the simulator
    *
    * @param observer
    */
  def addObserver(observer: CompactionObserver): Unit = compactionObservers += observer

  /**
    * Runs the simulator for the given time.
    *
    * @param begenStr begin date in the format yyyy-MM-dd HH:mm:ss
    * @param endStr   end date in the format yyyy-MM-dd HH:mm:ss
    */
  def run(begenStr: String, endStr: String): Unit = {
    run(dateFormat.parse(begenStr).getTime, dateFormat.parse(endStr).getTime)
  }

  /**
    * Runs the simulator for the given time.
    *
    * @param begin begin timestamp
    * @param end   end timestamp
    */
  def run(begin: Long, end: Long) = {
    var rfiles = List[RFile]()

    for {
      timestamp <- begin to end
      rfile <- profile.getFile(timestamp)
    } {
      rfiles = rfile :: rfiles

      if (strategy.shouldCompact(timestamp, rfiles)) {
        compactionObservers.foreach(_.precompact(timestamp, rfiles))
        rfiles = strategy.compact(timestamp, rfiles)
        compactionObservers.foreach(_.postcompact(timestamp, rfiles))
      }
    }
  }
}