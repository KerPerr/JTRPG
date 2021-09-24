import Items.Armor;
import Items.Weapon;

public class Equipement {
    Weapon weapon;
    Armor armor;

    public Equipement() {}

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Armor getArmor() {
        return this.armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }
}
