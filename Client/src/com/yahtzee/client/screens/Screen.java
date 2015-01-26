package com.yahtzee.client.screens;

import java.awt.Panel;

import com.yahtzee.client.main.ClientMain;
import com.yahtzee.client.main.YahtzeeClient;


/*
 *  Screen.java
 *  
 *  The Screen class is to be extended by all other classes that can be drawn
 *  fullscreen in the applet for this application.
 */

public class Screen extends Panel {

	YahtzeeClient m;
	ClientMain referrer;
	
	/*
	 *  Constructor Screen(m, referrer)
	 *  
	 *  Parameters:
	 *  - YahtzeeClient m - a very temporary way to keep track of the model
	 *  - ClientMain referrer - the class that instantiated the screen, the
	 *  entry(Main) class for the applet
	 */
	public Screen(YahtzeeClient m, ClientMain referrer) {
		this.m = m;
		this.referrer = referrer;
	}

	public void update() {
	}

}
