package de.hoverball.game;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Die Klasse {@link GameView} ist die View für die KLasse {@link ballVelocityY}.
 * Über die Methode "drawGame(...)" kann man die Spielobjekte auf den Bildschirm zeichnen.
 */
public class GameView extends SurfaceView {
 
	/** über den {@link SurfaceHolder} kann man sich ein Canvas (Leinwand) hohlen, auf welche man dann zeichnen kann */
	private SurfaceHolder surfaceHolder;
	
	/**
	 * Der Konstruktor der Klasse {@link GameView}.
	 * 
	 * @param context welcher Activity die View angehört, um auf System Daten zugreifen zu dürfen
	 */
	public GameView(Context context) {
		super(context);
		
		surfaceHolder = this.getHolder();
	}
	
	/**
	 * Die Methode zeichnet die übergebenen Objekte in der gewünschten Farbe auf das Canvas(Leinwand).
	 * 
	 * @param backgroundColorResourceId die Resource ID der gewünschten Farbe für den Hintergrund
	 * @param ball der Ball als {@link Rectangle}
	 * @param ballColorResourceId die Resource ID der gewünschten Farbe für den Ball
	 * @param barrierList die Liste mit allen Hindernissen die gezeichnet werden sollen
	 * @param barrierColorResourceId die Resource ID der gewünschten Farbe für die Hindernisse
	 */
	public void drawGame(int backgroundColorResourceId, Rectangle ball, int ballColorResourceId, ArrayList<Barrier> barrierList, int barrierColorResourceId ) {
		if (surfaceHolder.getSurface().isValid()) { 
			//Canvas hohlen und zum Zeichnen bereitmachen
			Canvas canvas = surfaceHolder.lockCanvas(); 
			
			///////////////Hier wird gezeichnet/////////////////
			// Hintergrund zeichnen
			canvas.drawColor(getResources().getColor(backgroundColorResourceId));
			
			// Ball zeichnen
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(ballColorResourceId));
			canvas.drawCircle(ball.getCenterX(), ball.getCenterY(), ball.getHeight() / 2, paint);
			
			// Hindernisse zeichnen
			paint.setColor(getResources().getColor(barrierColorResourceId));
			for (Barrier barrier : barrierList) {
				canvas.drawRect(new Rect((int)barrier.getBarrierPart1().getLeft(), (int)barrier.getBarrierPart1().getTop(), (int)barrier.getBarrierPart1().getRight(), (int)barrier.getBarrierPart1().getBottom()), paint);
				canvas.drawRect(new Rect((int)barrier.getBarrierPart2().getLeft(), (int)barrier.getBarrierPart2().getTop(), (int)barrier.getBarrierPart2().getRight(), (int)barrier.getBarrierPart2().getBottom()), paint);
			}
			////////////////////////////////////////////////////
			
			surfaceHolder.unlockCanvasAndPost(canvas); //Zeichnen Beenden und zum Anzeigen Freigeben
		} else {
			Log.e("SpielView", "Es kann nicht auf das Canvas gezeichnet werden!");
		}
	}
}
