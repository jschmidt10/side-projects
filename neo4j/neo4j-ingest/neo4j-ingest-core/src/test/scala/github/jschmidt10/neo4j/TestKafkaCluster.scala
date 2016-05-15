package github.jschmidt10.neo4j

import java.util.Properties
import scala.annotation.migration
import scala.collection.JavaConversions.seqAsJavaList
import scala.util.Try
import org.apache.kafka.clients.consumer.KafkaConsumer
import kafka.server.KafkaConfig
import kafka.server.KafkaServer
import kafka.utils.MockTime
import kafka.utils.TestUtils
import kafka.utils.ZkUtils
import kafka.zk.EmbeddedZookeeper
import org.apache.kafka.clients.producer.KafkaProducer
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.consumer.ConsumerConfig

/**
 * An embedded kafka server and zookeeper for use with testing.
 */
class TestKafkaCluster {

  private val Logger = LoggerFactory.getLogger(this.getClass)

  private var zk: EmbeddedZookeeper = null
  private var zkConnect: String = null
  private var server: KafkaServer = null
  private var zkUtils: ZkUtils = null

  lazy val brokerList = zkUtils
    .getAllBrokersInCluster
    .flatMap(b => b.endPoints.values)
    .map(e => s"${e.host}:${e.port}")
    .mkString(",")

  init()

  /**
   * Sets up a single locally running kafka broker.
   */
  def init() {
    Logger.info("Launching ZooKeeper")
    zk = new EmbeddedZookeeper()
    zkConnect = s"localhost:${zk.port}"
    Logger.info(s"Zookeeper started at ${zkConnect}")

    zkUtils = ZkUtils(zkConnect, 60000, 60000, false)

    println("Topics init: " + zkUtils.getAllTopics())
    
    Logger.info("Launching Kafka")
    server = TestUtils.createServer(
      new KafkaConfig(
        TestUtils.createBrokerConfig(0, zkConnect, true)),
      new MockTime)
    Logger.info(s"Started Kafka at ${brokerList}")

    createTopic("__consumer_offsets", 2)
    println("Topics after: " + zkUtils.getAllTopics())
  }

  /**
   * Create a new kafka producer.
   */
  def newProducer() = {
    val props = new Properties()

    props.put("bootstrap.servers", brokerList)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")

    new KafkaProducer[String, Array[Byte]](props)
  }

  /**
   * Creates a new consumer for the given topic using the groupId.
   */
  def newConsumer(groupId: String, topic: String) = {
    val props = new Properties()

    props.put("bootstrap.servers", brokerList)
    props.put("group.id", groupId)
    props.put("enable.auto.commit", "false")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    props.put("auto.offset.reset", "earliest")

    val consumer = new KafkaConsumer[String, Array[Byte]](props)
    consumer.subscribe(List(topic))
    println("Current topics: " + consumer.listTopics()) // this is a hack, not sure why it works
    consumer
  }

  /**
   * Create a new topic on the embedded broker.
   */
  def createTopic(topic: String, partitions: Int) {
    TestUtils.createTopic(zkUtils, topic, partitions, 1, List(server))
  }

  /**
   * Shuts down the kafka broker and zookeeper.
   */
  def stop() {
    Try(zkUtils.close)
    Try(server.shutdown)
    Try(zk.shutdown)
  }
}