import java.util.ArrayList;

import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;

public class Player extends Character {

    int numAtkUpgrades, numDefUpgrades;

    int gold, restLeft;

    String[] atkUpgrades = { "Strength", "Power", "Might", "Godlike" };
    String[] defUpgrades = { "Heavy Bones", "Stoneskin", "Scale Armor", "Holy Aura" };

    ArrayList<Item> items = new ArrayList<>();
    Equipement equipement;

    public Player(String name) {
        super(name, 100, 0);
        this.numAtkUpgrades = 0;
        this.numDefUpgrades = 0;

        this.gold = 5;
        this.restLeft = 1;
        this.items.add(new Consumable("Potion HP", 1));

        chooseTrait();
    }

    @Override
    public void setAlive(boolean isAlive) {
        super.setAlive(isAlive);
        if (!this.isAlive) {
            GameLogic.clearConsole();
            GameLogic.printHeading("You died ...");
            GameLogic.printHeading("You earned " + xp + " XP on your journey. Try to earn more next time.");
        }
    }

    public void chooseTrait() {
        GameLogic.clearConsole();
        GameLogic.printHeading("Choose a trait");
        System.out.println("(1) " + atkUpgrades[numAtkUpgrades]);
        System.out.println("(2) " + defUpgrades[numDefUpgrades]);
        int input = GameLogic.readInt("-> ", 2);
        GameLogic.clearConsole();
        if (input == 1) {
            GameLogic.printHeading("You choose " + atkUpgrades[numAtkUpgrades] + "!");
            numAtkUpgrades++;
        } else {
            GameLogic.printHeading("You choose " + defUpgrades[numDefUpgrades] + "!");
            numDefUpgrades++;
        }
        GameLogic.toContinue();
    }

    public Item getItem(String name) {
        return items.stream()
        .filter(i -> name.equals(i.name))
        .findAny()
        .orElse(new Item(name, 0));
    }

    public void setWeapon(Weapon weapon) {
        if(equipement.getWeapon() != null) {
            items.add(equipement.weapon);
        }
        equipement.setWeapon(weapon);
    }

    public void setArmor(Armor armor) {
        if(equipement.getArmor() != null) {
            items.add(equipement.armor);
        }
        equipement.setArmor(armor);
    }

    public void getInformation() {
        GameLogic.printHeading("CHARACTER INFO");
        System.out.println(this.name + "\tHP: " + this.hp + "/" + this.maxHp);
        GameLogic.printSeparator(20);
        System.out.println("XP: " + this.xp + "\tGold: " + this.gold);
        GameLogic.printSeparator(20);
        System.out.println("# of Rests: " + this.restLeft);
        GameLogic.printSeparator(20);

        for (Item i : items) {
            if(i instanceof Consumable) {
                System.out.println("# of "+ i.name +": " + i.quantite);
                GameLogic.printSeparator(20);
            }
        }
    
        if (this.numAtkUpgrades > 0) {
            System.out.println("Offensive trait: " + this.atkUpgrades[this.numAtkUpgrades - 1]);
        }
        if (this.numDefUpgrades > 0) {
            System.out.println("Defensive trait: " + this.defUpgrades[this.numDefUpgrades - 1]);
        }

        int input = GameLogic.answer("Do you want to equip something", new String[] {"1 - Weapon.", "2 - Armor.", "3 - No."});
        GameLogic.printHeading("Equipement");
        GameLogic.printSeparator(30);
        if(input == 1) {
            System.out.println(equipement.getWeapon().name + " : Equiped");
            /**
             * BOUCLE SUR ITEMS POUR AFFICHER QUE LES WEAPONS
             */
        } else if (input == 2) {
            
        }
    }

    public void rest() {
        GameLogic.clearConsole();
        if (this.restLeft >= 1) {
            GameLogic.printHeading("Do you want to take a  rest? (" + this.restLeft + " rest(s) left).");
            System.out.println("(1) Yes\n(2) No, not now.");

            int input = GameLogic.readInt("-> ", 2);
            if (input == 1) {
                GameLogic.clearConsole();
                if (this.hp < this.maxHp) {
                    int hpRestored = (int) (Math.random() * (this.xp / 4 + 1) + 10);
                    this.hp += hpRestored;
                    if (this.hp > this.maxHp)
                        this.hp = this.maxHp;
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

    @Override
    public int attack() {
        // TODO Auto-generated method stub
        return (int) (Math.random() * (xp / 4 + numAtkUpgrades * 3 + 3) + xp / 10 + numAtkUpgrades * 2 + numDefUpgrades
                + 1);
    }

    @Override
    public int defend() {
        // TODO Auto-generated method stub
        return (int) (Math.random() * (xp / 4 + numDefUpgrades * 3 + 3) + xp / 10 + numDefUpgrades * 2 + numAtkUpgrades
                + 1);
    }
}
