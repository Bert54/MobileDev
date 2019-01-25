package fr.ul.duckseditor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.ul.duckseditor.DucksEditor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.a = 8;
		config.width = 1000;
		config.height = 800;
		new LwjglApplication(new DucksEditor(), config);
	}
}
