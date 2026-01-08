import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Hebergement implements Reservable, Comparable<Hebergement> {
    protected String id;
    protected String nom;
    protected String type;
    protected int capaciteMax;
    protected double prixParNuit;
    protected List<Periode> periodesDisponibles;

    public Hebergement(String id, String nom, String type, int cap, double prix) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.capaciteMax = cap;
        this.prixParNuit = prix;
        this.periodesDisponibles = new ArrayList<>();
    }

    public void ajouterPeriodeDispo(Date debut, Date fin) {
        periodesDisponibles.add(new Periode(debut, fin));
    }

    @Override
    public boolean estDisponible(Date debut, Date fin) {
        for (Periode p : periodesDisponibles) {
            if (p.couvre(debut, fin)) return true;
        }
        return false;
    }

    @Override
    public double calculerPrix(Date debut, Date fin, int nbPersonnes) {
        long diff = Math.abs(fin.getTime() - debut.getTime());
        long jours = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return jours * prixParNuit;
    }

    @Override
    public int compareTo(Hebergement o) {
        return Double.compare(this.prixParNuit, o.prixParNuit);
    }

    // Getters
    public String getNom() { return nom; }
    public String getType() { return type; }
    public double getPrixParNuit() { return prixParNuit; }
    public int getCapacite() { return capaciteMax; }
    
    @Override
    public String toString() { return nom + " (" + type + ")"; }
}