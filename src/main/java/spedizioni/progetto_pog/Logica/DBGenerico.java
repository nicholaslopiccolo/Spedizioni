/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author giokk
 */
public class DBGenerico<E extends Serializable>{
    private ArrayList<E> lista;
    
    public DBGenerico(){
        clear();
    }

    public ArrayList<E> getLista() {
        return lista;
    }

    public void setLista(ArrayList<E> lista) {
        this.lista = lista;
    }
    
    public E get(int i){
        return (E) lista.get(i);
    }
    
    public void add(E e){
        lista.add(e);
    }
    public void remove(E e){
        lista.remove(e);
    }
    
    public void replace(int index,E e){
        lista.set(index,e);
    }
    
    public String toString(){
        String str = "";
        for(int i=0;i<lista.size();i++)
            str+=lista.get(i).toString();
        return lista.toString();
    }
    
    public int find(E e){
        return lista.indexOf(e);
    }
    
    public int size(){
        return lista.size();
    }
    
    public void clear(){
        this.lista = new ArrayList<E>();
    }
    
    public void print(){
        if(lista.isEmpty())return;
        for(int i=0;i<lista.size();i++)
            System.out.println(lista.get(i).toString());
    }
}
