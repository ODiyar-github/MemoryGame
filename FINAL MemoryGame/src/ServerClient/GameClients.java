package ServerClient;

/*
 * Der GameClient stellt den zweiten Spieler da, während der Erste Spieler
 * den Host darstellt.
 * Sobald der Spieler auf Join drückt, wird der GameClient gestartet, der auch 
 * von dem InternetPlayer erbt.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import application.GUI;
import application.Player;

public class GameClients extends InternetPlayer {

	private Socket server;
	PrintWriter writer;

	/**
	 * Im Konstruktor, versucht sich der Client mit der vorgegebenen IPAddresse zu verbinden.
	 * Dabei wird seine eigene Gui mit ins Spiel gebracht, da die Gui gleichzeitig die Spiel Logik ist.
	 * 
	 * @param iPAddress = IPAddresse des Host
	 * @param myGui = Gui vom Spieler.
	 */
	public GameClients(String iPAddress, GUI myGui) {
		server = new Socket();
		try {
			//Verbindet sich mit dem Server
			server.connect(new InetSocketAddress(iPAddress, 5000));
			
			//Neuer BufferedReader wird erzeugt
			reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			
			//Neuer PrintWriter wird erzeugt.
			writer = new PrintWriter(server.getOutputStream());
			
			//Sobald eine Nachricht verschickt wurde, so wird mit hilfe des BufferedReader
			//Die Nachricht gelesen und abgearbeitet.
			String receive = reader.readLine();
			
			//Mit dem StringTokenizer wird die Nachricht in einer Liste von String gesetzt
			//Beispiel: ("field abcdefg")-> ("field","a","b","c","d","e","f","g")
			StringTokenizer st = new StringTokenizer(receive, " ");
			
			//Wenn der reader erkannt hat, das "field" gelesen wurde, so wird das ganze Feld vom Host
			//in die das Feld vom Client gesichert.
			if (st.nextToken().equals("field")) {
				char[][] c = new char[4][5];
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 5; j++) {
						//nextToken() heißt das er in der Liste immer weiter geht und die Werte abspeichert in c[...][...]
						c[i][j] = st.nextToken().charAt(0);
					}
				}
				myGui.setMainCards(c);
			}
			
			receive = reader.readLine();
			st = new StringTokenizer(receive, " ");
			//Wenn der reader Player liest, dann wird der Player vom Client neu Inistialieisert 
			//mit den Vorgaben vom Host. 
			if (st.nextToken().equals("player")) {
				boolean b1 = Boolean.parseBoolean(st.nextToken());
				myGui.player = new Player("Client", !b1);
				myGui.player.setTurn(!b1);
				myGui.updateTurn();
			}
			//Sollte der Client nicht dran sein, so wird der TurnHandler gestartet.
			if (!myGui.player.isTurn()) {
				//Der TurnHandler nimmt dabei den reader und die Gui vom jeweiligen Spieler,
				//damit diese gelesen wurden und angezeigt werden kann.
				Thread t1 = new Thread(new TurnHandler(reader, myGui));
				t1.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Diese Methode kommt von der Abstakten Klasse "InternetPlayer". Die
	 * Methode, versendet die einzelentn gedrückten Buttons weiter,
	 * mithilfe vom reader, erkennt der andere Spieler, welche knopf gedrückt wurde.
	 */
	public void buttonClicked(int x, int y) {
		writer.println("clicked " + x + " " + y);
		writer.flush();
	}

}
