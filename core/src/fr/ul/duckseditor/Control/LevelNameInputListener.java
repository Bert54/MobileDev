package fr.ul.duckseditor.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.ul.duckseditor.View.EditorScreen;

/**
 * Ce listener s'occupe de gérer la boite de dialogue qui va permettre à l'utilisateur de saisir le nom du nouveau niveau
 * et de récupérer ce dernier
 */
public class LevelNameInputListener implements Input.TextInputListener {

    private EditorScreen editorScreen;
    private boolean modeNewFile;

    public LevelNameInputListener(EditorScreen es) {
        this.editorScreen = es;
    }

    @Override
    public void input(final String text) {
        /* Il est nécéssaire ici d'utiliser un nouveau thread pour la boite de dialogue afin que la capture d'écran
         * de la sauvegarde s'effectue correctement. Si on ne fait pas cela, la capture va tenter de s'effectuer
         * sur la boite qui n'est pas une application OpenGL, ce qui va en outre générer une exception (et pas de sauvegarde)...
         */
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (modeNewFile) {
                    editorScreen.saveFile(true, text);
                }
                else {
                    editorScreen.saveFile(false, text);
                }
            }
        });
    }

    @Override
    public void canceled () {
    }

    public void setMode(boolean mode) {
        this.modeNewFile = mode;
    }

}
