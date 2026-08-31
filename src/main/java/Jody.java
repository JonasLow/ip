import java.util.Scanner;

public class Jody {

    public static final int MAX_TASKS = 100;
    public static final String divider = "    ____________________________________________________________";
    public static void main(String[] args) {
        String banner = "     _           _       \n"
                + "    | | ___   __| |_   _ \n"
                + " _  | |/ _ \\ / _` | | | |\n"
                + "| |_| | (_) | (_| | |_| |\n"
                + " \\___/ \\___/ \\__,_|\\__, |\n"
                + "                   |___/ \n";
        System.out.println(divider);
        System.out.println(banner);
        System.out.println("    Hello! I'm Jody.");
        System.out.println("    What can I do for you?");
        System.out.println(divider + "\n");

        String[] taskList = new String[MAX_TASKS];
        int taskCount = 0;
        String[] taskState = new String[MAX_TASKS];

        for (int i = 0; i < MAX_TASKS; i++) {
            taskState[i] = "[ ]";
        }

        Scanner input = new Scanner(System.in);
        while (true) {
            String cmd = input.nextLine();
            if (cmd.equalsIgnoreCase("list")) {
                System.out.println(divider);
                System.out.println("    Here are the tasks in your list:");
                for (int i = 1; i <= taskCount; i++) {
                    System.out.println("    " + i + "." + taskState[i - 1] + " " + taskList[i - 1]);
                }
                System.out.println(divider + "\n");
            } else if (isMark(cmd, taskCount)) {
                System.out.println(divider);
                System.out.println("    Nice! I've marked this task as done:");
                int num = Integer.parseInt(cmd.split(" ")[1]) - 1;
                taskState[num] = "[X]";
                System.out.println("      " + taskState[num] + " " + taskList[num]);
                System.out.println(divider + "\n");
            } else if (isUnmarked(cmd, taskCount)) {
                System.out.println(divider);
                System.out.println("    OK, I've marked this task as not done yet:");
                int num = Integer.parseInt(cmd.split(" ")[1]) - 1;
                taskState[num] = "[ ]";
                System.out.println("      " + taskState[num] + " " + taskList[num]);
                System.out.println(divider + "\n");
            } else if (!cmd.equalsIgnoreCase("bye")) {
                System.out.println(divider);
                System.out.println("    added: " + cmd);
                taskList[taskCount++] = cmd;
                System.out.println(divider + "\n");
            } else {
                System.out.println(divider);
                System.out.println("    Bye. Hope to see you again soon!");
                System.out.println(divider + "\n");
                break;
            }
        }
    }

    private static boolean isMark(String cmd, int cmdCount) {
        String[] words = cmd.split(" ");
        if (words.length != 2) {
            return false;
        } else if (words[0].equalsIgnoreCase("mark")) {
            try {
                int num = Integer.parseInt(words[1]);
                return num <= cmdCount;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (words[0].equalsIgnoreCase("unmark")) {
            try {
                int num = Integer.parseInt(words[1]);
                return num <= cmdCount;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean isUnmarked(String cmd, int cmdCount) {
        String[] words = cmd.split(" ");
        if  (words.length != 2) {
            return false;
        } else if (words[0].equalsIgnoreCase("unmark")) {
            try {
                int num = Integer.parseInt(words[1]);
                return num <= cmdCount;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
