package PerfectGym;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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
        printEmptyLines(EMPTY_LINES);
        System.out.println("Welcome to the Perfect Gym!");
        printLine();
        ArrayList<SimpleEntry<String, Callable<Void>>> mainMenuEntries = new ArrayList<>();

        while (true) {
            mainMenuEntries.clear();
            getMainMenuEntries(mainMenuEntries);
            printEntries(mainMenuEntries);
            int option = getUserInput(1, mainMenuEntries.size() - 1);

            try {
                mainMenuEntries.get(option).getValue().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getMainMenuEntries(ArrayList<SimpleEntry<String, Callable<Void>>> mainMenuEntries) {
        if (perfectGym.getLoggedUser() == null) {
            mainMenuEntries.add(new SimpleEntry<>("Create Account", () -> {
                perfectGym.addUser(createAccountMenu());
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Login", () -> {
                loginMenu();
                return null;
            }));
        } else {
            mainMenuEntries.add(new SimpleEntry<>("Account Management", () -> {
                loggedInMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Logout", () -> {
                perfectGym.logoutMember();
                mainMenu();
                return null;
            }));
        }

        mainMenuEntries.add(new SimpleEntry<>("Exit", () -> {
            System.exit(0);
            return null;
        }));
    }

    private Member createAccountMenu() {
        printEmptyLines(EMPTY_LINES);
        System.out.println("Create Account");
        printLine();
        System.out.print("First name: ");
        String firstName = reader.nextLine();
        System.out.print("Last name: ");
        String lastName = reader.nextLine();
        System.out.print("Email: ");
        String email = reader.nextLine();
        System.out.print("Password: ");
        String password = reader.nextLine();
        System.out.print("Birth Year: ");
        int birthYear = Integer.parseUnsignedInt(reader.nextLine());
        System.out.print("Weight: ");
        double weight = Double.parseDouble(reader.nextLine());
        System.out.print("Height: ");
        double height = Double.parseDouble(reader.nextLine());
        System.out.print("Gender (Masculine, Feminine): ");
        String gender = reader.nextLine();
        printEmptyLines(EMPTY_LINES);

        return new Member(firstName, lastName, email, gender, birthYear, weight, height, password);
    }

    private void loginMenu() {
        printEmptyLines(EMPTY_LINES);
        System.out.println("Login");
        printLine();
        System.out.print("Membership Number: ");
        long number = Long.parseUnsignedLong(reader.nextLine());
        System.out.print("Password: ");
        String password = reader.nextLine();
        if (perfectGym.loginMember(number, password)) {
            mainMenu();
        } else {
            printEmptyLines(EMPTY_LINES);
            System.out.println("Incorrect Membership Number - Password combination. \nPlease try again.\n\nWelcome to the Perfect Gym!");
            printLine();
        }
    }

    private void loggedInMenu() {
        printEmptyLines(EMPTY_LINES);
        System.out.println("Account Management");
        printLine();
        ArrayList<SimpleEntry<String, Callable<Void>>> loggedInMenuEntries = new ArrayList<>();

        while (true) {
            loggedInMenuEntries.clear();
            getLoggedInMenuEntries(loggedInMenuEntries);
            printEntries(loggedInMenuEntries);
            int option = getUserInput(1, loggedInMenuEntries.size() - 1);

            try {
                loggedInMenuEntries.get(option).getValue().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getLoggedInMenuEntries(ArrayList<SimpleEntry<String, Callable<Void>>> loggedInMenuEntries) {
        if (perfectGym.getLoggedUser() != null) {
            loggedInMenuEntries.add(new SimpleEntry<>("Update Weight", () -> {
            	System.out.print("New Weight: ");
                double weight = Double.parseDouble(reader.nextLine());
                ((Member) perfectGym.getLoggedUser()).setWeight(weight);
                printEmptyLines(EMPTY_LINES);
                loggedInMenu();
                return null;
            }));
            loggedInMenuEntries.add(new SimpleEntry<>("Update Height", () -> {
            	System.out.print("New Height: ");
                double height = Double.parseDouble(reader.nextLine());
                ((Member) perfectGym.getLoggedUser()).setHeight(height);
                printEmptyLines(EMPTY_LINES);
                loggedInMenu();
                return null;
            }));
            loggedInMenuEntries.add(new SimpleEntry<>("Get Monthly Due", () -> {
                System.out.print("Your Montlhy due is: " + ((Member) perfectGym.getLoggedUser()).getMonthly());
                reader.nextLine();
                printEmptyLines(EMPTY_LINES);
                loggedInMenu();
                return null;
            }));
            loggedInMenuEntries.add(new SimpleEntry<>("Refer a Friend", () -> {
                perfectGym.addUserReferral(((Member) perfectGym.getLoggedUser()), createAccountMenu());
                printEmptyLines(EMPTY_LINES);
                loggedInMenu();
                return null;
            }));
            loggedInMenuEntries.add(new SimpleEntry<>("Back to Main Menu", () -> {
                printEmptyLines(EMPTY_LINES);
                mainMenu();
                return null;
            }));
        } else {
            mainMenu();
        }
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