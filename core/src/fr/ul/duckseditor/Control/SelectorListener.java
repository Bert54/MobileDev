package fr.ul.duckseditor.Control;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import java.util.ArrayList;

public class SelectorListener implements InputProcessor {

    private FileChooser fileChooser;
    protected final ArrayList<Body> listeBody;
    private Vector3 touch;
    private OrthographicCamera camera;

    public SelectorListener(FileChooser fc, OrthographicCamera c) {
        this.fileChooser = fc;
        this.listeBody = new ArrayList<Body>();
        this.camera = c;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // Exécution d'une action de l'éditeur en mode Desktop
            if (button == Input.Buttons.LEFT) {
                performActionSelector(screenX, screenY);
            }
        }
        else if (Gdx.app.getType() == Application.ApplicationType.Android) { // Exécution d'une action de l'éditeur en mode Android
            if (Gdx.input.isTouched(0)) {
                performActionSelector(screenX, screenY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    public void performActionSelector(int screenX, int screenY) {
        int performActionSelector = 0;
        this.listeBody.clear();
        Vector3 coords = new Vector3(screenX, screenY, 0);
        this.camera.unproject(coords); // Conversion des coordonnées de la position de l'écran vers la position monde
        this.touch = coords;
        fileChooser.getWorld().QueryAABB(queryCallback(), coords.x - 1f, coords.y - 1f, coords.x + 1f, coords.y + 1f); // Récupération des body concerné par l'appui
        for (Body b : this.listeBody) { // Récupération de l'action concerné lors de l'appui d'un des boutons du sélecteur
            performActionSelector = this.fileChooser.hasButton(b, performActionSelector);
        }
        switch (performActionSelector) { // performActionSelector != 0 : on a donc appuyé sur un bouton du sélecteur
            case 1: // On cycle un niveau dans le chargeur vers la gauche
                this.fileChooser.cycleLevel(false);
                break;
            case 2: // On cycle un niveau dans le chargeur vers la droite
                this.fileChooser.cycleLevel(true);
                break;
            case 3: // On charge un niveau
                this.fileChooser.loadLevel();
                break;
            case 4: // On quitte le chargeur de niveau
                this.fileChooser.hideSelector();
                break;
        }
    }

    private QueryCallback queryCallback() {
        QueryCallback query = new QueryCallback() {

            @Override
            public boolean reportFixture(Fixture fixture) {
                listeBody.add(fixture.getBody());
                return true;

            }
        };
        return query;
    }

}


