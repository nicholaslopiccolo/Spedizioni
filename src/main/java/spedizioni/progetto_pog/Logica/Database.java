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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giokk
 */
class Database{
    public DBGenerico <Spedizione> sped_std;
    public DBGenerico <SpedizioneAssicurata> sped_assi;
    public DBGenerico <User> utenti;
        
    private final File u_file = 
            new File("database/utenti.db");
    private final File sped_std_file = 
            new File("database/spedizioni.db");
    private final File sped_assi_file = 
            new File("database/spedizioniAssicurate.db");
    
    public Database(){
        sped_std = new DBGenerico();
        sped_assi = new DBGenerico();
        utenti = new DBGenerico();
    }
    /*
    *   Questa funzione andrà a riempire le variabili spedizioni ed utenti con
    *   il contenuto dei rispettivi file
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

    void print() {
        utenti.print();
        sped_std.print();
        sped_assi.print();
    }
    
}
