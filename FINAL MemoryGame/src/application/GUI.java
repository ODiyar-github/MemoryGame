package application;

import javax.swing.JOptionPane;
import ServerClient.InternetPlayer;
import ServerClient.TurnHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class GUI {
	private StackPane stackPane = new StackPane();
	private MemoryField field;
	public Player player;

	// Labels:
	private Label credits = new Label("Von Diyar Omar");
	private Label menuTitle = new Label("Memory kartenspiel");
	private Label twoPlayerTitle = new Label("Zwei Spieler");
	public Label hostPoints = new Label("Host: " + 0);
	public Label clientPoints = new Label(0 + " :Client");
	public Label isTurn = new Label("Turn");
	public Label warningLabel = new Label("");
	public TextField textField = new TextField();

	// Buttons:
	Button joinToServerButton = new Button("Join");
	Button onePlayerButton = new Button("Ein Spieler");
	Button twoPlayerButton = new Button("Zwei Spieler");
	Button startGameButton = new Button("Start");
	Button hostButton = new Button("HOST GAME");
	Button joinButton = new Button("JOIN GAME");
	Button setIP;
	Button backToMainButton = new Button();
	Button newGameButton = new Button();

	// Werte für einzelen Methoden in der GUI
	public boolean waiting = false;
	public boolean isTurnOfPlayers = false;
	private boolean locked = false;
	private int counter = 0;
	private int currentWert1 = 0;
	private int currentWert2 = 0;
	private int moves = 0;
	private Button currentButton = new Button();
	private Button[][] cardsButton;
	private boolean[][] fieldBool = new boolean[4][5];
	private char[][] mainCards;
	private Label moveLabel;
	public InternetPlayer ip = null;
	public boolean test = false;
	public int pointsPlayer1 = 0;
	public int pointsPlayer2 = 0;

	/**
	 * Die GUI uebernimmt die Grafische darstellung für das Memory Spiel. Weiter
	 * übernimmt sie Wesentliche bestand teile der Spiel Logik.
	 */
	public GUI() {
		// MemoryField-Klasse wird neu Erzeugt, hierbei wird ein charArray
		// Erzeugt mit einer unterschiedlichen
		// reihenfolgen von Zeichen.
		field = new MemoryField();
		// Wir erzeugen uns ein neues charArray, der die gleiche groeße wie im
		// MemoryField besitzt
		mainCards = new char[4][5];
		// Dadurch das wir, ein SpielFeld erzeugt haben, speicher wir diese in
		// die mainCards Variable ein.
		mainCards = field.getArray();
	}

	/*
	 * Diese Methoden, also setMainCards und getMainCards sind Methoden, fuer
	 * die ZweiSpieler funktion. setMainCards, setzt die im Parameter
	 * Uebergebenen charArray in die Gui mainCards-variable ein. getMainCards,
	 * return die mainCards Variable von der Gui aus.
	 */
	public void setMainCards(char[][] charArr) {
		mainCards = charArr;
	}

	public char[][] getMainCards() {
		return mainCards;
	}

	/**
	 * Die Scene fuer das Memory Feld wird erzeugt und die einzelen
	 * Labels,Buttons, etc. werden drauf inistialiesiert. Anschließend wird die
	 * Scene ausgegeben.
	 * 
	 * @return Scene = Das Gesamte Memory Feld wird angezeigt, fuer den Ein
	 *         Spieler.
	 */
	public Scene getSceneOfMemoryField() {
		/*
		 * Sollte die StackPane eine derzeitige Scene gestartet haben, so wird
		 * diese StackPane neu Erzeugt, damit eine neue Scene drauf gespielt
		 * werden kann.
		 */
		if (stackPane.getScene() != null) {
			stackPane = new StackPane();
		}
		// Scene für das Memory Feld wird erzeugt
		Scene sceneMemoField = new Scene(stackPane, 500, 450);

		// Die Labels und die Buttons, die in der Scene enthalten sind, werden
		// erzeugt.
		moveLabel = new Label();
		moveLabel.setFont(new Font("Arial", 30));
		moveLabel.setTranslateX(0);
		moveLabel.setTranslateY(-200);
		moveLabel.setText("Züge: " + Integer.toString(moves));
		backToMainButton = new Button("Zurück");
		backToMainButton.setTranslateX(-200);
		backToMainButton.setTranslateY(-200);
		backToMainButton.setFont(new Font("Arial", 18));
		newGameButton = new Button("Neustart");
		newGameButton.setFont(new Font("Arial", 18));
		newGameButton.setTranslateX(200);
		newGameButton.setTranslateY(-200);
		// Die Visualiesierung erfolgt mithilfe von Buttons:

		/*
		 * Wir erzeugen ein Button Array, die die genauen Angaben des Spiel
		 * Feldes enthalten sollen, das heißt, das SpielFeld soll mithilfe der
		 * Buttons angezeigt werden.
		 */
		cardsButton = new Button[4][5];
		for (int i = 0; i < cardsButton.length; i++) {
			for (int k = 0; k < cardsButton[0].length; k++) {
				// Jedes Button vom Array, wird neu erzeugt und ausgerichtet
				cardsButton[i][k] = new Button();
				cardsButton[i][k].setPrefSize(90, 90);
				cardsButton[i][k].setTranslateX((k - 2) * 100);
				cardsButton[i][k].setTranslateY((i - 1.27) * 100);
				cardsButton[i][k].setFont(new Font("Arial", 50));
				int wert1 = i;
				int wert2 = k;
				// Object erzeugung von den einzelnen Buttons, das heißt,
				// Die einzelen Buttons bekommen Funktionen zugewiesen.
				cardsButton[i][k].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// Bei Jedem Drücken des Buttons an der stelle i und k,
						// so wird die Methode setField(i,k) aufgerufen
						setField(wert1, wert2);
					}
				});
				// Die Buttons werden in die StackPane gespeichert und
				// hinzugefuegt.
				stackPane.getChildren().add(cardsButton[i][k]);
			}
		}

		// Labels und Buttons werden in die StackPane gespeichert und
		// hinzugefuegt.
		stackPane.getChildren().add(newGameButton);
		stackPane.getChildren().add(backToMainButton);
		stackPane.getChildren().add(moveLabel);

		// Die Scene wird augegeben, die die einzelen variablen gesetzt hat.
		return sceneMemoField;
	}

	/**
	 * Diese Scene fuer das Join-Button wird erzeugt und die einzelen Labels,
	 * Buttons, etc. werden drauf inistialiesiert. Anschließend wird die Scene
	 * ausgegeben.
	 * 
	 * @return Scene = Das Join Fenster, damit ein Spieler sich mit einem
	 *         anderen Spieler verbinden kann.
	 */
	public Scene getConnectWithServerScene() {
		/*
		 * Sollte die StackPane eine derzeitige Scene gestartet haben, so wird
		 * diese StackPane neu Erzeugt, damit eine neue Scene drauf gespielt
		 * werden kann.
		 */
		if (stackPane.getScene() != null) {
			stackPane = new StackPane();
		}
		// Die Scene fuer das Verbindungsfenster wird Erzeugt.
		Scene scene = new Scene(stackPane, 200, 150);

		// Die Labels und die Buttons, die in der Scene enthalten sind, werden
		// erzeugt.
		backToMainButton = new Button("Zurück");
		backToMainButton.setTranslateX(0);
		backToMainButton.setTranslateY(-50);
		backToMainButton.setFont(new Font("Arial", 13));
		Label ipAddressLabel = new Label("IP:");
		ipAddressLabel.setFont(new Font("Arial", 20));
		ipAddressLabel.setTranslateX(-90);
		ipAddressLabel.setTranslateY(0);
		textField.setFont(new Font("Arial", 15));
		textField.setTranslateX(0);
		textField.setTranslateY(0);
		textField.setMaxWidth(150);
		joinToServerButton.setFont(new Font("Arial", 15));
		joinToServerButton.setTranslateX(0);
		joinToServerButton.setTranslateY(50);
		warningLabel.setText("Konnte nicht Verbinden...");
		warningLabel.setTranslateX(0);
		warningLabel.setTranslateY(25);
		warningLabel.setVisible(false);

		// Labels,Buttons, etc. werden in die StackPane gespeichert und
		// hinzugefuegt.
		stackPane.getChildren().add(ipAddressLabel);
		stackPane.getChildren().add(backToMainButton);
		stackPane.getChildren().add(textField);
		stackPane.getChildren().add(warningLabel);
		stackPane.getChildren().add(joinToServerButton);

		// Die Scene wird augegeben, die die einzelen variablen gesetzt hat.
		return scene;
	}

	/**
	 * Diese Methode dient dem Online Spielen, die mit dem Server/Client
	 * gekoppelt sind. Falls locked = True ist, so darf der andere Spieler nicht
	 * die einzelen Buttons druecken.
	 * 
	 * @param locked
	 *            = Sperren der Methode setFieldForTwoPlayer fuer den Spieler,
	 *            der nicht dran ist.
	 */
	public void setLock(boolean locked) {
		System.out.println("Set lock: " + locked);
		this.locked = locked;
	}

	/**
	 * Die Scene für das Memory Menu wird erzeugt und die einzelen Labels,
	 * Buttons, etc. werden drauf inistialiesiert. Anschließend wird die Scene
	 * ausgegeben.
	 * 
	 * @return Scene = Das Memory Menu wird angezeigt.
	 */
	public Scene getSceneOfMemoryMenu() {
		/*
		 * Sollte die StackPane eine derzeitige Scene gestartet haben, so wird
		 * diese StackPane neu Erzeugt, damit eine neue Scene drauf gespielt
		 * werden kann.
		 */
		if (stackPane.getScene() != null) {
			stackPane = new StackPane();
		}
		// Die Scene fuer das Menu wird Erzeugt.
		Scene sceneMemoMenu = new Scene(stackPane, 325, 200);

		// Die Labels und die Buttons, die in der Scene enthalten sind, werden
		// erzeugt.
		credits.setFont(new Font("Arial", 10));
		credits.setTranslateX(110);
		credits.setTranslateY(90);
		menuTitle.setTranslateX(0);
		menuTitle.setTranslateY(-50);
		menuTitle.setFont(new Font("Arial", 30));
		onePlayerButton.setFont(new Font("Arial", 20));
		onePlayerButton.setTranslateX(0);
		onePlayerButton.setTranslateY(0);
		twoPlayerButton.setFont(new Font("Arial", 20));
		twoPlayerButton.setTranslateX(0);
		twoPlayerButton.setTranslateY(50);

		// Labels,Buttons, etc. werden in die StackPane gespeichert und
		// hinzugefuegt.
		stackPane.getChildren().add(menuTitle);
		stackPane.getChildren().add(onePlayerButton);
		stackPane.getChildren().add(twoPlayerButton);
		stackPane.getChildren().add(credits);

		// Die Scene wird augegeben, die die einzelen variablen gesetzt hat.
		return sceneMemoMenu;
	}

	/**
	 * Die Scene für das ZweiSpieler Menu wird erzeugt und die einzelen Labels,
	 * Buttons, etc. werden drauf initialieisert. Anschließend wird die Scene
	 * ausgegeben.
	 * 
	 * @return Scene= Memory Menu
	 */
	public Scene getSceneOfTwoPlayer() {
		/*
		 * Sollte die StackPane eine derzeitige Scene gestartet haben, so wird
		 * diese StackPane neu Erzeugt, damit eine neue Scene drauf gespielt
		 * werden kann.
		 */
		if (stackPane.getScene() != null) {
			stackPane = new StackPane();
		}
		// Die Scene fuer das ZweiSpieler-Menu wird Erzeugt.
		Scene scene = new Scene(stackPane, 250, 200);

		// Die Labels und die Buttons, die in der Scene enthalten sind, werden
		// erzeugt.
		twoPlayerTitle.setFont(new Font("Arial", 30));
		twoPlayerTitle.setTranslateX(0);
		twoPlayerTitle.setTranslateY(-50);
		hostButton.setFont(new Font("Arial", 13));
		hostButton.setTranslateX(0);
		hostButton.setTranslateY(0);
		joinButton.setFont(new Font("Arial", 13));
		joinButton.setTranslateX(0);
		joinButton.setTranslateY(40);
		backToMainButton = new Button("Zurück");
		backToMainButton.setFont(new Font("Arial", 13));
		backToMainButton.setTranslateX(0);
		backToMainButton.setTranslateY(80);

		// Labels,Buttons, etc. werden in die StackPane gespeichert und
		// hinzugefuegt.
		stackPane.getChildren().add(twoPlayerTitle);
		stackPane.getChildren().add(hostButton);
		stackPane.getChildren().add(joinButton);
		stackPane.getChildren().add(backToMainButton);

		// Die Scene wird augegeben, die die einzelen variablen gesetzt hat.
		return scene;
	}

	/**
	 * Die Gesamte Scene ist das Onlien-Spiel-Memory-Fenster. Die einzelen
	 * Buttons und Labels, werden erzeugt und drauf in inistialieisert.
	 * Anschließend wird die Scene ausgegeben.
	 * 
	 * @return Scene = Online Spiel Feld.
	 */
	public Scene getTwoPlayerField() {
		// Sollte die StackPane eine derzeitige Scene gestartet haben, so wird
		// diese ersetzt.
		if (stackPane.getScene() != null) {
			stackPane = new StackPane();
		}
		// Scene für das Memory Feld wird erzeugt
		Scene sceneMemoField = new Scene(stackPane, 500, 450);

		// Die Labels und die Buttons, die in der Scene enthalten sind, werden
		// erzeugt.
		hostPoints.setFont(new Font("Arial", 20));
		hostPoints.setTranslateX(-200);
		hostPoints.setTranslateY(-200);
		clientPoints.setFont(new Font("Arial", 20));
		clientPoints.setTranslateX(200);
		clientPoints.setTranslateY(-200);
		isTurn.setFont(new Font("Arial", 20));

		// Wir erzeugen ein Button Array, die die genauen Angaben des Spiel
		// Feldes enthalten sollen.
		cardsButton = new Button[4][5];
		for (int i = 0; i < cardsButton.length; i++) {
			for (int k = 0; k < cardsButton[0].length; k++) {
				// Jedes Button vom Array, wird neu erzeugt und ausgerichtet
				cardsButton[i][k] = new Button();
				cardsButton[i][k].setPrefSize(90, 90);
				cardsButton[i][k].setTranslateX((k - 2) * 100);
				cardsButton[i][k].setTranslateY((i - 1.27) * 100);
				cardsButton[i][k].setFont(new Font("Arial", 50));
				int wert1 = i;
				int wert2 = k;
				// Object erzeugung von den einzelnen Buttons, das heißt,
				// Die einzelen Buttons bekommen Funktionen zugewiesen.
				cardsButton[i][k].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// Bei Jedem Druecken des Buttons an der stelle i und k,
						// so wird die Methode setFieldForTwoPlayer(i,k)
						// aufgerufen.
						setFieldForTwoPlayer(wert1, wert2);
					}
				});

				// Die Buttons werden in die StackPane gespeichert und
				// hinzugefuegt.
				stackPane.getChildren().add(cardsButton[i][k]);
			}
		}

		// Labels und Buttons werden in die StackPane gespeichert und
		// hinzugefuegt.
		stackPane.getChildren().add(hostPoints);
		stackPane.getChildren().add(clientPoints);
		stackPane.getChildren().add(isTurn);

		// Am ende wird die Scene augegeben, die die einzelen variablen gesetzt
		// hat.
		return sceneMemoField;
	}

	/**
	 * Diese Methode zeigt, die Einzelen Buttons und setzt die Punkt Zahl der
	 * Spieler im Online Modus fest. Sie wird als Synchronized gesetzt, das
	 * heißt das nur ein Thread diese Methode abarbeiten kann.
	 * 
	 * @param wert1
	 *            = Array[wert1][...]-Index der Spalte
	 * @param wert2
	 *            = Array[...][wert2]-Index der Zeile : Insgesamt gilt :
	 *            Array[wert1][wert2]-Button Geklickt.
	 */
	public synchronized void setFieldForTwoPlayer(int wert1, int wert2) {
		/*
		 * sollte das ButtonResetTime-Thread am laufen sein, so wird das
		 * druecken verhindert und man kann keine weiteren Buttons druecken.
		 */
		if (waiting) {
			return;
		}
		
		/*
		 * Sollte ein Spieler dran sein, so muss der andere auf ihn warten und
		 * darf keine weitern Buttons druecken.
		 */
		if (locked) {
			return;
		}
		
		/*
		 * Diese Bedinung gilt, wenn ein Spieler dran ist und entsprechend wenn ip, das heißt, wenn die Abstracte Klasse 
		 * nicht null ist, so wird ButtonClicked von Wert1 und Wert2 zum naechste Spieler versendet, um die gedrueckten Buttons
		 * auch bei ihm anzeigen zu lassen.
		 */
		if (ip != null && player.isTurn()) {
			ip.buttonClicked(wert1, wert2);
		}
		
		/*
		 * Diese Bedingung gilt fuer das Erste Klicken eines Buttons. Sollte der
		 * Fall eintreffen, das ein Button geklickt wurden, so wird geprueft, ob
		 * das Button das erste oder das zweite Button, mithilfe des Counters
		 * ist, das heißt das Erste Button ist geklickt und somit wird der
		 * Counter auf 1 gesetzt um zu sagen, das ein Button geklickt wurde. Das
		 * Zeichen des MemoryField wird in das Button an der Stelle "Wert1" und
		 * "Wert2" gesetzt. Die Belegung an der Stelle wird auf True gesetzt.
		 * Und "Wert1" und "wert2" wird Zwischengespeichert, um einen Vergleich
		 * erzielen zu können, mit dem Zweiten Button Klick.
		 */
		if (counter == 0 && !fieldBool[wert1][wert2]) {
			counter = 1;
			cardsButton[wert1][wert2].setText(mainCards[wert1][wert2] + "");
			fieldBool[wert1][wert2] = true;
			currentButton.setText(cardsButton[wert1][wert2].getText());
			currentWert1 = wert1;
			currentWert2 = wert2;
		}

		/*
		 * Wenn das Zweite Button gedrueckt wurde, wird geprueft, ob die beiden buttons gleich oder ungleich sind.
		 */
		if (counter != 0 && !fieldBool[wert1][wert2]) {
			cardsButton[wert1][wert2].setText(mainCards[wert1][wert2] + "");
			
			/*
			 * Wenn die Buttons gleich sind, so werden die Buttons dauerhaft angezeigt und ihre Belegung auf True gesetzt
			 * Hinzu kommt, das die Punkt anzahl fuer den Spieler angepasst wird, da er ein Punkt erzielt hat.
			 * Sollte der Spieler nicht dran Sein, so wird ein neuer Thread-TurnHandler erzeugt, der auf den naechsten Spiel 
			 * zug des anderen Spieler warten muss.
			 */
			if (checkIfSame(currentWert1, currentWert2, wert1, wert2)) {
				currentButton.setText("");
				counter = 0;
				fieldBool[wert1][wert2] = true;
				fieldBool[currentWert1][currentWert2] = true;
				if (player.getName() == "Host") {
					pointsPlayer1++;
					player.setPoint();
					hostPoints.setText("Host: " + player.getPoints());
					updateTurn();
					if (!this.player.isTurn()) {
						Thread t = new Thread(new TurnHandler(ip.reader, this));
						t.start();
					}
				} else {
					pointsPlayer2++;
					player.setPoint();
					clientPoints.setText(pointsPlayer2 + " :Client");
					updateTurn();
					if (!this.player.isTurn()) {
						Thread t = new Thread(new TurnHandler(ip.reader, this));
						t.start();
					}
				}
			} 
			/*
			 * Wenn die Beiden Buttons ungleich sind, so wird der Turn von Spieler angepasst, ein neuer Thread fuer TurnHandler
			 * gestartet und die Buttons fuer 500 milliSekunde angezeigt und dann wieder geloescht.
			 */
			else {
				if (player.isTurn()) {
					Thread t1 = new Thread(new TurnHandler(ip.reader, this));
					t1.start();
				}
				player.setTurn(!player.isTurn());

				updateTurn();
				counter = 0;
				waiting = true;
				new ButtonResetTimer(cardsButton[wert1][wert2], cardsButton[currentWert1][currentWert2], 500, this)
						.start();
				fieldBool[currentWert1][currentWert2] = false;
			}
		}

	}

	/**
	 * Die Methode, vergleicht das erste gedrueckte Button mit dem zweiten
	 * gedrueckte Button im Online Spiel Fenster.
	 * 
	 * @param a
	 *            = Array[a][...]-Koordinate-Erste Button
	 * @param b
	 *            = Array[...][b]-koordinate-Erste Button
	 * @param c
	 *            = Array[c][...]-Koordinate-Zweite Button
	 * @param d
	 *            = Array[...][d]-Koordinate-Zweite Button
	 * @return True = Wenn beide Buttons gleich Sind
	 */
	public boolean checkIfSame(int a, int b, int c, int d) {
		return mainCards[a][b] == mainCards[c][d];
	}

	/**
	 * Die Methode, updated das Turn-Label fuer das Online Spielen. Das heißt,
	 * je Nach dem, welcher Spieler dran ist, so wird das TurnLabel im
	 * Spielfenster des Online-Spiel angepasst. Es gibt nur Host und Client:
	 * Host= Server-ersteller. Client= Der mit Server verbindet.
	 */
	public void updateTurn() {
		if (player.getName() == "Host") {
			if (player.isTurn()) {
				hostPoints.setText("Host: " + player.getPoints());
				isTurn.setText("<- Turn");
				isTurn.setTranslateX(-100);
				isTurn.setTranslateY(-200);
			} else {
				clientPoints.setText(player.getPoints() + " :Client");
				isTurn.setText("Turn ->");
				isTurn.setTranslateX(100);
				isTurn.setTranslateY(-200);
			}

		} else {
			if (player.isTurn()) {
				clientPoints.setText(player.getPoints() + " :Client");
				isTurn.setText("Turn ->");
				isTurn.setTranslateX(100);
				isTurn.setTranslateY(-200);
			} else {
				hostPoints.setText("Host: " + player.getPoints());
				isTurn.setText("<- Turn");
				isTurn.setTranslateX(-100);
				isTurn.setTranslateY(-200);
			}

		}
	}

	/**
	 * Die Methode ist Zuständig für die visualiesierung des Feldes in den
	 * einzelen Buttons von Memory Feld.
	 * 
	 * @param wert1
	 *            = Array[wert1][...]-Index der Spalte
	 * @param wert2
	 *            = Array[...][wert2]-Index der Zeile : Insgesamt gilt :
	 *            Array[wert1][wert2]-Button Geklickt.
	 */
	public void setField(int wert1, int wert2) {
		// sollte das ButtonResetTime-Thread am laufen sein,
		// so wird das drücken verhindert und man kann keine weiteren Buttons
		// druecken.
		if (waiting)
			return;

		/*
		 * Diese Bedingung gilt fuer das Erste Klicken eines Buttons. Sollte der
		 * Fall eintreffen, das ein Button geklickt wurden, so wird geprueft, ob
		 * das Button das erste oder das zweite Button, mithilfe des Counters
		 * ist, das heißt das Erste Button ist geklickt und somit wird der
		 * Counter auf 1 gesetzt um zu sagen, das ein Button geklickt wurde. Das
		 * Zeichen des MemoryField wird in das Button an der Stelle "Wert1" und
		 * "Wert2" gesetzt. Die Belegung an der Stelle wird auf True gesetzt.
		 * Und "Wert1" und "wert2" wird Zwischengespeichert, um einen Vergleich
		 * erzielen zu können, mit dem Zweiten Button Klick.
		 */
		if (counter == 0 && !fieldBool[wert1][wert2]) {
			counter = 1;
			cardsButton[wert1][wert2].setText(mainCards[wert1][wert2] + "");
			fieldBool[wert1][wert2] = true;
			currentButton.setText(cardsButton[wert1][wert2].getText());
			currentWert1 = wert1;
			currentWert2 = wert2;
		}

		/*
		 * Diese Bedingung gilt für das zweite klicken eines Buttons. Die
		 * Bedingun prueft, ob das Zeichen des Voherigen Buttons gleich dem
		 * jetzigen ist, sollte der fall eintreffen das sie gleich sind, so
		 * werden die Buttons belegt und können nicht mehr verändert werden.
		 * Wenn es nicht der Fall ist, so werden die Buttons mithilfe des
		 * ButtonResetTime-Thread angezeigt und anschließend gelöscht. Nebenbei,
		 * als feature werden die anzahl der Zuege des Spielers Gezaelt.
		 */
		if (counter != 0 && !fieldBool[wert1][wert2]) {
			cardsButton[wert1][wert2].setText(mainCards[wert1][wert2] + "");
			if (currentButton.getText().equals(cardsButton[wert1][wert2].getText())) {
				currentButton.setText("");
				counter = 0;
				fieldBool[wert1][wert2] = true;
				fieldBool[currentWert1][currentWert2] = true;
			} else {
				moves++;
				moveLabel.setText("Züge: " + Integer.toString(moves));
				currentButton.setText("");
				counter = 0;
				waiting = true;
				new ButtonResetTimer(cardsButton[wert1][wert2], cardsButton[currentWert1][currentWert2], 1000, this)
						.start();
				fieldBool[currentWert1][currentWert2] = false;

			}
		}

		/*
		 * Wenn das Gesamt feld belegt wurde, so kommt die Abfrage, ob man
		 * erneut spielen moechte oder nicht, mithilfe eines Dialog Fensters.
		 */
		if (fieldBoolTrue()) {
			int selectedOption = JOptionPane.showConfirmDialog(null, "Wollen Sie nochmal Spielen?", "Neustart",
					JOptionPane.YES_NO_OPTION);
			if (selectedOption == JOptionPane.YES_OPTION) {
				resetGame();
			}
		}
	}

	/**
	 * Methode für die Threads. Sobald zwei Buttons bzw. zwei felder ausgewaehlt
	 * wurden, so soll die Scene gesperrt werden bis die Threads beendet werden.
	 */
	public void unlockNewClick() {
		this.waiting = false;
	}

	/**
	 * [Feature] Falls das Feld Belegt wurde, wird False zurueck gegeben,
	 * andernfalls True.
	 * 
	 * @return true = Feld wurde noch nicht Belegt.
	 */
	public boolean FieldWasNotSet() {
		for (int i = 0; i < fieldBool.length; i++) {
			for (int j = 0; j < fieldBool[0].length; j++) {
				if (fieldBool[i][j] || moves != 0) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Wenn das gesamte Feld aufgedeckt wurde, dann soll True zurueckgegeben.
	 * 
	 * @return True = Feld komplett aufgedeckt.
	 */
	public boolean fieldBoolTrue() {
		for (int i = 0; i < fieldBool.length; i++) {
			for (int j = 0; j < fieldBool[0].length; j++) {
				if (!fieldBool[i][j]) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Das Spiel wird zurück und die einzelnen Eigenschaften auf die Standart
	 * inistialieserung gesetzt.
	 */
	public void resetGame() {
		moves = 0;
		moveLabel.setText("Züge: " + Integer.toString(moves));
		field = new MemoryField();
		mainCards = new char[4][5];
		mainCards = field.getArray();
		currentButton.setText("");
		counter = 0;
		for (int i = 0; i < fieldBool.length; i++) {
			for (int j = 0; j < fieldBool[0].length; j++) {
				fieldBool[i][j] = false;
				cardsButton[i][j].setText("");
			}

		}
	}

}
