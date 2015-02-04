package com.flyingh;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class Demo {

    @Test
    public void test2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CouchbaseCluster cluster = CouchbaseCluster.create();
        final Bucket bucket = cluster.openBucket();
        bucket.async().get("user").flatMap(document -> {
            System.out.println(document);
            document.content().put("name", "haha").put("age", 27);
            return bucket.async().replace(document);
        }).subscribe(document -> {
            System.out.println(document);
            latch.countDown();
        });
        latch.await();
        cluster.disconnect();
    }

    @Test
    public void test() {
        CouchbaseCluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket();
        bucket.upsert(JsonDocument.create("user", JsonObject.empty().put("id", 1).put("name", "flyingh").put("age", 22)));
        JsonDocument document = bucket.get("user");
        System.out.println(document);
        document.content().put("name", "flycoding");
        document = bucket.replace(document);
        System.out.println(document);
        cluster.disconnect();
    }
}
