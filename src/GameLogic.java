import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    static Scanner scan = new Scanner(System.in);

    static Player player;
    static boolean isRunning;
    static int place = 0;
    static String[] encounters = { "Battle", "Battle", "Battle", "Shop", "Battle" };

    public static ArrayList<Place> initPlaces() {
        ArrayList<Place> places = new ArrayList<>();

        Place town = new Place(0, "Town", new Enemy[] { new Enemy("Lapin", 10), new Enemy("Rat", 10),
                new Enemy("Chat", 10), new Enemy("Chien", 10) });

        Place forest = new Place(1, "Forest", new Enemy[] { new Enemy("Wolf", 25), new Enemy("Bear", 30),
                new Enemy("Squirel", 10), new Enemy("WereWolf", 50) });

        Place castle = new Place(2, "Castle", new Enemy[] { new Enemy("Mimic", 50), new Enemy("Swordman", 50),
                new Enemy("Spearman", 50), new Enemy("Axeman", 50), new Enemy("Batman", 80) });

        Place ending = new Place(3, "Final", new Enemy[]{ new Enemy("EVIL EMPEROR", 300)} );

        places.add(town);
        places.add(forest);
        places.add(castle);
        places.add(ending);
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
        for (int i = 0; i < 5; i++) {
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
        System.out.println(scan.nextLine());
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
        if (initPlaces().get(place).name != "Final") {
            randomEncounter();
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

    public static void randomEncounter() {
        int encounter = (int) (Math.random() * encounters.length);
        switch (encounters[encounter]) {
            case "Battle":
                initPlaces().get(place).randomBattle(player);
                break;
            case "Rest":
                player.rest();
                break;
            default:
                shop();
                break;
        }
    }

    public static void shop() {
        clearConsole();
        int price = (int) (Math.random() * (10 + player.potions * 3) + 10 + player.potions);
        int input = answer("You meet a mysterious stranger.\nHe offers you something:\n- Magic Potion: " + price + " gold.\nDo you want to buy one?", new String[] { "1 - Yes!", "2 - No thanks" });

        if (input == 1) {
            clearConsole();
            if (player.gold >= price) {
                printHeading("You bought a magical potion for " + price + " gold.");
                player.potions++;
                player.gold -= price;
            } else {
                printHeading("You don't have enought gold to buy this...");
            }
            toContinue();
        }
    }

    public static void checkAct() {
        // for (Enemy e : initPlaces().get(place).bestiaires) {
        //     System.out.println(e);
        // }
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
