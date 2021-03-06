/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Integer.max;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <strong>Database</strong> la classe database permette di creare un oggetto 
 * formato da 3 databse generici uno per file in cui verrano salvati i dati: spedizioni,
 * Spedizioni assicurate e gli utenti.
 * @author nicholaslopiccolo
 */
class Database{
    protected DBGenerico <Spedizione> sped_std;
    protected DBGenerico <SpedizioneAssicurata> sped_assi;
    protected DBGenerico <User> utenti;
        
    private final File u_file = 
            new File("database/utenti.db");
    private final File sped_std_file = 
            new File("database/spedizioni.db");
    private final File sped_assi_file = 
            new File("database/spedizioniAssicurate.db");
    
    /**
     * Il costruttore istanzia i 3 database generici.
     */
    public Database(){
        sped_std = new DBGenerico();
        sped_assi = new DBGenerico();
        utenti = new DBGenerico();
    }
    /**
     * Questa funzione andrà a riempire i 3 database generici di utenti, 
     * spedizioni e spedizioni assicurate.
     * Esegue un clear delle liste, un controllo che i file esistano (se no li crea)
     * dopodichè crea 3 ObjectInputStream ed esegue un loop per caricare i 
     * database ognuno dei quali termina con la EOFException al termine dei caricamenti 
     * chiude i canali di stream.
     */
    public void load(){
        System.out.println("Carico i dati da file");

        sped_std.clear();
        sped_assi.clear();
        utenti.clear();
        
        try{
            if(!u_file.exists())
                u_file.createNewFile();
            if(!sped_std_file.exists())
                sped_std_file.createNewFile();
            if(!sped_assi_file.exists())
                sped_assi_file.createNewFile();
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
        try{
            ObjectInputStream ois_utenti = 
                    new ObjectInputStream(new FileInputStream(u_file));
            ObjectInputStream ois_sped_std = 
                    new ObjectInputStream(new FileInputStream(sped_std_file));
            ObjectInputStream ois_sped_assi = 
                    new ObjectInputStream(new FileInputStream(sped_assi_file));

            try{
                while(true){
                    User u = (User)ois_utenti.readObject();
                    
                    if(u instanceof User)
                        utenti.add(u);
                }  
            }catch(EOFException eof){
		System.out.println("End of file reached");
            }
            
            try{
                while(true){
                    Spedizione std = (Spedizione)ois_sped_std.readObject();
                    
                    if(std instanceof Spedizione)
                        sped_std.add(std);
                }  
            }catch(EOFException eof){
		System.out.println("End of file reached");
            }
            
            try{
                while(true){
                    SpedizioneAssicurata assi = 
                            (SpedizioneAssicurata)ois_sped_assi.readObject();
                    
                    if(assi instanceof SpedizioneAssicurata)
                        sped_assi.add(assi);
                }  
            }catch(EOFException eof){
		System.out.println("End of file reached");
            }
            
            ois_utenti.close();
            ois_sped_std.close();
            ois_sped_assi.close();
            
        }catch(EOFException eof){
            System.out.println("file vuoti");
        }
        catch(IOException ioe){
                ioe.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * Questa funzione andrà a scrivere i 3 database generici di utenti, 
     * spedizioni e spedizioni assicurate negli appositi file.
     * Per iniziare crea 3 ObjectOutputStream ed esegue 3 loop foreach per scrivere gli 
     * oggetti dei database sui file in fine si esegue un fluch e si chiuse ogni
     * canale di stream.
     */
    public void write(){
        System.out.println("Scrivo i dati su file");
        try{
            
            ObjectOutputStream os_utenti = 
                    new ObjectOutputStream(new FileOutputStream(u_file));
            ObjectOutputStream os_sped_std = 
                    new ObjectOutputStream(new FileOutputStream(sped_std_file));
            ObjectOutputStream os_sped_assi = 
                    new ObjectOutputStream(new FileOutputStream(sped_assi_file));

            for (User u : utenti.getLista())		      
                os_utenti.writeObject(u);
            
            for (Spedizione std : sped_std.getLista())		      
                os_sped_std.writeObject(std);
            
            for (SpedizioneAssicurata assi : sped_assi.getLista())		      
                os_sped_assi.writeObject(assi);
            
            os_utenti.flush();
            os_sped_std.flush();
            os_sped_assi.flush();
            
            os_utenti.close();
            os_sped_std.close();
            os_sped_assi.close();
        }
        catch(InvalidClassException e){
            e.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }    
    }
    /**
     * Restituisce la spedizione trovata tramite la stringa univoca "codice", 
     * la ricerca viene eseguita nei due database generici contenente spedizioni
     * e spedizioni assicurate.
     * @param codice Codice univoco della singola spedizione
     * @return Object Può essere una Spedizione o una SpedizioneAssicrata
     */
    public Object trovaSpedizione(String codice){
        final int MAX = max(sped_std.size(),sped_assi.size());
        
        for(int i=0;i<MAX;i++){
            Spedizione std = null;
            SpedizioneAssicurata assi = null;
            
            if(i<sped_std.size()) std = sped_std.get(i);
            if(i<sped_assi.size())assi = sped_assi.get(i);
            
            if(std!=null && codice.equals(std.getCodice())) return std;
            if(assi!=null && codice.equals(assi.getCodice())) return assi;
        }
        return null;
    }
    /**
     * Restituisce una lista di tipo ArrayList contenente tutte le spedizioni e 
     * le spedizioni assicurate.
     * @return ArrayList
     * @see ArrayList
     */
    public ArrayList getSpedizioni(){
        ArrayList lista = new ArrayList();
        
        for(Spedizione std: sped_std.getLista())
            lista.add(std);
        
        for(SpedizioneAssicurata assi: sped_assi.getLista())
            lista.add(assi);
            
        return lista;
    }
    /**
     * Elimina la spedizione dal databse generico, prima la cerca con la funzione
     * trovaSpedizione.
     * @param codice 
     */
    public void eliminaSpedizione(String codice){
        Object spedizione = trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata)
                sped_assi.remove((SpedizioneAssicurata)spedizione);
            else
                sped_std.remove((Spedizione)spedizione);
    }
    
}
