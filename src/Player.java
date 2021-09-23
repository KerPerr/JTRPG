import java.util.ArrayList;

import Items.Item;

public class Player extends Character {

    int numAtkUpgrades, numDefUpgrades;

    int gold, restLeft;

    String[] atkUpgrades = {"Strength", "Power", "Might", "Godlike"};
    String[] defUpgrades = {"Heavy Bones", "Stoneskin", "Scale Armor", "Holy Aura"};

    ArrayList<Item> items = new ArrayList<>();

    public Player(String name) {
        super(name, 100, 0);
        this.numAtkUpgrades = 0;
        this.numDefUpgrades = 0;

        this.gold = 5;
        this.restLeft = 1;
        items.add(new Item("Health Potion", 1));
        items.add(new Item("Bombe", 1));

        chooseTrait();
    }

    public void chooseTrait() {
        GameLogic.clearConsole();
        GameLogic.printHeading("Choose a trait");
        System.out.println("1 - " + atkUpgrades[numAtkUpgrades]);
        System.out.println("2 - " + defUpgrades[numDefUpgrades]);
        int input = GameLogic.readInt("-> ", 2);
        GameLogic.clearConsole();
        if(input == 1) {
            GameLogic.printHeading("You choose " + atkUpgrades[numAtkUpgrades] + "!");
            numAtkUpgrades++;
        } else {
            GameLogic.printHeading("You choose " + defUpgrades[numDefUpgrades] + "!");
            numDefUpgrades++;
        }
        GameLogic.toContinue();
    }

    public void getInformation() {
        GameLogic.printHeading("CHARACTER INFO");
        System.out.println(this.name + "\tHP: " + this.hp + "/" + this.maxHp);
        GameLogic.printSeparator();
        System.out.println("XP: " + this.xp + "\tGold: " + this.gold);
        GameLogic.printSeparator();
        System.out.println("# of Rests: " + this.restLeft);
        GameLogic.printSeparator();
        for (Item item : items) {
            System.out.println("# of " + item.name + ": " + item.qte);
            GameLogic.printSeparator();
        }
        GameLogic.printSeparator();

        if (this.numAtkUpgrades > 0) {
            System.out.println("Offensive trait: " + this.atkUpgrades[this.numAtkUpgrades - 1]);
        }
        if (this.numDefUpgrades > 0) {
            System.out.println("Defensive trait: " + this.defUpgrades[this.numDefUpgrades - 1]);
        }
    }

    public void rest() {
        GameLogic.clearConsole();
        if(this.restLeft >= 1) {
            GameLogic.printHeading("Do you want to take a  rest? (" + this.restLeft + " rest(s) left).");
            System.out.println("(1) Yes\n(2) No, not now.");

            int input = GameLogic.readInt("-> ", 2);
            if(input == 1) {
                GameLogic.clearConsole();
                if(this.hp < this.maxHp) {
                    int hpRestored = (int) (Math.random() * (this.xp/4 + 1) + 10);
                    this.hp += hpRestored;
                    if(this.hp > this.maxHp) this.hp = this.maxHp;
                    System.out.println("You took a rest and restored up to " + hpRestored + " health.");
                    System.out.println("You're now at " + this.hp + "/" + this.maxHp + " health.");
                    this.restLeft--;
                } else {
                    System.out.println("You're at full health. You don't need to rest now!");
                }
                GameLogic.toContinue();
            }
        }
    }

    public Item getItem(String name) {
        return items.stream().filter(i -> name.equals(i.name)).findAny().orElse(new Item(name));
    }

    public void addItem(Item i) {
        Item item = getItem(i.name);
        if(item.qte > 0) {
            item.qte++;
        } else {
            items.add(item);
        }
    }
    
    public int use(Item item) {
        int result;
        Item i = getItem(item.name);
        if(i.qte == 0) {
            System.out.println("You don't have any " + i.name + "!");
            result = 0;
        } else {
            i.qte--;
            result = item.value;
        }
        return result;
    }

    @Override
    public int attack() {
        // TODO Auto-generated method stub
        return (int) (Math.random()*(xp/4 + numAtkUpgrades*3 + 3) + xp/10 + numAtkUpgrades*2 + numDefUpgrades + 1);
    }

    @Override
    public int defend() {
        // TODO Auto-generated method stub
        return (int) (Math.random()*(xp/4 + numDefUpgrades*3 + 3) + xp/10 + numDefUpgrades*2 + numAtkUpgrades + 1);
    }

    @Override
    public void setIsAlive(boolean alive) {
        super.setIsAlive(alive);
        if(!this.isAlive) {
            GameLogic.clearConsole();
            System.out.println("You died ...");
            System.out.println("You earned " + xp + " XP on your journey. Try to earn more next time.");
        }
    }
}
