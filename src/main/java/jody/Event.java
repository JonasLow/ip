package jody;

import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String input) throws JodyException {
        this(splitInput(input));
    }

    private Event(String[] parts) throws JodyException {
        this(parts[0], parts[1], parts[2]);
    }

    // Used when loading a saved task.
    public Event(String description, String from, String to)
            throws JodyException {
        super(description.trim());

        if (description.isBlank()) {
            throw new JodyException("An event needs a description.");
        }

        this.from = DateTimeParser.parseDateTime(from);
        this.to = DateTimeParser.parseDateTime(to);

        if (this.to.isBefore(this.from)) {
            throw new JodyException(
                    "An event cannot end before it starts.");
        }
    }

    private static String[] splitInput(String input) throws JodyException {
        String[] first = input.split("/from", -1);

        if (first.length == 2) {
            String[] times = first[1].split("/to", -1);

            if (times.length == 2) {
                return new String[] {
                        first[0], times[0], times[1]
                };
            }
        }

        throw new JodyException(
                "Use: event meeting /from 2/12/2019 1400 "
                        + "/to 2/12/2019 1600");
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }
}
