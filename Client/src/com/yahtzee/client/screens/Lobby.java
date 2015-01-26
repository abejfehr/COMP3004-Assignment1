package com.yahtzee.client.screens;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.yahtzee.client.main.ClientMain;
import com.yahtzee.client.main.YahtzeeClient;
import com.yahtzee.common.model.Game;
import com.yahtzee.common.model.Player;

/*
 *  Lobby.java
 *  
 *  This class will contain all of the information and controls that will show
 *  on the lobby screen.
 *    
 *  Layout of the Lobby Screen:
 *  __________________________________________________
 *  | 0,0         Yahtzee with friends               |
 *  |                                                |
 *  | 0,1        Username: [1,1_______]              |
 *  | _________________________________  __________  |
 *  | |0,2                            |  | New Game| |
 *  | |                               |  __________  |
 *  | |                               |  |Join Game| |
 *  | |          Game List            |              |
 *  | |                               |              |
 *  | |                               |  __________  |
 *  | |_______________________________|  |  Quit  |  |
 *  |________________________________________________|
 *  
 *  The layout is done with a GridBagLayout, which actually looks awful. I hope
 *  that in the future after the game is implemented  I'll get the time to make
 *  it more beautiful.
 */

public class Lobby extends Screen {
	
	private DefaultListModel model = new DefaultListModel();
	
	private JLabel titleLabel = new JLabel("Yahtzee with friends");
	private JLabel usernameLabel = new JLabel("Username: ");
	private JTextField usernameField = new JTextField(12);
	private JList gamesList = new JList(model);
	private JButton newGameButton = new JButton("New Game");
	private JButton joinGameButton = new JButton("Join Game");
	private JButton quitButton = new JButton("Quit");
    
    public Lobby(YahtzeeClient m, ClientMain referrer) {
    	super(m, referrer);
    	
    	// Define the layout and the constraints to use
    	setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
    	// The label for the title
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 3;
    	c.gridheight = 1;
    	titleLabel.setFont(titleLabel.getFont ().deriveFont (48.0f));
    	add(titleLabel, c);
    	
    	// The label for the username text field
    	c.anchor = GridBagConstraints.EAST;
    	c.gridx = 1;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	add(usernameLabel, c);
    	
    	// The username text field
    	c.anchor = GridBagConstraints.CENTER;
    	c.gridx = 2;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	if(m.player != null)
    		usernameField.setText(m.player.getUsername());
    	add(usernameField, c);
    	
    	// The list of games
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 2;
    	c.gridx = 0;
    	c.gridy = 2;
    	c.gridwidth = 2;
    	c.gridheight = 4;
    	gamesList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(gamesList.getSelectedValuesList().size() > 0) {
					joinGameButton.setEnabled(true);
				}
			}
    	});
    	add(gamesList, c);
    	
    	// The new game button
    	c.anchor = GridBagConstraints.CENTER;
    	c.weightx = 1;
    	c.gridx = 2;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	newGameButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			String username = usernameField.getText().trim();
    			if(username.isEmpty()) {
    				JOptionPane.showMessageDialog(null, "Your username cannot be blank.");
    				return;
    			}
    			
    			// Verify the field wasn't blank first
    			String gameTitle = JOptionPane.showInputDialog("Enter a game name: ");
    			if(gameTitle == null) {
    				return;
    			}
    			else if(gameTitle.isEmpty()) {
    				JOptionPane.showMessageDialog(null, "You must give the game a name.");
    				return;
    			}
    			
    			// Create a new player with the name specified
    			m.player = new Player(username);
    			
    			// Create the game
    			m.currentGame = new Game();
    			m.currentGame.addPlayer(m.player);
    			m.currentGame.setTitle(gameTitle);
    			
    			// Send the game to the server
    			referrer.sendNewGame();
    		}
    	});
    	add(newGameButton, c);
    	
    	// The join game button
    	c.anchor = GridBagConstraints.CENTER;
    	c.weightx = 1;
    	c.gridx = 2;
    	c.gridy = 3;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	joinGameButton.setEnabled(false); // enabled only when a game is selected
    	joinGameButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			String username = usernameField.getText().trim();
    			if(username.isEmpty()) {
    				JOptionPane.showMessageDialog(null, "Your username cannot be blank.");
    				return;
    			}
    			
    			// Create the player
    			m.player = new Player(username);
    			
    			// Start by creating a new, blank game
    			m.currentGame = new Game();
    			
    			// Assuming the below request will succeed(which is awful), let's set this game's title already
    			m.currentGame.setTitle((String) gamesList.getSelectedValue());
    			
    			// TODO: fix the above line
    			
    			// Request to join the current game
    			referrer.requestToJoinGame((String) gamesList.getSelectedValue());
    		}
    	});
    	add(joinGameButton, c);
    	
    	// The quit button
    	c.anchor = GridBagConstraints.CENTER;
    	c.weightx = 1;
    	c.gridx = 2;
    	c.gridy = 5;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	quitButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
    	});
    	add(quitButton, c);
    	
    	update();
    	// Show the Layout
    	setVisible(true);
    }
    
    @Override
    public void update() {    	
    	for(String key : m.gamesList.keySet()) {
    		model.addElement(key);
    	}
    	
    	gamesList.setModel(model);
    	// TODO: fix this code, since it doesn't currently refresh the screen.
    }
}
