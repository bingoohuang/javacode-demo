package org.n3r.sandbox.spring.mvc.controller;

import org.n3r.sandbox.spring.mvc.domain.Person;
import org.n3r.sandbox.spring.mvc.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// http://www.javacodegeeks.com/2013/04/spring-mvc-easy-rest-based-json-services-with-responsebody.html
@Controller
@RequestMapping("api")
public class PersonController {

    PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("person/random")
    @ResponseBody
    public Person randomPerson() {
        return personService.getRandom();
    }

    @RequestMapping("person/{id}")
    @ResponseBody
    public Person getById(@PathVariable Long id) {
        return personService.getById(id);
    }

    /* same as above method, but is mapped to
     * /api/person?id= rather than /api/person/{id}
     */
    @RequestMapping(value = "person", params = "id")
    @ResponseBody
    public Person getByIdFromParam(@RequestParam("id") Long id) {
        return personService.getById(id);
    }

    /**
     * Saves new person. Spring automatically binds the name
     * and age parameters in the request to the person argument
     *
     * @param person
     * @return String indicating success or failure of save
     */
    @RequestMapping(value = "person", method = RequestMethod.POST)
    @ResponseBody
    public String savePerson(Person person) {
        personService.save(person);
        return "Saved person: " + person.toString();
    }
}
