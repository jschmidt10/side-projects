package github.jschmidt10.neo4j

import java.io.Closeable
import java.util.Properties
import github.jschmidt10.neo4j.message.GeneralMessage

/**
 * A producer API for sending graph components to neo4j ingest.
 */
trait GraphProducer extends Closeable {

  /**
   * Initialize the producer with any user configuration.
   */
  def init(properties: Properties): Unit
  
  /**
   * Send components to the backing queue.
   */
  def send(components: List[GeneralMessage]): Unit
}