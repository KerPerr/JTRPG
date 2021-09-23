import Items.Item;

public class Place {
    String name;
    int index;
    Enemy[] bestiaires;
    String[] encounters;

    public Place(String name, int index, Enemy[] bestiaires, String[] rencontres) {
        this.name = name;
        this.index = index;
        this.bestiaires = bestiaires;
        this.encounters = rencontres;
    }

    public Place(String name, int index, Enemy[] bestiaires) {
        this.name = name;
        this.index = index;
        this.bestiaires = bestiaires;
    }

    public void randomBattle(Player player) {
        Enemy e = bestiaires[(int) (Math.random() * bestiaires.length)];
        GameLogic.clearConsole();
        GameLogic.printHeading("You encountered an " + e.name + ". You'll have to fight it!");
        GameLogic.toContinue();
        
        while (player.isAlive) {
            GameLogic.clearConsole();
            GameLogic.printHeading(e.name + "\nHP: " + e.hp + "/" + e.maxHp);
            GameLogic.printHeading(player.name + "\nHP: " + player.hp + "/" + player.maxHp);
            System.out.println("Choose an action");
            GameLogic.printSeparator(20);
            System.out.println("(1) Fight\n(2) Use Potion\n(3) Run Away");

            int input = GameLogic.readInt("-> ", 3);
            if (input == 1) {
                GameLogic.clearConsole();
                GameLogic.printHeading("BATTLE");

                int playerDamage = player.attack() - e.defend();
                int enemyDamage = e.attack() - player.defend();
                
                System.out.println("You dealt " + e.receive(playerDamage) + " damage to the " + e.name + ".");
                //e.receive(playerDamage);
                GameLogic.printSeparator(15);

                System.out.println("The " + e.name + " dealt " + (enemyDamage < 0 ? 0 : enemyDamage) + " damage to you.");
                player.receive(enemyDamage);
                GameLogic.toContinue();
                if (!e.isAlive) {
                    GameLogic.clearConsole();
                    GameLogic.printHeading("You defeated the " + e.name + "!");
                    player.xp += e.xp;
                    System.out.println("You earned " + e.xp + " XP!");
                    boolean rest = (Math.random() * 5 + 1 <= 2.25);
                    int gold = (int) (Math.random() * e.xp);
                    if(rest) {
                        player.restLeft++;
                        System.out.println("You earned an additional rest!");
                    }
                    if(gold > 0) {
                        player.gold += gold;
                        System.out.println("You collect " + gold + " gold from the " + e.name + "'s corpse!");
                    }
                    GameLogic.toContinue();
                    break;
                }
            } else if (input == 2) {
                GameLogic.clearConsole();
                Item potions = player.getItem("Potion HP");
                if(potions.quantite > 0 && player.hp < player.maxHp) {
                    GameLogic.printHeading("Do you want to drink a potion? (" + player.getItem("Potion HP").quantite + " left).");
                    System.out.println("(1) Yes\n(2) No, maybe later");
                    input = GameLogic.readInt("-> ", 2);
                    if(input == 1) {
                        player.hp = player.maxHp;
                        GameLogic.clearConsole();
                        potions.quantite--;
                        GameLogic.printHeading("You drank a magic potion. It restored your health back to " + player.maxHp);
                        GameLogic.toContinue();
                    }
                } else {
                    System.out.println("You don't have any potions or you're at full health.");
                    GameLogic.toContinue();
                }
            } else {
                GameLogic.clearConsole();
                if (index != 4) {
                    if (Math.random() * 10 + 1 <= 3.5) {
                        GameLogic.printHeading("You ran away from the " + e.name + "!");
                        GameLogic.toContinue();
                        break;
                    } else {
                        GameLogic.printHeading("You didn't managed to escape.");
                        int damage = e.attack();
                        System.out.println("You took " + (damage < 0 ? 0 : damage) + " damage!");
                        player.receive(damage);
                        GameLogic.toContinue();
                    }
                } else {
                    GameLogic.printHeading("YOU CANNOT ESCAPE THE EVIL EMPEROR !!");
                    int damage = e.attack();
                    System.out.println("You took " + (damage < 0 ? 0 : damage) + " damage!");
                    player.receive(damage);
                    GameLogic.toContinue();
                }
            }
        }
    }

    public void randomEncounter(Player player) {
        int encounter = (int) (Math.random() * encounters.length);
        switch (encounters[encounter]) {
            case "Battle":
                this.randomBattle(player);
                break;
            case "Rest":
                player.rest();
                break;
            case "Chest":
                int gold = (int) (Math.random() * (5 - 1 + 1) + 1);
                System.out.println("You found a chest, you got " + gold + "!");
                player.gold += gold;
                break;
            default:
                GameLogic.shop();
                break;
        }
    }

}
