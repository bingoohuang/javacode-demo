package org.n3r.sandbox.db.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

public class MongoDbDemo {
    public static void main(String[] args) throws UnknownHostException {

        // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));
//        MongoClient mongoClient = new MongoClient();


        DB db = mongoClient.getDB("mydb");
        DBCollection coll = db.getCollection("testCollection");
//        BasicDBObject doc = new BasicDBObject("name", "MongoDB").
//                append("type", "database").
//                append("count", 1).
//                append("info", new BasicDBObject("x", 203).append("y", 102));
//
//        WriteResult insert = coll.insert(doc);

//        for (int i=0; i < 100; i++) {
//            coll.insert(new BasicDBObject("i", i));
//        }

        System.out.println(coll.getCount());

        BasicDBObject query = new BasicDBObject("i", //new BasicDBObject("$gt", 90));
                new BasicDBObject("$gt", 20).append("$lte", 30));

        DBCursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
//
//
//        query = new BasicDBObject("i", 71);
//
//         cursor = coll.find(query);
//
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
//            }
//        } finally {
//            cursor.close();
//        }
//
//        cursor = coll.find();
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
//            }
//        } finally {
//            cursor.close();
//        }
//
//
//        DBObject myDoc = coll.findOne();
//        System.out.println(myDoc);
//
//
//        Set<String> colls = db.getCollectionNames();
//
//        for (String s : colls) {
//            System.out.println(s);
//        }

        mongoClient.close();
    }
}
