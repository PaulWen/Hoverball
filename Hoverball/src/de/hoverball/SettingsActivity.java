package de.hoverball;


import android.app.Activity;
import android.os.Bundle;

/**
 * Die Klasse {@link SettingsActivity} ist das Fenster, �ber welches man die Einstellungen des Spiels �ndern kann.
 * (z.B. Lautst�rke von Musik und Soundeffekten)
 */
public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
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
}
