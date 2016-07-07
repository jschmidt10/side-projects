package com.github.jschmidt10.accumulo

import scala.collection.mutable.ListBuffer

/**
 * The default accumulo strategy based on file size ratios.
 */
class DefaultCompactionStrategy(ratio: Double) extends CompactionStrategy {

  override def shouldCompact(timestamp: Long, files: List[RFile]): Boolean =
    if (files.length < 2) false
    else {
      val sorted = descendingSize(files)
      !getFilesUnderRatio(sorted).isEmpty
    }

  override def compact(timestamp: Long, files: List[RFile]): List[RFile] =
    if (files.length < 2) files
    else {
      val sorted = descendingSize(files)
      val toCombine = getFilesUnderRatio(sorted)

      val compacted = ListBuffer[RFile]()
      compacted ++= sorted.filter(!toCombine.contains(_))
      compacted += toCombine.reduce(_.combine(timestamp, _))

      compacted.toList
    }

  private def getFilesUnderRatio(sortedFiles: List[RFile]): List[RFile] =
    if (sortedFiles.isEmpty) Nil
    else {
      val totalSize = sortedFiles.map(_.size).sum

      if (sortedFiles.head.size * ratio < totalSize)
        sortedFiles
      else
        getFilesUnderRatio(sortedFiles.tail)
    }

  private def descendingSize(files: List[RFile]) = files.sortWith((r1, r2) => r1.size.compare(r2.size) > 0)
}