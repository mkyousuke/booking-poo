import java.util.ArrayList;
import java.util.List;

public class CollectionHebergements {
    private List<Hebergement> liste = new ArrayList<>();
    public void ajouter(Hebergement h) { liste.add(h); }
    public List<Hebergement> getListe() { return liste; }
}