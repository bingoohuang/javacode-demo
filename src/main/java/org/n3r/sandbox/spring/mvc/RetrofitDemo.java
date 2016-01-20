package org.n3r.sandbox.spring.mvc;

import org.n3r.sandbox.spring.mvc.domain.Person;
import retrofit.RestAdapter;
import retrofit.http.GET;

// http://square.github.io/retrofit/
public class RetrofitDemo {
    public static interface PersonService {
        @GET("/api/person/random")
        Person randomPerson();
    }

    public static void main(String[] args) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer("http://localhost:8080/")
                .build();

        PersonService service = restAdapter.create(PersonService.class);
        Person person = service.randomPerson();
        System.out.println(person);
    }
}
