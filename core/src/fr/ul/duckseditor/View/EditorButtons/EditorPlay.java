package fr.ul.duckseditor.View.EditorButtons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;
import fr.ul.duckseditor.View.EditorButtons.EditorButton;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class EditorPlay extends EditorButton {

    protected static float size = Math.max(CAMERAWIDTH/40f, CAMERAHEIGHT/40f) * 3;
    protected static float positionX = CAMERAWIDTH/6.5f;
    protected static float positionY = CAMERAHEIGHT/1.15f;

    public EditorPlay(World world) {
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
        sb.draw(TextureFactory.getInstance().getPlay(), this.positionX, this.positionY, this.size, this.size);
    }

}
