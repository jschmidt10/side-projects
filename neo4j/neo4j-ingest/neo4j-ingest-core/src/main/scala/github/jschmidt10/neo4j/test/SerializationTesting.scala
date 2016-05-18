package github.jschmidt10.neo4j.test

import github.jschmidt10.neo4j.message.RelationshipMessage
import github.jschmidt10.neo4j.message.NodeMessage
import github.jschmidt10.neo4j.message.KryoMessageSerialization
import java.io.ByteArrayOutputStream

object SerializationTesting {
  def main(args: Array[String]) {
    val messages = List(
      NodeMessage("s1", List("Person"), Map("name" -> "Jeremy")),
      NodeMessage("s3", List("Person"), Map("name" -> "Johnny")),
      RelationshipMessage("s1", "Person", "s3", "Person", "KNOWS", Map("since" -> "2016/02/01")))
      
    val s = new KryoMessageSerialization
    val out = new ByteArrayOutputStream
    
    s.write(out, messages)
    
    println(out.toByteArray.length + " bytes")
  }
}