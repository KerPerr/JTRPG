package Items;
public class Item {
    public String name;
    public int quantite;
    public int value;

    public Item(String name, int quantite) {
        this.quantite = quantite;
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
