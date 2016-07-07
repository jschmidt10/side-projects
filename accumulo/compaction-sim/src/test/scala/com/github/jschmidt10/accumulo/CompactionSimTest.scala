package com.github.jschmidt10.accumulo

import org.junit.Test

class CompactionSimTest {

  @Test
  def exampleRun() {
    val sim = new CompactionSim(new ConstantIngestProfile(100, 100), new DefaultCompactionStrategy(3))
    sim.run(10000, ((timestamp, files) => {
      println("TIMESTAMP: " + timestamp)
      files.foreach(println)
      println("-------------")
    }))
  }
}