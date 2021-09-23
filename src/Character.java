public abstract class Character {
    public String name;
    public int maxHp, hp, xp;
    public boolean isAlive = true;

    public Character(String name, int maxHp, int xp) {
        this.name = name;
        this.maxHp = maxHp;
        this.xp = xp;
        this.hp = maxHp;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public abstract int attack();
    public abstract int defend();

    public int receive(int damage) {
        if(damage < 0) {
            damage = 0;
        }
        this.hp -= damage; // < 0 ? 0 : damage;
        if(this.hp <= 0) {
            this.setAlive(false);
        }
        return damage;
    };
}