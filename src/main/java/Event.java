public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description) {
        super(description.split("/from")[0]);
        this.from = "from: " + description.split("/from")[1];
        this.to = "to: " + description.split("/to")[1];
    }

    @Override
    public String toString() {
        return ("[E]" + super.toString() + "(" + this.from + this.to + ")");
    }
}

