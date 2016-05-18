Neo4j Ingest
------------

This project is a stand-alone process for reading data from Kafka and inserting into Neo4j. The intention is to make a single process for ingesting to limit locking during inserts.

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


