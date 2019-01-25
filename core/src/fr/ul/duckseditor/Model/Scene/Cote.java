package fr.ul.duckseditor.Model.Scene;


import com.badlogic.gdx.physics.box2d.World;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class Cote extends Bord {

    public Cote(boolean gauche, World world) {
        super();
        float vertices[] = new float[8];
        this.width = UMWIDTH / 3f;
        this.height = WORLDHEIGHT;
        if (gauche) {
            this.bodyDef.position.set(0, 0);
        }
        else {
            this.bodyDef.position.set(CAMERAWIDTH + (WORLDWIDTH - CAMERAWIDTH) - this.width, 0);
        }
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = UMWIDTH;
        vertices[3] = 0;
        vertices[4] = UMWIDTH;
        vertices[5] = CAMERAHEIGHT;
        vertices[6] = 0;
        vertices[7] = CAMERAHEIGHT;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();

    }

}
