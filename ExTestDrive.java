/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static java.lang.System.out;

class MyEx extends Exception {
};

public class ExTestDrive {

    public static void main(String[] args) {

        out.print("t");

        try {
            if (args.length > 0) {
                doRisky(args[0]);
            } else {
                out.println("you must pass this program an argument at runtime.");
                System.exit(1);
            }
        } catch (MyEx e) {
            out.print("a");
        } finally {
            out.print("w");
            out.println("s");
        }
    }

    private static void doRisky(String t) throws MyEx {
        out.print("h");
        if ("yes".equals(t)) {
            throw new MyEx();
        }
        out.print("r");
        out.print("o");
    }

}
