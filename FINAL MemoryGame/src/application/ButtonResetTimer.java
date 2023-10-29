package application;

import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * Threads f�r die Bedingung, das sobald zwei Felder offen sind und Ungleich einander sind, soll angezeigt werden 
 * und anschlie�end gel�scht werden.
 */
public class ButtonResetTimer extends Thread {

	private Button firstButton, secondButton;
	private long sleepTime;
	private GUI gui;
	
	/**
	 * Der Konstruktor erwartet als Eingabe, das Erste Button was gedrueckt worden ist und das zweite gedrueckte Button.
	 * Eine bestimmte Zeitangabe, wie lange die buttons angezeigt werden sollen, bevor die gel�scht werden.
	 * Die GUI um die Methode unlockNewClick() aufzurufen, damit w�hrend der zeitspanne, das die Buttons Anzeigt, 
	 * keine Weiteren Buttons gedr�ckt werden k�nnen.
	 * 
	 * @param firstButton = Das Button, das als Erstes gedr�ckt worden ist
	 * @param secondButton = Das Button, das als letztes gedr�ckt worde ist 
	 * @param sleepTime = Wie lange sollen, die Buttons angezeigt werden
	 * @param gui = zu Methoden aufruf ben�tigt(unlockNewClick())
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
        	//Danach l�scht er es
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
