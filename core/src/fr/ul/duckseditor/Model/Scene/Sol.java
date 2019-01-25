package fr.ul.duckseditor.Model.Scene;

import com.badlogic.gdx.physics.box2d.World;
import static fr.ul.duckseditor.View.EditorScreen.*;

public class Sol extends Bord {

    public Sol(World world) {
        super();
        this.bodyDef.position.set(0, 0);
        float vertices[] = new float[8];
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = WORLDWIDTH;
        vertices[3] = 0;
        vertices[4] = WORLDWIDTH;
        vertices[5] = WORLDHEIGHT/6f;
        vertices[6] = 0;
        vertices[7] = WORLDHEIGHT/6f;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }


}
