import java.io.*;
import java.util.Scanner;

public class CPU {
    private static int PC, SP, IR, AC, X, Y;
    private static boolean kernelMode = false;

    public static void execute(String inputFile) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "Memory", inputFile);
            Process memoryProcess = pb.start();

            try (Scanner memoryInput = new Scanner(memoryProcess.getInputStream());
                 PrintWriter memoryOutput = new PrintWriter(memoryProcess.getOutputStream(), true)) {

                while (true) {
                    IR = memoryInput.nextInt();
                    System.out.println("Processing instruction: " + IR);


                    switch (IR) {
                    case 1: // Load value
                        AC = memoryInput.nextInt();
                        break;
                    case 2: // Load addr
                        int address = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case 3: // LoadInd addr
                        address = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        int indirectAddress = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(indirectAddress);
                        AC = memoryInput.nextInt();
                        break;
                    case 4: // LoadIdxX addr
                        address = memoryInput.nextInt() + X;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case 5: // LoadIdxY addr
                        address = memoryInput.nextInt() + Y;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case 6: // LoadSpX
                        address = SP + X;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case 7: // Store addr
                        address = memoryInput.nextInt();
                        memoryOutput.println("WRITE");
                        memoryOutput.println(address);
                        memoryOutput.println(AC);
                        break;
                    case 8: // Get
                        AC = (int) (Math.random() * 100 + 1);
                        break;
                    case 9: // Put port
                        int port = memoryInput.nextInt();
                        if (port == 1) {
                            System.out.println(AC);
                        } else if (port == 2) {
                            System.out.print((char) AC);
                        }
                        break;
                    case 10: // AddX
                        AC += X;
                        break;
                    case 11: // AddY
                        AC += Y;
                        break;
                    case 12: // SubX
                        AC -= X;
                        break;
                    case 13: // SubY
                        AC -= Y;
                        break;
                    case 14: // CopyToX
                        X = AC;
                        break;
                    case 15: // CopyFromX
                        AC = X;
                        break;
                    case 16: // CopyToY
                        Y = AC;
                        break;
                    case 17: // CopyFromY
                        AC = Y;
                        break;
                    case 18: // CopyToSp
                        SP = AC;
                        break;
                    case 19: // CopyFromSp
                        AC = SP;
                        break;
                    case 20: // Jump addr
                        PC = memoryInput.nextInt();
                        break;
                    case 21: // JumpIfEqual addr
                        if (AC == 0) {
                            PC = memoryInput.nextInt();
                        }
                        break;
                    case 22: // JumpIfNotEqual addr
                        if (AC != 0) {
                            PC = memoryInput.nextInt();
                        }
                        break;
                    case 23: // Call addr
                        address = memoryInput.nextInt();
                        memoryOutput.println("WRITE");
                        memoryOutput.println(SP--);
                        memoryOutput.println(PC);
                        PC = address;
                        break;
                    case 24: // Ret
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        PC = memoryInput.nextInt();
                        break;
                    case 25: // IncX
                        X++;
                        break;
                    case 26: // DecX
                        X--;
                        break;
                    case 27: // Push
                        memoryOutput.println("WRITE");
                        memoryOutput.println(SP--);
                        memoryOutput.println(AC);
                        break;
                    case 28: // Pop
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        AC = memoryInput.nextInt();
                        break;
                    case 29: // Int
                        if (!kernelMode) {
                            kernelMode = true;
                            memoryOutput.println("WRITE");
                            memoryOutput.println(SP--);
                            memoryOutput.println(PC);
                            PC = 1000; // Assuming interrupt starts at 1000
                        }
                        break;
                    case 30: // IRet
                        kernelMode = false;
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        PC = memoryInput.nextInt();
                        break;
                    case 50: // End
                        memoryOutput.println("EXIT");
                        memoryProcess.waitFor();
                        return;
                        default:
                            System.out.println("Unknown instruction: " + IR);
                            return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            execute(args[0]);
        } else {
            System.out.println("Please provide an input file.");
        }
    }
}
