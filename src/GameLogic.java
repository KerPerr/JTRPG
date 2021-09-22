import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    static Scanner scan = new Scanner(System.in);

    static Player player;
    static boolean isRunning;
    static int place = 0, act = 1;
    // static String[] places = { "Castle", "Forest", "Town" };
    static String[] encounters = { "Battle", "Battle", "Battle", "Shop", "Rest" };
    static String[] enemies = { "Skeleton", "Orc", "Goblin", "Mimic", "Swordman" };

    static Place currentPlace;

    public static ArrayList<Place> initPlaces() {
        ArrayList<Place> places = new ArrayList<>();

        Place town = new Place(0, "Town", new Enemy[] { new Enemy("Lapin", 5), new Enemy("Rat", 5),
                new Enemy("Chat", 5), new Enemy("Chien", 5) });

        Place forest = new Place(1, "Forest", new Enemy[] { new Enemy("Wolf", 15), new Enemy("Bear", 15),
                new Enemy("Squirel", 5), new Enemy("WereWolf", 30) });

        Place castle = new Place(2, "Castle", new Enemy[] { new Enemy("Mimic", 25), new Enemy("Swordman", 25),
                new Enemy("Spearman", 25), new Enemy("Axeman", 25), new Enemy("Batman", 25) });

        places.add(town);
        places.add(forest);
        places.add(castle);
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
        for (int i = 0; i < 30; i++) {
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
        printHeading(currentPlace.name);
        System.out.println("Choose an action: ");
        printSeparator(20);
        System.out.println("(1) Continue on your journey");
        System.out.println("(2) Character Information");
        System.out.println("(3) Exit Game");
    }

    public static void characterInfo() {
        clearConsole();
        player.getInformation();
        toContinue();
    }

    public static void playerDied() {
        clearConsole();
        printHeading("You died ...");
        printHeading("You earned " + player.xp + " XP on your journey. Try to earn more next time.");
    }

    public static void checkAct() {
        for (Enemy e : currentPlace.bestiaires) {
            System.out.println(e);
        }
        if (player.xp >= 10) {
            currentPlace = initPlaces().get(0);
            System.out.println("SECOND OUTRO");
            player.chooseTrait();
            System.out.println("SECOND ACT INTRO");
        } else if (player.xp >= 50) {
            currentPlace = initPlaces().get(1);
            System.out.println("SECOND ACT OUTRO");
            player.chooseTrait();
            System.out.println("THIRD ACT INTRO");
        } else if (player.xp >= 100) {
            currentPlace = initPlaces().get(2);
            System.out.println("THIRD ACT OUTRO");
            player.chooseTrait();
            System.out.println("FOURTH ACT INTRO");
            finalBattle();
        }
    }

    public static void randomBattle(Enemy e) {
        clearConsole();
        printHeading("You encountered an " + e.name + ". You'll have to fight it!");
        toContinue();
        battle(e);
    }

    public static void shop() {
        clearConsole();
        printHeading("You meet a mysterious stranger.\nHe offers you something:");
        int price = (int) (Math.random() * (10 + player.potions * 3) + 10 + player.potions);
        System.out.println("- Magic Potion: " + price + " gold.");
        printSeparator(15);
        int input = answer("Do you want to buy one?", new String[] { "(1) Yes!", "(2) No thanks" });
        /*
         * System.out.println("Do you want to buy one?\n(1) Yes!\n(2) No thanks.");
         * 
         * int input = readInt("-> ", 2);
         */
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

    public static int answer(String question, String[] responses) {
        System.out.println(question);
        for (int i = 0; i < responses.length; i++) {
            System.out.println(responses[i]);
        }
        int input = readInt("-> ", responses.length);
        return input;
    }

    public static void battle(Enemy e) {
        while (true) {
            clearConsole();
            printHeading(e.name + "\nHP: " + e.hp + "/" + e.maxHp);
            printHeading(player.name + "\nHP: " + player.hp + "/" + player.maxHp);
            System.out.println("Choose an action");
            printSeparator(20);
            System.out.println("(1) Fight\n(2) Use Potion\n(3) Run Away");

            int input = readInt("-> ", 3);
            if (input == 1) {
                clearConsole();
                printHeading("BATTLE");

                int playerDamage = player.attack() - e.defend();
                int enemyDamage = e.attack() - player.defend();

                System.out.println(
                        "You dealt " + (playerDamage < 0 ? 0 : playerDamage) + " damage to the " + e.name + ".");
                e.receive(playerDamage);
                printSeparator(15);

                System.out
                        .println("The " + e.name + " dealt " + (enemyDamage < 0 ? 0 : enemyDamage) + " damage to you.");
                player.receive(enemyDamage);
                toContinue();

                if (!player.isAlive) {
                    playerDied();
                    break;
                } else if (!e.isAlive) {
                    clearConsole();
                    printHeading("You defeated the " + e.name + "!");
                    player.xp += e.xp;
                    System.out.println("You earned " + e.xp + " XP!");
                    boolean rest = (Math.random() * 5 + 1 <= 2.25);
                    int gold = (int) (Math.random() * e.xp);
                    if (rest) {
                        player.restLeft++;
                        System.out.println("You earned an additional rest!");
                    }
                    if (gold > 0) {
                        player.gold += gold;
                        System.out.println("You collect " + gold + " gold from the " + e.name + "'s corpse!");
                    }
                    toContinue();
                    break;
                }
            } else if (input == 2) {
                clearConsole();
                if (player.potions > 0 && player.hp < player.maxHp) {
                    printHeading("Do you want to drink a potion? (" + player.potions + " left).");
                    System.out.println("(1) Yes\n(2) No, maybe later");
                    input = readInt("-> ", 2);
                    if (input == 1) {
                        player.hp = player.maxHp;
                        clearConsole();
                        printHeading("You drank a magic potion. It restored your health back to " + player.maxHp);
                        toContinue();
                    }
                } else {
                    System.out.println("You don't have any potions or you're at full health.");
                    toContinue();
                }
            } else {
                clearConsole();
                if (act != 4) {
                    if (Math.random() * 10 + 1 <= 3.5) {
                        printHeading("You ran away from the " + e.name + "!");
                        toContinue();
                        break;
                    } else {
                        printHeading("You didn't managed to escape.");
                        int damage = e.attack();
                        System.out.println("You took " + (damage < 0 ? 0 : damage) + " damage!");
                        player.receive(damage);
                        toContinue();
                        if (!player.isAlive) {
                            playerDied();
                        }
                    }
                } else {
                    printHeading("YOU CANNOT ESCAPE THE EVIL EMPEROR !!");
                    int damage = e.attack();
                    System.out.println("You took " + (damage < 0 ? 0 : damage) + " damage!");
                    player.receive(damage);
                    toContinue();
                    if (!player.isAlive) {
                        playerDied();
                    }
                    toContinue();
                }
            }
        }
    }

    public static void randomEncounter() {
        int encounter = (int) (Math.random() * encounters.length);
        switch (encounters[encounter]) {
            case "Battle":
                randomBattle(currentPlace.bestiaires[
                    (int) (Math.random() * currentPlace.bestiaires.length)
                ]);
                break;
            case "Rest":
                player.rest();
                break;
            default:
                shop();
                break;
        }
    }

    public static void continueJourney() {
        checkAct();
        if (act != 4) {
            randomEncounter();
        }
    }

    public static void start() {
        currentPlace = initPlaces().get(0);
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
        battle(new Enemy("EVIL EMPEROR", 300));
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
                    characterInfo();
                    break;
                default:
                    isRunning = false;
                    break;
            }
        }
    }
}
