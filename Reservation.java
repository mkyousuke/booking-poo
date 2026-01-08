import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private static int count = 0;
    private String id;
    private String nomHebergement;
    private Date debut, fin;
    private double prix;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Reservation(String nomHebergement, Date debut, Date fin, double prix) {
        this.id = "R" + (++count);
        this.nomHebergement = nomHebergement;
        this.debut = debut;
        this.fin = fin;
        this.prix = prix;
    }

    public Object[] toRow() {
        return new Object[]{id, nomHebergement, sdf.format(debut), sdf.format(fin), String.format("%.2f €", prix)};
    }
}