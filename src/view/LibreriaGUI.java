package view;

import controller.LibraryFacade;
import model.Libro;
import model.StatoLettura;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibreriaGUI extends JFrame {
    private LibraryFacade facade;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JTextField searchAutore;
    private JComboBox<String> filtroStato;
    private JComboBox<String> filtroGenere;
    private JLabel isbnWarningLabel;

    public LibreriaGUI() {
        facade = new LibraryFacade();

        setTitle("Gestore Libreria Personale");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAggiungi = new JButton("Aggiungi Libro");
        JButton btnModifica = new JButton("Modifica Selezionato");
        JButton btnRimuovi = new JButton("Rimuovi Selezionato");
        JButton btnSalva = new JButton("Salva su JSON");
        JButton btnCarica = new JButton("Carica da JSON");
        JButton btnCercaTitolo = new JButton("Cerca per Titolo");
        JButton btnCercaAutore = new JButton("Cerca per Autore");
        JButton btnFiltraStato = new JButton("Filtra per Stato");
        JButton btnFiltraGenere = new JButton("Filtra per Genere");

        searchField = new JTextField(10);
        searchAutore = new JTextField(10);
        filtroStato = new JComboBox<>(new String[]{"TUTTI", "LETTO", "DA_LEGGERE", "IN_LETTURA"});
        filtroGenere = new JComboBox<>(new String[]{"TUTTI", "Giallo", "Distopia", "Romanzo", "Fantascienza", "Altro"});
        isbnWarningLabel = new JLabel();
        isbnWarningLabel.setForeground(Color.RED);

        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Titolo:"));
        panelTop.add(searchField);
        panelTop.add(btnCercaTitolo);
        panelTop.add(new JLabel("Autore:"));
        panelTop.add(searchAutore);
        panelTop.add(btnCercaAutore);
        panelTop.add(new JLabel("Stato:"));
        panelTop.add(filtroStato);
        panelTop.add(btnFiltraStato);
        panelTop.add(new JLabel("Genere:"));
        panelTop.add(filtroGenere);
        panelTop.add(btnFiltraGenere);

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAggiungi);
        panelButtons.add(btnModifica);
        panelButtons.add(btnRimuovi);
        panelButtons.add(btnSalva);
        panelButtons.add(btnCarica);

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);

        btnAggiungi.addActionListener(e -> {
            JTextField isbnField = new JTextField();
            isbnField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { checkISBN(); }
                public void removeUpdate(DocumentEvent e) { checkISBN(); }
                public void changedUpdate(DocumentEvent e) { checkISBN(); }

                private void checkISBN() {
                    String isbn = isbnField.getText().trim();
                    boolean exists = facade.getLibri().stream()
                            .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));

                    if (exists) {
                        isbnField.setBackground(Color.PINK);
                        isbnWarningLabel.setText("⚠ ISBN già presente!");
                    } else {
                        isbnField.setBackground(Color.WHITE);
                        isbnWarningLabel.setText("");
                    }
                }
            });

            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField titoloField = new JTextField();
            JTextField autoreField = new JTextField();
            JTextField genereField = new JTextField();
            JTextField valutazioneField = new JTextField();
            JComboBox<String> statoBox = new JComboBox<>(new String[]{"LETTO", "DA_LEGGERE", "IN_LETTURA"});

            panel.add(new JLabel("Titolo:"));
            panel.add(titoloField);
            panel.add(new JLabel("Autore:"));
            panel.add(autoreField);
            panel.add(new JLabel("ISBN:"));
            panel.add(isbnField);
            panel.add(isbnWarningLabel);
            panel.add(new JLabel("Genere:"));
            panel.add(genereField);
            panel.add(new JLabel("Valutazione (1-5):"));
            panel.add(valutazioneField);
            panel.add(new JLabel("Stato:"));
            panel.add(statoBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Aggiungi Libro", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String titolo = titoloField.getText();
                    String autore = autoreField.getText();
                    String isbn = isbnField.getText();
                    String genere = genereField.getText();
                    int valutazione = Integer.parseInt(valutazioneField.getText());
                    StatoLettura stato = StatoLettura.valueOf((String) statoBox.getSelectedItem());
                    facade.aggiungiLibro(titolo, autore, isbn, genere, valutazione, stato);
                    aggiornaTabella();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Errore nei dati inseriti");
                }
            }
        });

        // Resto del codice invariato...
        btnModifica.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String titolo = JOptionPane.showInputDialog("Titolo:", tableModel.getValueAt(selected, 0));
                String autore = JOptionPane.showInputDialog("Autore:", tableModel.getValueAt(selected, 1));
                String isbn = JOptionPane.showInputDialog("ISBN:", tableModel.getValueAt(selected, 2));
                String genere = JOptionPane.showInputDialog("Genere:", tableModel.getValueAt(selected, 3));
                int valutazione = Integer.parseInt(JOptionPane.showInputDialog("Valutazione (1-5):", tableModel.getValueAt(selected, 4).toString()));
                String[] stati = {"LETTO", "DA_LEGGERE", "IN_LETTURA"};
                String statoStr = (String) JOptionPane.showInputDialog(null, "Stato di lettura:", "Stato", JOptionPane.QUESTION_MESSAGE, null, stati, tableModel.getValueAt(selected, 5));
                StatoLettura stato = StatoLettura.valueOf(statoStr);

                Libro nuovoLibro = new Libro(titolo, autore, isbn, genere, valutazione, stato);
                facade.modificaLibro(selected, nuovoLibro);
                aggiornaTabella();
            }
        });

        btnRimuovi.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String titolo = (String) tableModel.getValueAt(selected, 0);
                facade.rimuoviLibroPerTitolo(titolo);
                aggiornaTabella();
            }
        });

        btnSalva.addActionListener(e -> {
            try {
                facade.salvaJSON("libri.json");
                JOptionPane.showMessageDialog(null, "Salvataggio completato");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante il salvataggio");
            }
        });

        btnCarica.addActionListener(e -> {
            try {
                facade.caricaJSON("libri.json");
                aggiornaTabella();
                JOptionPane.showMessageDialog(null, "Caricamento completato");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante il caricamento");
            }
        });

        btnCercaTitolo.addActionListener(e -> {
            String testo = searchField.getText().trim();
            if (!testo.isEmpty()) {
                aggiornaTabellaConRisultati(facade.cercaPerTitolo(testo));
            } else {
                aggiornaTabella();
            }
        });

        btnCercaAutore.addActionListener(e -> {
            String testo = searchAutore.getText().trim();
            if (!testo.isEmpty()) {
                aggiornaTabellaConRisultati(facade.cercaPerAutore(testo));
            } else {
                aggiornaTabella();
            }
        });

        btnFiltraStato.addActionListener(e -> {
            String statoSelezionato = (String) filtroStato.getSelectedItem();
            if (statoSelezionato.equals("TUTTI")) {
                aggiornaTabella();
            } else {
                StatoLettura stato = StatoLettura.valueOf(statoSelezionato);
                aggiornaTabellaConRisultati(facade.filtraPerStato(stato));
            }
        });

        btnFiltraGenere.addActionListener(e -> {
            String genere = (String) filtroGenere.getSelectedItem();
            if (genere.equals("TUTTI")) {
                aggiornaTabella();
            } else {
                aggiornaTabellaConRisultati(facade.filtraPerGenere(genere));
            }
        });

        aggiornaTabella();
        setVisible(true);
    }

    private void aggiornaTabella() {
        aggiornaTabellaConRisultati(facade.getLibri());
    }

    private void aggiornaTabellaConRisultati(List<Libro> libri) {
        tableModel.setRowCount(0);
        for (Libro l : libri) {
            tableModel.addRow(new Object[]{
                    l.getTitolo(),
                    l.getAutore(),
                    l.getIsbn(),
                    l.getGenere(),
                    l.getValutazione(),
                    l.getStato()
            });
        }
    }

    public static void main(String[] args) {
        new LibreriaGUI();
    }
}