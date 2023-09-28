import java.io.*;
import java.util.Scanner;

public class CPU {
    private static int PC, SP, IR, AC, X, Y;
    private static boolean kernelMode = false;

    public static void execute() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "Memory");
            Process memoryProcess = pb.start();

            try (Scanner memoryInput = new Scanner(memoryProcess.getInputStream());
                 PrintWriter memoryOutput = new PrintWriter(memoryProcess.getOutputStream(), true)) {

                while (true) {
                    IR = memoryInput.nextInt();

                    switch (IR) {
                        case InstructionSet.LOAD_VALUE:
                            AC = memoryInput.nextInt();
                            break;
                    case InstructionSet.LOAD_ADDR:
                        int address = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.LOAD_IND_ADDR:
                        address = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        int indirectAddress = memoryInput.nextInt();
                        memoryOutput.println("READ");
                        memoryOutput.println(indirectAddress);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.LOAD_IDX_X_ADDR:
                        address = memoryInput.nextInt() + X;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.LOAD_IDX_Y_ADDR:
                        address = memoryInput.nextInt() + Y;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.LOAD_SP_X:
                        address = SP + X;
                        memoryOutput.println("READ");
                        memoryOutput.println(address);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.STORE_ADDR:
                        address = memoryInput.nextInt();
                        memoryOutput.println("WRITE");
                        memoryOutput.println(address);
                        memoryOutput.println(AC);
                        break;
                    case InstructionSet.GET:
                        AC = (int) (Math.random() * 100 + 1);
                        break;
                    case InstructionSet.PUT_PORT:
                        int port = memoryInput.nextInt();
                        if (port == 1) {
                            System.out.println(AC);
                        } else if (port == 2) {
                            System.out.print((char) AC);
                        }
                        break;
                    case InstructionSet.ADD_X:
                        AC += X;
                        break;
                    case InstructionSet.ADD_Y:
                        AC += Y;
                        break;
                    case InstructionSet.SUB_X:
                        AC -= X;
                        break;
                    case InstructionSet.SUB_Y:
                        AC -= Y;
                        break;
                    case InstructionSet.COPY_TO_X:
                        X = AC;
                        break;
                    case InstructionSet.COPY_FROM_X:
                        AC = X;
                        break;
                    case InstructionSet.COPY_TO_Y:
                        Y = AC;
                        break;
                    case InstructionSet.COPY_FROM_Y:
                        AC = Y;
                        break;
                    case InstructionSet.COPY_TO_SP:
                        SP = AC;
                        break;
                    case InstructionSet.COPY_FROM_SP:
                        AC = SP;
                        break;
                    case InstructionSet.JUMP_ADDR:
                        PC = memoryInput.nextInt();
                        break;
                    case InstructionSet.JUMP_IF_EQUAL:
                        if (AC == 0) {
                            PC = memoryInput.nextInt();
                        }
                        break;
                    case InstructionSet.JUMP_IF_NOT_EQUAL:
                        if (AC != 0) {
                            PC = memoryInput.nextInt();
                        }
                        break;
                    case InstructionSet.CALL_ADDR:
                        address = memoryInput.nextInt();
                        memoryOutput.println("WRITE");
                        memoryOutput.println(SP--);
                        memoryOutput.println(PC);
                        PC = address;
                        break;
                    case InstructionSet.RET:
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        PC = memoryInput.nextInt();
                        break;
                    case InstructionSet.PUSH:
                        memoryOutput.println("WRITE");
                        memoryOutput.println(SP--);
                        memoryOutput.println(AC);
                        break;
                    case InstructionSet.POP:
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        AC = memoryInput.nextInt();
                        break;
                    case InstructionSet.INT:
                        if (!kernelMode) {
                            kernelMode = true;
                            memoryOutput.println("WRITE");
                            memoryOutput.println(SP--);
                            memoryOutput.println(PC);
                            PC = 1000; // Assuming interrupt starts at 1000
                        }
                        break;
                    case InstructionSet.IRET:
                        kernelMode = false;
                        memoryOutput.println("READ");
                        memoryOutput.println(++SP);
                        PC = memoryInput.nextInt();
                        break;
                    case InstructionSet.END:
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        execute();
    }
}
