import java.util.Scanner;

public class Jody {
    public static void main(String[] args) {
        String banner = "     _           _       \n"
                + "    | | ___   __| |_   _ \n"
                + " _  | |/ _ \\ / _` | | | |\n"
                + "| |_| | (_) | (_| | |_| |\n"
                + " \\___/ \\___/ \\__,_|\\__, |\n"
                + "                   |___/ \n";
        System.out.println("    ____________________________________________________________");
        System.out.println(banner);
        System.out.println("    Hello! I'm Jody.");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________\n");

        String[] cmdList = new String[100];
        int cmdCount = 0;
        String[] cmdState = new String[100];

        for (int i = 0; i < 100; i++) {
            cmdState[i] = "[ ]";
        }

        while (true) {
            Scanner input = new Scanner(System.in);
            String cmd = input.nextLine();
            if (cmd.equalsIgnoreCase("list")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Here are the tasks in your list:");
                for (int i = 1; i <= cmdCount; i++) {
                    System.out.println("    " + i + "." + cmdState[i - 1] + " " + cmdList[i - 1]);
                }
                System.out.println("    ____________________________________________________________\n");
            } else if (equalsMark(cmd, cmdCount)) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Nice! I've marked this task as done:");
                int num = Integer.parseInt(cmd.split(" ")[1]) - 1;
                cmdState[num] = "[X]";
                System.out.println("      " + cmdState[num] + " " + cmdList[num]);
                System.out.println("    ____________________________________________________________\n");
            } else if (isUnmarked(cmd, cmdCount)) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    OK, I've marked this task as not done yet:");
                int num = Integer.parseInt(cmd.split(" ")[1]) - 1;
                cmdState[num] = "[ ]";
                System.out.println("      " + cmdState[num] + " " + cmdList[num]);
                System.out.println("    ____________________________________________________________\n");
            } else if (!cmd.equalsIgnoreCase("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    added: " + cmd);
                cmdList[cmdCount++] = cmd;
                System.out.println("    ____________________________________________________________\n");
            } else {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________\n");
                break;
            }
        }
    }

    private static boolean equalsMark(String cmd, int cmdCount) {
        String[] words = cmd.split(" ");
        if  (words.length != 2) {
            return false;
        } else if (words[0].equalsIgnoreCase("mark")) {
            try {
                int num = Integer.parseInt(words[1]);
                if (num <= cmdCount) {
                    return true;
                } else {
                    return false;
                }
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
                if (num <= cmdCount) {
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
