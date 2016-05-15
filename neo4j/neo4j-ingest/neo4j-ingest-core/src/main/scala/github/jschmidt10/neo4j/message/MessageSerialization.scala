package github.jschmidt10.neo4j.message

import java.io.OutputStream
import java.io.InputStream
import github.jschmidt10.neo4j.message.GeneralMessage

/**
 * Serialization for sending and receiving messages to the backing queue.
 */
trait MessageSerialization {
  
  /**
   * Serialize graph messages the given output stream.
   */
  def write(out: OutputStream, messages: List[GeneralMessage]): Unit
  
  /**
   * Reads graph messages back from an input stream.
   */
  def read(in: InputStream): List[GeneralMessage]
}