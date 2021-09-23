import Items.Item;

public class Place {
    int index;
    String name;
    Enemy[] bestiaires;
    String[] encounters;
    Place[] places;

    public Place(int index, String name, Enemy[] bestiaires) {
        this.index = index;
        this.name = name;
        this.bestiaires = bestiaires;
    }

    public Place(int index, String name, Enemy[] bestiaires, String[] encounters) {
        this.index = index;
        this.name = name;
        this.bestiaires = bestiaires;
        this.encounters = encounters;
    }

    public void randomBattle(Player player) {
        Enemy e = bestiaires[(int) (Math.random() * bestiaires.length)];
        GameLogic.clearConsole();
        GameLogic.printHeading("You encountered an " + e.name + ". You'll have to fight it!");

        while (player.isAlive) {
            GameLogic.printHeading(e.name + "\nHP: " + e.hp + "/" + e.maxHp);
            GameLogic.printHeading(player.name + "\nHP: " + player.hp + "/" + player.maxHp);
            int input = GameLogic.answer("Choose an action",
                    new String[] { "1 - Fight", "2 - Use Items", "3 - Run Away" });
            if (input == 1) {
                GameLogic.clearConsole();
                GameLogic.printHeading("BATTLE");

                int playerDamage = player.attack() - e.defend();
                int enemyDamage = e.attack() - player.defend();

                System.out.println("You dealt " + (playerDamage < 0 ? 0 : playerDamage) + " damage to the " + e.name + ".");
                e.receive(playerDamage);
                GameLogic.printSeparator();

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
                    if (rest) {
                        player.restLeft++;
                        System.out.println("You earned an additional rest!");
                    }
                    if (gold > 0) {
                        player.gold += gold;
                        System.out.println("You collect " + gold + " gold from the " + e.name + "'s corpse!");
                    }
                    GameLogic.toContinue();
                    break;
                }
            } else if (input == 2) {
                GameLogic.clearConsole();
                /**
                 * Make an item selection list based on my consumables
                 */
                /*
                ArrayList<Consumable> consos = new ArrayList<>();
                for (Item i : player.items) {
                    if(i instanceof Consumable) {
                        consos.add((Consumable) i);
                    }
                }
                input = GameLogic.answer("Do you want to use an item?", );
                */
                Item item = player.getItem("Health Potion");
                if (item.qte > 0 && player.hp < player.maxHp) {
                    input = GameLogic.answer("Do you want to drink a potion? (" + item.qte + " left).",
                            new String[] { "1 - Yes", "2 - No, maybe later" });
                    if (input == 1) {
                        player.hp = player.maxHp;
                        player.use(item);
                        GameLogic.printHeading( "You drank a magic potion. It restored your health back to " + player.hp);
                        GameLogic.toContinue();
                    }
                } else {
                    System.out.println("You don't have any potions or you're at full health.");
                    GameLogic.toContinue();
                }
            } else {
                GameLogic.clearConsole();
                if (index != 3) {
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
            case "Merchant":
                GameLogic.shop();
                break;
            case "Hideout":
                int loot = (int) (Math.random() * (5 - 1 + 1) + 1);
                GameLogic.clearConsole();
                GameLogic.printHeading("You discovered an hideout. You found " + loot + " gold!");
                player.gold += loot;
                GameLogic.toContinue();
            default:
                System.out.println("Something went wrong ~");
                break;
        }
    }
}
