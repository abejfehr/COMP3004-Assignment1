package com.yahtzee.client.screens;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.yahtzee.client.main.ClientMain;
import com.yahtzee.client.main.YahtzeeClient;
import com.yahtzee.common.model.Scoring;


/*
 *  Game.java
 *  
 *  This class will contain all of the information and controls that will show
 *  on the game screen.
 *    
 *  Layout of the Game Screen:
 *  __________________________________________________
 *  | 0,0         Yahtzee with friends               |
 *  |                                                |
 *  | 0,1      Your dice: [] [] [] [] []             |
 *  | _________________________________  __________  |
 *  | |0,2                            |  | Roll    | |
 *  | |                               |              |
 *  | |                               |              |
 *  | |        Score Sheet            |              |
 *  | |                               |              |
 *  | |                               |              |
 *  | |_______________________________|              |
 *  |________________________________________________|
 *  
 *  The layout is done with a GridBagLayout, which actually looks awful. I hope
 *  that in the future after the game is implemented  I'll get the time to make
 *  it more beautiful.
 */

public class GameScreen extends Screen {
	
	Object rowData[][] = {
			{"Ones", ""},
			{"Twos", ""},
			{"Threes", ""},
			{"Fours", ""},
			{"Fives", ""},
			{"Sixes", ""},
			{"Sum", ""},
			{"Bonus", ""},
			{"Three of a kind", ""},
			{"Four of a kind", ""},
			{"Full house", ""},
			{"Small straight", ""},
			{"Large straight", ""},
			{"Chance", ""},
			{"Yahtzee", ""},
			{"Your Score", ""},
			{"Total Score", ""}
		};
	Object colNames[] = {"","Score"};
	DefaultTableModel scoreModel = new DefaultTableModel(rowData, colNames);
	
	private JLabel titleLabel = new JLabel("Yahtzee with friends");
	private JLabel diceLabel = new JLabel("Your dice: ");
	private JButton[] diceButtons = new JButton[5];
	private JTable scoreSheet;
	private JButton rollButton = new JButton("Roll");
    
