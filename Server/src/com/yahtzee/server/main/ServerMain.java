package com.yahtzee.server.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
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

/*
 *  Main.java
 * 
 *  This file will be the controller in the Server application solution to
 *  JP's Assignment 1 for COMP3004.
 */

public class ServerMain {
	
	static Server server;
	static HashMap<Player, Connection> connectionList;
	static YahtzeeServer m;
	
	public static void main(String[] args) throws IOException {
				
		// Create the model's container
		m = new YahtzeeServer();
		
		// Create a new server and start it
		try {
			server = new Server();
		    server.start();
		    server.bind(Config.PORT);
		}
		catch(java.net.BindException e) {
			JOptionPane.showMessageDialog(null, "Another server must already be started somewhere, please close that first.");
		}
	    Kryo kryo = server.getKryo();
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

	    
	    /*
	     *  Debugging code will go here
	     */
	    
	    connectionList = new HashMap<Player, Connection>();
	    
	    // Listen for packets
	    server.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	        	// Receive a request for an updated games list
	        	if(object instanceof Packet) {
	        		/*
	        		 *  The following packet gets received when the lobby page is loaded
	        		 */
	        		if(object instanceof RequestUpdatedGamesListPacket) {
	        			// Say that we received the packet
	        			System.out.println("Received the request for an updated list of games");
	        			
	        			// Send the user the updated games
	        			UpdateGamesListPacket gp = new UpdateGamesListPacket();
	        			
	        			// Go through the model's games list and add each one
	        			for(Game g : m.gamesList) {
	        				gp.gamesList.put(g.getTitle(), 0);
	        			}
	        			
	        			// Send the thing
	        			connection.sendTCP(gp);
	        		}
	        		
	        		/*
	        		 *  The following packet gets received when a new game is created
	        		 */
	        		if(object instanceof NewGamePacket) {
	        			NewGamePacket np = (NewGamePacket) object;
	        			
	        			// Add the new user to the connection list
	        			connectionList.put(np.game.getPlayers().get(0), connection);
	        			
	        			m.gamesList.add(np.game);
	        			
	        			// Send something to tell the user that they were successfully received (maybe in the future)
	        		}
	        		
	        		/*
	        		 *  JoinGamePacket gets received when the user wants to join a game
	        		 */
	        		if(object instanceof JoinGamePacket) {
	        			JoinGamePacket jp = (JoinGamePacket) object;
	        				        			
	        			// Add the user to the game
	        			Game g = m.getGame(jp.gameTitle);
	        			g.addPlayer(jp.player);
	        			System.out.println("Player was successfully added to the game.");

	        			// Add the new user to the connection list
	        			connectionList.put(jp.player, connection);
	        			
	        			// Tell the client everything was A-OK
	        			PlayerJoinedGamePacket pj = new PlayerJoinedGamePacket();
	        			pj.players = m.getGame(jp.gameTitle).getPlayers();
	        			pj.messageText = m.getGame(jp.gameTitle).getMessageText();
	        			
	        			// Send this to everyone in the game
	        			for(int i=0;i<g.getPlayers().size();++i) {
	        				connectionList.get(g.getPlayers().get(i)).sendTCP(pj);
	        			}
	        		}
	        		
	        		/*
	        		 *  Whenever a player sends a message
	        		 */
	        		if(object instanceof SendMessagePacket) {
	        			System.out.println("Receiving a message now");
	        			
	        			SendMessagePacket sp = (SendMessagePacket)object;
	        			m.getGame(sp.gameTitle).addMessage(sp.message);
	        			
	        			for(int i=0;i<m.getGame(sp.gameTitle).getPlayers().size();++i) {
	        				// Send to only the OTHER players as well
	        				System.out.println("Sending player's username: " + sp.sendingPlayer.getUsername());
	        				System.out.println("Iterating player's username: " + m.getGame(sp.gameTitle).getPlayers().get(i).getUsername());
	        				
	        				if(!sp.sendingPlayer.getUsername().equals(m.getGame(sp.gameTitle).getPlayers().get(i).getUsername())) {
	        					server.sendToTCP(connectionList.get(m.getGame(sp.gameTitle).getPlayers().get(i)).getID(), sp);
	        					System.out.println("Relaying message to " + m.getGame(sp.gameTitle).getPlayers().get(i));
	        				}
	        				else
	        					System.out.println("That was a user I couldn't relay the message to.");
	        			}
	        		}
	        		
	        		/*
	        		 *  A packet to signify the player is ready
	        		 */
	        		if(object instanceof ReadyPacket) {
	        			System.out.println("I've received a ready packet.");
	        			
	        			ReadyPacket rp = (ReadyPacket) object;
	        			
	        			Game g = m.getGame(rp.readyPlayer);
	        			m.readyPlayers.add(rp.readyPlayer);
	        			
	        			System.out.println("Ready Players: " + m.readyPlayers.size());
	        			
	        			if(m.readyPlayers.size() == g.getPlayers().size()) {
	        				System.out.println("Everyone is ready to play!");
	        				
	        				StartGamePacket sp = new StartGamePacket();
	        				
		        			for(int i=0;i<g.getPlayers().size();++i) {
		        				connectionList.get(g.getPlayers().get(i)).sendTCP(sp);
		        			}
		        			
		        			// Clear the ready players so we can begin adding them again.
		        			// We're going to use this to keep track of players finishing a round
		        			m.readyPlayers.clear();
	        				
	        				return;
	        			}
	        			
	        			// Send a message to all users saying this user is ready
	        			SendMessagePacket sp = new SendMessagePacket();
	        			sp.message = rp.readyPlayer.getUsername() + " is ready to play";
	        			sp.sendingPlayer = rp.readyPlayer;
	        			
	        			for(int i=0;i<g.getPlayers().size();++i) {
	        				connectionList.get(g.getPlayers().get(i)).sendTCP(sp);
	        			}

	        		}
	        		
		        	/*
		        	 *  A packet to say a player backed out
		        	 */
	        		if(object instanceof PlayerBacksOutPacket) {
	        			PlayerBacksOutPacket pp = (PlayerBacksOutPacket) object;
	                	// Go through the player/connection list and remove the player from their game
	                	Player keyToRemove = null;
	                	Game g = null;
	                	for(Entry<Player, Connection> e: connectionList.entrySet()) {
	                		if(e.getValue() == connection) {
	                			// Find which game they belonged to
	                        	g = m.getGame(e.getKey());
	                        	
	                			m.clearPlayer(e.getKey());
	                			keyToRemove = e.getKey();
	                   		}
	                	}
	                	connectionList.remove(keyToRemove);
	                	sendUpdatedPlayerList(g, keyToRemove);
	        		}
	        		
	        		/*
	        		 *  Player updated score
	        		 */
	        		if(object instanceof SendPlayerScorePacket) {
	        			SendPlayerScorePacket sp = (SendPlayerScorePacket) object;
	        			
	        			Game g = m.getGame(sp.gameTitle);
	        		    g.totalScores.put(sp.category, sp.score);
	        			
	        			m.readyPlayers.add(sp.player);

	        			// Update all the other players to let them know of the new score
	        			SendTotalScorePacket tp = new SendTotalScorePacket();
	        			tp.totalScores = m.getGame(sp.gameTitle).totalScores;
	        			
	        			for(int i=0;i<g.getPlayers().size();++i) {
	        				connectionList.get(g.getPlayers().get(i)).sendTCP(tp);
	        			}	        			
	        			
	        			System.out.println("I should be done sending the new scores now. ");
	        			
	        			// If everyone is ready, send out a new round packet
	        			if(m.readyPlayers.size() == g.getPlayers().size()) {
	        				NewRoundPacket np = new NewRoundPacket();

	        				for(int i=0;i<g.getPlayers().size();++i) {
		        				connectionList.get(g.getPlayers().get(i)).sendTCP(np);
		        			}
	        				
	        				m.readyPlayers.clear();
	        			}
	        		}
	        	}	        	
	        }
	        
