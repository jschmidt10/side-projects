package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class SparkExecutor implements StageExecutor {

    @Override
    public <IN, OUT> void execute(Stage<IN, OUT> stage, ReaderFactory<IN> readerFactory, WriterFactory<OUT> writerFactory) throws Exception {
        SparkConf conf = new SparkConf().setAppName("Spark Executor: " + stage.getName()).setMaster("local[1]").set("spark.executor.instances", "1");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            convertToRdd(sc, readerFactory).foreachPartition(partitionIter -> {
                try (Writer<OUT> writer = writerFactory.newWriter()) {
                    stage.process(partitionIter, writer);
                }
            });
        }
        finally {
            sc.stop();
        }
    }

    private <IN> JavaRDD<MultiPair<IN>> convertToRdd(JavaSparkContext sc, ReaderFactory<IN> readerFactory) throws Exception {
        return sc.parallelize(readIntoList(readerFactory));
    }

    private <IN> List<MultiPair<IN>> readIntoList(ReaderFactory<IN> readerFactory) throws Exception {
        List<MultiPair<IN>> list = new ArrayList<>();

        try (Reader<IN> reader = readerFactory.newReader()) {
            for (MultiPair<IN> multiPair : reader) {
                list.add(multiPair);
            }

            return list;
        }
    }
}
