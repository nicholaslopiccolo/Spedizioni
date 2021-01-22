package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Logica.Core;
import spedizioni.progetto_pog.Logica.Spedizione;
import spedizioni.progetto_pog.Logica.SpedizioneAssicurata;
import spedizioni.progetto_pog.Logica.Stato;

/**
 * <strong>ListaSpedizioniPanel</strong> é il pannello che mostra la lista delle
 * spedizioni attualmente in corso o fallite. La classe stessa implementa Runnable
 * che permette l'utilizzo del pannello come se fosse un thread questo per eseguire
 * un refresh della lista delle spedizioni ogni 5 secondi.
 * @author nicholaslopiccolo
 */
public class ListaSpedizioniPanel extends JPanel implements Runnable{
    /**
     * Thread che verrà utilizzato per avviare lo swingWorker
     */
    private Thread thread;
    /**
     * Variabile che memorizza il core dell'applicazione.
     */
    private Core core;
    /**
     * Tabella spedizioni appiccicata al pannello
     */
    private JTable tabella_spedizioni;
    /**
     * Modello della tabella delle spedizioni
     */
    private DefaultTableModel modello_tabella;
    /**
     * Indice della colonna di stato
     */
    private final int C_STATO = 0;
    /**
     * Array di stringhe contenente il nome delle colonne
     */
    private final String[] colonne = new String[]{
                                        "Stato Consegna",
                                        "Codice",
                                        "Data",
                                        "Indirizzo",
                                        "Peso",
                                        "Val. Assicurato"
                                    };;

    /**
     * Il cotruttore esegue un setup grafico del pannello con la funzione 
     * initComponents() e avvia il thread con la funzione start
     * @param frame Frame antenato su cui è posizionato il pannello
     * @param core Gestisce la logica dell'applicazione
     */
    @SuppressWarnings("empty-statement")
    public ListaSpedizioniPanel(AppFrame frame, Core core) {
        super();
        this.core = core;

        initComponents();
        
        start();
    }
    /**
     * Questa funzione esegue un setup degli elementi principali del pannello
     * quali scrollpane, tabella e model di quest'ultima.
     * Le colonne della tabella sono statiche e vengono settate tramite la variabile
     * colonne contenuta dello stato dell'oggetto, il contenuto delle row viene 
     * caricato ad ogni spin del thread tramite uno swingworker
     * (gli override verranno spiegati nel dettaglio nel file polimorfismo.txt).
     * 
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JScrollPane scroll_panel = new JScrollPane();
        tabella_spedizioni = new JTable() {
            /**
             * Modifica il colore delle row in base allo stato della spedizione
             */
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                int modelColumn = convertColumnIndexToModel(col);

                Component l;
                Stato stato;
                
                // Se il refresh sta girando potrebbe non trovare questi oggetti
                try{
                    l = super.prepareRenderer(renderer, row, col);
                    stato = (Stato) getModel().getValueAt(row, 0);
                }catch(Exception e){
                    return null;
                }
                
