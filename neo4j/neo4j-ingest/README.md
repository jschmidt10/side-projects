Neo4j Ingest
------------

This project is a stand-alone process for reading data from Kafka and inserting into Neo4j. The intention is to make a single process for ingesting to limit locking during inserts.

#### Running the ingester

The neo4j-ingest-ingester is the executable jar. To build it run the following:

    $> cd neo4j-ingest-ingester
    $> mvn clean install 

Then you can launch the neo4j-ingest-ingester-1.0.0-SNAPSHOT-jar-with-dependencies.jar.

Run it without arguments to get a full listing of all available command line options. Below is an example run:

    java -jar neo4j-ingest-ingester-1.0.0-SNAPSHOT-jar-with-dependencies.jar -n bolt://localhost:7687 -cp kafka-consumer.properties
    
Here are some sample kafka consumer properties to get you started. All of the configured properties will be passed on to the underlying KafkaConsumer. Additionally, you must supply a 'kafka.topic' and 'kafka.pollTimeout'.

    kafka.topic:graph-testing-1
    kafka.pollTimeout:200
    bootstrap.servers:192.168.99.100:9092
    group.id:ingester-1
    enable.auto.commit:true
    auto.commit.interval.ms:1000
    auto.offset.reset:earliest

#### Creating a client (producer)

You will need to instantiate a KafkaGraphProducer. Once you have that created, you can send NodeMessage's or RelationshipMessage's. Below is an example:

    val serialization = new KryoMessageSerialization() // our default serialization
    val producer = new KafkaGraphProducer(serialization)
    producer.init(producerProps)
    
    producer.send(
        List(
            NodeMessage(
                "jschmidt10",         // node surrogate id (not internal neo4j id)
                "GithubUser",         // node label
                Map("name" -> "Jeff") // node attributes
            ),
            NodeMessage("side-projects", "GithubRepo", Map("created" -> "2016-05-01")),
            RelationshipMessage(
                "jschmidt10",          // from node surrogate id
                "GithubUser",          // from node label
                "side-projects",       // to node surrogate id
                "GithubRepo",          // to node label
                "COMMITS_TO",          // relationship type
                Map("role" -> "owner") // relationship attributes
            )))
    
Again, you can configure any Kafka producer properties and they will be passed along. Here's a minimum configuration.

    kafka.topic:graph-testing-1
    bootstrap.servers:192.168.99.100:9092
