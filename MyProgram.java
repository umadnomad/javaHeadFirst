
public class MyProgram {

    private boolean activated;

    private void setActivated(boolean param) {
        this.activated = param;
    }

    private void sayGoodBye() {
        if (!this.activated) {
            System.out.println("Fine del programma");
        }
    }

    public static void main(String[] args) {
        MyProgram entity = new MyProgram();

        for (String arg : args) {
            switch (arg) {
                case "pirry":
                    System.out.println("Pirreco in da house");
                    entity.setActivated(true);
                    break;
                default:
                    System.out.println("Comando \"" + arg + "\" non riconosciuto");
            }
        }
        entity.sayGoodBye();
    }
}
