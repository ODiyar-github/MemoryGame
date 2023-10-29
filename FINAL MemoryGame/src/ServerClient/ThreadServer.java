package ServerClient;

/*
 * Der Server ist zeitgleich der Erste Player.
 * Der Server wird von dem jenigen gestartet der auf HOST klicked.
 * Hierbei wird die Eigene GUI, vom Host mit geschickt im Konstruktor, damit die
 * Zeichen des Spielers zu dem Client geschickt werden, um gleiche Feld-Zeichen zu haben.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import application.GUI;
import application.Player;
import javafx.application.Platform;

/*
 * Der ThreadServer erbt von der Abstakten Klasse InternetPlayer und erbt zeitglich von Runnable, das dafür sorgt um ein
 * Thread zu starten.
 */
public class ThreadServer extends InternetPlayer implements Runnable {

	private GUI gui;
	private ServerSocket server;
	private Socket client;
	private PrintWriter writer;

	/**
	 * Im Konstruktor wird bereits der Server mit der eigenen IP-Addresse
	 * gestartet.
	 * 
	 * @param gui
	 *            = GUI des Host-Player.
	 */
	public ThreadServer(GUI gui) {
		// Der Server versucht sich mit der eigenen Ip zu verbinden, andernfalls
		// gibt er eine Fehler meldung(Exception) aus.
		try {
			server = new ServerSocket();
			// Der Server verbindet sich mit der localen Ip-Addresse und der
			// Port nummer 5000
			server.bind(new InetSocketAddress("0.0.0.0", 5000));
			this.gui = gui;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Diese Methode kommt von der Abstakten Klasse "InternetPlayer". Die
	 * Methode, versendet die einzelentn gedrückten Buttons weiter,
	 * mithilfe vom reader, erkennt der andere Spieler, welche knopf gedrückt wurde.
	 */
	public void buttonClicked(int x, int y) {
		// schreibt die Koordinaten des gedureckten Buttons ein.
		writer.println("clicked " + x + " " + y);
		// Versendet die Nachicht.
		writer.flush();
	}

	/**
	 * Die Methode, versendet den Player Zug vom Host. Somit kann der
	 * Spieler(Client), seinen Zug anpassen. Wenn der Host Spieler dran
	 * ist(ZUFALL), dann ist der Client, nicht dran.
	 * 
	 * @param player
	 *            = Player Zug vom Host
	 */
	public void sendPlayer(Player player) {
		// Schreibt den Client, wer von den beiden Spielern dran ist.
		writer.println("player " + player.isTurn());
		// Versendet die Nachicht.
		writer.flush();
	}

	/**
	 * Der Host, hat sein eigenes SpielFeld mit den einzelen Feldzeichen. Damit
	 * der Client mit den selben Feld Zeichen Spielen kann, wird das Feld vom
	 * Host zum Client versendet.
	 */
	public void sendField() {
		char[][] b = gui.getMainCards();
		String a = "field ";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				a = a + b[i][j] + " ";
			}
		}
		// Schreibt die Gesammelten Zeichen von Feld
		writer.println(a);
		// versendet den String.
		writer.flush();
	}

	/*
	 * Ein Server kann, die ganze Zeit laufen, in dem der Server selbst ein
	 * Thread ist. Der Thread dient dabei, das die Verbindung neben dem Thread
	 * der Application, gleichzeitig am laufen ist. somit wird gewährleistet,
	 * das es zu keinen abbrüchen kommen kann, und der Server erst beendet wird,
	 * sobald die Verbindung zum Client abgebrochen wurde.
	 */
	@Override
	public void run() {
		try {
			//Warte bis ein Spieler Verbunden ist.
			client = server.accept();
			//Turn vom Player wird gesetzt.
			//Platform.runLater bedeutet, das man vom anderen Thread, in die JavaFx Thread eine funktion ausführen kann
			//ohne das es zu einem freeze(einfrieren) kommt.
			Platform.runLater(() -> {
				if (gui.player.isTurn()) {
					gui.isTurn.setText("<- Turn");
					gui.isTurn.setTranslateX(-100);
					gui.isTurn.setTranslateY(-200);
				} else {
					gui.isTurn.setText("Turn ->");
					gui.isTurn.setTranslateX(100);
					gui.isTurn.setTranslateY(-200);
				}
			});
			//Ein Neuer BufferedReader wird erstellt, abhängig vom Client, dient dazu die Nachichten lesen zu können.
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			//Ein neuer PrintWriter wird erstellt, abhängig vom Client, dient dazu die Nachichten versenden zu können.
			writer = new PrintWriter(client.getOutputStream());
			
			//Zunächst wird Das Feld dem Spieler versendet.
			sendField();
			
			//Anschließend wird dem Spieler sein Zug angepasst.
			sendPlayer(gui.player);
			
			//Wenn der Host nicht dran ist, so wird der Thread vom TurnHandler gestartet.
			//Hierbei bezieht er sich, auf die eigene reader, damit er auch die Nachrichten lesen kann, während
			//Der Client was tut.
			if (!gui.player.isTurn()) {
				Thread t1 = new Thread(new TurnHandler(reader, gui));
				t1.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
