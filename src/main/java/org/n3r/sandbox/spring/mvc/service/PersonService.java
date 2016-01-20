package org.n3r.sandbox.spring.mvc.service;


import org.n3r.sandbox.spring.mvc.domain.Person;

public interface PersonService {
    Person getRandom();

    Person getById(Long id);

    void save(Person person);
}
