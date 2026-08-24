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

        while (true) {
            Scanner input = new Scanner(System.in);
            String cmd = input.nextLine();
            if (cmd.equalsIgnoreCase("list")) {
                System.out.println("    ____________________________________________________________");
                for (int i = 1; i <= cmdCount; i++) {
                    System.out.println("    " + i + ". " + cmdList[i - 1]);
                }
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
}
