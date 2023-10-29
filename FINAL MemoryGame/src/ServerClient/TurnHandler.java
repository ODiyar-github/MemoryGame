package ServerClient;

/*
 * Im TurnHandler wird der ButtonClicked vom InternetPlayer verarbeitet.
 * Diese Klasse erbt dabei von dem Interface Runnable, und erstellt den TurnHandler 
 * als Thread.
 * 
 * ANMERKUNG: DIE PUNKTE ZAHL WIRD NICHT RICHTIG ANGEZEIGT...
 * NOCH FEHLERHAFT BEZ�GLICH DEM PUNKTESTAND....
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import application.GUI;
import javafx.application.Platform;

public class TurnHandler implements Runnable {

	private BufferedReader reader;
	private GUI myGui;

	/**
	 * Der TurnHandler wird immer dann aufgerufen, wenn ein Spieler nicht mehr am zug ist.
	 * hierbei wird der reader vom Player der nicht mehr dran ist und seine GUI als Parameter
	 * �bergeben. Die idee ist, wenn der Spieler warten muss, so wird das Feld angezeigt und
	 * der Spieler der nicht dran ist, bekommt auch angezeigt, was der Spieler der dran ist, dr�ckt.
	 */
	public TurnHandler(BufferedReader reader, GUI myGui) {
		this.reader = reader;
		this.myGui = myGui;
	}

	@Override
	public void run() {
		/*
		 * Sobald ein Spieler nicht dran ist, so wird f�r ihn das Klicken gespeert,
		 * das hei�t, der spieler kann w�hrend der zeit nicht dr�cken.
		 */
		myGui.setLock(true);
		
		//Werte zum vergleich gebraucht.
		int wert1 = 0;
		int wert2 = 0;
		int wert3 = 0;
		int wert4 = 0;
		try {
			// Jeder Spieler darf 2 mal Dr�cken.
			for (int i = 0; i < 2; i++) {
				//Mithilfe des reader, wird das Buttonclicked gelesen
				String receive = reader.readLine();
				//Der Tokenizer erstellt eine Verkettete liste
				//Beispiel: ("clicked  1 2")->("clicked","1","2")
				StringTokenizer stringToken = new StringTokenizer(receive, " ");
				if (stringToken.nextToken().equals("clicked")) {
					//Mit nextToken werden immer die n�chsten eingespeichert, dabei steht wert1 f�r spalte und wert2 f�r zeile
					wert1 = Integer.parseInt(stringToken.nextToken());
					wert2 = Integer.parseInt(stringToken.nextToken());
					//abspeicherung der ersten Buttons
					int x = wert1;
					int y = wert2;
					//Platform.runLater bedeutet, das man vom anderen Thread, in die JavaFx Thread eine funktion ausf�hren kann
					//ohne das es zu einem freeze(einfrieren) kommt.
					Platform.runLater(() -> {
						//das setLock->FALSE ist, damit der andere Spieler sehen kann, was gedr�ckt wurde, um danach es wieder zu sperren.
						myGui.setLock(false);
						myGui.setFieldForTwoPlayer(x, y);
						myGui.setLock(true);
						//Das Turn des Spieler wird angepasst.
						myGui.updateTurn();
					});
				}
				//Wenn man beim zweiten Klick ist, und das checkifSame gleich dem Button was vorher gedrueckt wurde, dann
				//sollen alle werte zuruck gesetzt werden und der Spieler darf nochmal 2 buttons dr�cken.
				if (i == 1) {
					if (myGui.checkIfSame(wert1, wert2, wert3, wert4)) {
						i = 1;
						wert1 = 0;
						wert2 = 0;
						wert3 = 0;
						wert4 = 0;
					}
				
				}
				//Wenn man beim ersten dr�cken ist, werden die werte von x und y in wert3 und wert4 gespeichert,
				//damit beim n�chsten dr�cken, die beiden Buttons mit einander vergleichen werden k�nnen.
				else {
					wert3 = wert1;
					wert4 = wert2;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		//Der Spieler der nicht dran war bekommt die erlaubnis wieder etwas zu dr�cken und 
		//der andere der die buttons vorher gedr�ckt hat ist gesperrt.
		Platform.runLater(() -> myGui.setLock(false));
	}

}
