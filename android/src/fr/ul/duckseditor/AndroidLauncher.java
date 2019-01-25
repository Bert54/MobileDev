package fr.ul.duckseditor;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// On vérifie qu'on a bien les permissions de lecture/écriture si on est sous Android M ou plus
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
					checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				this.initGame();
			}
		} else {
			this.initGame();
		}
	}

	private void initGame() {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.a = 8;
		initialize(new DucksEditor(), config);
	}
}