package Items;
public class Consomable extends Item {

    IUseBehavior type;

    public Consomable(String name, IUseBehavior type, int quantite) {
        super(name, quantite);
        //TODO Auto-generated constructor stub
        this.type = type;
    }
    
}
