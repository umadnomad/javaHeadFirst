/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nomad
 */
public class UninstantiableClass implements nomad_interface {

    static { System.out.println(UninstantiableClass.class + "class loaded"); }
    
    private UninstantiableClass() {
    }

    public static UninstantiableClass getInstance() {

        UninstantiableClass instance = new UninstantiableClass();

        return instance;
    }

    @Override
    public void talk(String what_to_say) {

        try {
            System.out.println(what_to_say);
        } catch (Exception e) {
            System.out.println("no input, exiting");
            System.exit(0);
        }

    }

    public static void statictalk(String what_to_say) {
        try {
            System.out.println(what_to_say);
        } catch (Exception e) {
            System.out.println("no input, exiting");
            System.exit(0);
        }
    }
    
    @Override
    public void random() {
    }

}
