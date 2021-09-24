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

        Place town = new Place("Town", 0, new Enemy[] {
            new Enemy("Rat", 10),
            new Enemy("Little boy",10)
        }, new String[]{"Battle", "Battle", "Battle"});

        Place forest = new Place("Forest", 1, new Enemy[] {
            new Enemy("Wolf", 20),
            new Enemy("WereWolf", 50)
        }, new String[]{"Shop", "Rest", "Battle", "Battle"});

        Place castle = new Place("Castle", 2, new Enemy[] {
            new Enemy("Swordman", 30),
            new Enemy("Spearman", 30),
            new Enemy("Mimic", 70)
        }, new String[]{"Battle", "Battle", "Battle", "Shop"});

        Place throne = new Place("Throne", 3, new Enemy[] {
            new Enemy("EVIL EMPEROR", 300)
        });

        places.add(town);
        places.add(forest);
        places.add(castle);
        places.add(throne);
        return places;
    }

    public static int readInt(String prompt, int userChoice) {
        int input;

        do {
            System.out.println(prompt);
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
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }

    public static void printSeparator(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void printHeading(String title) {
        printSeparator(30);
        System.out.println(title);
        printSeparator(30);
    }

    public static void toContinue() {
        System.out.println("\nEnter anything to continue ...");
        System.out.println(scan.next().compareTo("\r"));
    }

    public static void printMenu() {
        clearConsole();
        printHeading(initPlaces().get(place).name);
        System.out.println("Choose an action: ");
        printSeparator(20);
        System.out.println("(1) Continue on your journey");
        System.out.println("(2) Character Information");
        System.out.println("(3) Exit Game");
    }

    public static int answer(String question, String[] responses) {
        System.out.println(question);
        for (int i = 0; i < responses.length; i++) {
            System.out.println(responses[i]);
        }
        int input = readInt("-> ", responses.length);
        return input;
    }

    public static void start() {
        boolean nameSet = false;
        String name;
        clearConsole();
        printSeparator(70);
        System.out.println("AGE OF THE EVIL EMPEROR");
        System.out.println("TEXT RPG BY FRED FOR CODESTUDENT.NET");
        printSeparator(70);
        toContinue();

        do {
            clearConsole();
            printHeading("What's your name Adventurer?");
            name = scan.next();
            clearConsole();
            printHeading("Your name is " + name + ".\nIs that correct?");
            System.out.println("(1) Yes!");
            System.out.println("(2) No! I want to change my name.");

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

    public static void finalBattle() {
        initPlaces().get(place).randomBattle(player);
        System.out.println("THIS IS THE END");
        isRunning = false;
    }

    static void gameLoop() {
        while (isRunning) {
            printMenu();
            int input = readInt("-> ", 3);
            switch (input) {
                case 1:
                    continueJourney();
                    break;
                case 2:
                    clearConsole();
                    player.getInformation();
                    toContinue();
                    break;
                default:
                    isRunning = false;
                    break;
            }
        }
    }

    /**
     * TO MOVE
     */

    public static void shop() {
        clearConsole();
        printHeading("You meet a mysterious stranger.\nHe offers you something:");
        Item potions = player.getItem("Potion HP");
        int price = (int) (Math.random() * (10 + potions.quantite * 3) + 10 + potions.quantite);
        System.out.println("- " + potions.name + ": " + price + " gold.");
        printSeparator(15);
        int input = answer("Do you want to buy one?", new String[] { "(1) Yes!", "(2) No thanks" });
        if (input == 1) {
            clearConsole();
            if (player.gold >= price) {
                printHeading("You bought a magical potion for " + price + " gold.");
                if(potions.quantite == 0) {
                    potions.quantite = 1;
                    player.items.add(potions);
                } else {
                    potions.quantite++;
                }
                player.gold -= price;
            } else {
                printHeading("You don't have enought gold to buy this...");
            }
            toContinue();
        }
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

    public static void continueJourney() {
        checkAct();
        if (place != 3) {
            initPlaces().get(place).randomEncounter(player);
        }
    }
}
