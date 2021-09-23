package Items;

public class Item {

    public boolean consumable;

    public String name;
    public int value;
    public int qte;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, int qte) {
        this.name = name;
        this.qte = qte;
    }

    public Item(String name, int value, int qte) {
        this.name = name;
        this.value = value;
        this.qte = qte;
    }
}
