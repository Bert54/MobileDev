package fr.ul.duckseditor.Model.Characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;

import static fr.ul.duckseditor.View.EditorScreen.WORLDHEIGHT;
import static fr.ul.duckseditor.View.EditorScreen.WORLDWIDTH;

public class Enemy extends Personnage {

    public Enemy(World world) {
        super();
        this.bodyDef.position.set(WORLDWIDTH/2f+3f, WORLDHEIGHT/2f+30f);
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.body.setAwake(false);
        this.body.setFixedRotation(true);
        this.shape.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getTargetBeige(), this.body.getPosition().x - this.radius/2f, this.body.getPosition().y - this.radius / 2f,this.radius/2f, this.radius/2f, this.radius, this.radius, 1, 1, this.body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 70, 70, false, false);
    }
}
