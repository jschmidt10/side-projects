package com.github.jschmidt10.neo4j.message

import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.twitter.chill.ScalaKryoInstantiator

/**
 * Graph Serialization with Kryo.
 */
class KryoMessageSerialization extends MessageSerialization {

  private val kryo = new ScalaKryoInstantiator()
    .setRegistrationRequired(false)
    .newKryo
    
  kryo.register(classOf[NodeMessage])
  kryo.register(classOf[RelationshipMessage])

  override def write(out: OutputStream, messages: List[GeneralMessage]) {
    withResource(new Output(out), (output: Output) => {
      kryo.writeObject(output, messages)
    })
  }

  override def read(in: InputStream): List[GeneralMessage] = {
    withResource(new Input(in), (input: Input) => {
      kryo.readObject(input, classOf[List[GeneralMessage]])
    })
  }

  private def withResource[R <: Closeable, T](resource: R, body: R => T): T =
    try {
      body(resource)
    } finally {
      resource.close()
    }
}