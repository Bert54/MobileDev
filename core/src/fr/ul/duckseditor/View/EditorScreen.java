package fr.ul.duckseditor.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.ul.duckseditor.Control.FileChooser;
import fr.ul.duckseditor.Control.LevelNameInputListener;
import fr.ul.duckseditor.DataFactory.TextureFactory;
import fr.ul.duckseditor.Model.*;
import fr.ul.duckseditor.View.EditorButtons.*;

import java.util.ArrayList;
import java.util.Scanner;

public class EditorScreen extends ScreenAdapter {

    public static final float CAMERAWIDTH = Gdx.graphics.getWidth();
    public static final float CAMERAHEIGHT = Gdx.graphics.getHeight();
    public static final float WORLDWIDTH = CAMERAWIDTH; //longueur du monde
    public static final float WORLDHEIGHT = CAMERAHEIGHT; //largeur du monde
    public static final float UMWIDTH = WORLDWIDTH/40f; // unité monde en longueur
    public static final float UMHEIGHT = WORLDHEIGHT/40f; // unité monde en largeur

    private SpriteBatch sb;
    private OrthographicCamera camera;
    private ArrayList<EditorButton> boutons;
    private EditorPanel edPanel;
    private Monde monde;
    private FileChooser fileChooser;
    private LevelNameInputListener levelNameInput;

    private InputProcessor editorListener;

