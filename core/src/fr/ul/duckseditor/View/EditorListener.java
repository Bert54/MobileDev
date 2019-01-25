package fr.ul.duckseditor.View;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import fr.ul.duckseditor.Model.Monde;

import java.util.ArrayList;

public class EditorListener implements InputProcessor {

    private OrthographicCamera camera;
    private Monde monde;
    protected final ArrayList<Body> listeBody;
    protected Body bodyDragged;
    private EditorScreen editorScreen;
    private Vector3 touch;

    public EditorListener(OrthographicCamera c, Monde m, EditorScreen ec) {
        this.camera = c;
        this.bodyDragged = null;
        this.monde = m;
        this.listeBody = new ArrayList<Body>();
        this.editorScreen = ec;
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
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // Exécution d'une action de l'éditeur en mode Desktop
            if (button == Input.Buttons.LEFT) {
                this.performActionEditor(screenX, screenY);
            }
        }
        else if (Gdx.app.getType() == Application.ApplicationType.Android) { // Exécution d'une action de l'éditeur en mode Android
            if (Gdx.input.isTouched(0)) {
                this.performActionEditor(screenX, screenY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.bodyDragged != null) {
            Body deleteBody = null;
            int performActionEditor = 0;
            this.listeBody.clear();
            Vector3 coords = new Vector3(screenX, screenY, 0);
            this.camera.unproject(coords); // Conversion des coordonnées de la position de l'écran vers la position monde
            this.touch = coords;
            monde.getWorld().QueryAABB(queryCallback(), coords.x - 1f, coords.y - 1f, coords.x + 1f, coords.y + 1f); // Récupération des body concerné par l'appui
            for (Body b : this.listeBody) { // Récupération de l'action concerné lors du relâchement de l'appui sur l'écran
                performActionEditor = this.editorScreen.hasButton(b, performActionEditor);
                if (performActionEditor == 3) {
                    deleteBody = b;
                }
            }
            if (deleteBody != null) { //deleteBody n'est pas nul : on a donc relaché l'appui sur la corbeille
                boolean suppr = false;
                Array<Fixture> fixtures = deleteBody.getFixtureList();
                for (Fixture f : fixtures) {
                    if (f.testPoint(bodyDragged.getPosition().x, bodyDragged.getPosition().y)) {
                        suppr = true;
                    }
                }
                if (suppr) { // si suppr = vrai, alors un body est sur la corbeille lors du relâchement : on le supprime donc
                    this.monde.deleteBody(this.bodyDragged);
                }
            }
        }
        this.bodyDragged = null;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {  // Exécution d'une action de l'éditeur en mode Desktop
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) { // Mode rotation
                this.rotateObjects(screenX, screenY);
            }
            else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { // Mode translation
                this.translateObjects(screenX, screenY);
            }
        }
        else if (Gdx.app.getType() == Application.ApplicationType.Android) {  // Exécution d'une action de l'éditeur en mode Android
            if (Gdx.input.isTouched(1)) { // Mode rotation
                this.rotateObjects(screenX, screenY);
            }
            else if (Gdx.input.isTouched(0)) { // Mode translation
                this.translateObjects(screenX, screenY);
            }
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
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

    /**
     * Exécute une action dans l'éditeur en fonction du bouton appuyé déterminé par les coordonnées de l'appui
     * @param screenX Coordonnées X de l'appui
     * @param screenY Coordonnées Y de l'appui
     */
    private void performActionEditor(int screenX, int screenY) {
        int performActionEditor = 0;
        this.listeBody.clear();
        Vector3 coords = new Vector3(screenX, screenY, 0);
        this.camera.unproject(coords); // Conversion des coordonnées de la position de l'écran vers la position monde
        this.touch = coords;
        monde.getWorld().QueryAABB(queryCallback(), coords.x - 0.1f, coords.y - 0.1f, coords.x + 0.1f, coords.y + 0.1f); // Récupération des body concerné par l'appui
        for (Body b : this.listeBody) { // Récupération de l'action concerné lors de l'appui d'un des boutons de l'éditeur
            performActionEditor = this.editorScreen.hasButton(b, performActionEditor);
        }
        switch (performActionEditor) { // performActionEditor != 0 : on a donc appuyé sur un bouton de l'éditeur
            case 1: // Charge le sélecteur de fichier
                if (!this.monde.hasGravityEnabled()) {
                    this.editorScreen.setEditorLoad(true);
                }
                break;
            case 2: // Crée un fichier de sauvegarde vide
                if (!this.monde.hasGravityEnabled()) {
                    this.editorScreen.showLevelNameDialog(true);
                }
                break;
            //case 3 correspond à la poubelle, donc pas utile ici
            case 4: // Change l'état du monde en mode actif si eu seulement si celui-ci est inactif
                if (!this.monde.hasGravityEnabled()) {
                    this.monde.setState();
                }
                break;
            // Change l'état du monde en mode inactif si eu seulement si celui-ci est actif, dans le cas contraire, on ferme l'application
            case 5:
                if (this.monde.hasGravityEnabled()) {
                    this.monde.setState();
                }
                else {
                    Gdx.app.exit();
                }
                break;
            case 6: // Ajout d'un rectangle
                if (!this.monde.hasGravityEnabled()) {
                    monde.addBeam();
                }
                break;
            case 7: // Ajout d'un bloc
                if (!this.monde.hasGravityEnabled()) {
                    monde.addSquare();
                }
                break;
            case 8: // Ajout d'un prisonnier
                if (!this.monde.hasGravityEnabled()) {
                    monde.addPrisonner();
                }
                break;
            case 9: // Ajout d'un ennemi
                if (!this.monde.hasGravityEnabled()) {
                    monde.addEnemy();
                }
                break;
            case 10: // Remplace un fichier de sauvegarde
                if (!this.monde.hasGravityEnabled()) {
                    this.editorScreen.showLevelNameDialog(false);
                }
                break;
            case 11: // Réinitialise le niveau
                if (!this.monde.hasGravityEnabled()) {
                    this.editorScreen.resetLevel();
                }
        }
    }

    /**
     * Change la position d'un objet en fonction de la position de l'appui lorsque celui-ci est maintenant pendant le mouvement
     * @param screenX Position X de la nouvelle position
     * @param screenY Position Y de la nouvelle position
     */
    private void translateObjects(int screenX, int screenY) {
        if (!this.monde.hasGravityEnabled()) { // On ne peut pas bouger les objets si le monde est actif
            Vector3 newTouch = new Vector3(screenX, screenY, 0);
            this.camera.unproject(newTouch);
            this.listeBody.clear();
            monde.getWorld().QueryAABB(queryCallback(), touch.x - 1f, touch.y - 1f, touch.x + 1f, touch.y + 1f); // Récupération des body concerné par l'appui
            if (this.bodyDragged == null) { // bodyDragged est nul : on vient donc de commencer un déplacement ; on cherche donc si on doit déplacer un body
                for (Body b : this.listeBody) {
                    if (this.monde.possedeObjetMonde(b)) {
                        this.bodyDragged = b;
                    }
                }
            }
            if (this.bodyDragged != null) { // bodyDragged n'est pas nul : on doit donc changer la position d'un body
                Vector2 translate = new Vector2();
                translate.x = touch.x - newTouch.x;
                translate.y = touch.y - newTouch.y;
                this.bodyDragged.setTransform(touch.x - translate.x, touch.y - translate.y, this.bodyDragged.getAngle());
                this.monde.setPositionInit(this.bodyDragged);
            }
            touch = newTouch;
        }
    }

    /**
     * Change l'angle d'une objet en fonction de la position de l'appui du deuxième doigt lorsque celui-ci est maintenant pendant le mouvement
     * Attention : une rotation n'est possible que si un objet est déjà en mouvement
     * @param screenX Position X du deuxieme doigt
     * @param screenY POsition Y du deuxième doigt
     */
    private void rotateObjects(int screenX, int screenY) {
        if (!this.monde.hasGravityEnabled()) {
            double degrees;
            Vector3 touch = new Vector3(screenX, screenY, 0);
            this.camera.unproject(touch);
            this.listeBody.clear();
            if (this.bodyDragged == null) {
                for (Body b : this.listeBody) {
                    if (this.monde.possedeObjetMonde(b)) {
                        this.bodyDragged = b;
                    }
                }
            }
            if (this.bodyDragged != null) { // bodyDragged n'est pas nul : on doit donc changer l'angle d'un body
                degrees = Math.atan2(
                        this.touch.x + touch.x,
                        this.touch.y + touch.y
                ) * 180.0d / Math.PI;
                this.bodyDragged.setTransform(this.bodyDragged.getPosition().x, this.bodyDragged.getPosition().y, (float) degrees);
                this.monde.setRotationInit(this.bodyDragged);

            }
        }
    }
}
