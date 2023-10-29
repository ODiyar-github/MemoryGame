package application;

public class MemoryField {
	private final int cardsLength=10;
	private final char[][] charArray=new char[4][5]; 
	private final boolean[][] boolArray=new boolean[4][5];
	private String randomString="";
	
	public MemoryField (){
		randomString=randomString2();
		Matchfield();
	}
	
	/**
	 * Die Methode "randomString()" Erstellt ein String aus unterschiedlichen kleinen Buchstaben.
	 * @return String =randomString
	 */
	private String randomString(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<cardsLength;i++){
			sb.append((char)((int)(Math.random()*26)+97)); 
		}
		return sb.toString();
	}
	
	/**
	 * Falls die Methode "randomString()" ein String mit mehreren gleichen Chars erstellt,
	 * dann wird in "randomString2" dafuer gesorgt, dass es keine gleichen chars gibt.
	 * Ein neues String wird solange neu erzeugt, bis keine chars vorhanden sind, die gleich 
	 * vorkommen.
	 * @return String=randomString
	 */
	private String randomString2(){
		String x=randomString();
		for(int i=0;i<x.length();i++){
			for(int j=i+1;j<x.length();j++){
				if(x.charAt(i)==x.charAt(j)){
					x=randomString();
					i=0;
					j=i+1;
				}
			}
		}
		return x;
	}
	
	public char[][] getArray(){
		return charArray;
	}
	
	/**
	 * Die Methode erstellt das Char-Feld für Das Memory Spiel.
	 * Die Methode ist abhängig von der methode "random_String2()", da sie die vorraussetztung ist, 
	 * um die werte ins Feld einzuspeichern.
	 */
	private void Matchfield(){
		int rand1=(int)(Math.random() *4); //randomWert das hoechstens von 0-3 werte annhemen kann
		int rand2=(int)(Math.random() *5); //randomWert das hoechstens von 0-4 werte annhemen kann
		int index=0; //Lauf Index fuer ranom_zeichenfolge 
		while(!allTrue()){//Solang alle Werte nicht belegt sind machen folgendes:
			//wir pruefen, ob an der stelle rand1 und rand2 von boolArray kein wert belegt ist, wenn WAHR dann:
			if(!boolArray[rand1][rand2] ){
				//Ein Char an der stelle des Index von randomString wird ins Feld gespeichert.
				charArray[rand1][rand2]=randomString.charAt(index);
				//Belegung auf True setzen
				boolArray[rand1][rand2]=true;
				//neue werte werden gesetzt
				rand1=(int)(Math.random() *4);
				rand2=(int)(Math.random() *5);
				//index wird angepasst, falls das array die grenze überschritten hat, soll wieder auf 0 gesetzt werden 
				//und von vorn anfangen, andernfalls soll erhoet werden.
				if(index>=9){
					index=0;
				}
				else{
					index++;
				}
			}else{
				//Wenn die gegebene Stelle belegt ist, werden neue werte erstellt.
				rand1=(int)(Math.random() *4);
				rand2=(int)(Math.random() *5);
			}
		}
	}

	/**
	 * Eine Methode, um zu pruefen, ob alle Werte bzw. alle Chars ins Feld eingespeichert worde,
	 * wenn JA, dann soll True zurück gegeben werden.
	 * @return True = Wenn alles Belegt wurde im Feld
	 */
	public boolean allTrue(){
		for(int i=0;i<boolArray.length;i++){
			for(int j=0;j<boolArray[0].length;j++){
				if(!boolArray[i][j]){
					return false;
				}
			}
			
		}
		return true;
	}	
}
