package de.davidschwietz.expensetracker.model;

public class Category {

    private long id;
    private String name;

    public Category(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is not valid.");
        }

        this.name = name;
    }

    public Category(long id, String name) {

        this(name);

        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Id must be greater than 0.");
        }

        this.name = name;
    }

    public String toString() {
        return "Name of the category: " + name;
    }
}
