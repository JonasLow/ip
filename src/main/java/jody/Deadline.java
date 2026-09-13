package jody;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description) {
        super(description.split("/by")[0]);
        this.by =  "by: " + description.split("/by")[1];
    }

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return("[D]" + super.toString() + "(" + this.by + ")");
    }
}
