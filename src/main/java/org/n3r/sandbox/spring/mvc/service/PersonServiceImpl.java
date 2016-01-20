package org.n3r.sandbox.spring.mvc.service;

import org.n3r.sandbox.spring.mvc.domain.Person;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PersonServiceImpl implements PersonService {

    String[] names = {"Nikolaus Otto", "Robert Ford", "Gottlieb Daimler", "Lt. General Masaharu Homma"};

    @Override
    public Person getRandom() {
        Person person = new Person();
        person.setName(randomName());
        person.setAge(randomAge());
        return person;
    }

    @Override
    public Person getById(Long id) {
        Person person = new Person();
        person.setName(names[id.intValue()]);
        person.setAge(50);
        return person;
    }

    @Override
    public void save(Person person) {
        // Save person to database ...
    }

    private Integer randomAge() {
        Random random = new Random();
        return 10 + random.nextInt(100);
    }

    private String randomName() {
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }

}
