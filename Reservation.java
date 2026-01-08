import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    // Compteur statique pour générer des ID uniques (R1, R2, R3...)
    private static int count = 0;
    
    private String id;
    private String nomHebergement;
    private Date debut;
    private Date fin;
    private double prix;
    
    // Format de date pour l'affichage
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Reservation(String nomHebergement, Date debut, Date fin, double prix) {
        this.id = "R" + (++count);
        this.nomHebergement = nomHebergement;
        this.debut = debut;
        this.fin = fin;
        this.prix = prix;
    }

    /**
     * Méthode utilisée par l'Interface Graphique (MainBooking) 
     * pour remplir les lignes du tableau.
     */
    public Object[] toRow() {
        return new Object[]{
            id, 
            nomHebergement, 
            sdf.format(debut), 
            sdf.format(fin), 
            String.format("%.2f €", prix)
        };
    }

    /**
     * Méthode utilisée par le Terminal (MainInteractive)
     * pour afficher la réservation proprement.
     */
    @Override
    public String toString() {
        return String.format("Réservation [%s] : %s | Du %s au %s | Total: %.2f €", 
            id, nomHebergement, sdf.format(debut), sdf.format(fin), prix);
    }

    // Getters basiques (utiles pour des évolutions futures)
    public String getId() { return id; }
    public double getPrix() { return prix; }
}