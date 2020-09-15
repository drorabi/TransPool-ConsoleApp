package ui;

import engine.converted.classes.Trip;
import engine.exceptions.*;
import engine.ui.Engine;

import javax.xml.bind.JAXBException;
import java.util.*;

public class UIMain {
    static final private int EXIT = 7;
    static final private String SystemName = "TransPool";
    static private boolean validInput;
    static private Scanner scanner;
    static private Engine engine;
    static private boolean loaded=false;
    UIMain() {
        scanner = new Scanner(System.in);
        engine = new Engine();
    }

    public static void main(String[] args) {
        int inputMenu = 0;
        UIMain systemUI = new UIMain();
        System.out.println("Welcome to " + SystemName + " system!");
        while (inputMenu != EXIT) {
            do {
                System.out.println(SystemName + " Menu:");
                printMenu();
                System.out.println("Please enter the number of the wanted menu option, and then press enter: \n");
                try {
                    inputMenu = scanner.nextInt();
                    validInput = true;
                } catch (InputMismatchException exception) {
                    System.out.println("you didn't enter a number, please try again");
                    validInput = false;
                    System.out.println(scanner.nextLine());
                }
            } while (validInput == false);

            try {
                activateChoice(inputMenu);
            } catch (NoInformationInTheSystem e) {
               System.out.println(e.getMessage());
            }
        }
    }