    public EditorScreen() {
        int i = 1;
        this.monde = new Monde();
        this.edPanel = new EditorPanel();
        this.boutons = new ArrayList<fr.ul.duckseditor.View.EditorButtons.EditorButton>();
        this.boutons.add(new EditorLoad(monde.getWorld()));
        this.boutons.add(new EditorSave(monde.getWorld()));
        this.boutons.add(new EditorDelete(monde.getWorld()));
        this.boutons.add(new EditorPlay(monde.getWorld()));
        this.boutons.add(new EditorExit(monde.getWorld()));
        this.boutons.add(new EditorBlockBeam(monde.getWorld()));
        this.boutons.add(new EditorBlocSquare(monde.getWorld()));
        this.boutons.add(new EditorCharacterPris(monde.getWorld()));
        this.boutons.add(new EditorCharacterTarget(monde.getWorld()));
        this.boutons.add(new EditorRewrite(monde.getWorld()));
        this.boutons.add(new EditorReset(monde.getWorld()));
        for (EditorButton eb : this.boutons) { // Assigne un id unique aux boutons de l'interface qui nous permettra de distinguer les boutons lors de l'appui
            eb.setValue(i);
            i++;
        }
        this.camera = new OrthographicCamera();
        camera.viewportWidth = CAMERAWIDTH;
        camera.viewportHeight = CAMERAHEIGHT;
        this.sb = new SpriteBatch();
        camera.position.set(CAMERAWIDTH / 2f, CAMERAHEIGHT / 2f, 0);
        this.fileChooser = new FileChooser(this);
        this.editorListener = new EditorListener(this.camera, this.monde, this); // Le listener de l'éditeur
        this.levelNameInput = new LevelNameInputListener(this);
        this.setListenerToEditor();
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    /**
     *  Détecte si le body donné en paramètre se trouve bien dans l'interface de l'éditeur
     * @param bodyButton Le body
     * @param oldValue ancienne valeur de retour
     * @return SI le body se trouve dans l'interface de l'éditeur
     */
    public int hasButton(Body bodyButton, int oldValue) {
        int contains = 0;
        for (EditorButton eb: this.boutons) {
            if (eb.getBody().equals(bodyButton) && contains == 0) {
                contains = eb.getButtonValue();
            }
        }
        if (contains == 0) {
            return oldValue;
        }
        return contains;
    }


    /**
     * Charge un monde depuis un texte
     * @param fileTxt Le texte issue d'un fichier .mdl
     */
    public void loadSelectedFile(String fileTxt) {
        this.resetLevel(); // On reset tout d'abord le niveau
        Scanner levelScanner = new Scanner(fileTxt);
        String levelLine;
        boolean continueRead = true;
        // Ces 2 variables représentent les dimensions de la caméra de l'application qui a sauvegardé le niveau
        float oCameraW = -1;
        float oCameraH = -1;
        while (continueRead && levelScanner.hasNextLine()) { // On parse le texte tant qu'il en reste
            levelLine = levelScanner.nextLine();
            if (levelLine.equals("(O_Camera)")) { // Permet de savoir qu'on est dans le bloc de la caméra
                levelLine = levelScanner.nextLine();
                while (!levelLine.equals("EOB")) { // EOB permet de déterminer la fin d'un bloc
                    int parserInd = 0;
                    StringBuilder parser = new StringBuilder("");
                    StringBuilder parser2 = new StringBuilder("");
                    char parserChar = levelLine.charAt(0);
                    while (parserChar != ':') {
                        parser.append(parserChar);
                        parserInd++;
                        parserChar = levelLine.charAt(parserInd);
                    }
                    parserInd++;
                    parserChar = levelLine.charAt(parserInd);
                    while (parserChar != ';') {
                        parser2.append(parserChar);
                        parserInd++;
                        parserChar = levelLine.charAt(parserInd);
                    }
                    if (parser.toString().equals("CAMERAWIDTH")) {
                        oCameraW = Float.parseFloat(parser2.toString());
                    }
                    else if (parser.toString().equals("CAMERAHEIGHT")) {
                        oCameraH = Float.parseFloat(parser2.toString());
                    }
                    levelLine = levelScanner.nextLine();
                }
            }
            if (levelLine.equals("(characters)") || levelLine.equals("(blocks)")) { // Permet de déterminer si on est dans le bloc des personnages ou dans celui des blocs
                levelLine = levelScanner.nextLine();
                while (!levelLine.equals("EOB")) {
                    this.parseObject(levelLine, oCameraW, oCameraH); // On parse en objet à la fois
                    levelLine = levelScanner.nextLine();
                }
            }
        }
        levelScanner.close();
    }

    /**
     * Permet de parser un objet et de le creer dans le monde
     * @param levelLine La ligne à parser
     * @param oCameraW Largeur abcienne caméra
     * @param oCameraH Hauteur ancienne caméra
     */
    public void parseObject(String levelLine, float oCameraW, float oCameraH) {
        String objectClass;
        float objectX;
        float objectY;
        float objectRotation;
        int parserInd = 0;
        StringBuilder parser = new StringBuilder("");
        char parserChar = levelLine.charAt(0);
        // Détermine le type de l'objet
        while (parserChar != ':') {
            parser.append(parserChar);
            parserInd++;
            parserChar = levelLine.charAt(parserInd);
        }
        objectClass = parser.toString();
        parserInd++;
        parserChar = levelLine.charAt(parserInd);
        parser.setLength(0);
        // Recherche position en abscisse de l'objet
        while (parserChar != '-') {
            parser.append(parserChar);
            parserInd++;
            parserChar = levelLine.charAt(parserInd);
        }
        objectX = Float.parseFloat(parser.toString());
        // Conversion de l'abscisse en fonction de l'ancienne camera
        if (CAMERAWIDTH < oCameraW) {
            objectX = objectX / (oCameraW / CAMERAWIDTH);
        }
        else {
            objectX = objectX / (CAMERAWIDTH / oCameraW);
        }
        parserInd++;
        parserChar = levelLine.charAt(parserInd);
        parser.setLength(0);
        // Recherche position ordonnee de l'objet
        while (parserChar != '-') {
            parser.append(parserChar);
            parserInd++;
            parserChar = levelLine.charAt(parserInd);
        }
        objectY = Float.parseFloat(parser.toString());
        // Conversion de l'ordonnee en fonction de l'ancienne camera
        if (CAMERAHEIGHT < oCameraH) {
            objectY = objectY / (oCameraH / CAMERAHEIGHT);
        }
        else {
            objectY = objectY / (CAMERAHEIGHT / oCameraH);
        }
        parserInd++;
        parserChar = levelLine.charAt(parserInd);
        parser.setLength(0);
        // Recherche angle de l'objet
        while (parserChar != ';') {
            parser.append(parserChar);
            parserInd++;
            parserChar = levelLine.charAt(parserInd);
        }
        objectRotation = Float.parseFloat(parser.toString());
        // On a fini de parser l'objet, on l'ajoute au monde
        this.monde.addObject(objectClass, objectX, objectY, objectRotation);
    }

    /**
     * Demande au monde de reset le niveau
     */
    public void resetLevel() {
        this.monde.resetLevel();
    }

    /**
     * Construit les données nécessaire à la sauvegarde du niveau puis sauvegarde ce dernier
     * @param newFile Si on doit sauvegarder un nouveau fichier ou en remplacer un
     * @param fileName Le nom du fichier
     */
    public void saveFile(boolean newFile, String fileName) {
        Pixmap screenshot = makeScreenshot();
        if (newFile) {
            this.fileChooser.saveNewFile(fileName, buildSaveFile(fileName), screenshot);
        }
        else {
            this.fileChooser.saveExistingFile(fileName, buildSaveFile(fileName), screenshot);
        }
        screenshot.dispose();
    }

    /**
     * Crée la capture d'écran
     * @return La capture d'écran
     */
    private Pixmap makeScreenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels((int)(CAMERAWIDTH/3.5f), 0, (int)(CAMERAWIDTH - (CAMERAWIDTH/3.5f)), (int)CAMERAHEIGHT,true);
        for (int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }
        Pixmap pixmap = new Pixmap((int)(CAMERAWIDTH - (CAMERAWIDTH/3.5f)), (int)CAMERAHEIGHT, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        return pixmap;
    }

    /**
     * Crée les données du niveau à sauvegarder
     * @param fileName Le nom du fichier
     * @return Les données crées
     */
    private String buildSaveFile(String fileName) {
        StringBuilder string = new StringBuilder();
        string.append(fileName + ":\n\n");
        string.append("(O_Camera)\n");
        string.append("CAMERAWIDTH: " + CAMERAWIDTH + ";\n");
        string.append("CAMERAHEIGHT: " + CAMERAHEIGHT + ";\n");
        string.append("EOB\n");
        string.append(this.monde.toString());
        return string.toString();
    }

    /**
     * Bascule l'application en mode chargement de niveau ou non
     * @param select
     */
    public void setEditorLoad(boolean select) {
        this.fileChooser.setSelected(select);
        if (select) { // Change le listener à celui qui correspond au mode actuel
            this.fileChooser.setListenerToSelector();
        }
        else {
            this.setListenerToEditor();
        }
    }

    /**
     * Set le listener de l'application à celui de l'éditeur
     */
    public void setListenerToEditor() {
        Gdx.input.setInputProcessor(this.editorListener);
    }

    /**
     * OUvre la fenetre de saisie (sous desktop) ou le clavier (sous android) afin que l'utilisateur entre le nom du niveau à enregistrer
     * @param newFile Détermine si on sauvegarde un nouveau niveau ou si on remplace un qui existe déjà
     */
    public void showLevelNameDialog(boolean newFile) {
        if (newFile) {
            this.levelNameInput.setMode(true); // Mode enregistrement nouveau niveau
            Gdx.input.getTextInput(this.levelNameInput, "Nom du niveau", "", "Nom du nouveau niveau : ");
        }
        else {
            this.levelNameInput.setMode(false); // Mode écrasement ancien niveau
            Gdx.input.getTextInput(this.levelNameInput, "Nom du niveau", "", "Nom du niveau à remplacer (si celui-ci n'existe pas, un nouveau niveau sera crée) : ");
        }
    }

    @Override
    public void render(float delta) {
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        this.sb.begin();
        if (!this.fileChooser.getSelected()) {
            this.sb.draw(TextureFactory.getInstance().getBackground(), 0, 0, WORLDWIDTH, WORLDHEIGHT);
            if (!this.monde.hasGravityEnabled()) {
                this.edPanel.draw(this.sb);
                for (EditorButton eb : this.boutons) {
                    eb.draw(this.sb);
                }
            } else {
                for (EditorButton eb : this.boutons) {
                    if (eb.getButtonValue() == 5) {
                        eb.draw(this.sb);
                    }
                }
            }
            this.monde.render(this.sb);
        }
        else {
            this.fileChooser.render(this.sb);
        }
        this.sb.end();
        /**this.sb.begin();
        debugRenderer.render(monde.getWorld(), camera.combined);
        debugRenderer.render(fileChooser.getWorld(), camera.combined);
        this.sb.end();*/
    }

    public void dispose() {
        this.sb.dispose();
    }

}