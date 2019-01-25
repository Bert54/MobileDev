package fr.ul.duckseditor.Model.Characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.duckseditor.DataFactory.TextureFactory;

import static fr.ul.duckseditor.View.EditorScreen.WORLDHEIGHT;
import static fr.ul.duckseditor.View.EditorScreen.WORLDWIDTH;

public class Hero extends Personnage {

    public Hero(World world) {
        super();
        this.body = world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        if (world.getGravity().x == 0f && world.getGravity().y == 0f) {
            this.body.setAwake(false);
        }
        this.shape.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getDuck(), this.body.getPosition().x - this.radius/2f, this.body.getPosition().y - this.radius / 2f,this.radius/2f, this.radius/2f, this.radius, this.radius, 1, 1, this.body.getAngle() * MathUtils.radiansToDegrees, 0, 0, 207, 208, false, false);
    }
}