    private static void activateChoice(int inputMenu) throws NoInformationInTheSystem { // and exception in case menu option 1 is not first
        if(!loaded){
            if(inputMenu!=1 && inputMenu!=7)
                throw new NoInformationInTheSystem();
        }
        switch (inputMenu) {
            case 1:
                loadData();
                break;
            case 2:
                makerRideRequest();
                break;
            case 3:
                displayOfferedRides();
                break;
            case 4:
                displayRequestedRides();
                break;
            case 5:
                try {
                    makeRideMatch();
                } catch (NoAvailableRequests e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 6:
                InsertNewTrip();
                break;
            case EXIT:
                System.out.println("Goodbye!");
                System.exit(EXIT);
                break;
            default:
                System.out.println("your choice doesn't match any of the menu options, please try again");
                validInput = false;
                break;
        }
    }


//---------------------------------------------------------------------------------------------------

    private static void makeRideMatch() throws NoAvailableRequests {
        try {
            engine.checkIfEmpty();
        } catch (NoTripsInTheSystem | InvalidDisplayNoRequests e) {
            System.out.println(e.getMessage());
            return;
        }
        Set<Trip> matchingTrips = new HashSet<>();
        String input = "";
        int choiceRequest = 0;
        int choiceRide=0;
        String cleanBuffer = scanner.nextLine();
        boolean flag = false;
        boolean validNumber = false;


        while (!flag) {
            while (!validNumber) {
                displayNotMatchedRequested();
                System.out.println("Please enter the number of the ride request you wish to find match for\n");
                choiceRequest = receivingInt();
                cleanBuffer = scanner.nextLine();
                try {
                    engine.checkRequest(choiceRequest);
                    validNumber = true;
                } catch (InvalidRequestName e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Please enter the number of offered rides you want to see\n");
            int numberOfRides = receivingInt();
            cleanBuffer = scanner.nextLine();

            try {
                matchingTrips = engine.findMatchingTrips(choiceRequest, numberOfRides);
                flag = true;
            } catch (InvalidRequestName | InvalidRideStartDay | InvalidRideStartHour | InvalidRideStartMinutes | NoMatchingRides e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different serial number\nPress any other key to return to main menu\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }


        validNumber=false;
        flag = false;
        while (!flag) {
            System.out.println("Matching trips:\n");
            System.out.println(engine.displayMtachingTrips(matchingTrips, choiceRequest));
            System.out.println("Press 1 to choose a ride\nPress any other key to return to main menu\n");
            input = scanner.nextLine();
            if (!input.trim().equals("1"))
                return;
            while (!validNumber) {
                System.out.println("Enter the serial number of the ride you want to carpool\n");
                choiceRide = receivingInt();
                cleanBuffer = scanner.nextLine();
                try {
                    engine.checkTrip(choiceRide, matchingTrips);
                    validNumber = true;
                } catch (InvalidChoiceForSerialNumber e) {
                    System.out.println(e.getMessage());
                }
            }

            try {
                engine.updateTrip(choiceRide, choiceRequest);
                engine.upDateRequest(choiceRequest, choiceRide);
                flag = true;
            } catch (InvalidChoiceForSerialNumber e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different serial number\nPress any other key to return to main menu\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }
        System.out.println("Match created successfully!\n");
    }

//---------------------------------------------------------------------------------------------------

    private static void makerRideRequest() {

        String from = "";
        String to = "";
        int minutes = 0;
        int hour = 8;
        boolean flag = false;
        boolean byDeparture=true;
        String cleanBuffer = scanner.nextLine();
        String input;
        System.out.println("Please enter your name \n");
        String name = scanner.nextLine();

        displayAllStations();

        while (!flag) {
            System.out.println("Please enter the station you wish to depart from\n");
            from = scanner.nextLine();
            try {
                engine.checkStation(from.trim().toUpperCase());
                flag = true;
            } catch (InvalidStationsNameRequest e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different station\nPress any other key to return to main menu\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }

        flag = false;

        while (!flag) {
            System.out.println("Please enter the destination station\n");
            to = scanner.nextLine();
            try {
                engine.checkStation(to.trim().toUpperCase());
                flag = true;
            } catch (InvalidStationsNameRequest e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different station\nPress any other key to return to main menu\n");;
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }

        System.out.println("Press 1 to set request by departure\nPress 2 to set request by arrival\nPress any other key to return to main menu");
        input=scanner.nextLine();
        if(input.trim().equals("1"))
            byDeparture=true;
        else if(input.trim().equals("2"))
            byDeparture=false;
        else
            return;

        flag = false;
        while (!flag) {
            hour = chooseHour();
            minutes = chooseMinutes();
            try {
                engine.checkTime(hour, minutes);
                flag = true;
            } catch (InvalidRequestMinutes | InvalidRequestHour e) {
                System.out.println(e.getMessage());
                System.out.println("if you want to choose a different time press 1\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }

        try {
            engine.addRideRequest(name.trim(), from.trim().toUpperCase(), to.trim().toUpperCase(),/* recurrences, day,*/ hour, minutes, byDeparture);
            System.out.println("Request created successfully!\n");
        } catch (InvalidRideStartHour | InvalidRideStartDay | InvalidRideStartMinutes | InvalidRequestDepartureDestination e) {
            System.out.println(e.getMessage());
        }


    }

//---------------------------------------------------------------------------------------------------

    //displays-----------------


    private static void displayOptionalStations(String from, String route) throws NoOptionalStationsToGoNext {
        System.out.println(engine.displayOptionalStations(from, route));
    }

    private static void displayOfferedRides() {
        try {
            System.out.println(engine.displayOfferedRides());
        } catch (InvalidDisplayNoRides e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayRequestedRides() {
        try {
            System.out.println(engine.displaydRidesRequests());
        } catch (InvalidDisplayNoRequests e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayNotMatchedRequested() throws NoAvailableRequests {
        try {
            System.out.println(engine.displayNotMatchedRequests());
        } catch (InvalidDisplayNoRequests e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayAllStations() {
        System.out.println(engine.displayAllStations());
    }

    private static void printMenu() {
        System.out.println("1) Load data to the system.\n");
        System.out.println("2) Request a ride.\n");
        System.out.println("3) Display the status of all the offered rides.\n");
        System.out.println("4) Display the status of all the requested rides.\n");
        System.out.println("5) Match a ride request to an offered ride.\n");
        System.out.println("6) Insert a new transpool trip.\n");
        System.out.println("7) Exit the system.\n");
    }

    //load XML file-----------------

    private static void loadData() {
        System.out.println("Please enter the full path of the XML file (.xml) you want to load: \n"); // add example of path
        do {
            try {
                String XMLName = scanner.next(); // add exceptions
                validInput = true;
                engine.loadData(XMLName);
                loaded=true;
            } catch (InputMismatchException e) {
                System.out.println("you didn't enter a string, please try again\n");
                validInput = false;
            } catch (InvalidStationsCoordinates | InvalidRoute | InvalidPathDepatureDestination | InvalidMap | InvalidStationsNames
                    | InvalidRideStartHour | InvalidStationsLocation | InvalidPathNames | FileDoesNotExist
                    | NotXmlFile | JAXBException | InvalidRideStartDay | InvalidRouteThroughTheStationTwice e) {
                System.out.println(e.getMessage());
                validInput = false;
                System.out.println("Please enter the full path of the XML file again");
            }
        } while (validInput == false);
        System.out.println("Data loaded successfully!");
    }

    //NEW TRIP---------------------------------------

    private static void InsertNewTrip() {

        boolean toContinue = true;
        int capacity=0;
        String from = "";
        String next = "";
        String curr = "";
        String route = "";
        int ppk=0;
        int minutes=0;
        int hour=0;
        boolean flag = false;
        String cleanBuffer = scanner.nextLine();
        String input;
        System.out.println("Please enter your name \n");
        String name = scanner.nextLine();

        displayAllStations();


        while (!flag) {
            System.out.println("Please type the station you wish to depart from\n");
            from = scanner.nextLine().trim().toUpperCase();
            try {
                engine.checkStation(from);
                route = from;
                flag = true;
            } catch (InvalidStationsNameRequest e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different station\nPress any other key to return next main menu\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }

        flag = false;
        curr = from;

        while (toContinue) {

            try {
                int counter = 0;
                while (!flag) {
                    System.out.println("Please type the next station on your trip\n");

                    displayOptionalStations(curr, route);
                    next = scanner.nextLine().toUpperCase();
                    try {
                        engine.checkStation(next.trim());
                        engine.checkTrail(curr, next);
                        engine.checkRoute(next, route);
                        counter++;
                        flag = true;
                        route = route + "," + next;
                    } catch (InvalidStationsNameRequest | InvalidRouteThroughTheStationTwice | InvalidPathForTrip e) {
                        System.out.println(e.getMessage());
                        System.out.println("Press 1 to choose a different station\nPress any other key next return next main menu\n");
                        input = scanner.nextLine();
                        if (!input.trim().equals("1"))
                            return;
                    }
                }
            } catch (NoOptionalStationsToGoNext e) {
                System.out.println(e.getMessage());
                System.out.println(" Press 1 to choose " + next + " as your destination\nPress any other key next return next main menu\n");
                input = scanner.nextLine();
                if (input.trim().equals("1")) {
                    toContinue = false;
                    break;
                }
                else return;
            }
            System.out.println("Press 1 to choose the next station on your trip\nPress 2 to choose " + next + " as your destination\nPress any other key next return next main menu\n");
            input = scanner.nextLine();
            if (input.trim().equals("1"))
                flag=false;
            else if(input.trim().equals("2"))
                toContinue = false;
            else
                return;
            curr = next.trim();
        }

        flag=false;
        while(!flag) {
            hour = chooseHour();
            minutes = chooseMinutes();
            try {
                engine.checkTime(hour, minutes);
                flag = true;
            } catch (InvalidRequestMinutes | InvalidRequestHour e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to choose a different time\nPress any other key to return to main menu\n");
                input = scanner.nextLine();
                if (!input.trim().equals("1"))
                    return;
            }
        }

        ppk=choosePPK();
        capacity=chooseCapacity();

            engine.addTrip(name.trim(),/* recurrences, day,*/ hour, minutes, route.trim(), ppk, capacity);
            System.out.println("Trip created successfully!\n");
    }



    private static int chooseHour() {
        int hour;
        System.out.println("Please enter the number of hour in a 24 hours number\nfor example: to choose 5pm press 17, next choose 5am press 5\n");
        hour = receivingInt();
        if (hour == 24)
            hour = 0;
        return hour;
    }


    private static int chooseMinutes() {

        System.out.println("Please enter the number of minutes \nFYI:the system supports only number of minutes that ends with 5 or 0, for example if you enter 12 we will change it to 10.\n");
        int minutes = receivingInt();
        String cleanBuffer = scanner.nextLine();
        if (minutes % 5 == 0)
            minutes = minutes;
        else if (minutes % 5 < 3)
            minutes = minutes - (minutes % 5);
        else
            minutes = minutes + (5 - minutes % 5);
        return minutes;
    }


    private static int choosePPK(){
        int ppk=0;
        boolean flag=false;
        while (!flag) {
            System.out.println("Please enter the trip's price per kilometer\n");
            ppk = receivingInt();
            if (ppk < 0)
                System.out.println("please choose a positive number\n");
            else
                flag = true;
        }
        return ppk;
    }


   private static int chooseCapacity(){
        int capacity=0;
        boolean flag=false;
        while (!flag) {
            System.out.println("Please enter the maximum number of passengers you want to pick up\n");
            capacity = receivingInt();
            if (capacity < 0)
                System.out.println("please choose a positive number\n");
            else
                flag = true;
        }
        return capacity;
    }

    private static int receivingInt(){
        int number=0;
        boolean flag=false;
        while(!flag) {
            try {
                number = scanner.nextInt();
                flag=true;
            } catch (InputMismatchException exception) {
                System.out.println("you didn't enter a number, please enter a number");
                scanner.nextLine();
            }
        }
    return number;
    }
}



    /*   private String chooseRecueeences() {
           System.out.println("Please choose one of the three options by pressing the option number:\n1.One Time \n2.Daily \n3.Weekly \n4.Monthly");
           int choice = scanner.nextInt();
           String recurrences;
           switch (choice) {
               case 1:
                   return "One Time";
               case 2:
                   return "Daily";
               case 3:
                   return "Weekly";
               case 4:
                   return "Monthly";
               default:
                   return "One Time";
           }
       }


    private int chooseDay() {
        System.out.println("Please choose one of the day you want to departure:\n1.Sunday \n2.Monday \n3.Tuesday \n4.Wednesday \n5.Thursday \n6.Friday \n7.Saturday");
        int choice = scanner.nextInt();
        return choice;
    }*/


