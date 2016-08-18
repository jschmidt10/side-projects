package com.github.jschmidt10.accumulo

/**
  * An observer that will be notified pre and post compactions.
  */
trait CompactionObserver {

  /**
    * An event handler for notification prior to compaction.
    *
    * @param timestamp
    * @param files
    */
  def precompact(timestamp: Long, files: List[RFile]): Unit = ()

  /**
    * An event handler for notification after compaction.
    *
    * @param timestamp
    * @param files
    */
  def postcompact(timestamp: Long, files: List[RFile]): Unit = ()
}
