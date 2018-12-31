package PerfectGym;

@SuppressWarnings("all")
public class Main {
  public static void main(String[] args) {
    PerfectGym perfectGym = new PerfectGym();
    
    CommandLineInterface cli = new CommandLineInterface(perfectGym);
    cli.mainMenu();

  }
}
