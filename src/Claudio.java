/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nomad
 */
class FegatoNotFoundException extends Exception {}

public class Claudio {

    private final static boolean FEGATO = false;
    private boolean merdaineccesso = true;

    public static void main(String[] args) {
        Claudio claudio = new Claudio();

        try {
            claudio.mangiare();
        } catch (FegatoNotFoundException ex) {
            claudio.merdaineccesso = false;
        } finally {
            claudio.piangi();
            claudio.stai_bene();
            System.out.println("FINISH PROJECT SUCCESSFULLY");
            System.exit(0);
        }
    }

    private void mangiare() throws FegatoNotFoundException {
        if (!FEGATO) {
            throw new FegatoNotFoundException();
        }
    }

    private void piangi() {
    }

    private void stai_bene() {
    }

}
