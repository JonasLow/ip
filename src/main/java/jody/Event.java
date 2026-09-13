package jody;

public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description) {
        super(description.split("/from")[0]);
        this.from = "from: " + description.split("/from")[1].split("/to")[0];
        this.to = "to: " + description.split("/to")[1];
    }

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return ("[E]" + super.toString() + "(" + this.from + " " + this.to + ")");
    }
}