            public void disconnected (Connection connection) {
            	// Go through the player/connection list and remove the player from their game
            	Player keyToRemove = null;
            	Game g = null;
            	for(Entry<Player, Connection> e: connectionList.entrySet()){
            		if(e.getValue() == connection) {
            			// Find which game they belonged to
                    	g = m.getGame(e.getKey());
                    	
            			m.clearPlayer(e.getKey());
            			keyToRemove = e.getKey();
               		}
            	}
            	connectionList.remove(keyToRemove);
            	sendUpdatedPlayerList(g, keyToRemove);
            }
	     });
	}
	
	private static void sendUpdatedPlayerList(Game g, Player disconnected) {
		UpdatePlayerListPacket up = new UpdatePlayerListPacket();
		if(up.players != null) {
			up.players = m.getGame(g.getTitle()).getPlayers();
			for(int i=0;i<up.players.size();++i) {
				if(!up.players.get(i).getUsername().equals(disconnected.getUsername()))
					server.sendToTCP(connectionList.get(up.players.get(i)).getID(), up);
			}
		}
	}
	
	private static void sendUpdatedPlayerList(Game g) {
		UpdatePlayerListPacket up = new UpdatePlayerListPacket();
		if(up.players != null) {
			up.players = m.getGame(g.getTitle()).getPlayers();
			for(int i=0;i<up.players.size();++i) {
				server.sendToTCP(connectionList.get(up.players.get(i)).getID(), up);
			}
		}
	}
}
