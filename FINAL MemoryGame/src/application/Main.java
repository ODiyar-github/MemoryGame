package application;

/**
 * Diese MAIN-Klasse dient für das Gesamte Spiel.
 */

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import ServerClient.GameClients;
import ServerClient.InternetPlayer;
import ServerClient.ThreadServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	GUI myGui = new GUI();
	Scene currentScene = myGui.getSceneOfMemoryMenu();

	@Override
	public void start(Stage primaryStage) {
		try {
			/*
			 * Das Memory-Menu-Fenster wird angezeigt
			 */
			currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(currentScene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Memory Game");
			primaryStage.show();

			/*
			 * Die Funktion fuer das EinSpieler Button wird erzeugt.
			 */
			myGui.onePlayerButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Scene wird für MemoryFeld angepasst und erzeugt
					currentScene = myGui.getSceneOfMemoryField();
					primaryStage.close();

					/*
					 * Zurueck Button, wenn eine Belegung festgestellt wurde, so
					 * wird mithilfe eines Dialog Fensters abgefragt, ob man
					 * zurueck zum Menu will, wenn JA dann wird das Spiel neu
					 * gesetzt. Andernfalls, sollte also, nichts gedrueckt
					 * worden sein, so gelangt man ohne Dialog Fenster zum
					 * Hauptmenu.
					 */
					myGui.backToMainButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (myGui.FieldWasNotSet()) {
								currentScene = myGui.getSceneOfMemoryMenu();
								primaryStage.close();

								currentScene.getStylesheets()
										.add(getClass().getResource("application.css").toExternalForm());
								primaryStage.setScene(currentScene);
								primaryStage.setResizable(false);
								primaryStage.show();
							} else {
								int selectedOption = JOptionPane.showConfirmDialog(null,
										"Wollen Sie wirklich Zurück? \nDas Spiel wird dann zurückgesetzt.", "Zurück",
										JOptionPane.YES_NO_OPTION);
								if (selectedOption == JOptionPane.YES_OPTION) {
									myGui.resetGame();
									currentScene = myGui.getSceneOfMemoryMenu();
									primaryStage.close();

									currentScene.getStylesheets()
											.add(getClass().getResource("application.css").toExternalForm());
									primaryStage.setScene(currentScene);
									primaryStage.setResizable(false);
									primaryStage.show();
								}
							}
						}
					});

					/*
					 * Neustart Button, wenn man draufdrueckt wird das Spiel
					 * Neugestartet.
					 */
					myGui.newGameButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							myGui.resetGame();
						}
					});

					currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(currentScene);
					primaryStage.setResizable(false);
					primaryStage.show();
				}
			});
			
			
			/*
			 * Die Funktion fuer das zweiSpieler Button wird erzeugt.
			 */
			myGui.twoPlayerButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
					/*
					 * Die Scene aendert sich und man gelangt in den Zweispieler Menu-Fenster.
					 */
					currentScene = myGui.getSceneOfTwoPlayer();
					primaryStage.close();
					myGui.backToMainButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							currentScene = myGui.getSceneOfMemoryMenu();
							primaryStage.close();
							currentScene.getStylesheets()
									.add(getClass().getResource("application.css").toExternalForm());
							primaryStage.setScene(currentScene);
							primaryStage.setResizable(false);
							primaryStage.show();
						}
					});

					currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(currentScene);
					primaryStage.setResizable(false);
					primaryStage.show();
				}
			});

			/*
			 * [ZweiSpieler] Die Funktion fuer das host Button im ZweiSpieler-Menu-Fenster wird erzeugt.
			 */
			myGui.hostButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					/*
					 * Sollte das HostButton gedrueckt worden sein, so wird per Zufall entschieden, welcher Spieler,
					 * seinen zug als erstes machen darf.
					 */
					double randomTurn = (double) (Math.random());
					if (randomTurn < 0.5) {
						myGui.player = new Player("Host", true);
						myGui.isTurn.setText("<- Turn");
						myGui.isTurn.setTranslateX(-100);
						myGui.isTurn.setTranslateY(-200);
					} else {
						myGui.player = new Player("Host", false);
						myGui.isTurn.setText("Turn ->");
						myGui.isTurn.setTranslateX(100);
						myGui.isTurn.setTranslateY(-200);
					}
					
					String ipAddresse;
					int selectedOption = 0;
					/*
					 * Ein Dialog Fenster Popt auf, kurz vor dem Server start.
					 * Damit man sich mit dem Jeweiligen Spieler verbindet, benoetigt der Client
					 * die IP-Addresse des Host, also der jenige, der den Server gestartet hat.
					 * Diese IP-Addresse wird angezeigt und der Client, kann sich damit verbinden.
					 */
					try {
						ipAddresse = InetAddress.getLocalHost().getHostAddress();
						selectedOption=JOptionPane.showConfirmDialog(null, "Deine IpAddresse lautet: "+ipAddresse, "IPAddress",
								JOptionPane.WARNING_MESSAGE);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					 * Wenn OKAY/YES Gedrueckt wurde, dann wird der Server gestartet und der Host wartet, 
					 * bis eine Verbindung hergestellt wurde.
					 */
					if(selectedOption == JOptionPane.YES_OPTION){
						ThreadServer x = new ThreadServer(myGui);
						Thread t = new Thread(x);
						InternetPlayer ip = x;
						t.start();
						myGui.ip = ip;
						currentScene = myGui.getTwoPlayerField();
						myGui.isTurn.setText("Warte auf Spieler...");
						myGui.isTurn.setTranslateX(0);
						primaryStage.close();
						currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(currentScene);
						primaryStage.setResizable(false);
						primaryStage.show();
					}
					/*
					 * Andernfalls gelangt er zum Haupt-menu zurueck.
					 */
					else{
						currentScene = myGui.getSceneOfTwoPlayer();
						primaryStage.close();
						myGui.backToMainButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								currentScene = myGui.getSceneOfMemoryMenu();
								primaryStage.close();
								currentScene.getStylesheets()
										.add(getClass().getResource("application.css").toExternalForm());
								primaryStage.setScene(currentScene);
								primaryStage.setResizable(false);
								primaryStage.show();
							}
						});

						currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(currentScene);
						primaryStage.setResizable(false);
						primaryStage.show();
					}

				}
			});

			/*
			 * [ZweiSpieler] Die Funktion fuer das Join Button wird erzeugt
			 */
			myGui.joinButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					currentScene = myGui.getConnectWithServerScene();
					
					primaryStage.close();
					//Zurueck Button
					myGui.backToMainButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							currentScene = myGui.getSceneOfMemoryMenu();
							primaryStage.close();
							currentScene.getStylesheets()
									.add(getClass().getResource("application.css").toExternalForm());
							primaryStage.setScene(currentScene);
							primaryStage.setResizable(false);
							primaryStage.show();
						}
					});
					
					/*
					 * JoinToServerButton wird erzeugt.
					 * Sobald man auf JOIN gedrueckt hat, so Verbindet der Client mit dem Server
					 * vorrausgesetzt die Ip, die man im Textfeld eingegeben hat, ist gueltig.
					 */
					myGui.joinToServerButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							String ipAddress = myGui.textField.getText();
							try {
								GameClients client = new GameClients(ipAddress, myGui);
								myGui.ip = client;
								if(!myGui.warningLabel.isVisible()){
									currentScene = myGui.getTwoPlayerField();
									primaryStage.close();
									currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
									primaryStage.setScene(currentScene);
									primaryStage.setResizable(false);
									primaryStage.show();
								}
							} catch (Exception e) {
								myGui.warningLabel.setVisible(true);
							}

						}
					});

					currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(currentScene);
					primaryStage.setResizable(false);
					primaryStage.show();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//Start 
		launch(args);
	}
}