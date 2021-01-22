/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

/**
 *  <strong>Stato</strong>. Questo file contiene l'enum Stato.
 *
 * @author nicholaslopiccolo
 */
public enum Stato{
    /**
     * Stato preparazione
     */
    PREPARAZIONE,
    /**
     * Stato transito
     */
    TRANSITO,
    /**
     * Stato ricevuto
     */
    RICEVUTO,
    /**
     * Stato fallito
     */
    FALLITO,
    /**
     * Stato rimborso richiesto
     */
    RIMBORSO_RICHIESTO,
    /**
     * Stato rimbordo erogato
     */
    RIMBORSO_EROGATO
}

