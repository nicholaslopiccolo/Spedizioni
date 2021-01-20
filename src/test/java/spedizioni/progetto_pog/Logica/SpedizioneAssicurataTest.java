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
public class SpedizioneAssicurataTest {
    private SpedizioneAssicurata sa;
    public SpedizioneAssicurataTest() {
        sa = new SpedizioneAssicurata("Mario-14","Via trento trieste 13, MO",9,100);
    }
    /**
     * Test of getValoreAssicurato method, of class SpedizioneAssicurata.
     */
    @Test
    public void testGetValoreAssicurato() {
        assertEquals(sa.getValoreAssicurato(),100);
    }
    
}
