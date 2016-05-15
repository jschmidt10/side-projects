Neo4j Ingest
------------

This project is a stand-alone process for reading data from Kafka and inserting into Neo4j. The intention is to make a single process for ingesting to limit locking during inserts.

The neo4j-ingest-ingester is the executable jar. To build it run the following:

$> cd neo4j-ingest-ingester
$> mvn clean install assembly:single
