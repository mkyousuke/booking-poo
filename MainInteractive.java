import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
// L'import ArrayList a été supprimé ici car inutile

public class MainInteractive {

    private static CollectionHebergements catalogue = new CollectionHebergements();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    private static Client clientConnecte = null;
    private static Administrateur adminConnecte = null;

    public static void main(String[] args) {
        initDonnees();
        
        boolean running = true;
        while (running) {
            if (clientConnecte == null && adminConnecte == null) {
                running = menuPrincipal();
            } else if (clientConnecte != null) {
                menuClient();
            } else if (adminConnecte != null) {
                menuAdmin();
            }
        }
        System.out.println("Fermeture de Mini-Booking (B2 CYBER). Au revoir !");
    }

    // --- MENUS ---
    private static boolean menuPrincipal() {
        System.out.println("\n=================================");
        System.out.println("   MINI-BOOKING (B2 CYBER) - CLI");
        System.out.println("=================================");
        System.out.println("1. Connexion Client (Sophie)");
        System.out.println("2. Inscription Nouveau Client");
        System.out.println("3. Connexion Administrateur");
        System.out.println("4. Quitter");
        System.out.print(">>> Votre choix : ");

        String choix = scanner.nextLine();
        switch (choix) {
            case "1": connexionClient(); break;
            case "2": inscriptionClient(); break;
            case "3": connexionAdmin(); break;
            case "4": return false;
            default: System.out.println("Choix invalide.");
        }
        return true;
    }

    private static void menuClient() {
        System.out.println("\n--- ESPACE CLIENT : " + clientConnecte.getPrenom() + " ---");
        System.out.println("1. Voir le catalogue");
        System.out.println("2. Faire une réservation");
        System.out.println("3. Voir mes réservations");
        System.out.println("4. Se déconnecter");
        System.out.print(">>> Votre choix : ");

        String choix = scanner.nextLine();
        switch (choix) {
            case "1": afficherCatalogue(); pause(); break;
            case "2": reserverHebergement(); break;
            case "3": afficherMesReservations(); pause(); break;
            case "4": clientConnecte = null; System.out.println("Déconnexion..."); break;
            default: System.out.println("Option inconnue.");
        }
    }

    private static void menuAdmin() {
        System.out.println("\n--- ADMINISTRATION ---");
        System.out.println("1. Ajouter un hébergement");
        System.out.println("2. Voir tout le catalogue");
        System.out.println("3. Se déconnecter");
        System.out.print(">>> Votre choix : ");

        String choix = scanner.nextLine();
        switch (choix) {
            case "1": ajouterHebergementAdmin(); break;
            case "2": afficherCatalogue(); pause(); break;
            case "3": adminConnecte = null; break;
            default: System.out.println("Option inconnue.");
        }
    }

    // --- PAUSE ---
    private static void pause() {
        System.out.println("\n[Appuyez sur ENTRÉE pour continuer...]");
        scanner.nextLine();
    }

    // --- ACTIONS ---
    private static void inscriptionClient() {
        System.out.println("\n--- INSCRIPTION ---");
        System.out.print("Nom : "); String nom = scanner.nextLine();
        System.out.print("Prénom : "); String prenom = scanner.nextLine();
        System.out.print("Email : "); String email = scanner.nextLine();
        System.out.print("Adresse : "); String adresse = scanner.nextLine();
        clientConnecte = new Client(nom, prenom, email, adresse);
        System.out.println("Compte créé !");
    }

    private static void connexionClient() {
        System.out.println("\n(Connexion auto : Sophie Martin)");
        clientConnecte = new Client("Martin", "Sophie", "sophie@mail.com", "Lyon");
    }

    private static void connexionAdmin() {
        System.out.print("Mot de passe (root) : ");
        if (scanner.nextLine().equals("root")) {
            adminConnecte = new Administrateur("Super", "Admin", "admin@b2.cyber");
            System.out.println("Mode Admin activé.");
        } else {
            System.out.println("Erreur mot de passe.");
        }
    }

    private static void afficherCatalogue() {
        System.out.println("\n--- CATALOGUE ---");
        List<Hebergement> liste = catalogue.getListe();
        if (liste.isEmpty()) {
            System.out.println("Aucun hébergement.");
        } else {
            for (int i = 0; i < liste.size(); i++) {
                Hebergement h = liste.get(i);
                System.out.println("[" + i + "] " + h.getType() + " : " + h.getNom() + " | " + h.getPrixParNuit() + "€/nuit");
            }
        }
    }

    private static void reserverHebergement() {
        afficherCatalogue();
        System.out.print("\nNuméro de l'hébergement : ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < catalogue.getListe().size()) {
                Hebergement h = catalogue.getListe().get(index);
                System.out.print("Date arrivée (jj/mm/aaaa) : ");
                Date d1 = sdf.parse(scanner.nextLine());
                System.out.print("Date départ (jj/mm/aaaa) : ");
                Date d2 = sdf.parse(scanner.nextLine());

                if (h.estDisponible(d1, d2)) {
                    double prix = h.calculerPrix(d1, d2, 1);
                    Reservation r = new Reservation(h.getNom(), d1, d2, prix);
                    
                    // SAUVEGARDE DANS LE CLIENT
                    clientConnecte.ajouterReservation(r);

                    System.out.println(">> SUCCÈS ! Réservation confirmée pour " + String.format("%.2f", prix) + " €");
                } else {
                    System.out.println(">> ERREUR : Indisponible.");
                }
            } else { System.out.println("Numéro invalide."); }
        } catch (Exception e) { System.out.println("Erreur de saisie."); }
        pause();
    }

    private static void afficherMesReservations() {
        System.out.println("\n--- MES RÉSERVATIONS ---");
        // Vérification de sécurité
        if(clientConnecte == null) return;

        List<Reservation> mesResas = clientConnecte.getReservations();

        if (mesResas.isEmpty()) {
            System.out.println("Vous n'avez aucune réservation.");
        } else {
            for (Reservation r : mesResas) {
                System.out.println(r.toString());
            }
        }
        // La pause est gérée par le menu
    }

    private static void ajouterHebergementAdmin() {
        try {
            System.out.print("Type (1=Hotel, 2=Appart, 3=Villa) : "); String t = scanner.nextLine();
            System.out.print("Nom : "); String n = scanner.nextLine();
            System.out.print("Prix : "); double p = Double.parseDouble(scanner.nextLine());
            
            Hebergement h = t.equals("1") ? new ChambreHotel("N", n, 2, p) :
                            t.equals("3") ? new Villa("N", n, 8, p) : 
                            new Appartement("N", n, 4, p);
                            
            h.ajouterPeriodeDispo(sdf.parse("01/01/2024"), sdf.parse("31/12/2025"));
            catalogue.ajouter(h);
            System.out.println("Ajouté !");
        } catch (Exception e) { System.out.println("Erreur."); }
        pause();
    }

    private static void initDonnees() {
        try {
            Date d1 = sdf.parse("01/01/2024"), d2 = sdf.parse("31/12/2025");
            Hebergement h1 = new ChambreHotel("H1", "Ibis Cyber", 2, 80.0); h1.ajouterPeriodeDispo(d1, d2);
            Hebergement h2 = new Villa("V1", "Villa Firewall", 6, 250.0); h2.ajouterPeriodeDispo(d1, d2);
            catalogue.ajouter(h1); catalogue.ajouter(h2);
        } catch (Exception e) {}
    }
}