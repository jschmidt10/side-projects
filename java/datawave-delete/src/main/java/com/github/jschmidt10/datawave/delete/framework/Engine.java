package com.github.jschmidt10.datawave.delete.framework;

import com.github.jschmidt10.datawave.delete.impl.*;

import java.util.ArrayList;
import java.util.Collection;

public class Engine {

    private StageExecutor stageExecutor = new SparkExecutor();

    public void run(DeleteJob job) throws Exception {
        StageOne stageOne = new StageOne();
        FileBucket<Integer> idLengthBucket = new FileBucket<>();

        stageExecutor.execute(stageOne, new RequestReaderFactory<>(job), idLengthBucket);

        StageTwo stageTwo = new StageTwo();
        FileBucket<Integer> doubledBucket = new FileBucket<>();

        stageExecutor.execute(stageTwo, idLengthBucket, doubledBucket);

        System.out.println(doubledBucket);
    }

    public static void main(String[] args) throws Exception {
        Collection<DeleteRequest> requests = new ArrayList<>();
        requests.add(new DeleteRequest("FIELD", "1"));
        requests.add(new DeleteRequest("FIELD", "11"));
        requests.add(new DeleteRequest("FIELD", "111"));

        DeleteJob job = new DeleteJob(requests);

        new Engine().run(job);
    }
}
