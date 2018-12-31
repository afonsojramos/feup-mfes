package PerfectGym;

import PerfectGym.*;
import org.overture.codegen.runtime.*;

import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class CommandLineInterface {

    private static final int EMPTY_LINES = 10;

    private Scanner reader = new Scanner(System.in);
    private PerfectGym perfectGym;

    CommandLineInterface(PerfectGym perfectGym) {
        this.perfectGym = perfectGym;
    }

    public void mainMenu() {
        printLine();
        System.out.println("Welcome to the Perfect Gym!");
        ArrayList<SimpleEntry<String, Callable<Void>>> mainMenuEntries = new ArrayList<>();

        while (true) {
            mainMenuEntries.clear();
            addMainMenuEntries(mainMenuEntries);
            printEntries(mainMenuEntries);
            int option = getUserInput(1, mainMenuEntries.size() - 1);

            try {
                mainMenuEntries.get(option).getValue().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addMainMenuEntries(ArrayList<SimpleEntry<String, Callable<Void>>> mainMenuEntries) {
        if (perfectGym.getLoggedUser() == null) {
            mainMenuEntries.add(new SimpleEntry<>("Create Account", () -> {
                createAccountMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Login", () -> {
                loginMenu();
                return null;
            }));
        } else {
            mainMenuEntries.add(new SimpleEntry<>("Logout", () -> {
                perfectGym.logoutMember();
                loginMenu();
                return null;
            }));
        }

        mainMenuEntries.add(new SimpleEntry<>("Exit", () -> {
            System.exit(0);
            return null;
        }));
    }

    private void createAccountMenu() {
        printEmptyLines(EMPTY_LINES);
        printLine();
        System.out.println("Create account menu");
        System.out.print("First name: ");
        String firstName = reader.nextLine();
        System.out.print("Last name: ");
        String lastName = reader.nextLine();
        System.out.print("Email: ");
        String email = reader.nextLine();
        System.out.print("Password: ");
        String password = reader.nextLine();
        System.out.print("Weight: ");
        int birthYear = Integer.parseUnsignedInt(reader.nextLine());
        System.out.print("Birth Year: ");
        double weight = Double.parseDouble(reader.nextLine());
        System.out.print("Height: ");
        double height = Double.parseDouble(reader.nextLine());
        System.out.print("Gender (Masculine, Feminine): ");
        String gender = reader.nextLine();
        perfectGym.addUser(new Member(firstName, lastName, email, gender, birthYear, weight, height, password));
        printEmptyLines(EMPTY_LINES);
    }

    private void loginMenu() {
        printEmptyLines(EMPTY_LINES);
        printLine();
        System.out.println("Login menu");
        System.out.print("Membership Number: ");
        int number = Integer.parseUnsignedInt(reader.nextLine());
        System.out.print("Password: ");
        String password = reader.nextLine();
        if (PerfectGym.loginMember(number, password)) {
            loggedInMenu();
        } else {
            printEmptyLines(EMPTY_LINES);
            System.out.println("Incorrect Membership Number, Password combination. Please try again.");
        }
        printEmptyLines(EMPTY_LINES);
    }

    private void printLine() {
        System.out.println("====================================================");
    }

    private void printEntries(ArrayList<SimpleEntry<String, Callable<Void>>> menuEntries) {
        for (int i = 0; i < menuEntries.size(); i++) {
            System.out.println((i + 1) + ": " + menuEntries.get(i).getKey());
        }
    }

    private int getUserInput(int bottomBound, int upperBound) {
        System.out.print("Choose an option: ");
        int option = Integer.parseInt(reader.nextLine());

        if (option < bottomBound && option > upperBound) {
            System.out.println("Invalid option");
            option = getUserInput(bottomBound, upperBound);
        }

        return option - 1;
    }

    private void printEmptyLines(int linesToPrint) {
        for (int i = 0; i < linesToPrint; i++) {
            System.out.println();
        }
    }
}