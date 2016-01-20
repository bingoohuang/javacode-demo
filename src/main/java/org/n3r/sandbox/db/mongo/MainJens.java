package org.n3r.sandbox.db.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;
import java.util.Arrays;

public class MainJens {
    public static void main(String[] args) throws UnknownHostException {
        // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));
//        MongoClient mongo = new MongoClient();
        DB db = mongoClient.getDB("yourdb");
        Jongo jongo = new Jongo(db);
        MongoCollection jens = jongo.getCollection("jens");
        jens.insert(new Cat("jens's Cat"));
        mongoClient.close();

    }
}
