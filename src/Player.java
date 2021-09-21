public class Player extends Character {

    int numAtkUpgrades, numDefUpgrades;

    int gold, restLeft, potions;

    String[] atkUpgrades = {"Strength", "Power", "Might", "Godlike"};
    String[] defUpgrades = {"Heavy Bones", "Stoneskin", "Scale Armor", "Holy Aura"};

    public Player(String name) {
        super(name, 100, 0);
        this.numAtkUpgrades = 0;
        this.numDefUpgrades = 0;

        this.gold = 5;
        this.restLeft = 1;
        this.potions = 0;

        chooseTrait();
    }

    public void chooseTrait() {
        GameLogic.clearConsole();
        GameLogic.printHeading("Choose a trait");
        System.out.println("(1) " + atkUpgrades[numAtkUpgrades]);
        System.out.println("(2) " + defUpgrades[numDefUpgrades]);
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
        GameLogic.printSeparator(20);
        System.out.println("XP: " + this.xp + "\tGold: " + this.gold);
        GameLogic.printSeparator(20);
        System.out.println("# of Potions: " + this.potions);
        GameLogic.printSeparator(20);
        System.out.println("# of Rests: " + this.restLeft);
        GameLogic.printSeparator(20);

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
}
