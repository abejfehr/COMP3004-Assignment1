package com.yahtzee.client.screens;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.yahtzee.client.main.ClientMain;
import com.yahtzee.client.main.YahtzeeClient;

/*
 *  WaitingRoom.java
 *  
 *  This class will contain all of the information and controls that will show
 *  on the waiting room screen.
 *    
 *  Layout of the Waiting Room Screen:
 *  __________________________________________________
 *  | 0,0         Yahtzee with friends               |
 *  |                                                |
 *  | 0,1      Waiting for game XXX to begin         |
 *  | _________________________________  __________  |
 *  | |0,2       |1,2                 |  |  Ready! | |
 *  | |          |                    |              |
 *  | |  Player  |    Chat Window     |              |
 *  | |   List   |                    |              |
 *  | |          |                    |              |
 *  | |          |____________________|  __________  |
 *  | |__________|Hey, what's up?|Send|  |  Back  |  |
 *  |________________________________________________|
 *  
 *  The layout is done with a GridBagLayout, which actually looks awful. I hope
 *  that in the future after the game is implemented  I'll get the time to make
 *  it more beautiful.
 */

public class WaitingRoom extends Screen {
	
	private JLabel titleLabel = new JLabel("Yahtzee with friends");
	private JLabel waitingLabel = new JLabel("Waiting for game " + m.currentGame.getTitle() + " to begin");
	private JList playerList =
		new JList(m.currentGame.getPlayers().toArray());
	private JTextArea messageArea = new JTextArea(m.currentGame.getMessageText());
	private JTextField messageField = new JTextField(25);
	private JButton sendButton = new JButton("Send");
	private JButton readyButton = new JButton("Ready!");
	private JButton backButton = new JButton("Back");
		
	public WaitingRoom(YahtzeeClient m, ClientMain referrer) {
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
    	
    	// The label for the waiting text
    	c.anchor = GridBagConstraints.CENTER;
    	c.gridx = 0;
    	c.gridy = 1;
    	c.gridwidth = 3;
    	c.gridheight = 1;
    	add(waitingLabel, c);
    	
    	// The list of players
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 1;
    	c.gridx = 0;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	c.gridheight = 4;
    	playerList.setEnabled(false);
    	add(playerList, c);
    	
    	// The messaging text area
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 2;
    	c.gridx = 1;
    	c.gridy = 2;
    	c.gridwidth = 2;
    	c.gridheight = 3;
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	add(messageScrollPane, c);
    	
    	// The message text field
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 2;
    	c.gridx = 1;
    	c.gridy = 5;
    	c.gridwidth = 1;
    	c.gridheight = 1;
        messageField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        			sendMessage();
        		}
        	}
        });
    	add(messageField, c);
    	
    	// The send button for the messages
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.NONE;
    	c.weightx = 1;
    	c.gridx = 2;
    	c.gridy = 5;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	sendButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			sendMessage();
    		}
    	});
    	add(sendButton, c);
    	
    	// The ready button
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 1;
    	c.gridx = 3;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	readyButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			beReady();
    		}
    	});
    	add(readyButton, c);
    	
    	// The back button
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 1;
    	c.gridx = 3;
    	c.gridy = 5;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	backButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			// The user pussied out, people need to be notified
    			referrer.playerBackedOut();
    		}
    	});
    	add(backButton, c);
    	    	
    	update();
    	
    	// Show the Layout
    	setVisible(true);
	}
	
	@Override
	public void update() {
		super.update();
		
		// Update the game chat text
		messageArea.setText(m.currentGame.getMessageText());
		
		System.out.println("The number of players in the game: " + m.currentGame.getPlayers().size());

		// Update the players list
		DefaultListModel model = new DefaultListModel();
		for(int i=0;i<m.currentGame.getPlayers().size();++i) {
			model.addElement(m.currentGame.getPlayers().get(i));
		}
		playerList.setModel(model);
		
		// Revalidate and repaint the window
		revalidate();
		repaint();
	}
	
	private void sendMessage() {
		String messageText = m.player.getUsername() + ": " + messageField.getText();
		referrer.sendMessage(messageText);
		
		// Update the message text area
		messageArea.setText(messageArea.getText() + "\n" + messageText);
		messageField.setText("");
		messageField.requestFocus();
	}
	
	private void beReady() {
		readyButton.setEnabled(false);
		
		referrer.tellReady();
	}
}
