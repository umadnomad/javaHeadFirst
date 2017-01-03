/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Output {

    public static void main(String[] args) {
        Output o = new Output();
        o.go();
    }

    void go() {

        int y = 7;

        for (int x = 1; x < 8; x++) {
            y++;

            System.out.println("y: " + y);
            System.out.println("x: " + x);
            if (x > 4) {
                System.out.print(++y + " ");
            }
            if (y > 14) {
                System.out.println(" x = " + x);
                break;
            }
        }
    }
}
