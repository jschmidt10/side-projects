package com.github.jschmidt10.accumulo

import org.junit.Test

class CompactionSimTest {

  private val debugObserver = new CompactionObserver() {
    override def postcompact(timestamp: Long, files: List[RFile]): Unit = {
      println(
        s"""
           |Timestamp: $timestamp
           |${files.mkString("\n")}""".stripMargin)
    }
  }

  @Test
  def exampleRun() {
    val sim = new CompactionSim(new ConstantIngestProfile(100, 100), new DefaultCompactionStrategy(3))
    sim.addObserver(debugObserver)
    sim.run(0, 10000)
  }
}