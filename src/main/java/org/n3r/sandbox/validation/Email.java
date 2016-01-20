package org.n3r.sandbox.validation;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;


public class Email {
    @NotEmpty
//    @Pattern(regexp = ".+@.+\\.[a-z]+")
    @org.n3r.sandbox.validation.contraints.Email
    private String from;

    @NotEmpty
    //@Pattern(regexp = ".+@.+\\.[a-z]+")
    @org.n3r.sandbox.validation.contraints.Email
    @Size(max = 3)
    private String to;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
