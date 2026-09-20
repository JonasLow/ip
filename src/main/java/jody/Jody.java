package jody;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class Jody {
    private final Ui ui = new Ui();

    public static void main(String[] args) {
        new Jody().run();
    }

    public void run() {
        Storage storage = new Storage(Path.of("data", "jody.txt"));

        try (ui) {
            ArrayList<Task> taskList = storage.load();

            ui.showWelcome();
            runCommand(taskList, storage);
        } catch (JodyException e) {
            ui.showError(e.getMessage());
        }
    }

    private void runCommand(ArrayList<Task> taskList, Storage storage) {
        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                boolean hasChanged = processTask(line, taskList);

                if (hasChanged) {
                    storage.save(taskList);
                }
            } catch (JodyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private boolean processTask(String line, ArrayList<Task> taskList) throws JodyException {
        String[] parts = line.split("\\s+", 2);
        String command = parts[0].toLowerCase(Locale.ROOT);
        String arguments = parts.length == 2 ? parts[1].trim() : "";

        switch (command) {
            case "list":
                requireNoArguments(arguments, "list");
                ui.showTasks(taskList, taskList.size());
                return false;

            case "on":
                showTasksOnDate(arguments, taskList);
                return false;

            case "todo":
                requireDescription(arguments, "Please give your todo a description. "
                        + "Example: todo read a book");
                addTask(new Todo(arguments), taskList);
                return true;

            case "deadline":
                requireDescription(arguments, "Please give your deadline a description and date. "
                        + "Example: deadline return book /by 2/12/2019 1800");
                addTask(new Deadline(arguments), taskList);
                return true;

            case "event":
                requireDescription(arguments, "Please give your event a description and dates. "
                        + "Example: event meeting "
                        + "/from 2/12/2019 1400 /to 2/12/2019 1600");
                addTask(new Event(arguments), taskList);
                return true;

            case "mark":
                return markTask(arguments, taskList);

            case "unmark":
                return unmarkTask(arguments, taskList);

            case "delete":
                deleteTask(arguments, taskList);
                return true;

            case "find":
                findTask(arguments, taskList);
                return true;

            default:
                throw new JodyException("I don't recognize that command. "
                        + "Try todo, deadline, event, list, on, "
                        + "mark, unmark, delete, or bye.");
        }
    }

    private void requireDescription(String description, String message)
            throws JodyException {
        if (description.isBlank()) {
            throw new JodyException(message);
        }
    }

    private void requireNoArguments(String arguments, String command)
            throws JodyException {
        if (!arguments.isEmpty()) {
            throw new JodyException("The " + command + " command does not take arguments.");
        }
    }

    private void addTask(Task task, ArrayList<Task> taskList) {
        taskList.add(task);
        ui.showAddedTask(task, taskList.size());
    }

    private void deleteTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "delete");
        Task removedTask = taskList.remove(taskIndex);

        ui.showDeletedTask(removedTask, taskList.size());
    }

    private boolean markTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "mark");
        Task task = taskList.get(taskIndex);
        boolean hasChanged = !task.isDone();

        task.markAsDone();
        ui.showMarkedTask(task);

        return hasChanged;
    }

    private boolean unmarkTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "unmark");
        Task task = taskList.get(taskIndex);
        boolean hasChanged = task.isDone();

        task.markAsNotDone();
        ui.showUnmarkedTask(task);

        return hasChanged;
    }

    private void findTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        ui.findInDescription(arguments, taskList);
    }

    private int parseTaskNumber(String arguments, int taskCount, String command) throws JodyException {
        if (arguments.isBlank()) {
            throw new JodyException("Please enter a task number. Example: " + command + " 1");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new JodyException("Please enter one whole task number. "
                    + "Example: " + command + " 1");
        }

        if (taskCount == 0) {
            throw new JodyException("Your task list is empty. Add a task first.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new JodyException("Please enter a task number between 1 and "
                    + taskCount + ".");
        }

        return taskNumber - 1;
    }

    private void showTasksOnDate(String input, ArrayList<Task> taskList) throws JodyException {
        LocalDate date = DateTimeParser.parseDate(input);
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            if (task instanceof Deadline deadline) {
                if (deadline.getBy().toLocalDate().equals(date)) {
                    matches.add(i);
                }
            } else if (task instanceof Event event) {
                if (!date.isBefore(event.getFrom().toLocalDate())
                        && !date.isAfter(event.getTo().toLocalDate())) {
                    matches.add(i);
                }
            }
        }

        matches.sort(Comparator
                .comparing(index -> scheduledTime(taskList.get(index))));

        ui.showTasksOnDate(date, taskList, matches);
    }

    private LocalDateTime scheduledTime(Task task) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy();
        }

        return ((Event) task).getFrom();
    }
}
