package de.hoverball.game;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;
import de.hoverball.GameOverActivity;
import de.hoverball.R;
import de.hoverball.SettingsActivity;

/**
 * Die Klasse {@link ballVelocityY} ist das eigentliche Spiel.
 * Sie steuert das komplette Spielgeschehen.
 */
public class GameActivity extends Activity implements Runnable, SensorEventListener, OnClickListener {

	/** unter welchem Namen die Information über die ANzahl der Punkte an die GameOverActivity im Intent übermittelt werden sollen */
	public static final String POINTS = "points";
	
	/** der Abstand zwischen den einzelnen Hindernissen (in Pixel) */
	private final int SPACING_BETWEEN_BARRIERS = 350;
	/** der Durchmesser vom Ball (in Pixel) */
	private final int BALL_DIAMETER = 75;
	/** die Geschwindigkeit vom Ball auf der X-Achse (in Pixel/Sekunde) */
	private final int BALL_VELOCITY_X = 100;
	/** die Geschwindigkeit vom Ball auf der Y-Achse (in Pixel/Sekunde) */
	private final int BALL_VELOCITY_Y = 220;
	/** die Anziehungskraft mit welcher der Ball zum Boden gezogen wird (in Pixel/Sekunde) */
	private final int GRAVITY = 200;
	/** die Breite von einem Hindernis (in Pixel) */
	private final int BARRIER_WIDTH = 75;
	/** die Größe der Lücke in einem Hindernis, durch welche der Ball durch muss */
	private final int GAP_SIZE_OF_ONE_BARRIER = (int)(3 * BALL_DIAMETER);

	
	/** die "Leinwand" auf die alle Spielobjekte gezeichnet werden */
	private GameView view;
	/** die Breite des Bildschirms (in Pixel) */
	private int screenWidth;
	/** die Höhe des Bildschirms (in Pixel) */
	private int screenHeight;
	
	/** zum steuern des Threads, damit der z.B. pausiert und fortgesetzt werden kann */
	private Handler handler;
	/** gibt an ob das Spiel gerade läuft oder z.B. gerade pausiert ist */
	private boolean gameRunning;
	/** wann das Spiel das letzte mal neu gerendert wurde */
	private long lastFrame;

	/** falls ein Toast angezeigt wird ist diese Variable != null */
	private Toast toast;
	
	
	
	/** die Liste mit allen Hindernissen */
	private ArrayList<Barrier> barrierList;
	/** der Ball als {@link Rectangle}, damit man es einfach hat bei der Kollisionsabfrage */
	private Rectangle ball;
	/** der Zeitpunkt zu dem der Ball das letzte mal noch oben "geworfen" wurde */
	private long time0;
	/** die aktuelle Geschwindigkeit, mit welcher sich der Ball auf der X-Achse bewegt (int Pixel/Sekunde) */
	private float currentVelocityX;
	/** die Anzahl der Hindernisse, welcher der Ball durchquert hat */
	private int barriersCrossed;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Datenfelder initialisieren
		view = new GameView(this);
		
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		
		
		handler = new Handler();
		gameRunning = false;
    	lastFrame = 0;
    	
    	toast = null;
    	
    	barrierList = new ArrayList<Barrier>();
    	ball = new Rectangle(350, 500, BALL_DIAMETER, BALL_DIAMETER);
    	time0 = System.currentTimeMillis();
    	currentVelocityX = 0;
    	barriersCrossed = 0;
    	
    	
    	
		// die Listener für die Spielsteuerung setzen
			// eine Instanz vom Accelerometer hohlen und den "SensorEventListener" bei ihm registrieren
		Sensor accelerometer = ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			// den "OnClickListener" bei der View registrieren
		view.setOnClickListener(this);
		
		
		
		// Hindernisse erstellen
		for (int i = screenWidth - SPACING_BETWEEN_BARRIERS; i < screenWidth * 2; i += SPACING_BETWEEN_BARRIERS) {
			barrierList.add(generateNewBarrier(i));
		}
		
		
		
