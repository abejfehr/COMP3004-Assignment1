package com.yahtzee.client.main;
import java.applet.Applet;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.yahtzee.client.screens.GameScreen;
import com.yahtzee.client.screens.Lobby;
import com.yahtzee.client.screens.Screen;
import com.yahtzee.client.screens.WaitingRoom;
import com.yahtzee.common.model.Config;

/*
 *  Main.java
 * 
 *  The entry point for the Java Applet(view) portion of the client.
 */

public class ClientMain extends Applet {

	Screen currentScreen;
	
	YahtzeeClient m = new YahtzeeClient();
	
	// The layout for the applet
	CardLayout cardLayout;
	
	// The class for managing network communication
	NetworkManager nm = new NetworkManager(m, this);
	
    public void init() {
    	/*
    	 *  Test Area
    	 *  
    	 *  Everything here will be for debugging. Class's tests will be run
    	 *  here, and much more!
    	 */
    	// TestPlayer tp = new TestPlayer();
    	// tp.runTests();    	
    	
    	Config.IP_ADDRESS = JOptionPane.showInputDialog("Please enter the IP address of the server", Config.IP_ADDRESS);

    	// Make the applet larger so we can see what's going on
    	setSize(800, 400);
    	
    	/*
    	 *  First things first, connect with the server. If this is not
    	 *  possible, the game isn't even playable
    	 */
    	try {
			nm.connect();
			
			/*
			 * Get the updated list of games from the server, so they're ready to display later
			 */
			nm.getUpdatedGamesList();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to the server, exiting.");
			System.exit(0);
		}
    	
    	/*
    	 * At this point it would be helpful to check if the user is visiting
    	 * from a special URL to join a particular game. 
    	 * 
    	 * If that's the case, the waiting screen for that game should be
    	 * loaded.
    	 * 
    	 * This can be checked with getDocumentBase();
    	 */
    	    	
    	currentScreen = new Lobby(m, this);
    	
    	// Configure the cards
    	cardLayout = new CardLayout();
    	setLayout(cardLayout);
    	
    	// Add the lobby
    	add(new Lobby(m, this), "Lobby");
    	
    	cardLayout.show(this, "Lobby");
    	
        setVisible(true);
    }
    
    public void joinGame(GameScreen g) {
    	add(new GameScreen(m, this), "Game");
    	cardLayout.show(this, "Game");    	
    }
    
    public void showLobby() {
    	// Update the games list since this will be shown
		nm.getUpdatedGamesList();
		
		// Show the lobby
		currentScreen = new Lobby(m, this);
    	add(currentScreen, "Lobby");
    	cardLayout.show(this, "Lobby");
    }

	public void update() {
		if(currentScreen != null) {
			currentScreen.update();
			System.out.println("I just told the screen to update. ");
		}
	}
	
	public void joinWaitingRoom() {
		if(!(currentScreen instanceof WaitingRoom)) {
			// Show the waiting room
			currentScreen = new WaitingRoom(m, this);
	    	add(currentScreen, "WaitingRoom");
	    	cardLayout.show(this, "WaitingRoom");		
		}
		else {
			currentScreen.update();
		}
	}

	public void sendMessage(String text) {
		nm.sendMessage(text);
		m.currentGame.addMessage(text);
	}

	public void tellReady() {
		nm.tellReady();
	}

	public void startGame() {
		// Show the game screen
		currentScreen = new GameScreen(m, this);
    	add(currentScreen, "Game");
    	cardLayout.show(this, "Game");
	}

	/*
	 *  Sends an already-made game to the server with the player
	 */
	public void sendNewGame() {
		nm.sendNewGame();
		
		// Finally join the waiting room
		joinWaitingRoom();
	}

	/*
	 *  When the user hits "join game" it should send a request to the server
	 */
	public void requestToJoinGame(String selectedValue) {
		nm.requestToJoinGame(selectedValue);
	}

	public void playerBackedOut() {
		nm.playerBackedOut();
		
		showLobby();
	}

	public void sendScore(int category, int score) {
		nm.sendScore(category, score);
	}
 }
 