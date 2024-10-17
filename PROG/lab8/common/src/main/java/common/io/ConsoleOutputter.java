package common.io;

/**
 * class with static methods for printing
 */
public interface ConsoleOutputter {
    static void print(Object o) {
        System.out.println(o.toString());
    }

    static void printErr(Object o) {
        System.out.println("Err: " + o.toString());
    }
}
