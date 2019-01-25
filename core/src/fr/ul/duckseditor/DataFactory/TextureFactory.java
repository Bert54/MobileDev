package fr.ul.duckseditor.DataFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureFactory {

    private static TextureFactory instance = new TextureFactory();

    public static TextureFactory getInstance() {
        return instance;
    }

    private static final String relativePath = "images/";
    private static Texture background = new Texture(Gdx.files.internal(relativePath + "background.png"));
    private static Texture beam = new Texture(Gdx.files.internal(relativePath + "beam.png"));
    private static Texture block = new Texture(Gdx.files.internal(relativePath + "block.png"));
    private static Texture cancel = new Texture(Gdx.files.internal(relativePath + "cancel.png"));
    private static Texture editPanel = new Texture(Gdx.files.internal(relativePath + "editPanel.png"));
    private static Texture leftArrow = new Texture(Gdx.files.internal(relativePath + "leftarrow.png"));
    private static Texture load = new Texture(Gdx.files.internal(relativePath + "Load.png"));
    private static Texture play = new Texture(Gdx.files.internal(relativePath + "Play.png"));
    private static Texture rewrite = new Texture(Gdx.files.internal(relativePath + "Rewrite.png"));
    private static Texture rightArrow = new Texture(Gdx.files.internal(relativePath + "rightarrow.png"));
    private static Texture save = new Texture(Gdx.files.internal(relativePath + "Save.png"));
    private static Texture stop = new Texture(Gdx.files.internal(relativePath + "Stop.png"));
    private static Texture targetBeige = new Texture(Gdx.files.internal(relativePath + "targetbeige.png"));
    private static Texture targetBlue = new Texture(Gdx.files.internal(relativePath + "targetblue.png"));
    private static Texture trash = new Texture(Gdx.files.internal(relativePath + "Trash.png"));
    private static Texture duck = new Texture(Gdx.files.internal(relativePath + "duck.png"));
    private static Texture circularArrow = new Texture(Gdx.files.internal(relativePath + "circular_arrow.png"));

    public Texture getBackground() {
        return this.background;
    }

    public Texture getBeam() {
        return this.beam;
    }

    public Texture getBlock() {
        return this.block;
    }

    public Texture getCancel() {
        return this.cancel;
    }

    public Texture getEditPanel() {
        return this.editPanel;
    }

    public Texture getLeftArrow() {
        return this.leftArrow;
    }

    public Texture getLoad() {
        return this.load;
    }

    public Texture getPlay() {
        return this.play;
    }

    public Texture getRewrite() {
        return this.rewrite;
    }

    public Texture getRightArrow() {
        return this.rightArrow;
    }

    public Texture getSave() {
        return this.save;
    }

    public Texture getStop() {
        return this.stop;
    }

    public Texture getTargetBeige() {
        return this.targetBeige;
    }

    public Texture getTargetBlue() {
        return this.targetBlue;
    }

    public Texture getTrash() {
        return this.trash;
    }

    public Texture getDuck() {
        return this.duck;
    }

    public Texture getCircularArrow() {
        return this.circularArrow;
    }
}
