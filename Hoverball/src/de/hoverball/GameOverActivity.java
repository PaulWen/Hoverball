package de.hoverball;

import de.hoverball.game.GameActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Die Klasse {@link GameOverActivity} ist ein Fenster indem die erreichten Punkte angezeigt werden,
 * eine neue Spielrunde gestartet werden kann und man zum Start Men� zur�ck kann.
 */
public class GameOverActivity extends Activity implements OnClickListener {

	private Button restartButton;
	private Button startMenuButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// das GameOver-Fenster anzeigen
		setContentView(R.layout.game_over);
		
		// Instanzen von den Buttons aus dem GameOver-Fenster hohlen und den OnClickListener bei ihnen registrieren 
		restartButton = (Button)findViewById(R.id.restartButton);
		restartButton.setOnClickListener(this);
		startMenuButton = (Button)findViewById(R.id.startMenuButton);
		startMenuButton.setOnClickListener(this);
		
		// die Punkte im GameOver-Fenster eintragen
		((TextView)findViewById(R.id.pointsTextView)).setText("" + getIntent().getExtras().getInt(GameActivity.POINTS));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Men�- und Zur�ck-Button-Ereignisse werden abgefangen
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// die Optionen �ffnen
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			// das Start-Men� �ffnen
			finish();
			Intent intent = new Intent(this, StartActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		// sagen, das das Ereignis bearbeitet wurde
		return true;
	}
	
	@Override
	public void onClick(View view) {
		// eine neue Spielrunde starten, wenn der RestartButton gedr�ckt wird
		if (view == restartButton) {
			finish();
			Intent intent = new Intent(this, GameActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		// das Start-Men� �ffnen, wenn der Start-Men�-Button gedr�ckt wird
		} else if (view == startMenuButton) {
			finish();
			Intent intent = new Intent(this, StartActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
