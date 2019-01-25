package fr.ul.duckseditor.Model.Blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;

import static fr.ul.duckseditor.View.EditorScreen.UMHEIGHT;
import static fr.ul.duckseditor.View.EditorScreen.UMWIDTH;

public class Beam extends Block {

        public Beam(World world) {
        super();
        this.width = Math.max(UMWIDTH,UMHEIGHT);
        this.height = Math.max(UMWIDTH,UMHEIGHT) * 4;
        float vertices[] = new float[8];
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = this.width;
        vertices[3] = 0;
        vertices[4] = this.width;
        vertices[5] = this.height;
        vertices[6] = 0;
        vertices[7] = this.height;
        this.shape.set(vertices);
        this.fixtureDef.shape = this.shape;
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.body.setAwake(false);
        this.body.setFixedRotation(true);
        this.shape.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getBeam(), this.body.getPosition().x, this.body.getPosition().y,0, 0, this.width, this.height, 1, 1, this.body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 70, 220, false, false);

    }
}
