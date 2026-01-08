import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainBooking extends JFrame {

    private CollectionHebergements catalogue = new CollectionHebergements();
    private List<Reservation> mesReservations = new ArrayList<>();
    
    private DefaultTableModel modelHebergements;
    private DefaultTableModel modelReservations;
    private JTable tableHebergements;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public MainBooking() {
        initDonnees();
        configurerFenetre();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Catalogue & Réservation", createClientPanel());
        tabbedPane.addTab("Mes Réservations", createMesReservationsPanel());
        tabbedPane.addTab("Administration", createAdminPanel());

        add(tabbedPane);
    }

    private void configurerFenetre() {
        // Changement demandé : Titre B2 CYBER
        setTitle("Mini-Booking (B2 CYBER)");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Style moderne
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
    }

    private void initDonnees() {
        try {
            Date d1 = sdf.parse("01/01/2024");
            Date d2 = sdf.parse("31/12/2025");
            
            Hebergement h1 = new ChambreHotel("H1", "Ibis Tour Eiffel", 2, 95.0);
            h1.ajouterPeriodeDispo(d1, d2);
            
            Hebergement h2 = new Appartement("A1", "Loft Lyon Centre", 4, 150.0);
            h2.ajouterPeriodeDispo(d1, d2);
            
            Hebergement h3 = new Villa("V1", "Villa Azure Nice", 8, 450.0);
            h3.ajouterPeriodeDispo(d1, d2);

            catalogue.ajouter(h1);
            catalogue.ajouter(h2);
            catalogue.ajouter(h3);
        } catch (Exception e) {}
    }

    // --- PANELS ---

    private JPanel createClientPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitre = new JLabel("Hébergements disponibles");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(44, 62, 80));
        panel.add(lblTitre, BorderLayout.NORTH);

        String[] colNames = {"Type", "Nom", "Capacité", "Prix/Nuit (€)"};
        modelHebergements = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        remplirTableHebergements();

        tableHebergements = new JTable(modelHebergements);
        tableHebergements.setRowHeight(25);
        tableHebergements.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(tableHebergements), BorderLayout.CENTER);

        JButton btnReserver = new JButton("Réserver la sélection");
        btnReserver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReserver.setBackground(new Color(52, 152, 219));
        btnReserver.setForeground(Color.WHITE);
        btnReserver.setPreferredSize(new Dimension(200, 40));
        btnReserver.addActionListener(e -> actionReserver());

        JPanel btnContainer = new JPanel();
        btnContainer.add(btnReserver);
        panel.add(btnContainer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMesReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Historique des réservations");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        String[] colNames = {"ID", "Hébergement", "Arrivée", "Départ", "Prix Total"};
        modelReservations = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(modelReservations);
        table.setRowHeight(25);
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(null);
        
        JLabel lblTitre = new JLabel("Ajout Hébergement");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setBounds(30, 20, 200, 30);
        panel.add(lblTitre);

        int y = 70;
        JLabel lNom = new JLabel("Nom :"); lNom.setBounds(30, y, 100, 25); panel.add(lNom);
        JTextField tfNom = new JTextField(); tfNom.setBounds(100, y, 200, 25); panel.add(tfNom);

        y += 40;
        JLabel lPrix = new JLabel("Prix :"); lPrix.setBounds(30, y, 100, 25); panel.add(lPrix);
        JTextField tfPrix = new JTextField(); tfPrix.setBounds(100, y, 200, 25); panel.add(tfPrix);

        y += 40;
        JLabel lType = new JLabel("Type :"); lType.setBounds(30, y, 100, 25); panel.add(lType);
        String[] types = {"Hôtel", "Appartement", "Villa"};
        JComboBox<String> cbType = new JComboBox<>(types); cbType.setBounds(100, y, 200, 25); panel.add(cbType);

        JButton btnAdd = new JButton("Ajouter");
        btnAdd.setBounds(100, y + 50, 200, 35);
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        
        btnAdd.addActionListener(e -> {
            try {
                String nom = tfNom.getText();
                double prix = Double.parseDouble(tfPrix.getText());
                String type = (String) cbType.getSelectedItem();
                
                Hebergement h;
                if(type.equals("Hôtel")) h = new ChambreHotel("N", nom, 2, prix);
                else if(type.equals("Villa")) h = new Villa("N", nom, 8, prix);
                else h = new Appartement("N", nom, 4, prix);
                
                h.ajouterPeriodeDispo(sdf.parse("01/01/2024"), sdf.parse("31/12/2025"));

                catalogue.ajouter(h);
                remplirTableHebergements();
                JOptionPane.showMessageDialog(this, "Ajouté avec succès !");
                tfNom.setText(""); tfPrix.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de format");
            }
        });

        panel.add(btnAdd);
        return panel;
    }

    private void remplirTableHebergements() {
        modelHebergements.setRowCount(0);
        for (Hebergement h : catalogue.getListe()) {
            modelHebergements.addRow(new Object[]{
                h.getType(), h.getNom(), h.getCapacite(), h.getPrixParNuit()
            });
        }
    }

    private void actionReserver() {
        int row = tableHebergements.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne.");
            return;
        }

        Hebergement h = catalogue.getListe().get(row);
        JTextField t1 = new JTextField("01/06/2024");
        JTextField t2 = new JTextField("05/06/2024");
        
        Object[] msg = {"Arrivée (jj/mm/aaaa):", t1, "Départ (jj/mm/aaaa):", t2};
        
        if (JOptionPane.showConfirmDialog(this, msg, "Réserver " + h.getNom(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Date d1 = sdf.parse(t1.getText());
                Date d2 = sdf.parse(t2.getText());
                
                if (h.estDisponible(d1, d2)) {
                    double prix = h.calculerPrix(d1, d2, 1);
                    Reservation r = new Reservation(h.getNom(), d1, d2, prix);
                    mesReservations.add(r);
                    modelReservations.addRow(r.toRow());
                    JOptionPane.showMessageDialog(this, "Confirmé ! Montant : " + String.format("%.2f €", prix));
                } else {
                    JOptionPane.showMessageDialog(this, "Indisponible à ces dates.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Format de date incorrect.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainBooking().setVisible(true));
    }
}