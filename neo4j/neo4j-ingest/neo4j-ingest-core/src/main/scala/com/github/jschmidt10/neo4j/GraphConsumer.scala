package com.github.jschmidt10.neo4j

import java.io.Closeable
import java.util.Properties

import com.github.jschmidt10.neo4j.message.GeneralMessage

/**
 * A consumer API for receiving graph components.
 */
trait GraphConsumer extends Closeable {

  /**
   * Initialize the consumer with any user configuration.
   */
  def init(properties: Properties): Unit
  
  /**
   * Send components to the backing queue.
   */
  def receive(handler: List[GeneralMessage] => Unit): Unit
}