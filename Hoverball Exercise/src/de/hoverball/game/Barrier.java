package de.hoverball.game;

/**
 * Die Klasse {@link Barrier} stellt ein Hindernis da.
 * Ein Hindernis besteht aus zwei Rechtecken, die übereinander liegen mit einer Lücke zwischen ihnen.
 */
public class Barrier {
	private Rectangle barrierPart1;
	private Rectangle barrierPart2;
	
	/** ob das Hindernis bereits vom Ball/Spieler durchquert wurde */ 
	private boolean crossed;
	
	/**
	 * Der Konstruktor der Klasse {@link Barrier}.
	 * 
	 * @param hindernissTeil1 das erste {@link Rectangle}, welches zum Hindernis gehört
	 * @param hindernissTeil2 das zweite {@link Rectangle}, welches zum Hindernis gehört
	 */
	public Barrier(Rectangle hindernissTeil1, Rectangle hindernissTeil2) {
		this.barrierPart1 = hindernissTeil1;
		this.barrierPart2 = hindernissTeil2;
		
		crossed = false;
	}
	
	/**
	 * Die Methode gibt den oberen Teil des Hindernisses aus.
	 * 
	 * @return der obere Teil des Hindernisses als {@link Rectangle}
	 */
	public Rectangle getBarrierPart1() {
		return barrierPart1;
	}
	
	/**
	 * Die Methode gibt den unteren Teil des Hindernisses aus.
	 * 
	 * @return der unteren Teil des Hindernisses als {@link Rectangle}
	 */
	public Rectangle getBarrierPart2() {
		return barrierPart2;
	}
	
	/**
	 * Die Methode gibt aus, ob das Hindernis bereits als durchquert markiert wurde.
	 * 
	 * @return true: wenn das Hindernis bereits als durchquert markiert wurde
	 *         false: wenn das Hindernis noch NICHT als durchquert markiert wurde
	 */
	public boolean isCrossed() {
		return crossed;
	}
	
	/**
	 * Die Methode vermerkt, dass das Hindernis durchquert wurde.
	 * Ob ein Hindernis als durchquert markiert ist lässt sich über {@link #isCrossed()} herausfinden.
	 */
	public void setCrossed() {
		crossed = true;
	}
	
	/**
	 * Die Methode gibt die x-Koordinate der linken Seite des Hindernisses aus.
	 * 
	 * @return die x-Koordinate der linken Seite des Hindernisses
	 */
	public float getX() {
		return barrierPart1.getX();
	}
	
	/**
	 * Die Methode verschiebt das Hindernis so, dass die linke Seite vom Hindernis an der gewünschten
	 * X-Koordinate liegt.
	 * 
	 * @param x die X-Koordinate, an welcher die linke Seite vom Hindernis liegen soll
	 */
	public void setX(float x) {
		barrierPart1.setX(x);
		barrierPart2.setX(x);
	}
}
