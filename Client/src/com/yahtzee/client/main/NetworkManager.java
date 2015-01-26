package com.yahtzee.client.main;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.yahtzee.common.model.Config;
import com.yahtzee.common.model.Game;
import com.yahtzee.common.model.Player;
import com.yahtzee.common.packets.JoinGamePacket;
import com.yahtzee.common.packets.NewGamePacket;
import com.yahtzee.common.packets.NewRoundPacket;
import com.yahtzee.common.packets.Packet;
import com.yahtzee.common.packets.PlayerBacksOutPacket;
import com.yahtzee.common.packets.PlayerJoinedGamePacket;
import com.yahtzee.common.packets.ReadyPacket;
import com.yahtzee.common.packets.RequestUpdatedGamesListPacket;
import com.yahtzee.common.packets.SendMessagePacket;
import com.yahtzee.common.packets.SendPlayerScorePacket;
import com.yahtzee.common.packets.SendTotalScorePacket;
import com.yahtzee.common.packets.StartGamePacket;
import com.yahtzee.common.packets.UpdateGamesListPacket;
import com.yahtzee.common.packets.UpdatePlayerListPacket;
import com.yahtzee.common.packets.UpdatedGamePacket;


public class NetworkManager {

	Client client;
	
	YahtzeeClient m;
	
	ClientMain ui;
	
	public NetworkManager(YahtzeeClient m, ClientMain ui) {
		this.m = m;
		this.ui = ui;
	}
	
	public void connect() throws IOException {
		client = new Client();
	    client.start();
		client.connect(5000, Config.IP_ADDRESS, Config.PORT);

		Kryo kryo = client.getKryo();
		kryo.register(NewGamePacket.class);
	    kryo.register(JoinGamePacket.class);
	    kryo.register(RequestUpdatedGamesListPacket.class);
	    kryo.register(PlayerJoinedGamePacket.class);
	    kryo.register(UpdateGamesListPacket.class);
	    kryo.register(SendMessagePacket.class);
	    kryo.register(ReadyPacket.class);
	    kryo.register(UpdatedGamePacket.class);
	    kryo.register(PlayerBacksOutPacket.class);
	    kryo.register(UpdatePlayerListPacket.class);
	    kryo.register(StartGamePacket.class);
	    kryo.register(SendPlayerScorePacket.class);
	    kryo.register(SendTotalScorePacket.class);
	    kryo.register(NewRoundPacket.class);
	    kryo.register(java.util.ArrayList.class);
	    kryo.register(java.util.HashMap.class);
	    kryo.register(Game.class);
	    kryo.register(Player.class);
	    kryo.register(int[].class);
	    kryo.register(java.util.Random.class);
	    kryo.register(java.util.concurrent.atomic.AtomicLong.class);
	    kryo.register(java.util.HashSet.class);
	    
		client.addListener(new Listener() {
			public void received(Connection connectoin, Object object) {
				// This is what will be called when the connection is made
				if(object instanceof Packet) {
					if(object instanceof UpdateGamesListPacket) {
						HashMap<String, Integer> gamesList =
								((UpdateGamesListPacket) object).gamesList;
						m.gamesList = gamesList;
						ui.update();
					}

					if(object instanceof PlayerJoinedGamePacket) {
						m.currentGame.setPlayers(((PlayerJoinedGamePacket) object).players);
						m.currentGame.setMessageText(((PlayerJoinedGamePacket) object).messageText);					
						
						// Go to the waiting room for that game now
						ui.joinWaitingRoom();
					}
					
	        		/*
	        		 *  Whenever a player sends a message
	        		 */
	        		if(object instanceof SendMessagePacket) {
	        			// Update the chat window
	        			SendMessagePacket sp = (SendMessagePacket)object;
	        			m.currentGame.addMessage(sp.message);
	        			
	        			System.out.println("Received message: " + sp.message);
	        			System.out.println("Total messages text is now: " + m.currentGame.getMessageText());
	        			
	        			// Update the view
	        			ui.update();
	        		}
	        		
	        		/*
	        		 *  Server updated the game
	        		 */
	        		if(object instanceof UpdatedGamePacket) {
	        			UpdatedGamePacket ug = (UpdatedGamePacket) object;
	        			m.currentGame = ug.game;
	        			
	        			// The game has been updated, notify in console
	        			System.out.println("The game has been updated");
	        			ui.update();
	        		}

	        		
	        		/*
	        		 *  Server updated the player list
	        		 */
	        		if(object instanceof UpdatePlayerListPacket) {
	        			UpdatePlayerListPacket up = (UpdatePlayerListPacket) object;
	        			up.players = up.players;
	        			
	        			System.out.println("Updating the players list on my side");
	        			ui.update();
	        		}
	        		
	        		/*
	        		 *  Game Started!
	        		 */
	        		if(object instanceof StartGamePacket) {
	        			System.out.println("The game has been started!");
	        			ui.startGame();
	        		}
	        		
	        		/*
	        		 *  Receiving an updated total score list
	        		 */
	        		if(object instanceof SendTotalScorePacket) {
	        			System.out.println("Received updated totals!");
	        			
	        			// Put the updated totals somewhere
	        			SendTotalScorePacket sp = (SendTotalScorePacket) object;
	        			m.currentGame.totalScores = sp.totalScores;
	        			
	        			if(sp.totalScores.size() == 13) {
	        				// Tell the server we backed out(so we get removed from the game)
	        				playerBackedOut();
	        				
	        				// Bring the user back to the lobby
	        				ui.showLobby();
	        			}
	        			
	        			ui.update();
	        		}
	        		
	        		/*
	        		 *  Notification for a new round
	        		 */
	        		if(object instanceof NewRoundPacket) {
	        			System.out.println("Starting a new round!");
	        			
	        			// Reset the player
	        			m.player.roundFinished = false;
	        			m.player.resetRollsRemaining();
	        			m.player.resetHeld();
	        			
	        			ui.update();
	        			JOptionPane.showMessageDialog(null, "All the players have submitted their scores. Next Round!");
	        		}
				}	
			}
			
			public void disconnected(Connection connection) {
				JOptionPane.showMessageDialog(null, "Lost connection with server, exiting");
				System.exit(0);
			}
		});
	}
	
