package com.github.jschmidt10.accumulo

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.Matchers._

class DefaultCompactionStrategyTest {

  private val compactionStrategy = new DefaultCompactionStrategy(2)

  @Test
  def shouldCompactWhenRatioNotMet() {
    val timestamp = System.currentTimeMillis

    val files = List(
      RFile(timestamp, 1000),
      RFile(timestamp, 1000),
      RFile(timestamp, 1000))

    assertThat(compactionStrategy.shouldCompact(timestamp, files), is(true))
    assertThat(compactionStrategy.compact(timestamp, files), is(List(RFile(timestamp, 3000))))
  }

  @Test
  def shouldCompactSubsetOfFilesWhenRatioMet() {
    val timestamp = System.currentTimeMillis

    val files = List(
      RFile(timestamp, 1000),
      RFile(timestamp, 1000),
      RFile(timestamp, 3100),
      RFile(timestamp, 1000))

    assertThat(compactionStrategy.shouldCompact(timestamp, files), is(true))
    assertThat(compactionStrategy.compact(timestamp, files), is(List(RFile(timestamp, 3100), RFile(timestamp, 3000))))
  }
}