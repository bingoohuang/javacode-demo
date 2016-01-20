package org.n3r.sandbox.db.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.bson.LazyDBList;
import org.bson.types.ObjectId;
import org.ingini.monogo.testbed.MongoManager;
import org.ingini.monogo.testbed.annotation.MongoTestBedCollection;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.ResultHandler;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.n3r.sandbox.db.mongo.model.heroes.Gender;
import org.n3r.sandbox.db.mongo.model.heroes.Hero;
import org.n3r.sandbox.db.mongo.model.heroes.Heroine;
import org.n3r.sandbox.db.mongo.model.heroes.Human;

import javax.inject.Inject;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class TestFindOne {

    public static final String HEROES = "heroes";

    @ClassRule
    public static MongoManager mongoManager = MongoManager.mongoConnect("mongodb://localhost,localhost:27018,localhost:27019");

    @MongoTestBedCollection(name = HEROES, location = "heroes.json")
    public static DBCollection collection;

    @Inject
    public static Mongo mongo;

    @Inject
    public static DB mongoDB;

    public static MongoCollection heroes;

    @BeforeClass
    public static void beforeClass() {
        Jongo jongo = new Jongo(mongoDB);
        heroes = jongo.getCollection(HEROES);
    }

    @Test
    public void shouldFindOneEntryBasedOnOIDOperator() {
        //GIVEN

        //WHEN
        Hero hero = heroes.findOne("{_id : {$oid: #}}", "5186a0da21622e48542ea6af").as(Hero.class);

        //THEN
        assertThat(hero).isNotNull();

    }

    @Test
    public void shouldFindOneEntryBasedOnObjectId() {
        //GIVEN

        //WHEN
        Hero hero = heroes.findOne(new ObjectId("5186a0da21622e48542ea6af")).as(Hero.class);

        //THEN
        assertThat(hero).isNotNull();

    }


    @Test
    public void shouldFindOneEntryBasedOnGenderAndFirstName() {
        //GIVEN

        //WHEN
        Heroine heroine = heroes.findOne("{" + Human.GENDER + ": #, " + Human.FIRST_NAME + ": #}", Gender.FEMALE, "Arya")//
                .as(Heroine.class);

        //THEN
        assertThat(heroine).isNotNull();

    }

    @Test
    public void shouldFindOneArrayElement() {
        //GIVEN

        //WHEN
        Heroine heroine = heroes.findOne("{_id : {$oid: #}}", "5186a0da21622e48542ea6af").projection("{children: {$elemMatch: {" + Human.FIRST_NAME + ": #, " + //
                Human.LAST_NAME + ": #}}}", "Sansa", "Stark").map(new ResultHandler<Heroine>() {
            @Override
            public Heroine map(DBObject result) {
                LazyDBList o = (LazyDBList) result.get(Human.CHILDREN);
                DBObject basicDbObject = (DBObject) o.get(0);
                ObjectMapper objectMapper = new ObjectMapper();
                String content = basicDbObject.toString();
                try {
                    return objectMapper.readValue(content, Heroine.class);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to deserialize " + content);
                }
            }
        });

        //THEN
        assertThat(heroine).isNotNull();

    }
}
