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
public class UserTest {
    private User u;

    public UserTest() {
        this.u = new User("mario","password","Largo Garibaldi 12");
    }

    /**
     * Test of getNome method, of class User.
     */
    @Test
    public void testGetUsername() {
        assertEquals(u.getUsername(),"Mario");
    }
    
    public void testGetPassword() {
        assertEquals(u.getPassword(),"password");
    }
    
    public void testGetIndirizzo() {
        assertEquals(u.getIndirizzo(),"Largo Garibaldi 12");
    }
}
