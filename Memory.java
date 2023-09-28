import java.io.*;
import java.util.Scanner;

public class Memory {
    private static final int[] memory = new int[2000];

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             PrintWriter pw = new PrintWriter(System.out, true)) {

            while (true) {
                String command = sc.nextLine();
                if (command.equals("READ")) {
                    int address = sc.nextInt();
                    pw.println(memory[address]);
                } else if (command.equals("WRITE")) {
                    int address = sc.nextInt();
                    int value = sc.nextInt();
                    memory[address] = value;
                    pw.println("OK");
                } else if (command.equals("EXIT")) {
                    break;
                }
            }
        }
    }
}
