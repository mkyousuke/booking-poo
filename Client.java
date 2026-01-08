import java.util.ArrayList;
import java.util.List;

public class Client extends Personne {
    // Attributs spécifiques au client
    private String adresse;
    
    // LA LISTE QUI STOCKE LES RÉSERVATIONS (C'est la partie cruciale)
    private List<Reservation> reservations; 

    // Constructeur
    public Client(String nom, String prenom, String email, String adresse) {
        super(nom, prenom, email); // Appelle le constructeur de Personne
        this.adresse = adresse;
        
        // IMPORTANT : On initialise la liste ici pour qu'elle soit vide mais prête à l'emploi.
        // Si on oublie cette ligne, on aura une erreur "NullPointerException".
        this.reservations = new ArrayList<>();
    }

    // Méthode pour ajouter une réservation à la liste
    public void ajouterReservation(Reservation r) {
        this.reservations.add(r);
    }

    // Méthode pour récupérer toute la liste (pour l'affichage)
    public List<Reservation> getReservations() {
        return this.reservations;
    }

    // Implémentation de la méthode abstraite de Personne
    @Override
    public String getTypePersonne() {
        return "Client";
    }

    // Getter adresse
    public String getAdresse() { 
        return adresse; 
    }
}