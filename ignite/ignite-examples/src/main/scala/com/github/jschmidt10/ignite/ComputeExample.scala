package com.github.jschmidt10.ignite

import java.net.InetAddress

import scala.collection.JavaConverters._

import org.apache.ignite.Ignition
import org.apache.ignite.lang.IgniteCallable

/**
  * Example using the distributed compute of Apache Ignite.
  */
object ComputeExample {
  def main(args: Array[String]): Unit = {
    Ignition.setClientMode(true)

    val conf = this.getClass.getResourceAsStream("/default-ignite.xml")
    val ignite = Ignition.start(conf)

    val allGreetings = ignite.compute().broadcast(new IgniteCallable[String] {
      override def call(): String = s"Howdy from ${InetAddress.getLocalHost.getHostAddress}"
    })

    allGreetings
      .asScala
      .foreach(println)

    ignite.close()
  }
}
