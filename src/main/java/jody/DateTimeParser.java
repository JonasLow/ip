package jody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public final class DateTimeParser {
    private static final String[] DATE_PATTERNS = {
            "uuuu-MM-dd", "d/M/uuuu", "d MMMM uuuu", "d MMM uuuu"
    };

    private static final String[] TIME_PATTERNS = {
            "HHmm", "H:mm", "ha", "h:mma"
    };

    private static final DateTimeFormatter DISPLAY =
            DateTimeFormatter.ofPattern("MMM dd uuuu, h:mm a", Locale.ENGLISH);

    private DateTimeParser() {
    }

    private static String normalize(String input) {
        return input.trim()
                .replaceAll("(?i)\\b(\\d{1,2})(st|nd|rd|th)\\b", "$1")
                .replaceAll("\\s+", " ")
                .replaceAll("(?i)\\s+(am|pm)\\b", "$1");
    }

    private static DateTimeFormatter formatter(String pattern) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);
    }

    public static LocalDate parseDate(String input) throws JodyException {
        String value = normalize(input);

        for (String pattern : DATE_PATTERNS) {
            try {
                return LocalDate.parse(value, formatter(pattern));
            } catch (DateTimeParseException _) {
            }
        }

        throw new JodyException(
                "Invalid date. Try 2019-12-02, 2/12/2019 "
                        + "or 2nd December 2019.");
    }

    public static LocalDateTime parseDateTime(String input)
            throws JodyException {
        String value = normalize(input);

        try {
            // Accepts the ISO representation written by Storage.
            return LocalDateTime.parse(
                    value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            // Try the user-facing formats below.
        }

        for (String datePattern : DATE_PATTERNS) {
            for (String timePattern : TIME_PATTERNS) {
                try {
                    return LocalDateTime.parse(
                            value,
                            formatter(datePattern + " " + timePattern));
                } catch (DateTimeParseException _) {
                }
            }
        }

        try {
            // A date without a time means midnight.
            return parseDate(value).atStartOfDay();
        } catch (JodyException e) {
            throw new JodyException(
                    "Invalid date/time. Try 2/12/2019 1800 "
                            + "or 2nd December 2019 6pm. "
                            + "A date alone is also accepted.");
        }
    }

    public static String format(LocalDateTime value) {
        return value.format(DISPLAY);
    }
}