package fr.ul.duckseditor.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.Control.SelectorButtons.*;
import fr.ul.duckseditor.DataFactory.TextureFactory;
import fr.ul.duckseditor.View.EditorScreen;

import java.io.File;
import java.util.ArrayList;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class FileChooser {

    private String appFilesName = "MadDucksFiles";
    private EditorScreen editorScreen;
    private String saveFilesPath;
    private ArrayList<String> saveFiles;
    private int fileSelector;

    private ArrayList<SelectorButton> buttons;
    private Texture levelPicture;
    private boolean selected;
    private InputProcessor selectorListener;
    private World selectorWorld;

    public FileChooser(EditorScreen sc) {
        this.editorScreen = sc;
        this.saveFilesPath = Gdx.files.getExternalStoragePath() + this.appFilesName + "/";
        if (!Gdx.files.external(this.appFilesName + "/").exists()) {
            new File(this.saveFilesPath).mkdirs();
        }
        this.saveFiles = new ArrayList<String>();
        this.fileSelector = -1;
        this.updateFiles();
        if (!this.saveFiles.isEmpty()) {
            // Pour la texture, on doit passer par un FileHandle car la construction d'une texture par une String ne se fait que par chemin interne
            FileHandle pngPath = Gdx.files.external(this.appFilesName + "/" + this.saveFiles.get(0) + ".png");
            this.fileSelector = 0;
            this.levelPicture = new Texture(pngPath);
        }


        // On charge l'interface du chargeur de niveau
        this.buttons = new ArrayList<SelectorButton>();
        this.selected = false;
        this.selectorWorld = new World(new Vector2(0, 0), false);
        this.selectorListener = new SelectorListener(this, this.editorScreen.getCamera());
        this.buttons.add(new SelectorLeft(this.selectorWorld));
        this.buttons.add(new SelectorRight(this.selectorWorld));
        this.buttons.add(new SelectorConfirm(this.selectorWorld));
        this.buttons.add(new SelectorExit(this.selectorWorld));

        int i = 1;
        for (SelectorButton b : this.buttons) {
            b.setValue(i);
            i++;
        }


    }


    public void setSelected(boolean select) {
        this.selected = select;
    }

    public boolean getSelected() {
        return this.selected;
    }

    /**
     * Met à jour la liste des fichiers du répertoire courant
     */
    public void updateFiles() {
        FileHandle[] dirFiles = Gdx.files.external(this.appFilesName + "/").list();
        String fileName;
        for (FileHandle file: dirFiles) {
            fileName = file.name();
            if (fileName.endsWith(".mdl") || fileName.endsWith(".png")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                if (!this.saveFiles.contains(fileName)) {
                    this.saveFiles.add(fileName);
                }
            }
        }
    }

    /**
     * Sauvegarde un nouveau fichier
     * @param fileName nom du fichier
     * @param data Les données à écrire
     * @param screenshot La capture d'écran
     */
    public void saveNewFile(String fileName, String data, Pixmap screenshot) {
        if (this.saveFiles.contains(fileName)) {
            int i = 1;
            while (this.saveFiles.contains(fileName + i)) {
                i++;
            }
            fileName = fileName + i;
        }
        this.saveFile(fileName, data, screenshot);
        this.updateFiles();
        this.fileSelector = this.saveFiles.size() - 1;
        FileHandle pngPath = Gdx.files.external(this.appFilesName + "/" + this.saveFiles.get(fileSelector) + ".png");
        this.levelPicture = new Texture(pngPath);
    }

    /**
     * Sauvegarde par-dessus un fichier existant
     * @param fileName nom du fichier
     * @param data Les données à écrire
     * @param screenshot La capture d'écran
     */
    public void saveExistingFile(String fileName, String data, Pixmap screenshot) {
        if (!this.saveFiles.contains(fileName)) { // On sauvegarde un nouveau fichier puisque celui passé en paramète n'existe pas
            this.saveNewFile(fileName, data, screenshot);
        }
        else {
            this.saveFile(fileName, data, screenshot);
            FileHandle pngPath = Gdx.files.external(this.appFilesName + "/" + this.saveFiles.get(fileSelector) + ".png");
            this.levelPicture = new Texture(pngPath);
        }
    }

    /**
     * Méthode qui exécute la sauvegarde
     * @param fileName nom du fichier
     * @param data Les données à écrire
     * @param screenshot La capture d'écran
     */
    private void saveFile(String fileName, String data, Pixmap screenshot) {
        PixmapIO.writePNG(Gdx.files.external(this.appFilesName + "/" + fileName + ".png"), screenshot);
        FileHandle dataFile = Gdx.files.external(this.appFilesName + "/" + fileName + ".mdl");
        dataFile.writeString(data, false);
    }

    /**
     * Retourne le monde physique du chargeur de fichier
     * @return Le monde du chargeur de fichier
     */
    public World getWorld() {
        return this.selectorWorld;
    }

    /**
     * Change le listener de l'application à celui du chargeur du fichier
     */
    public void setListenerToSelector() {
        Gdx.input.setInputProcessor(this.selectorListener);
    }

    /**
     * Permet de déterminer si un body donné correspond à un des boutons du chargeur de niveau
     * @param bodyButton Le body qu'on doit comparer
     * @param oldValue L'ancienne valeur de retour
     * @return L'ID du bouton concerné (0 si il n'y en a pas)
     */
    public int hasButton(Body bodyButton, int oldValue) {
        int contains = 0;
        for (SelectorButton eb: this.buttons) {
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
     * Permet de désactiver le chargeur de niveau
     */
    public void hideSelector() {
        this.editorScreen.setEditorLoad(false);
    }

    /**
     * Change le niveau actif dans le chargeur de fichier
     * @param next Permet de savoir si on doit charger le prochain niveau ou le précédent
     */
    public void cycleLevel(boolean next) {
        if (!this.saveFiles.isEmpty()) {
            if (next) {
                if (this.fileSelector == this.saveFiles.size() - 1) {
                    this.fileSelector = 0;
                } else {
                    this.fileSelector++;
                }
            } else {
                if (this.fileSelector == 0) {
                    this.fileSelector = this.saveFiles.size() - 1;
                } else {
                    this.fileSelector--;
                }
            }
            // Pour la texture, on doit passer par un FileHandle car la construction d'une texture par une String ne se fait que par chemin interne
            FileHandle pngPath = Gdx.files.external(this.appFilesName + "/" + this.saveFiles.get(fileSelector) + ".png");
            this.levelPicture = new Texture(pngPath);
        }
    }

    /**
     * Commence le chargement du niveau en récupérant les données dans le .mdl
     */
    public void loadLevel() {
        if (this.fileSelector > -1) { // On vérifie qu'on a bien un niveau chargeable
            FileHandle level = Gdx.files.external(appFilesName + "/" + this.saveFiles.get(this.fileSelector) + ".mdl");
            String levelText = level.readString();
            this.editorScreen.loadSelectedFile(levelText);
            this.hideSelector();
        }
    }

    public void render(SpriteBatch sb) {
        this.selectorWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
        sb.draw(TextureFactory.getInstance().getEditPanel(), 0, 0, WORLDWIDTH, WORLDHEIGHT);
        for (SelectorButton b: this.buttons) {
            b.draw(sb);
        }
        if (this.fileSelector >= 0) {
            sb.draw(this.levelPicture, (CAMERAWIDTH-(this.levelPicture.getWidth()/2)) / 2f, (CAMERAHEIGHT-(this.levelPicture.getHeight()/2)) / 1.6f, this.levelPicture.getWidth()/2, this.levelPicture.getHeight()/2);
        }
    }

}
