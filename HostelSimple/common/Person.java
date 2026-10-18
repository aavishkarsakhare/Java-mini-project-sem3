package common;

// Abstract class: parent of Student and Warden
public abstract class Person {
    protected String id;
    protected String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // every child class must write its own version of this method
    public abstract String getRole();
}
