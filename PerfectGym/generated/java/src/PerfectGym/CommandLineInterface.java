package PerfectGym;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.overture.codegen.runtime.SeqUtil;
import org.overture.codegen.runtime.SeqUtil;
import org.overture.codegen.runtime.VDMSeq;
import org.overture.codegen.runtime.Utils;

public class CommandLineInterface {

    private static final int EMPTY_LINES = 10;

    private Scanner reader = new Scanner(System.in);
    private PerfectGym perfectGym;

    CommandLineInterface(PerfectGym perfectGym) {
        this.perfectGym = perfectGym;
        initDB();
    }
    
    public void mainMenu() {
        printEmptyLines(EMPTY_LINES);
        if (perfectGym.getLoggedUser() != null)
            System.out.println("Welcome to the Perfect Gym " + perfectGym.getLoggedUser().getName() + "!");
        else 
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

    private void initDB() {
        Professor prof = new Professor("Jose", "Luis", "test@test.com", "Masculine", "a");
        perfectGym.addUser(prof);
        GymClass gclass1 = new GymClass("cycling", "Cycling", "cycling class", 10, prof, "Monday", new GymClass.Time(15, 20), 90);
        GymClass gclass5 = new GymClass("bodyattack", "Body Combat", "bodyattack class", 10, prof, "Tuesday", new GymClass.Time(15, 20), 90);
        GymClass gclass2 = new GymClass("yoga", "Yoga", "yoga class", 10, prof, "Monday", new GymClass.Time(8, 0), 45);
        GymClass gclass3 = new GymClass("zumba I", "Zumba", "zumba class", 10, prof, "Monday", new GymClass.Time(20, 30), 90);
        GymClass gclass4 = new GymClass("cycling II", "Cycling", "cycling class", 10, prof, "Saturday", new GymClass.Time(9, 40), 60);
        GymClass gclass6 = new GymClass("cycling3", "Cycling", "cycling class", 10, prof, "Monday", new GymClass.Time(8, 50), 45);
        perfectGym.addUser(new Member("Claudia", "Rodrigues", "up201508262@fe.up.pt", "Feminine", 1997, 50, 1.67, "qwerty1234"));
        perfectGym.addUser(new Member("Afonso", "Ramos", "up201506239@fe.up.pt", "Masculine", 1950, 75, 1.91, "qwerty1234"));
        perfectGym.addClass(gclass1);
        perfectGym.addClass(gclass2);
        perfectGym.addClass(gclass3);
        perfectGym.addClass(gclass4);
        perfectGym.addClass(gclass5);
        perfectGym.addClass(gclass6);
        
    }

    private void getMainMenuEntries(ArrayList<SimpleEntry<String, Callable<Void>>> mainMenuEntries) {
        if (perfectGym.getLoggedUser() == null) {
            mainMenuEntries.add(new SimpleEntry<>("Create Account", () -> {
                perfectGym.addUser(createAccountMenu());
                System.out.print("User Created with ID: " + perfectGym.getUsers().size());
                reader.nextLine();
                mainMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Login", () -> {
                loginMenu();
                return null;
            }));
        } else {
            mainMenuEntries.add(new SimpleEntry<>("Get Activity Schedule", () -> { // TODO
                System.out.print("Activity: ");
                String activity = reader.nextLine();
                System.out.print(perfectGym.getSchedule2(activity));
                reader.nextLine();
                mainMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Get Day Schedule", () -> {
                System.out.print("Day of the Week: ");
                String day = reader.nextLine();
                System.out.print("\n  |    Name    |   Type   |     Description     |   Capacity   |  Professor  |    Date    |  Time  | Duration\n");
                for (Iterator iter = SeqUtil.inds((VDMSeq) perfectGym.getSchedule(day).get(day)).iterator(); iter.hasNext(); ) {
                    Number i = (Number) iter.next();
                    
                    System.out.print(i + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getName(), "    Name    ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getType().toString(), "   Type   ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDescription(), "     Description     ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getCapacity().toString(), "   Capacity   ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getProfessor().getName(), "  Professor  ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDate().toString(), "    Date    ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getTime().hour 
                        + "h" 
                        + ((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getTime().minute, "  Time  ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDuration().toString(), 10) + "\n");
                }
                reader.nextLine();
                mainMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Get All Classes", () -> { //TODO
                System.out.print("Day of the Week: ");
                String day = reader.nextLine();
                System.out.print("\n  |    Name    |   Type   |     Description     |   Capacity   |  Professor  |    Date    |  Time  | Duration\n");
                for (Iterator iter = SeqUtil.inds((VDMSeq) perfectGym.getSchedule(day).get(day)).iterator(); iter.hasNext(); ) {
                    Number i = (Number) iter.next();
                    
                    System.out.print(i + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getName(), "    Name    ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getType().toString(), "   Type   ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDescription(), "     Description     ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getCapacity().toString(), "   Capacity   ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getProfessor().getName(), "  Professor  ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDate().toString(), "    Date    ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getTime().hour 
                        + "h" 
                        + ((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getTime().minute, "  Time  ".length())
                        + " | " + 
                        printWithWhitespace(((GymClass) Utils.get((VDMSeq) perfectGym.getSchedule(day).get(day), i)).getDuration().toString(), 10) + "\n");
                }
                reader.nextLine();
                mainMenu();
                return null;
            }));
            mainMenuEntries.add(new SimpleEntry<>("Enroll in Class", () -> { // TODO
                System.out.print("Activity: ");
                String activity = reader.nextLine();
                System.out.print(perfectGym.getSchedule2(activity));
                reader.nextLine();
                mainMenu();
                return null;
            }));
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
        System.out.println(perfectGym.getLoggedUser().getClass().getSimpleName() + " Account Management");
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
            if (perfectGym.getLoggedUser().getClass().getSimpleName() == "Member"){
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
                loggedInMenuEntries.add(new SimpleEntry<>("Update Mobile", () -> {
                    System.out.print("New Mobile Number: ");
                    int height = Integer.parseInt(reader.nextLine());
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
                
            } else if (perfectGym.getLoggedUser().getClass().getSimpleName() == "Professor") {

            }
            loggedInMenuEntries.add(new SimpleEntry<>("Update Mobile", () -> {
                System.out.print("New Mobile Number: ");
                int height = Integer.parseInt(reader.nextLine());
                ((Member) perfectGym.getLoggedUser()).setHeight(height);
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

    private String printWithWhitespace(String toPrint, int space) {
        String white = "";
        for (int i = toPrint.length() ; i < space - 2 ; i++){
            white = white + " ";
        }
        return toPrint + white;
    }
}