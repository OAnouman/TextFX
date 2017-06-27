package com.graphicodeci.address.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper to wrap a list of person. Used to save person list to XML
 *
 * Created by MARTIAL ANOUMAN on 6/25/2017.
 *
 * @author Martial Anouman
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {

    private List<Person> persons ;

    @XmlElement(name = "person")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}

