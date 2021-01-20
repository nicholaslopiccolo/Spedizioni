package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 *
 * @author giokk
 */
public class ListaSpedizioniPanel extends JPanel implements Runnable{
    private Thread thread;
    
    private Core core;

    private JTable tabella_spedizioni;
    private DefaultTableModel modello_tabella;
    
    private final int C_STATO = 0;

    private final String[] colonne = new String[]{
                                        "Stato Consegna",
                                        "Codice",
                                        "Data",
                                        "Indirizzo",
                                        "Peso",
                                        "Val. Assicurato"
                                    };;

    /**
     *
     * @param frame
     * @param core
     */
    @SuppressWarnings("empty-statement")
    public ListaSpedizioniPanel(AppFrame frame, Core core) {
        super();
        this.core = core;

        initComponents();
        
        start();
    }

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
//                if(column==0){
//                    Stato stato = (Stato) this.getValueAt(row, C_STATO);
//                    return(core.isAdmin() && (stato==Stato.RICEVUTO || stato==Stato.RIMBORSO_EROGATO));
//                }
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
    
    // Thread stuff
    
    public void start() {
        if (thread == null) {
          thread = new Thread(this);
          thread.start();
        }
    }

    public void stop() {
        thread = null;
    }

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

    private void eliminaRighe() {
        for (int i : tabella_spedizioni.getSelectedRows()) {
            modello_tabella.removeRow(i);
        }
    }

    private void eliminaRigaSelezionata() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public class TableSwingWorker extends SwingWorker<DefaultTableModel, Object[]> {

        private final DefaultTableModel tableModel;

        public TableSwingWorker(DefaultTableModel tableModel) {
            this.tableModel = tableModel;
        }

        @Override
        protected DefaultTableModel doInBackground() throws Exception {

            // This is a deliberate pause to allow the UI time to render
            tableModel.setRowCount(0);
            System.out.println("Start polulating");
            
            ArrayList<Spedizione> lista_sped_std;
            ArrayList<SpedizioneAssicurata> lista_sped_assi;

            // Carica le spedizione in base a chi è lo user
            if (core.isAdmin()) {
                lista_sped_std = core.getSpedizioni().getLista();
                lista_sped_assi = core.getSpedizioniAssicurate().getLista();
            } else {
                lista_sped_std = core.getSpedizioniUtente();
                lista_sped_assi = core.getSpedizioniAssicurateUtente();
            }

            for (int i = 0; i < lista_sped_std.size(); i++) {
                Object row[] = new Object[6];
                Spedizione std = lista_sped_std.get(i);

                //row[0] = false;
                row[0] = std.getStatoConsegna();
                row[1] = std.getCodice();
                row[2] = std.getData();
                row[3] = std.getDestinazione();
                row[4] = std.getPeso();
                row[5] = "";

                publish(row);
                Thread.yield();
            }
            for (int i = 0; i < lista_sped_assi.size(); i++) {
                Object row[] = new Object[6];
                SpedizioneAssicurata assi = lista_sped_assi.get(i);

                //row[0] = false;
                row[0] = assi.getStatoConsegna();
                row[1] = assi.getCodice();
                row[2] = assi.getData();
                row[3] = assi.getDestinazione();
                row[4] = assi.getPeso();
                row[5] = assi.getValoreAssicurato();

                publish(row);
                Thread.yield();
            }
            return tableModel;
        }

        @Override
        protected void process(List<Object[]> chunks) {
            System.out.println("Refresh...");
            for(int i=0;i<chunks.size();i++)
                tableModel.addRow(chunks.get(i));
            //ableModel.addRows(chunks);
        }
    }
}