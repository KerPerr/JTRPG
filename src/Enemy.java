public class Enemy extends Character {

    int playerXP;

    public Enemy(String name, int playerXP) {
        super(name, (int) (Math.random()*playerXP/3 + 5), (int) (Math.random()*(playerXP/4 + 2)) + 1);
        this.playerXP = playerXP;
    }

    @Override
    public int attack() {
        // TODO Auto-generated method stub
        return (int) (Math.random()*(playerXP/4 + 1) + xp/4 + 3);
    }

    @Override
    public int defend() {
        // TODO Auto-generated method stub
        return (int) (Math.random()*(playerXP/4 + 1) + xp/4 + 3);
    }
    
    @Override
    public String toString() {
        return this.name + " HP: " + this.hp + "/" + this.maxHp;
    }
}
