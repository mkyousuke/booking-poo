import java.util.Date;

public class Periode {
    private Date debut;
    private Date fin;

    public Periode(Date debut, Date fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public boolean couvre(Date d1, Date d2) {
        return !d1.before(debut) && !d2.after(fin);
    }
}