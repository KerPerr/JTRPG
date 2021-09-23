public class Place {
    int index;
    String name;
    Enemy[] bestiaires;
    String[] encounters = {"Battle", "Battle", "Battle", "Rest", "Shop"};
    Place[] places;

    public Place(int index, String name, Enemy[] bestiaires) {
        this.index = index;
        this.name = name;
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
            int input = GameLogic.answer("Choose an action", new String[] {"1 - Fight", "2 - Use Potion", "3 - Run Away"});
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

                if (!player.isAlive) {
                    break;
                } else if (!e.isAlive) {
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
                // if(player.herb.nb > 0) {
                //     player.use(player.herb);
                // }
                if (player.potions > 0 && player.hp < player.maxHp) {
                    input = GameLogic.answer("Do you want to drink a potion? (" + player.potions + " left).", new String[] {"1 - Yes", "2 - No, maybe later"});
                    if (input == 1) {
                        player.hp = player.maxHp;
                        GameLogic.printHeading("You drank a magic potion. It restored your health back to " + player.maxHp);
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
}
