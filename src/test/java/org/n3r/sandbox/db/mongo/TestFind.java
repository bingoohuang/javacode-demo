package org.n3r.sandbox.db.mongo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DB;
import org.ingini.monogo.testbed.MongoManager;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.n3r.sandbox.db.mongo.model.weapons.Sword;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.fest.assertions.Assertions.assertThat;

public class TestFind {
    public static final String WEAPONS = "weapons";

    @ClassRule
    public static MongoManager mongoManager = MongoManager
            .mongoConnect("mongodb://localhost,localhost:27018,localhost:27019");

    @Inject
    public static DB mongoDB;

    public static MongoCollection weapons;
    private static MongoCollection jens;

    @BeforeClass
    public static void beforeClass() {
        Jongo jongo = new Jongo(mongoDB);
        weapons = jongo.getCollection(WEAPONS);
        jens = jongo.getCollection("JENS");
    }

    @Test
    public void shouldFindWithRegexOperator() {
        jens.insert(new Cat("tom"));


        //GIVEN
        weapons.remove();

        weapons.insert("{ _id : 'Lightbringer' }");
        weapons.insert("{ _id : 'Longclaw', material: 'Valyrian steel' }");
        weapons.insert("{ _id : 'Dark Sister', material: 'Valyrian steel' }");
        weapons.insert("{ _id : 'Ice', material: 'Valyrian steel' }");
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("name", "goo");
        map.put("sex", 0);
        weapons.insert(map);

        Person person = new Person();
        person.setAddress("金融街");
        person.setPostcode("100012");
        weapons.insert(person);

        Person as = weapons.findOne("{ _id : #}", person.getId()).as(Person.class);
        System.out.println(as);

        //WHEN
        List<Sword> swordsOfSteel = Lists.newArrayList(weapons
                .find("{#: {$regex: #}}", Sword.MATERIAL, "steel.*").as(Sword.class));

        //THEN
        assertThat(swordsOfSteel).isNotEmpty();
        assertThat(swordsOfSteel).hasSize(3);
    }

    @Test
    public void shouldFindWithPatternCompile() {
        //GIVEN
        weapons.remove();

        weapons.insert("{ _id : 'Lightbringer' }");
        weapons.insert("{ _id : 'Longclaw', material: 'Valyrian steel' }");
        weapons.insert("{ _id : 'Dark Sister', material: 'Valyrian steel' }");
        weapons.insert("{ _id : 'Ice', material: 'Valyrian steel' }");

        Map as = weapons.findOne("{ _id : 'Lightbringer' }").as(Map.class);
        System.out.println(as);

        //WHEN
        List<Sword> swordsOfSteel = Lists.newArrayList(weapons
                .find("{# : #}", Sword.MATERIAL, Pattern.compile("steel.*")).as(Sword.class));

        //THEN
        assertThat(swordsOfSteel).isNotEmpty();
        assertThat(swordsOfSteel).hasSize(3);
    }

    public static class Person {
        @Id
        private String id;
        private String address;
        private String postcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
