/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * <strong>DBGenerico</strong> permette di creare un database generico contenete
 * una lista di oggetti E, la classe sfrutta i generics.
 * @author nicholaslopiccolo
 */
public class DBGenerico<E extends Serializable>{
    private ArrayList<E> lista;
    
    /**
     * Il costruttore inizializza la lista tramite la funzione clear().
     */
    public DBGenerico(){
        clear();
    }
    /**
     * Restituisce la lista del dbgenerico.
     * @return ArrayList contenente oggetti E
     */
    public ArrayList<E> getLista() {
        return lista;
    }
    /**
     * Funzione di servizio che restituisce l'elemento in posizione i.
     * @param i Indice dell'elemento da restituire
     * @return E Oggetto generico
     */
    public E get(int i){
        return (E) lista.get(i);
    }
    /**
     * Funzione di servizio che aggiunge un'elemento alla lista.
     * @param e Elemento da aggiungere
     */
    public void add(E e){
        lista.add(e);
    }
    /**
     * Funzione di servizio che rimuove un'elemento dalla lista.
     * @param e Elemento da rimuovere
     */
    public void remove(E e){
        lista.remove(e);
    }
    /**
     * Funzione di servizio che esegue il replace di un elemento della lista.
     * @param i Indice dell'elemento da rimpiazzare
     * @param e Nuovo elemento
     */
    public void replace(int i,E e){
        lista.set(i,e);
    }
    /**
     * Funzione di servizio che trova l'elemento e restituisce il suo indice.
     * @param e E Oggetto generico
     * @return int indice dell'oggetto trovato
     * @see int
     */
    public int find(E e){
        return lista.indexOf(e);
    }
    /**
     * Funzione di servizio che restituisce la grandezza della lista
     * @return int grandezza della lista
     * @see int
     */
    public int size(){
        return lista.size();
    }
    /**
     * Funzione che pulisce la lista creandone una nuova.
     */
    public void clear(){
        this.lista = new ArrayList<E>();
    }
}
