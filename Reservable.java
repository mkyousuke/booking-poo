import java.util.Date;

public interface Reservable {
    boolean estDisponible(Date debut, Date fin);
    double calculerPrix(Date debut, Date fin, int nbPersonnes);
    String getType();
    String getNom();
    double getPrixParNuit();
}