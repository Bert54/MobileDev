package fr.ul.duckseditor.Model.Scene;

import com.badlogic.gdx.physics.box2d.World;
import static fr.ul.duckseditor.View.EditorScreen.*;

public class Haut extends Bord {

    public Haut(World world) {
        super();
        this.width = WORLDWIDTH;
        this.height = UMHEIGHT / 3f;
        this.bodyDef.position.set(0, (CAMERAHEIGHT + (WORLDHEIGHT- CAMERAHEIGHT)) - this.height);
        float vertices[] = new float[8];
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = WORLDWIDTH;
        vertices[3] = 0;
        vertices[4] = WORLDWIDTH;
        vertices[5] = UMHEIGHT;
        vertices[6] = 0;
        vertices[7] = UMHEIGHT;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

}
