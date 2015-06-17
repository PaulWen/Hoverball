package de.hoverball.game;



/**
 * Die Klasse {@link Rectangle} stellt ein Rechteck da, welches in einem Koordinatensystem positioniert ist,
 * dass den Ursprung in der oberen-linken-Ecke besitzt.
 */
public class Rectangle {
	
	/** die x-Koordinate der oberen-linken-Ecke des Rechtecks */
	private float x;
	/** die Y-Koordinate der oberen-linken-Ecke des Rechtecks */
	private float y;
	/** die Breite des Rechteckes */
	private float width;
	/** die H�he des Rechteckes */
	private float height;
	

	/**
	 * Der Konstruktor der Klasse {@link Rectangle}.
	 * 
	 * @param x die x-Koordinate der oberen-linken-Ecke des Rechtecks
	 * @param y die Y-Koordinate der oberen-linken-Ecke des Rechtecks
	 * @param width die Breite des Rechteckes (der Wert muss >= 0 sein!)
	 * @param height die H�he des Rechteckes (der Wert muss >= 0 sein!)
	 */
	public Rectangle(float x, float y, float width, float height) {
		// Datenfelder initialisieren
		this.x = x;
		this.y = y;
		if (width >= 0 && height >= 0) {
			this.width = width;
			this.height = height;
		} else {
			throw new IllegalArgumentException("H�he (" + height + ") und Breite (" + width + ") m�ssen >= 0 sein!");
		}
	}
	

	
	/**
	 * Die Methode gibt die x-Koordinate der oberen-linken-Ecke des Rechtecks aus.
	 * 
	 * @return die x-Koordinate der oberen-linken-Ecke des Rechtecks
	 */
	public float getX() {
		return x;
	}
	/**
	 * Die Methode dient dazu die x-Koordinate der oberen-linken-Ecke des Rechtecks neu zusetzen.
	 * 
	 * @param die gew�nschte neue x-Koordinate der oberen-linken-Ecke des Rechtecks
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * Die Methode gibt die y-Koordinate der oberen-linken-Ecke des Rechtecks aus.
	 * 
	 * @return die y-Koordinate der oberen-linken-Ecke des Rechtecks
	 */
	public float getY() {
		return y;
	}
	/**
	 * Die Methode dient dazu die y-Koordinate der oberen-linken-Ecke des Rechtecks neu zusetzen.
	 * 
	 * @param die gew�nschte neue y-Koordinate der oberen-linken-Ecke des Rechtecks
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Die Methode gibt die Breite des Rechteckes aus.
	 * 
	 * @return die Breite des Rechteckes
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * Die Methode dient dazu die Breite des Rechteckes neu zusetzen.
	 * 
	 * @param die gew�nschte neue Breite des Rechteckes
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Die Methode gibt die H�he des Rechteckes aus.
	 * 
	 * @return die H�he des Rechteckes
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * Die Methode dient dazu die H�he des Rechteckes neu zusetzen.
	 * 
	 * @param die gew�nschte neue H�he des Rechteckes
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	

	
	/**
	 * Die Methode gibt die x-Koordinate der linken Seite des Rechteckes aus.
	 * 
	 * @return die x-Koordinate der linken Seite des Rechteckes
	 */
	public float getLeft() {
		return x;
	}
	/**
	 * Die Methode gibt die x-Koordinate der RechteSeite des Rechteckes aus.
	 * 
	 * @return die x-Koordinate der rechten Seite des Rechteckes
	 */
	public float getRight() {
		return x + width;
	}
	
	/**
	 * Die Methode gibt die y-Koordinate der Unterseite des Rechteckes aus.
	 * 
	 * @return die y-Koordinate der unteren Seite des Rechteckes
	 */
	public float getBottom() {
		return y + height;
	}
	/**
	 * Die Methode gibt die y-Koordinate der Oberseite des Rechteckes aus.
	 * 
	 * @return die y-Koordinate der oberen Seite des Rechteckes
	 */
	public float getTop() {
		return y;
	}
	
	/**
	 * Die Methode gibt die x-Koordinate des Mittelpunktes vom Rechteck aus.
	 * 
	 * @return die x-Koordinate des Mittelpunktes vom Rechteck
	 */
	public float getCenterX() {
		return x + width / 2;
	}
	
	/**
	 * Die Methode gibt die y-Koordinate des Mittelpunktes vom Rechteck aus.
	 * 
	 * @return die y-Koordinate des Mittelpunktes vom Rechteck
	 */
	public float getCenterY() {
		return y + height / 2;
	}
	
	
	/**
	 * Die Methode pr�ft, ob sich das Rechteck mit einem anderen Rechteck schneidet.
	 * 
	 * @param rectangle das Rechteck, welches auf �berscheidungen gepr�ft werden soll
	 * @return true: die Rechtecke �berschneiden sich
	 */
    public boolean intersects(Rectangle rectangle) {
        return this.getLeft() <= rectangle.getRight() && rectangle.getLeft() <= this.getRight() && this.getTop() <= rectangle.getBottom() && rectangle.getTop() <= this.getBottom();
    }
}