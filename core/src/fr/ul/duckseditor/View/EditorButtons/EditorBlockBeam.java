package fr.ul.duckseditor.View.EditorButtons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class EditorBlockBeam extends EditorButton {

    protected float width = CAMERAWIDTH/40f;
    protected float height = CAMERAHEIGHT/40f * 4;

    protected static float positionX = CAMERAWIDTH/6.5f;
    protected static float positionY = CAMERAHEIGHT/1.45f;

    public EditorBlockBeam(World world) {
        super();
        float vertices[] = new float[8];
        vertices[0] = positionX;
        vertices[1] = positionY;
        vertices[2] = positionX+width;
        vertices[3] = positionY;
        vertices[4] = positionX+width;
        vertices[5] = positionY+height;
        vertices[6] = positionX;
        vertices[7] = positionY+height;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getBeam(), positionX, positionY, this.width, this.height);
    }


}
