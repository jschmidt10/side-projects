package github.jschmidt10.neo4j

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.hamcrest.Matchers.is
import org.junit.Assert.assertThat
import org.junit.Test
import github.jschmidt10.neo4j.message.KryoMessageSerialization
import github.jschmidt10.neo4j.message.RelationshipMessage
import github.jschmidt10.neo4j.message.NodeMessage
import github.jschmidt10.neo4j.message.GeneralMessage

class KryoGraphSerializationTest {

  private val serialization = new KryoMessageSerialization()

  @Test
  def shouldDeserializeBackToOriginal() {

    val baos = new ByteArrayOutputStream
    
    val messages = List[GeneralMessage](
      NodeMessage("s1", List("Person", "Boy"), Map("age" -> 19, "name" -> "Tommy")),
      NodeMessage("s2", List("Person", "Girl"), Map("age" -> 21, "name" -> "Tina")),
      RelationshipMessage("s1", "Person", "s2", "Person", "KNOWS", Map("since" -> "2016/01/01")))

    serialization.write(baos, messages)

    val copies = serialization.read(new ByteArrayInputStream(baos.toByteArray))

    assertThat(copies, is(messages))
  }
}