    public GameScreen(YahtzeeClient m, ClientMain referrer) {
    	super(m, referrer);
    	
    	// Define the layout and the constraints to use
    	setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
    	// The label for the title
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 7;
    	c.gridheight = 1;
    	titleLabel.setFont(titleLabel.getFont ().deriveFont (48.0f));
    	add(titleLabel, c);
    	
    	// The label for the dice
    	c.anchor = GridBagConstraints.EAST;
    	c.gridx = 0;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	add(diceLabel, c);

    	// Initialize the dice buttons
    	for(int i=0;i<5; ++i) {
    		final int j = i;
    		diceButtons[i] = new JButton("0");
	    	c.anchor = GridBagConstraints.CENTER;
	    	c.gridx = i+1;
	    	c.gridy = 1;
	    	c.gridwidth = 1;
	    	c.gridheight = 1;
	    	diceButtons[i].addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			// You can't hold the dice right off the hop
	    			if(diceButtons[j].getText().trim() != "0" && m.player.hasRolls()) {
	    				m.player.toggleHold(j);

		    			// Somehow reflect that in the model
		    			if(diceButtons[j].getForeground() == Color.RED)
		    				diceButtons[j].setForeground(Color.BLACK);
		    			else
		    				diceButtons[j].setForeground(Color.RED);
	    			}
	    			else if(!m.player.hasRolls()) {
	    				JOptionPane.showMessageDialog(null, "You have no more rolls so you can't hold any more dice!");
	    			}
	    			else {
	    				JOptionPane.showMessageDialog(null, "You can't hold a die that hasn't yet been rolled!");
	    			}
	    		}
	    	});
	    	add(diceButtons[i], c);
    	}
    	
    	// The score sheet
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridy = 2;
    	c.gridwidth = 6;
    	c.gridheight = 1;
    	scoreSheet = new JTable(scoreModel) {
    		/*
    		 * BEGIN CRAZY CODE
    		 */
    		
    		// Sets borders
    		private Border outside = new MatteBorder(1, 0, 1, 0, Color.red);
            private Border inside = new EmptyBorder(0, 1, 0, 1);
            private Border highlight = new CompoundBorder(outside, inside);
            
            // Creates a table renderer
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            	
                Component comp = super.prepareRenderer(renderer, row, column);
                
                int category = rowToCategory(row);
                
                JComponent jc = (JComponent) comp;
                
                comp.setForeground(Color.GRAY);
                comp.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                int modelRow = convertRowIndexToModel(row);
                String type = (String) getModel().getValueAt(modelRow, 0);
                if (category != -1 && m.currentGame.playerScores.get(category) != null) {
                    comp.setFont(new Font("Sans serif", Font.BOLD, 12));
                    comp.setForeground(Color.GREEN);
                }
                else if(category != -1 && m.currentGame.totalScores.get(category) != null) {
                    comp.setFont(new Font("Sans serif", Font.BOLD, 12));
                    comp.setForeground(Color.RED);
                }
                else if(row == 6 || row == 7 || row == 15 || row == 16) {
                    comp.setFont(new Font("Sans serif", Font.BOLD, 12));
                    comp.setForeground(Color.BLACK);
                }
                else {
                    comp.setFont(new Font("Sans serif", Font.PLAIN, 12));
                    comp.setForeground(Color.GRAY);
                }
                jc.setBorder(BorderFactory.createCompoundBorder(jc.getBorder(), BorderFactory.createEmptyBorder(0, 0, 0, 5)));
                return comp;
            }
            
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };

		/*
		 * END CRAZY CODE
		 */
    	};
    	JScrollPane scoreScrollPane = new JScrollPane();
    	scoreScrollPane.setRowHeaderView(scoreSheet);
    	scoreScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, scoreSheet.getTableHeader());
    	scoreSheet.setRowSelectionAllowed(false);
    	scoreSheet.addMouseListener(new MouseAdapter() {
    	    public void mousePressed(MouseEvent e) {
    	        JTable table = (JTable) e.getSource();
    	        Point p = e.getPoint();
    	        int row = table.rowAtPoint(p);
    	        if (e.getClickCount() == 2) {
    	        	// Select that score for keeping. Not sure where to store these yet
    	        	int category = rowToCategory(row);
                    
                    if(category > -1 && !m.player.roundFinished) {
                    	submitScore(category, Scoring.getScoreFor(category, m.player.getDice()));
                    }
                    
                    
                    
                    update();
    	        }
    	    }
    	});
    	add(scoreSheet, c);
    	
    	// The roll button
    	c.anchor = GridBagConstraints.CENTER;
    	c.gridx = 7;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	rollButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(m.player.hasRolls()) {
    				m.player.roll();
       			}
    			else
    				JOptionPane.showMessageDialog(null, "You're out of rolls for this turn! Select a category to score in.");
    			
    			update();
    		}
    	});
    	add(rollButton, c);
    	
    	update();
    	// Show the Layout
    	setVisible(true);
    }
    
    @Override
    public void update() {
    	// Make the buttons say things relative to the player's actual dice
    	for(int i=0;i<5;++i) {
			diceButtons[i].setText(String.valueOf(m.player.getDieValue(i)));
    	}
    	
    	// Update the table model with the scores
    	for(int i=0;i<13;++i) {
    		if(m.currentGame.totalScores.get(i) == null)
    			scoreModel.setValueAt(Scoring.getScoreFor(i, m.player.getDice()), categoryToRow(i), 1);
    		else
    			scoreModel.setValueAt(m.currentGame.totalScores.get(i), categoryToRow(i), 1);
    	}
    	
    	// Count up the totals
    	int topTotal = 0;
    	for(int i=0;i<6;++i) {
    		if(m.currentGame.totalScores.get(i) != null)
    			topTotal += m.currentGame.totalScores.get(i);
    	}
    	
    	scoreModel.setValueAt(topTotal, 6, 1);
    	scoreModel.setValueAt("-", 7, 1);
    	
    	int bottomTotal = 0;
    	for(int i=0;i<7;++i) {
    		if(m.currentGame.totalScores.get(i) != null)
    			bottomTotal += m.currentGame.totalScores.get(i);
    	}
    	
    	int playerTotal = 0;
    	for(int i=0;i<13;++i) {
    		if(m.currentGame.playerScores.get(i) != null)
    			playerTotal += m.currentGame.playerScores.get(i);
    	}
    	
    	scoreModel.setValueAt(playerTotal, 15, 1);
    	scoreModel.setValueAt(topTotal+bottomTotal, 16, 1);
    	

    	// Revalidate and repaint for funsies
    	revalidate();
    	repaint();
    }
    
    private int rowToCategory(int row) {
    	if(row < 6) {
    		return row;
    	}
    	else if(row > 7 && row  < 15) {
    		return row - 2;
    	}
    	
    	return -1;
    }
    
    private int categoryToRow(int category) {
    	if(category < 6) {
    		return category;
    	}
    	else if(category > 5 && category < 13) {
    		return category + 2;
    	}
    	
    	return -1;    	
    }
    
    private void submitScore(int category, int score) {
    	// Check to see if we can even score in that place
    	if(m.currentGame.totalScores.get(category) == null) { 
    	
	    	// Store the player's score locally
	    	m.currentGame.playerScores.put(category, Scoring.getScoreFor(category, m.player.getDice()));
	    	
	    	// Send the player's score to the server
	    	referrer.sendScore(category, score);
	    	
	    	m.player.roundFinished = true;
	    	
	    	// Reset the dice to their original color
	    	for(int i=0;i<5;++i) {
	    		diceButtons[i].setForeground(Color.BLACK);
	    		diceButtons[i].setText("0");
	    	}
    	}
    	else {
    		JOptionPane.showMessageDialog(null, "Someone has already stored a score for that category, please pick another one.");
    	}
    }
}
