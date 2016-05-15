package github.jschmidt10.neo4j.test

import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.driver.v1.AuthTokens
import github.jschmidt10.neo4j.message.NodeMessage
import github.jschmidt10.neo4j.cypher.MessageCypher
import github.jschmidt10.neo4j.message.RelationshipMessage

object Neo4jTesting {
  def main(args: Array[String]) {
    val driver = GraphDatabase.driver("bolt://192.168.99.100:7687", AuthTokens.basic("neo4j", "neo4j123"))
    val session = driver.session()

    val c1 = MessageCypher(NodeMessage("234567890", "Person", Map("name" -> "Tommy", "age" -> 31)))
    val c2 = MessageCypher(NodeMessage("123456789", "Person", Map("name" -> "Tammy", "age" -> 28)))

    val c3 = MessageCypher(
      RelationshipMessage(
        "123456789",
        "Person",
        "234567890",
        "Person",
        "KNOWS",
        Map("since" -> "2015-01-01", "closeness" -> 2)))

    println(c1)
    println(c2)
    println(c3)
    
    session.run(c1)
    session.run(c2)
    session.run(c3)

    session.close
    driver.close
  }
}