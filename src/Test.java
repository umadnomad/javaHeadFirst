/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Test {

    public static void main(String[] args) {
        int x = 0;
        int y = 0;

        while (x < 5) {

            x = x + 1;
            y = y + x;

            System.out.print(x + "" + y + " ");
            x = x + 1;
        }
    }
}
