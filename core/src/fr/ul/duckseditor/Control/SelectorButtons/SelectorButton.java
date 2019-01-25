package fr.ul.duckseditor.Control.SelectorButtons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public abstract class SelectorButton {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected PolygonShape shape;
    protected Body body;
    protected int value = -1;

    public SelectorButton() {
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.shape = new PolygonShape();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.fixtureDef.isSensor = true;
    }

    /**
     * Retourne le body de l'objet
     * @return Le body de l'objet
     */
    public Body getBody() {
        return this.body;
    }

    /**
     * Retourne l'id unique du bouton
     * @return La valeur
     */
    public int getButtonValue() {
        return this.value;
    }

    /**
     * Set l'id unique du bouton
     * @param value la valeur à associer
     */
    public void setValue(int value) {
        this.value = value;
    }

    public abstract void draw(SpriteBatch sb);

}
