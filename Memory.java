import java.io.*;
import java.util.Scanner;

public class Memory {
    private static final int[] memory = new int[2000];

    public static void main(String[] args) {
        if (args.length > 0) {
            loadProgram(args[0]);
        } else {
            System.out.println("No input file provided.");
            System.exit(1);
        }

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

    private static void loadProgram(String inputFile) {
        try (Scanner fileScanner = new Scanner(new File(inputFile))) {
            int address = 0;
            while (fileScanner.hasNextInt()) {
                memory[address++] = fileScanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
