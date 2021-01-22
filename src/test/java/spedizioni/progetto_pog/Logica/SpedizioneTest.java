/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author giokk
 */
public class SpedizioneTest {
    private Spedizione s;
    
    public SpedizioneTest() {
        s = new Spedizione("Mario-12","Via Uccelliera",12);
    }

    @Test
    public void testGetCodice(){
        assertEquals(s.getCodice(),"Mario-12");
    }
    public void testGetStatoConsegna(){
        assertEquals(s.getStatoConsegna(),Stato.PREPARAZIONE);
    }
    public void testGetPeso(){
        assertEquals(s.getPeso(),12);
    }
    public void testGetDataImmissione(){
        assertNotNull(s.getData());
    }
    
}
