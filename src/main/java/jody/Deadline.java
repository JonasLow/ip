package jody;

import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String input) throws JodyException {
        this(splitInput(input));
    }

    private Deadline(String[] parts) throws JodyException {
        this(parts[0], parts[1]);
    }

    public Deadline(String description, String by) throws JodyException {
        super(description.trim());

        if (description.isBlank()) {
            throw new JodyException("A deadline needs a description.");
        }

        this.by = DateTimeParser.parseDateTime(by);
    }

    private static String[] splitInput(String input) throws JodyException {
        String[] parts = input.split("/by", -1);

        if (parts.length != 2) {
            throw new JodyException(
                    "Use: deadline return book /by 2/12/2019 1800");
        }

        return parts;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + DateTimeParser.format(by) + ")";
    }
}
