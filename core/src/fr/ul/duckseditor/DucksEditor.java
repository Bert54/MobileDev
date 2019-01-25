package fr.ul.duckseditor;

import com.badlogic.gdx.Game;
import fr.ul.duckseditor.View.EditorScreen;

public class DucksEditor extends Game {

	public EditorScreen jeu;

	/**
	 * Pour le moment le bouton play de EditorScreen permet de toggle l'état du monde (celui commence désactivé lors de l'ouverture).
	 * Les objets crées interagissent entres-eux, mais ont ne peut pas les déplacer pour le moment.
	 * On peut créer des objets à volonté en appuyant sur les boutons de l'interface prévus à cet effet.
	 */

	@Override
	public void create () {
        this.jeu  = new EditorScreen();
        this.setScreen(this.jeu);
	}

	@Override
	public void dispose () {
		this.jeu.dispose();
	}
}