	public void getUpdatedGamesList() {
		RequestUpdatedGamesListPacket rug = new RequestUpdatedGamesListPacket();
		System.out.println("Requesting a list of updated games");
		client.sendTCP(rug);
	}

	public void joinGame(String gameTitle, String username) {
		JoinGamePacket jp = new JoinGamePacket();
		client.sendTCP(jp);
	}

	public void sendMessage(String text) {
		SendMessagePacket sp = new SendMessagePacket();
		sp.message = text;
		sp.gameTitle = m.currentGame.getTitle();
		sp.sendingPlayer = m.player;
		
		// Debug text
		System.out.println("Sending a message now");
		
		client.sendTCP(sp);
	}

	public void tellReady() {
		ReadyPacket rp = new ReadyPacket();
		rp.readyPlayer = m.player;
		
		System.out.println("Actually sending the readypacket now.");
		client.sendTCP(rp);
	}

	/*
	 *  Sends the newly created current game to the server
	 */
	public void sendNewGame() {
		NewGamePacket np = new NewGamePacket();
		np.game = m.currentGame;
		
		client.sendTCP(np);
	}

	public void requestToJoinGame(String title) {
		JoinGamePacket jp = new JoinGamePacket();
		jp.player = m.player;
		jp.gameTitle = title;
		
		client.sendTCP(jp);
	}

	public void playerBackedOut() {
		PlayerBacksOutPacket pp = new PlayerBacksOutPacket();
		pp.player = m.player;
		
		client.sendTCP(pp);
	}

	public void sendScore(int category, int score) {
		SendPlayerScorePacket sp = new SendPlayerScorePacket();
		sp.category = category;
		sp.score = score;
		sp.gameTitle = m.currentGame.getTitle();
		
		client.sendTCP(sp);
	}
}
