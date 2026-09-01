import java.util.Scanner;

public class Jody {
    private static final int MAX_TASKS = 100;
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "     _           _       \n"
            + "    | | ___   __| |_   _ \n"
            + " _  | |/ _ \\ / _` | | | |\n"
            + "| |_| | (_) | (_| | |_| |\n"
            + " \\___/ \\___/ \\__,_|\\__, |\n"
            + "                   |___/ \n";

    public static void main(String[] args) {
        displayStartup();
        Task[] taskList = new Task[MAX_TASKS];
        Scanner input = new Scanner(System.in);
        runCommand(input, taskList);
    }

    private static void runCommand(Scanner input, Task[] taskList) {
        int taskCount = 0;
        while (input.hasNextLine()) {
            String task = input.nextLine();
            if (task.equalsIgnoreCase("bye")) {
                displayShutdown();
                break;
            }
            taskCount = processTask(task, taskList, taskCount);
        }
    }

    private static int processTask(String task, Task[] taskList, int taskCount) {
        if (task.equalsIgnoreCase("list")) {
            listTasks(taskList, taskCount);
        } else if (task.toLowerCase().startsWith("mark ")) {
            markTask(task, taskList, taskCount);
        } else if (task.toLowerCase().startsWith("unmark ")) {
            unmarkTask(task, taskList, taskCount);
        } else {
            return addTask(task, taskList, taskCount);
        }
        return taskCount;
    }

    private static int addTask(String task, Task[] taskList, int taskCount) {
        taskList[taskCount] = new Task(task);
        System.out.println(DIVIDER);
        System.out.println("    added: " + task);
        System.out.println(DIVIDER + "\n");
        return taskCount + 1;
    }

    private static void listTasks(Task[] taskList, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("    " + (i + 1) + "." + taskList[i]);
        }
        System.out.println(DIVIDER + "\n");
    }

    private static void markTask(String task, Task[] taskList, int taskCount) {
        System.out.println(DIVIDER);
        int taskIndex = parseTaskNumber(task);
        if (taskIndex >= 0 && taskIndex < taskCount) {
            taskList[taskIndex].markAsDone();
            System.out.println("    Nice! I've marked this task as done:");
            System.out.println("      " + taskList[taskIndex]);
        } else {
            System.out.println("    Unable to mark task.");
        }
        System.out.println(DIVIDER + "\n");
    }

    private static void unmarkTask(String task, Task[] taskList, int taskCount) {
        System.out.println(DIVIDER);
        int taskIndex = parseTaskNumber(task);
        if (taskIndex >= 0 && taskIndex < taskCount) {
            taskList[taskIndex].markAsNotDone();
            System.out.println("    OK, I've marked this task as not done yet:");
            System.out.println("      " + taskList[taskIndex]);
        } else {
            System.out.println("    Unable to unmark task.");
        }
        System.out.println(DIVIDER + "\n");
    }

    private static int parseTaskNumber(String task) {
        String[] words = task.split(" ");
        if (words.length != 2) {
            return -1;
        }
        try {
            return Integer.parseInt(words[1]) - 1;
        } catch (NumberFormatException e) {
            return -1;
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