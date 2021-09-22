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

    public void randomBattle() {
        
    }
}
