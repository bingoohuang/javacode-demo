package org.n3r.sandbox.db.mongo;

import com.google.common.collect.Sets;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.ingini.monogo.testbed.MongoManager;
import org.ingini.monogo.testbed.annotation.MongoTestBedCollection;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.n3r.sandbox.db.mongo.model.beasts.DireWolf;
import org.n3r.sandbox.db.mongo.model.heroes.Hero;
import org.n3r.sandbox.db.mongo.model.heroes.Heroine;
import org.n3r.sandbox.db.mongo.model.heroes.Human;
import org.n3r.sandbox.db.mongo.model.weapons.Sword;
import org.n3r.sandbox.db.mongo.model.weapons.WeaponDetails;

import javax.inject.Inject;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class TestUpdate {

    public static final String WEAPONS = "weapons";
    public static final String HEROES = "heroes";

    @ClassRule
    public static MongoManager mongoManager = MongoManager.mongoConnect("mongodb://localhost,localhost:27018,localhost:27019");

    @Inject
    public static Mongo mongo;

    @Inject
    public static DB mongoDB;

    public static MongoCollection weapons;

    @MongoTestBedCollection(name = HEROES, location = "org/n3r/sandbox/db/mongo/heroes.json")
    public static DBCollection collection;

    public static MongoCollection heroes;

    @BeforeClass
    public static void beforeClass() {
        Jongo jongo = new Jongo(mongoDB);
        weapons = jongo.getCollection(WEAPONS);
        heroes = jongo.getCollection(HEROES);
    }

    @Test
    public void shouldAddFieldToTheLightbringer() {
        //GIVEN
        WeaponDetails details = new WeaponDetails("The one who pulls out this sword from fire will be named Lord's Chosen ...", "Azor Ahai");

        //WHEN
        WriteResult lightbringer = weapons.update("{_id: #}", "Lightbringer").with("{$set: {details: #}}", details);

        //THEN
        assertThat(lightbringer.getError()).isNull();

        //AND WHEN
        Sword sword = weapons.findOne("{_id: 'Lightbringer'}").as(Sword.class);


        //THEN
        assertThat(sword).isNotNull();
    }

    @Test
    public void shouldAddADireWolfForEachStarkChild() {
        //GIVEN
        Hero eddardStark = heroes.findOne(new ObjectId("5186a0da21622e48542ea6af")).as(Hero.class);

        Set<Human> updatedChildren = Sets.newHashSet();

        for (Human child : eddardStark.getChildren()) {
            if (child.getFirstName().equals("Robb")) {
                updatedChildren.add(Hero.addBeast((Hero) child, new DireWolf("Grey Wind")));
            }

            if (child.getFirstName().equals("Sansa")) {
                updatedChildren.add(Heroine.addBeast((Heroine) child, new DireWolf("Lady")));
            }

            if (child.getFirstName().equals("Arya")) {
                updatedChildren.add(Heroine.addBeast((Heroine) child, new DireWolf("Nymeria")));
            }

            if (child.getFirstName().equals("Bran")) {
                updatedChildren.add(Hero.addBeast((Hero) child, new DireWolf("Summer")));
            }

            if (child.getFirstName().equals("Rickon")) {
                updatedChildren.add(Hero.addBeast((Hero) child, new DireWolf("Shaggydog")));
            }

            if (child.getFirstName().equals("Jon")) {
                updatedChildren.add(Hero.addBeast((Hero) child, new DireWolf("Ghost")));
            }
        }

        Hero updatedEddardStark = Hero.updateChildren(eddardStark, updatedChildren);

        //WHEN
        WriteResult save = heroes.save(updatedEddardStark);

        //THEN
        assertThat(save.getError()).isNull();

    }
}
