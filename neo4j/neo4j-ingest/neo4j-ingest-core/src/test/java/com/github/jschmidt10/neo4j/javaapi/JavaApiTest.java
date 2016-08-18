package com.github.jschmidt10.neo4j.javaapi;

import com.github.jschmidt10.neo4j.message.GeneralMessage;
import com.github.jschmidt10.neo4j.message.KryoMessageSerialization;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class JavaApiTest {
    @Test
    @Ignore
    public void exampleRun() {
        Map<String, Object> props = new TreeMap<>();
        props.put("since", "2014-01-01");

        NodeMessage nm = new NodeMessage("tommy", Arrays.asList("User"), props);

        KafkaGraphProducer producer = new KafkaGraphProducer(new KryoMessageSerialization());

        producer.init(new Properties());
        producer.send(Arrays.<GeneralMessage>asList(nm));
        producer.close();
    }
}
