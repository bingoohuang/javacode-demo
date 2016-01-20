package org.n3r.sandbox.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThymeleafDemo {
    public static void main(String[] args) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("XHTML");
        resolver.setSuffix(".html");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);


        Context context = new Context(Locale.FRANCE);
        context.setVariable("date", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        context.setVariable("contact", new Contact("John", "Doe"));

        List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(new Contact("John","Doe"));
        contacts.add(new Contact("Jane","Doe"));
        context.setVariable("contacts",contacts);

        StringWriter writer = new StringWriter();
        engine.process("org/n3r/sandbox/thymeleaf/home", context, writer);

        System.out.println(writer.toString());
    }

    public static class Contact {
        public String lastName;
        public String firstName;

        public Contact(String lastName, String firstName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }
    }
}
