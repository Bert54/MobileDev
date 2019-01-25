package fr.ul.duckseditor.View.EditorButtons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;
import fr.ul.duckseditor.View.EditorButtons.EditorButton;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class EditorCharacterPris extends EditorButton {

    protected float size = Math.max(CAMERAWIDTH/40f, CAMERAHEIGHT/40f) * 2;

    protected static float positionX = CAMERAWIDTH/20f;
    protected static float positionY = CAMERAHEIGHT/1.75f;

    public EditorCharacterPris(World world) {
        super();
        float vertices[] = new float[8];
        vertices[0] = positionX;
        vertices[1] = positionY;
        vertices[2] = positionX+size;
        vertices[3] = positionY;
        vertices[4] = positionX+size;
        vertices[5] = positionY+size;
        vertices[6] = positionX;
        vertices[7] = positionY+size;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getTargetBlue(), positionX, positionY, this.size, this.size);
    }
}
