package com.github.jschmidt10.neo4j

import org.neo4j.driver.v1.AuthTokens
import org.neo4j.driver.v1.GraphDatabase
import com.beust.jcommander.Parameter
import java.util.Properties
import java.io.FileInputStream
import com.github.jschmidt10.neo4j.message.KryoMessageSerialization
import com.github.jschmidt10.neo4j.message.MessageSerialization
import com.github.jschmidt10.neo4j.message.KryoMessageSerialization
import org.neo4j.driver.v1.AuthToken
import org.apache.commons.lang3.StringUtils

/**
  * Receives command line arguments and creates the Ingester.
  */
class IngesterInitializer {

  @Parameter(names = Array("-n", "--neo4jBoltUrl"), description = "Neo4J Driver URL (example: \"bolt://localhost:7687\")", required = true)
  var neo4jUrl: String = ""

  // TODO: Authentication needs to be handled somewhere else
  @Parameter(names = Array("-nu", "--neo4jUser"), description = "Neo4J username", required = false)
  var neo4jUser: String = ""

  @Parameter(names = Array("-np", "--neo4jPassword"), description = "Neo4J password", required = false)
  var neo4jPassword: String = ""

  @Parameter(names = Array("-c", "--consumer"), description = "Fully qualified consumer class to use.", required = false)
  var consumerClass: String = classOf[KafkaGraphConsumer].getName

  @Parameter(names = Array("-cp", "--consumerProps"), description = "Properties file that will be injected into the consumer", required = true)
  var consumerProps: String = ""

  @Parameter(names = Array("-s", "--serialization"), description = "Fully qualified serialization class to use.", required = false)
  var serializationClass: String = classOf[KryoMessageSerialization].getName

  /**
    * Uses the provided parameters to create the Ingester.
    */
  def createIngester(): Ingester = {
    val authToken = if (StringUtils.isBlank(neo4jUser) || StringUtils.isBlank(neo4jPassword))
      AuthTokens.none()
    else
      AuthTokens.basic(neo4jUser, neo4jPassword)

    val driver = GraphDatabase.driver(neo4jUrl, authToken)
    val consumer = createConsumer(createSerialization)

    new Ingester(consumer, driver)
  }

  private def createSerialization(): MessageSerialization = {
    try {
      val clazz = Class.forName(serializationClass).asInstanceOf[Class[_ <: MessageSerialization]]
      clazz.newInstance

    } catch {
      case ex: Exception =>
        throw new IllegalArgumentException(s"Could not instantiate MessageSerialization: ${serializationClass}")
    }
  }

  private def createConsumer(serialization: MessageSerialization): GraphConsumer = {
    var consumer: GraphConsumer = instantiateConsumer(consumerClass, serialization)

    val props = new Properties()
    props.load(new FileInputStream(consumerProps))

    consumer.init(props)

    consumer
  }

  private def instantiateConsumer(className: String, serialization: MessageSerialization): GraphConsumer = {
    try {
      val clazz = Class.forName(className).asInstanceOf[Class[_ <: GraphConsumer]]
      val constructor = clazz
        .getConstructors
        .find(c => c.getParameterCount == 1)
        .getOrElse(throw new IllegalArgumentException(s"Could not find constructor for $className that takes a GraphSerialization parameter."))

      constructor.newInstance(serialization).asInstanceOf[GraphConsumer]
    } catch {
      case ex: IllegalArgumentException => throw ex
      case ex: Exception => {
        throw new IllegalArgumentException(s"Invalid GraphConsumer implementation: ${className}", ex)
      }
    }
  }
}