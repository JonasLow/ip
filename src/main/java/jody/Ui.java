package jody;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Ui implements AutoCloseable {
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "     _           _       \n"
            + "    | | ___   __| |_   _ \n"
            + " _  | |/ _ \\ / _` | | | |\n"
            + "| |_| | (_) | (_| | |_| |\n"
            + " \\___/ \\___/ \\__,_|\\__, |\n"
            + "                   |___/ \n";

    private final Scanner input = new Scanner(System.in);

    public boolean hasNextCommand() {
        return input.hasNextLine();
    }

    public String readCommand() {
        return input.nextLine().trim();
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        System.out.println("    Hello! I'm Jody.");
        System.out.println("    What can I do for you?");
        System.out.println(DIVIDER + "\n");
    }

    public void showGoodbye() {
        showLine();
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(DIVIDER + "\n");
    }

    public void showError(String message) {
        showLine();
        System.out.println("    Oops! " + message);
        System.out.println(DIVIDER + "\n");
    }

    public void showAddedTask(Task task, int taskCount) {
        showLine();
        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
    }

    public void showDeletedTask(Task task, int taskCount) {
        showLine();
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showTasks(List<Task> tasks, int taskCount) {
        showLine();
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("    " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER + "\n");
    }

    public void showMarkedTask(Task task) {
        showLine();
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("      " + task);
        System.out.println(DIVIDER + "\n");
    }

    public void showUnmarkedTask(Task task) {
        showLine();
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("      " + task);
        System.out.println(DIVIDER + "\n");
    }

    public void showUnableToMark() {
        showLine();
        System.out.println("    Unable to mark task.");
    }

    public void showTasksOnDate(LocalDate date, List<Task> tasks,
                                List<Integer> indices) {
        showLine();
        System.out.println("    Tasks on " + date + ":");

        if (indices.isEmpty()) {
            System.out.println("    No deadlines or events on this date.");
        }

        for (int index : indices) {
            System.out.println("    " + (index + 1) + "." + tasks.get(index));
        }

        System.out.println(DIVIDER + "\n");
    }

    @Override
    public void close() {
        input.close();
    }
}
