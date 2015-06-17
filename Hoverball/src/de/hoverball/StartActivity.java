package de.hoverball;


import de.hoverball.game.GameActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Die Klasse {@link StartActivity} ist das Start-Fenster der App.
 * Im Start-Fenster kann man eine neue Spielrunde starten, in die Einstellungen gelangen
 * und die App komplett Beenden.
 */
public class StartActivity extends Activity implements OnClickListener {

	private Button playButton;
	private Button settingsButton;
	private Button exitButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// das StartMenu-Fenster anzeigen
		setContentView(R.layout.start);
		
		// Instanzen von den Buttons aus dem StartMenu-Fenster hohlen und den OnClickListener bei ihnen registrieren
		playButton = (Button)findViewById(R.id.playButton);
		playButton.setOnClickListener(this);
		settingsButton = (Button)findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(this);
		exitButton = (Button)findViewById(R.id.exitButton);
		exitButton.setOnClickListener(this);
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
			// die App beenden
			System.exit(0);
		}
		// sagen, das das Ereignis bearbeitet wurde
		return true;
	}
	
	@Override
	public void onClick(View view) {
		// eine neue Spielrunde starten, wenn der PlayButton gedr�ckt wird
		if (view == playButton) {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
		// die Einstellungen �ffnen, wenn der SettingsButton gedr�ckt wird
		} else if (view == settingsButton) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		// die App schlie�en, wenn der ExitButton gedr�ckt wird
		} else if (view == exitButton) {
			System.exit(0);
		}
		
	}
}
