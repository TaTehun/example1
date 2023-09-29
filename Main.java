public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            CPU.execute(args[0]);
        } else {
            System.out.println("Please provide an input file.");
        }
    }
}