                if(modelColumn == 0){
                    switch (stato) {
                        case RICEVUTO -> {
                            l.setBackground(Color.GREEN);
                            l.setForeground(Color.black);
                            break;
                        }
                        case FALLITO -> {
                            l.setBackground(Color.RED);
                            l.setForeground(Color.WHITE);
                            break;
                        }
                        case PREPARAZIONE -> {
                            l.setBackground(Color.GRAY);
                            l.setForeground(Color.WHITE);
                            break;
                        }
                        case TRANSITO -> {
                            l.setBackground(Color.YELLOW);
                            l.setForeground(Color.black);
                            break;

                        }
                        case RIMBORSO_RICHIESTO -> {
                            l.setBackground(Color.CYAN);
                            l.setForeground(Color.black);
                            break;

                        }
                        case RIMBORSO_EROGATO -> {
                            l.setBackground(Color.BLUE);
                            l.setForeground(Color.WHITE);
                            break;
                        }
                    }
                }else{
                    l.setBackground(Color.WHITE);
                    l.setForeground(Color.BLACK);
                }
                modello_tabella.fireTableCellUpdated(row, col);
                return l;
            }
            
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);

                if (modelColumn == C_STATO) {
                    JComboBox combo_box = comboBoxCustom((Stato) this.getModel().getValueAt(row, modelColumn));
                    if (combo_box != null) {
                        return new DefaultCellEditor(combo_box);
                    }
                }
                // Cancellare una row
                /*if (core.isAdmin() && modelColumn == 0) {
                    //JButton bottone_elimina = new JButton();
                    return new DefaultCellEditor(new JCheckBox());
                }*/
                return null;
            }
        };
        modello_tabella = new DefaultTableModel(new Object[][]{}, colonne) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == C_STATO) {
                    // Passa il codice della spedizione e lo stato
                    core.aggiornaStatoSpedizione((String) super.getValueAt(row, 1), (Stato) aValue);
                    super.setValueAt(aValue, row, column);
                    super.fireTableCellUpdated(row, column);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == C_STATO) {
                    boolean assi = true;
                    Stato stato = (Stato) this.getValueAt(row, C_STATO);

                    try{
                        assi = !((String)this.getValueAt(row, 5)).isEmpty();
                    } catch(Exception e){}
                    
                    return (!core.isAdmin() && stato == Stato.FALLITO && assi);
                }
                return false;
            }
        };
        
        if(core.isAdmin())
            tabella_spedizioni.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    int r = tabella_spedizioni.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < tabella_spedizioni.getRowCount()) {
                        tabella_spedizioni.setRowSelectionInterval(r, r);
                    } else {
                        tabella_spedizioni.clearSelection();
                    }

                    int rowindex = tabella_spedizioni.getSelectedRow();
                    if (rowindex < 0)
                        return;
                    if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                        String codice = (String) tabella_spedizioni.getValueAt(rowindex, 1);
                        if(core.isSpedizioneStatoFinale(codice)){
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem elimina = new JMenuItem("Elimina "+codice);
                            elimina.addActionListener((ActionEvent actionEvent) -> {
                                core.eliminaSpedizione(codice);
                                modello_tabella.removeRow(rowindex);
                                System.out.println(codice);
                            });
                            //popup.add("ELIMINA " + codice);
                            popup.add(elimina);
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            });
        
        tabella_spedizioni.setRowSelectionAllowed(core.isAdmin());
        tabella_spedizioni.setModel(modello_tabella);
        tabella_spedizioni.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scroll_panel.setViewportView(tabella_spedizioni);
        
        add(scroll_panel);
    }
    /**
     * Questa funzione è necessaria per la creazione di un menù a tendina che permetta
     * la modifica dello stato in particolari condizioni.
     * @param stato Stato della spedizione presa in considerazione
     * @return JComboBox Crea un combobox con i possibili stati successivi
     * @see JComboBox
     */
     private JComboBox comboBoxCustom(Stato stato) {
        JComboBox combo_box = new JComboBox();
        switch (stato) {
            case FALLITO -> {
                if (core.isAdmin() == false) {
                    combo_box.addItem(Stato.FALLITO);
                    combo_box.addItem(Stato.RIMBORSO_RICHIESTO);
                } else {
                    return null;
                }

                break;
            }
            case PREPARAZIONE -> {
                if (core.isAdmin()) {
                    combo_box.addItem(Stato.PREPARAZIONE);
                    combo_box.addItem(Stato.TRANSITO);
                    combo_box.addItem(Stato.FALLITO);
                } else {
                    return null;
                }

                break;
            }
            case TRANSITO -> {
                if (core.isAdmin()) {
                    combo_box.addItem(Stato.TRANSITO);
                    combo_box.addItem(Stato.RICEVUTO);
                    combo_box.addItem(Stato.FALLITO);
                } else {
                    return null;
                }

                break;
            }
            case RIMBORSO_RICHIESTO -> {
                if (core.isAdmin()) {
                    combo_box.addItem(Stato.RIMBORSO_RICHIESTO);
                    combo_box.addItem(Stato.RIMBORSO_EROGATO);
                } else {
                    return null;
                }

                break;
            }
            default -> {
                return null;
            }
        }
        return combo_box;
    }
    /**
     * Esegue lo start del thread del pannello
     */
    public void start() {
        if (thread == null) {
          thread = new Thread(this);
          thread.start();
        }
    }
    /**
     * Esegue lo stop del thread del pannello settando quest'ultimo a null
     */
    public void stop() {
        thread = null;
    }
    /**
     * Funzione che avvia il thread creando il worker ed eseguendo l'execute al 
     * termine va in fase di sleep per 5 secondi
     */
    @Override
    public void run() {
        while (thread != null) {
            TableSwingWorker worker = new TableSwingWorker(modello_tabella);
            try {
                worker.execute();
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
            
        }
        thread = null;
    }
    /**
     * <strong>TableSwingWorker</strong> questa classe crea uno swingworker, 
     * che eseguirà un refresh della lista delle spedizioni andando 
     * a svuotare per poi ricaricare il modello della tabella.
     */
    public class TableSwingWorker extends SwingWorker<DefaultTableModel, Object[]> {

        private final DefaultTableModel tableModel;

        /**
         * Il costruttore aggancia il modello della tabella ad una variabile del
         * proprio stato interno.
         * @param tableModel Modella della tabella delle spedizioni
         */
        public TableSwingWorker(DefaultTableModel tableModel) {
            this.tableModel = tableModel;
        }
        /**
         * La funzione così modificata cancella la lista delle row della tabella
         * ed esegue un publish per ogni spedizione.
         * @return DefaultTableModel Modello della tabella modificato
         */

        @Override
        protected DefaultTableModel doInBackground(){

            // This is a deliberate pause to allow the UI time to render
            tableModel.setRowCount(0);
            System.out.println("Start polulating");
            
            ArrayList spedizioni = core.getSpedizioni();
            
            for(Object sped: spedizioni){
                Object row[] = new Object[6];
                
                if(sped instanceof SpedizioneAssicurata){
                    SpedizioneAssicurata assi = (SpedizioneAssicurata)sped;
                    row[0] = assi.getStatoConsegna();
                    row[1] = assi.getCodice();
                    row[2] = assi.getData();
                    row[3] = assi.getDestinazione();
                    row[4] = assi.getPeso();
                    row[5] = assi.getValoreAssicurato();
                }else{
                    Spedizione std = (Spedizione)sped;
                    row[0] = std.getStatoConsegna();
                    row[1] = std.getCodice();
                    row[2] = std.getData();
                    row[3] = std.getDestinazione();
                    row[4] = std.getPeso();
                    row[5] = "";
                }
                
                publish(row);
                Thread.yield();
                    
            }
            return tableModel;
        }

        /**
         * La funzione così modificata esegue un loop foreach dei chunks di ogni singola row publicata dalla funzione doInBackground e la aggiunge al modello della tabella
         * @param chunks Lista di row della tabella
         */
        @Override
        protected void process(List<Object[]> chunks) {
            for(Object[] o: chunks)
                tableModel.addRow(o);
            //ableModel.addRows(chunks);
        }
    }
}