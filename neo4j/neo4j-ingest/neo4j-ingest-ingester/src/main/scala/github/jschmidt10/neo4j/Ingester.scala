package github.jschmidt10.neo4j

import java.io.Closeable
import scala.util.Try
import org.apache.log4j.Logger
import org.neo4j.driver.v1.Driver
import com.beust.jcommander.JCommander
import github.jschmidt10.neo4j.cypher.MessageCypher
import github.jschmidt10.neo4j.message.GeneralMessage
import com.beust.jcommander.ParameterException

/**
 * Ingests data into Neo4j using the configured consumer.
 */

class Ingester(consumer: GraphConsumer, driver: Driver) extends Closeable {

  private val Log = Logger.getLogger(this.getClass)

  private val session = driver.session

  /**
   * Starts the ingester.
   */
  def start() {
    Log.info("Starting consumer")
    consumer.receive(processMessages)
  }

  /**
   * Process a batch of messages.
   */
  def processMessages(messages: List[GeneralMessage]) {
    Log.info(s"Ingesting ${messages.size} messages")
    val tx = session.beginTransaction

    try {
      messages
        .map(MessageCypher.apply)
        .foreach(tx.run)

      tx.success
      Log.info("Committed messages")

    } catch {
      case ex: Exception => {
        Log.error("Could not commit transaction", ex)
        tx.failure
      }
    }

    tx.close
  }

  override def close() = {
    Log.info("Closing")
    Try(session.close)
    Try(driver.close)
    Try(consumer.close)
  }
}
object Ingester {
  def main(args: Array[String]) {
    val initializer = new IngesterInitializer
    val jc = JCommanderFactory.newInstance(initializer)

    if (args.length == 0) {
      jc.usage()
      System.exit(1)
    }

    try {
      jc.parse(args: _*)
    } catch {
      case ex: ParameterException => {
        jc.usage()
        System.exit(1)
      }
    }

    val ingester = initializer.createIngester

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable() {
      override def run() {
        ingester.close
      }
    }))

    ingester.start
  }
}