import java.util.ArrayList;
import java.util.Scanner;

import Items.Item;

public class GameLogic {
    static Scanner scan = new Scanner(System.in);

    static Player player;
    static boolean isRunning;
    static int place = 0;

    public static ArrayList<Place> initPlaces() {
        ArrayList<Place> places = new ArrayList<>();

        Place town = new Place(0, "Town", new Enemy[] {
            new Enemy("Wild Rabbit", 10),
            new Enemy("Wild Rat", 10),
            new Enemy("Wild Cat", 10),
            new Enemy("Wild Dog", 10)
        }, new String[] { "Hideout", "Hideout", "Battle", "Battle" });

        Place forest = new Place(1, "Forest", new Enemy[] {
            new Enemy("Wolf", 25),
            new Enemy("Bear", 30),
            new Enemy("Squirel", 10),
            new Enemy("WereWolf", 50)
        }, new String[] { "Merchant", "Rest", "Battle", "Battle" });

        Place castle = new Place(2, "Castle", new Enemy[] {
            new Enemy("Mimic", 50),
            new Enemy("Swordman", 50),
            new Enemy("Spearman", 50)
        }, new String[] { "Merchant", "Merchant", "Battle", "Battle" });

        Place throne = new Place(3, "Throne", new Enemy[]{ new Enemy("EVIL EMPEROR", 300)} );

        places.add(town);
        places.add(forest);
        places.add(castle);
        places.add(throne);
        return places;
    }

    public static int answer(String question, String[] responses) {
        System.out.println(question);
        for (int i = 0; i < responses.length; i++) {
            System.out.println(responses[i]);
        }
        int input = readInt("-> ", responses.length);
        scan.nextLine();
        return input;
    }

    public static int readInt(String prompt, int userChoice) {
        int input;

        do {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scan.next());
            } catch (Exception e) {
                input = -1;
                System.out.println("Please select a choice !");
            }
        } while (input < 1 || input > userChoice);
        return input;
    }

    public static void clearConsole() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    public static void printSeparator() {
        for (int i = 0; i < 20; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void printHeading(String title) {
        printSeparator();
        System.out.println(title);
        printSeparator();
    }

    public static void toContinue() {
        System.out.println("\nEnter anything to continue ...");
        //System.out.println(scan.nextLine());
        System.out.println(scan.next().compareTo("\r"));
    }

    public static void printMenu() {
        clearConsole();
        printHeading(initPlaces().get(place).name);
        System.out.println("Choose an action: ");
        printSeparator();
        System.out.println("1 - Continue on your journey");
        System.out.println("2 - Character Information");
        System.out.println("3 - Exit Game");
    }

    public static void characterInfo() {
        clearConsole();
        player.getInformation();
        toContinue();
    }

    public static void start() {
        boolean nameSet = false;
        String name;
        clearConsole();
        printSeparator();
        System.out.println("AGE OF THE EVIL EMPEROR");
        System.out.println("TEXT RPG BY FRED FOR CODESTUDENT.NET");
        printSeparator();
        toContinue();

        do {
            clearConsole();
            printHeading("What's your name Adventurer?");
            name = scan.next();
            clearConsole();
            printHeading("Your name is " + name + ".\nIs that correct?");
            System.out.println("1 - Yes!");
            System.out.println("2 - No! I want to change my name.");

            int input = readInt("-> ", 2);
            if (input == 1)
                nameSet = true;
        } while (!nameSet);

        Story.printIntro();

        player = new Player(name);

        Story.printIntro();

        isRunning = true;
        gameLoop();
    }

    static void gameLoop() {
        while (isRunning && player.isAlive) {
            printMenu();
            int input = readInt("-> ", 3);
            switch (input) {
                case 1:
                    continueJourney();
                    break;
                case 2:
                    characterInfo();
                    break;
                default:
                    isRunning = false;
                    break;
            }
        }
    }

    public static void continueJourney() {
        checkAct();
        if (place != 3) {
            initPlaces().get(place).randomEncounter(player);
        }
    }

    public static void finalBattle() {
        initPlaces().get(3).randomBattle(player);
        System.out.println("THIS IS THE END");
        isRunning = false;
    }

    /**
     * TO MOVE
     */

    public static void shop() {
        clearConsole();
        System.out.println("You meet a mysterious stranger.\nHe offers you something:");
        System.out.println("(" + player.gold + " left(s).)");
        /**
         * On cree les items que nous vend le marchand
         */
        Item[] items = {
            new Item("Health Potion", (int) (Math.random() * (20 - 5 + 1) + 5), 1),
            new Item("Herb", 5)
        };
        
        String[] res = new String[items.length];

        for (int i = 0; i < items.length; i++) {
            printSeparator();
            res[i] = i+1 + " - " + items[i].name + "!";
            System.out.println(items[i].name + " for " + items[i].value +"!");
        }
        printSeparator();

        int input = answer("Do you want anything ?", res);
        clearConsole();
        if (player.gold >= items[input-1].value) {
            printHeading("You bought a " + items[input-1].name + " for " + items[input-1].value + " gold.");
            // On ajoute cet item au player
            player.addItem(items[input-1]);
            player.gold -= items[input-1].value;
        } else {
            printHeading("You don't have enought gold to buy this...");
        }
        toContinue();
    }

    public static void checkAct() {
        if (player.xp >= 10 && place == 0) {
            place = 1;
            System.out.println("SECOND OUTRO");
            player.chooseTrait();
            System.out.println("SECOND ACT INTRO");
        } else if (player.xp >= 50 && place == 1) {
            place = 2;
            System.out.println("SECOND ACT OUTRO");
            player.chooseTrait();
            System.out.println("THIRD ACT INTRO");
        } else if (player.xp >= 100 && place == 2) {
            place = 3;
            System.out.println("THIRD ACT OUTRO");
            player.chooseTrait();
            System.out.println("FOURTH ACT INTRO");
            finalBattle();
        }
    }
}
