package application;

import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * Threads für die Bedingung, das sobald zwei Felder offen sind und Ungleich einander sind, soll angezeigt werden 
 * und anschließend gelöscht werden.
 */
public class ButtonResetTimer extends Thread {

	private Button firstButton, secondButton;
	private long sleepTime;
	private GUI gui;
	
	/**
	 * Der Konstruktor erwartet als Eingabe, das Erste Button was gedrueckt worden ist und das zweite gedrueckte Button.
	 * Eine bestimmte Zeitangabe, wie lange die buttons angezeigt werden sollen, bevor die gelöscht werden.
	 * Die GUI um die Methode unlockNewClick() aufzurufen, damit während der zeitspanne, das die Buttons Anzeigt, 
	 * keine Weiteren Buttons gedrückt werden können.
	 * 
	 * @param firstButton = Das Button, das als Erstes gedrückt worden ist
	 * @param secondButton = Das Button, das als letztes gedrückt worde ist 
	 * @param sleepTime = Wie lange sollen, die Buttons angezeigt werden
	 * @param gui = zu Methoden aufruf benötigt(unlockNewClick())
	 */
    public ButtonResetTimer(Button firstButton, Button secondButton, long sleepTime, GUI gui){
    	this.firstButton=firstButton;
    	this.secondButton=secondButton;
    	this.sleepTime=sleepTime;
    	this.gui=gui;
    }

    public void run(){
        try {
        	//Zeigt es 1 Sekunde an
        	Thread.sleep(sleepTime);
        	//Danach löscht er es
        	Platform.runLater(() -> {
            	firstButton.setText("");
            	secondButton.setText("");
            	gui.unlockNewClick();
        	});
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
