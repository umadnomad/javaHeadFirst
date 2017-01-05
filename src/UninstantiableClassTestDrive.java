/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nomad
 */
public class UninstantiableClassTestDrive {

    public static void main(String[] args) {
        
        UninstantiableClass.statictalk("static talk");
        
        UninstantiableClass miracle = UninstantiableClass.getInstance();
        
        miracle.talk("Miracle: non static talk");
        if (miracle instanceof UninstantiableClass) {
            System.out.println("System: man, he is fo real");
        }
    }
}
