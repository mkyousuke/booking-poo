public class Administrateur extends Personne {
    public Administrateur(String nom, String prenom, String email) {
        super(nom, prenom, email);
    }

    @Override
    public String getTypePersonne() {
        return "Administrateur";
    }
}