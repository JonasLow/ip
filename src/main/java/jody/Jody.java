package jody;

import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Path;

public class Jody {
    public static final int INVALID_INDEX = -1;
    public static final int TODO_LEN = 5;
    public static final int DEADLINE_LEN = 9;
    public static final int EVENT_LEN = 6;

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
        int taskCount = taskList.size();

        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                taskCount = processTask(line, taskList, taskCount);

                if (!line.equalsIgnoreCase("list")) {
                    storage.save(taskList);
                }
            } catch (JodyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private int processTask(String line, ArrayList<Task> taskList, int taskCount) throws JodyException {
        if (line.equalsIgnoreCase("list")) {
            ui.showTasks(taskList, taskCount);
        } else if (line.toLowerCase().startsWith("mark")) {
            String description = line.substring("mark".length()).trim();
            if (description.isEmpty()) {
                throw new JodyException("Please give your mark a description. Example: mark live till 30");
            }
            markTask(line, taskList, taskCount);
        } else if (line.toLowerCase().startsWith("unmark")) {
            String description = line.substring("unmark".length()).trim();
            if (description.isEmpty()) {
                throw new JodyException("Please give your unmark a description. Example: unmark live till 30");
            }
            unmarkTask(line, taskList, taskCount);
        } else if (line.toLowerCase().startsWith("todo")) {
            String description = line.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new JodyException("Please give your todo a description. Example: todo read a book");
            }
            return addTask(new Todo(line.substring(TODO_LEN).trim()), taskList, taskCount);
        } else if (line.toLowerCase().startsWith("deadline")) {
            String description = line.substring("deadline".length()).trim();
            if (description.isEmpty()) {
                throw new JodyException("Please give your deadline a description. Example: sacrifice a goat /by Friday 6pm");
            }
            return addTask(new Deadline(line.substring(DEADLINE_LEN).trim()), taskList, taskCount);
        } else if (line.toLowerCase().startsWith("event")) {
            String description = line.substring("event".length()).trim();
            if (description.isEmpty()) {
                throw new JodyException("Please give your event a description. Example: event rob a bank /from Friday 4pm /to 6pm");
            }
            return addTask(new Event(line.substring(EVENT_LEN).trim()), taskList, taskCount);
        } else if (line.toLowerCase().startsWith("delete")) {
            return deleteTask(line, taskList, taskCount);
        } else {
            throw new JodyException("I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.");
        }
        return taskCount;
    }

    private int deleteTask(String line, ArrayList<Task> taskList, int taskCount)
            throws JodyException {
        if (taskList.isEmpty()) {
            throw new JodyException("Your task list is empty. Add a task first.");
        }

        int taskIndex = parseTaskNumber(line);

        if (taskIndex < 0 || taskIndex >= taskList.size()) {
            throw new JodyException(
                    "Please enter a task number between 1 and "
                            + taskList.size() + ".");
        }

        Task removedTask = taskList.remove(taskIndex);
        taskCount--;

        ui.showDeletedTask(removedTask, taskCount);
        return taskCount;
    }

    private int addTask(Task task, ArrayList<Task> taskList, int taskCount) {
        taskList.add(task);
        taskCount++;

        ui.showAddedTask(task, taskCount);
        return taskCount;
    }

    private void markTask(String task, ArrayList<Task> taskList, int taskCount) {
        int taskIndex = parseTaskNumber(task);
        if (taskIndex < 0 || taskIndex >= taskCount) {
            ui.showUnableToMark();
            return;
        }
        taskList.get(taskIndex).markAsDone();
        ui.showMarkedTask(taskList.get(taskIndex));
    }

    private void unmarkTask(String task, ArrayList<Task> taskList, int taskCount) {
        int taskIndex = parseTaskNumber(task);
        if (taskIndex < 0 || taskIndex >= taskCount) {
            ui.showUnableToMark();
            return;
        }
        taskList.get(taskIndex).markAsNotDone();
        ui.showUnmarkedTask(taskList.get(taskIndex));
    }

    private static int parseTaskNumber(String task) {
        String[] words = task.split(" ");
        if (words.length != 2) {
            return INVALID_INDEX;
        }
        try {
            return Integer.parseInt(words[1]) - 1;
        } catch (NumberFormatException e) {
            return INVALID_INDEX;
        }
    }
}
