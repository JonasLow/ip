package jody;

import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Path;

public class Jody {
    public static final int INVALID_INDEX = -1;
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "     _           _       \n"
            + "    | | ___   __| |_   _ \n"
            + " _  | |/ _ \\ / _` | | | |\n"
            + "| |_| | (_) | (_| | |_| |\n"
            + " \\___/ \\___/ \\__,_|\\__, |\n"
            + "                   |___/ \n";
    public static final int TODO_LEN = 5;
    public static final int DEADLINE_LEN = 9;
    public static final int EVENT_LEN = 6;

    public static void main(String[] args) {
        Storage storage = new Storage(Path.of("data", "jody.txt"));

        try (Scanner input = new Scanner(System.in)) {
            ArrayList<Task> taskList = storage.load();

            displayStartup();
            runCommand(input, taskList, storage);
        } catch (JodyException e) {
            System.out.println(DIVIDER);
            System.out.println("    Oops! " + e.getMessage());
            System.out.println(DIVIDER + "\n");
        }
    }

    private static void runCommand(Scanner input, ArrayList<Task> taskList,
                                   Storage storage) {
        int taskCount = taskList.size();

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("bye")) {
                displayShutdown();
                break;
            }

            try {
                taskCount = processTask(line, taskList, taskCount);

                if (!line.equalsIgnoreCase("list")) {
                    storage.save(taskList);
                }
            } catch (JodyException e) {
                System.out.println(DIVIDER);
                System.out.println("    Oops! " + e.getMessage());
                System.out.println(DIVIDER + "\n");
            }
        }
    }

    private static int processTask(String line, ArrayList<Task> taskList, int taskCount) throws JodyException {
        if (line.equalsIgnoreCase("list")) {
            listTasks(taskList, taskCount);
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

    private static int deleteTask(String line, ArrayList<Task> taskList, int taskCount)
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

        System.out.println(DIVIDER);
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + removedTask);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
        return taskCount;
    }

    private static int addTask(Task task, ArrayList<Task> taskList, int taskCount) {
        taskList.add(task);
        taskCount++;

        System.out.println(DIVIDER);
        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
        return taskCount;
    }

    private static void listTasks(ArrayList<Task> taskList, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("    " + (i + 1) + "." + taskList.get(i));
        }
        System.out.println(DIVIDER + "\n");
    }

    private static void markTask(String task, ArrayList<Task> taskList, int taskCount) {
        System.out.println(DIVIDER);
        int taskIndex = parseTaskNumber(task);
        if (taskIndex < 0 || taskIndex >= taskCount) {
            System.out.println("    Unable to mark task.");
            return;
        }
        taskList.get(taskIndex).markAsDone();
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("      " + taskList.get(taskIndex));
        System.out.println(DIVIDER + "\n");
    }

    private static void unmarkTask(String task, ArrayList<Task> taskList, int taskCount) {
        System.out.println(DIVIDER);
        int taskIndex = parseTaskNumber(task);
        if (taskIndex < 0 || taskIndex >= taskCount) {
            System.out.println("    Unable to mark task.");
            return;
        }
        taskList.get(taskIndex).markAsNotDone();
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("      " + taskList.get(taskIndex));
        System.out.println(DIVIDER + "\n");
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

    private static void displayStartup() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("    Hello! I'm Jody.");
        System.out.println("    What can I do for you?");
        System.out.println(DIVIDER + "\n");
    }

    private static void displayShutdown() {
        System.out.println(DIVIDER);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(DIVIDER + "\n");
    }
}
