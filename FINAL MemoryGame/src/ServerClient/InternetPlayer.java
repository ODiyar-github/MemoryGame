package ServerClient;
/*
 * Die Abstakte Klasse wird benötigt um die Komunikation zwischen den Server und den Client
 * darzustellen. 
 */

import java.io.BufferedReader;

public abstract class InternetPlayer {
	public abstract void buttonClicked(int x, int y);
	public BufferedReader reader;
}
