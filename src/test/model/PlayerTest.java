package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    //checks: constructs new player with default name "Sakura Sato" and default username "not_sakura
    public void testPlayerConstructDefaultName() {
        Player p = new Player();
        assertEquals("Sakura", p.firstName);
        assertEquals("Sato",p.lastName);
        assertEquals("Sakura Sato", p.fullName);
        assertEquals("not_sakura", p.userName);
    }

    @Test
    //checks: changes player first name from "Sakura" to "Haru"
    public void testFirstNameChangedToHaru() {
        Player p = new Player();
        p.changePlayerFirstName("Haru");
        assertEquals("Haru",p.firstName);
        assertEquals("Haru Sato", p.fullName);
    }

    @Test
    //checks: changes player last name from "Sakura" to "Suzuki"
    public void testLastNameChangedToSuzuki() {
        Player p = new Player();
        p.changePlayerLastName("Suzuki");
        assertEquals("Suzuki",p.lastName);
        assertEquals("Sakura Suzuki", p.fullName);
    }

    @Test
    //checks: changes player username from "not_sakura" to "i_am_sakura"
    public void testUserNameChangedCorrectly() {
        Player p = new Player();
        p.changePlayerUserName("i_am_sakura");
        assertEquals("i_am_sakura",p.userName);
    }

    @Test
    //checks: correct first name is returned using get method
    public void testGetFirstName() {
        Player p = new Player();
        assertEquals("Sakura",p.getFirstName());
    }

    @Test
    //checks: correct last name is returned using get method
    public void testGetLastName() {
        Player p = new Player();
        assertEquals("Sato",p.getLastName());
    }

    @Test
    //checks: correct first name is returned using get method
    public void testGetFullName() {
        Player p = new Player();
        assertEquals("Sakura Sato",p.getFullName());
    }

    @Test
    //checks: correct username is returned using get method
    public void testGetUserName() {
        Player p = new Player();
        assertEquals("not_sakura", p.getUserName());
    }


}