		// das Spielfeld anzeigen
    	setContentView(view);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// das Spiel läuft
		gameRunning = true;
		// den Zeitpunkt des letzten Frames auf die aktuelle Zeit setzen
		lastFrame = System.currentTimeMillis();
		// den Render-Thread starten
		handler.postDelayed(this, 0);	
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// das Spiel pausiert
		gameRunning = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Menü- und Zurück-Button-Ereignisse werden abgefangen
		if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK) {
			// die Optionen öffnen
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		// sagen, das das Ereignis bearbeitet wurde
		return true;
	}

	public void run() {
		// berechnen wie lange der letzte Frame her ist 
		long renderDuration = System.currentTimeMillis() - lastFrame;
		lastFrame += renderDuration;
		
		if (renderDuration > 0) {
			// FPS ausgeben
			Log.i("RENDERER", "FPS: " + 1000 / renderDuration);
			
			// prüfen, ob das Spiel vorbei ist, weil der Spieler/Ball mit einem Hindernis kollidiert ist
			if (didPlayerCrashIntoBarrier()) {
				// die GameOverActivity starten, wenn der Spieler/Ball mit einem Hindernis kollidiert ist
				finish();
				Intent intent = new Intent(this, GameOverActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(GameActivity.POINTS, barriersCrossed);
				startActivity(intent);
			} else {
				// das Spiel animieren, wenn Spieler/Ball noch am "leben" ist
				moveAllBarriers(renderDuration);
				moveBall(renderDuration);
				// das Spielfeld neu zeichnen
				view.drawGame(R.color.color3, ball, R.color.color2,  barrierList, R.color.color1);
			}
		}
		
		// wenn das Spiel noch läuft in 12ms den nächsten Frame rendern
		if (gameRunning) {
			handler.postDelayed(this, 12);
		}		
	}
	
	
	@Override
	public void onClick(View view) {
		// wenn der Bildschirm berührt wird soll der Ball nach oben fliegen
		if (view == this.view) {
			time0 = System.currentTimeMillis();
		}
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// wenn das Gerät nach links oder rechts geneigt wird soll der Ball nach links oder rechts fliegen
		if (event.values[1] < -0.5) { // fragen ob nach links bewegen
			currentVelocityX = (BALL_VELOCITY_X) * event.values[1];
		} else if(event.values[1] > 0.5) { // fragen ob nach rechts bewegen
			currentVelocityX = (BALL_VELOCITY_X) * event.values[1];
		} else {
			currentVelocityX = 0;
		}
	}
	
	
	/**
	 * Die Methode generiert ein neues Hindernis.
	 * Die linke Seite vom Hindernis befindet sich dabei an der gewünschten X-Position.
	 * 
	 * @param x die gewünschte X-Position der linken Seite vom Hindernis
	 * @return das neue Hindernis
	 */
	private Barrier generateNewBarrier(float x) {
		// zufällig bestimmen was die Y-Position der oberen Kante von der Lücke im Hindernis ist
		int y = (int)(Math.random() * (screenHeight - GAP_SIZE_OF_ONE_BARRIER + 1));
		
		// das neue Hindernis zurückgeben
		return new Barrier(new Rectangle(x, 0, BARRIER_WIDTH, y),
							  new Rectangle(x, (float)(y + GAP_SIZE_OF_ONE_BARRIER), BARRIER_WIDTH, (float)(screenHeight - (y + GAP_SIZE_OF_ONE_BARRIER))));
	}
	
	/**
	 * Die Methode bewegt alle Hindernisse in Abhängigkeit von der {@link GameActivity#currentVelocityX} und wie lange
	 * der letzte Frame her ist.
	 * 
	 * @param renderDuration wie lange der letzte Frame her ist 
	 */
	private void moveAllBarriers(long renderDuration) {
		// nur die Hindernisse bewegen, wenn das vorderste Hindernis vor x = 250 liegt...
		if ((renderDuration / 1000.0 * currentVelocityX) > 0 || barrierList.get(0).getX() <= SPACING_BETWEEN_BARRIERS) {
			// den Toast beenden, falls gerade noch ein Toast angezeigt wird
			if (toast != null) {
				toast.cancel();
				toast = null;
			}
			
			// die Liste ist nötig, da man nicht durch eine Liste iterieren kann und gleichzeitig etwas in sie einfügen kann
			ArrayList<Barrier> newBarriers = new ArrayList<Barrier>();
			
			// mit einem Iterator durch alle Hindernisse gehen, damit man auch während dessen Hindernisse löschen kann
			Iterator<Barrier> barrierListIterator = barrierList.iterator();
		    while (barrierListIterator.hasNext()) {
		    	Barrier barrier = barrierListIterator.next();
		    	
		    	// das Hindernis auch seine neue Position bewegen
				barrier.setX(barrier.getX() - (float)(renderDuration / 1000.0 * currentVelocityX));
				// prüfen ob Hindernis damit außerhalb des "Sichtfeldes" ist
				if (barrier.getX() < (screenWidth * -1)) {
					// Hindernis entfernen, wenn es außerhalb des "Sichtfeldes" ist
					barrierListIterator.remove();

				// prüfen ob der Ball ein Hindernis durchquert hat
				} else if (!barrier.isCrossed() && barrier.getX() + BARRIER_WIDTH / 2 <= ball.getCenterX()) {
					// für jedes durchquerte Hindernis einen Punkt hinzuzählen und ein neues Hindernis generieren
					barrier.setCrossed();
					barriersCrossed++;
					newBarriers.add(generateNewBarrier(barrierList.get(barrierList.size() - 1).getX() + SPACING_BETWEEN_BARRIERS));
				}
			}
		    
		    // die neu erstellten Hindernisse der Hindernis-Liste hinzufügen
		    barrierList.addAll(newBarriers);
		   
		// ... ansonsten einen Toast ausgeben, indem steht, dass man sich in die Falsche Richtung bewegt
		} else {
			// wenn bereits ein Toast angezeigt wird
			if (toast != null) {
				// den Toast der gerade angezeigt wird beenden
				toast.cancel();
			}
			// einen neuen Toast öffnen
			toast = Toast.makeText(getApplicationContext(), "Falsche Richtung!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	/**
	 * Die Methode bewegt den Ball in Abhängigkeit seiner aktuellen Geschwindigkeit
	 * (wird ebenfalls in dieser Methode berechnet mit Hilfe von {@link GameActivity#time0})
	 * und wie lange der letzte Frame her ist.
	 * 
	 * @param renderDuration wie lange der letzte Frame her ist
	 */
	private void moveBall(long renderDuration) {
		// die aktuelle Geschwindigkeit vom Ball auf der Y-Achse ausrechnen
		float ballVelocityY = (float)(BALL_VELOCITY_Y - GRAVITY * ((System.currentTimeMillis() - time0) / 1000.0));
		// die neue Position vom Ball berechnen
		float newPosition = ball.getY() - (float)(renderDuration / 1000.0 * ballVelocityY);
		
		// prüfen ob die neue Position des Balls noch innerhalb des Spielfeldes ist
		if (newPosition < 0) {
			// wenn der Ball oberhalb des Spielfeldes ist
			ball.setY(0);
		} else if (newPosition + ball.getHeight() > screenHeight) {
			// wenn der Ball unterhalb des Spielfeldes ist
			ball.setY(screenHeight - ball.getHeight());
		} else {
			// wenn der Ball innerhalb des Spielfeldes ist
			ball.setY(newPosition);
		}
	}
	
	/**
	 * Die Methode gibt aus, ob der Spieler/Ball mit einem Hindernis kollidiert ist.
	 * 
	 * @return true: der Spieler/Ball ist mit einem Hindernis kollidiert
	 * 		   false: der Spieler/Ball ist NICHT mit einem Hindernis kollidiert
	 */
	private boolean didPlayerCrashIntoBarrier() {
		// jedes Hindernis überprüfen, ob es mit dem Spieler/Ball kollidiert
		for (Barrier barrier : barrierList) {
			if (barrier.getBarrierPart1().intersects(ball) || barrier.getBarrierPart2().intersects(ball)) {
				return true;
			}
		}
		
		return false;
	}
}
