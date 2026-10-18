package warden;

import common.Person;

// Warden also extends Person (inheritance)
public class Warden extends Person {

    public Warden(String id, String name) {
        super(id, name);
    }

    public String getRole() {
        return "Warden";
    }
}
