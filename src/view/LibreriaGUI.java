package view;

import controller.LibraryFacade;
import model.Libro;
import model.StatoLettura;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        //Login all'avvio
        facade = LibraryFacade.creaConUtente();

        setTitle("Gestore Libreria Personale - Utente: " + facade.getNomeUtente());
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Salva su chiusura finestra
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                facade.salvaSuFile();
            }
        });

        tableModel = new DefaultTableModel(new String[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAggiungi = new JButton("Aggiungi Libro");
        JButton btnModifica = new JButton("Modifica Selezionato");
        JButton btnRimuovi = new JButton("Rimuovi Selezionato");
        JButton btnSalva = new JButton("Salva su JSON");
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
                        isbnWarningLabel.setText("ISBN già presente!");
                    } else {
                        isbnField.setBackground(Color.WHITE);
                        isbnWarningLabel.setText("");
                    }
                }
            });

            while (true) {
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
                panel.add(new JLabel("Valutazione (0-5):"));
                panel.add(valutazioneField);
                panel.add(new JLabel("Stato:"));
                panel.add(statoBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Aggiungi Libro", JOptionPane.OK_CANCEL_OPTION);
                if (result != JOptionPane.OK_OPTION) return;

                // Raccogli input
                String titolo = titoloField.getText().trim();
                String autore = autoreField.getText().trim();
                String isbn = isbnField.getText().trim();
                String genere = genereField.getText().trim();
                String valStr = valutazioneField.getText().trim();

                // 1. Verifica che tutti i campi siano compilati
                if (titolo.isEmpty() || autore.isEmpty() || isbn.isEmpty() || genere.isEmpty() || valStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi prima di continuare.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // 2. Verifica valutazione
                int valutazione;
                try {
                    valutazione = Integer.parseInt(valStr);
                    if (valutazione < 0 || valutazione > 5) {
                        JOptionPane.showMessageDialog(null, "La valutazione deve essere tra 0 e 5.", "Errore", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci un numero valido nella valutazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // 3. Verifica che il genere sia una stringa alfabetica
                if (!genere.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                    JOptionPane.showMessageDialog(null, "Il genere deve contenere solo lettere.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // 4. Controllo ISBN duplicato
                boolean isbnDuplicato = facade.getLibri().stream()
                        .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));
                if (isbnDuplicato) {
                    JOptionPane.showMessageDialog(null, "ISBN già presente! Inserisci un codice univoco.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Tutto OK → aggiungi il libro
                try {
                    StatoLettura stato = StatoLettura.valueOf((String) statoBox.getSelectedItem());
                    facade.aggiungiLibro(titolo, autore, isbn, genere, valutazione, stato);
                    aggiornaTabella();
                    break;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Errore imprevisto durante l'inserimento.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });




        btnModifica.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(null, "Nessun libro selezionato");
                return;
            }

            String oldIsbn = (String) tableModel.getValueAt(selected, 2);

            String titolo = JOptionPane.showInputDialog("Titolo:", tableModel.getValueAt(selected, 0));
            if (titolo == null) return;

            String autore = JOptionPane.showInputDialog("Autore:", tableModel.getValueAt(selected, 1));
            if (autore == null) return;

            String isbn = JOptionPane.showInputDialog("ISBN:", oldIsbn);
            if (isbn == null) return;

            String genere = JOptionPane.showInputDialog("Genere:", tableModel.getValueAt(selected, 3));
            if (genere == null || genere.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Genere non valido");
                return;
            }

            String valStr = JOptionPane.showInputDialog("Valutazione (1-5):", tableModel.getValueAt(selected, 4).toString());
            if (valStr == null) return;

            int valutazione;
            try {
                valutazione = Integer.parseInt(valStr);
                if (valutazione < 0 || valutazione > 5)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Valutazione deve essere un numero tra 0 e 5");
                return;
            }

            String[] stati = {"LETTO", "DA_LEGGERE", "IN_LETTURA"};
            String statoStr = (String) JOptionPane.showInputDialog(null, "Stato di lettura:", "Stato", JOptionPane.QUESTION_MESSAGE, null, stati, tableModel.getValueAt(selected, 5));
            if (statoStr == null) return;

            // Verifica ISBN duplicato
            boolean isbnEsiste = facade.getLibri().stream()
                .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn) && !l.getIsbn().equalsIgnoreCase(oldIsbn));

            if (isbnEsiste) {
                JOptionPane.showMessageDialog(null, "ISBN già presente in un altro libro");
                return;
            }

            StatoLettura stato = StatoLettura.valueOf(statoStr);
            Libro nuovoLibro = new Libro(titolo, autore, isbn, genere, valutazione, stato);
            facade.modificaLibro(selected, nuovoLibro);
            aggiornaTabella();
        });


        btnRimuovi.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String titolo = (String) tableModel.getValueAt(selected, 0);
                facade.rimuoviLibroPerTitolo(titolo);
                aggiornaTabella();
            } else {
                JOptionPane.showMessageDialog(null, "Seleziona un libro da rimuovere.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalva.addActionListener(e -> {
            try {
                facade.salvaSuFile();
                JOptionPane.showMessageDialog(null, "Salvataggio completato");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dei dati.", "Errore", JOptionPane.ERROR_MESSAGE);
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
