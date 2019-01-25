package fr.ul.duckseditor.Model.Scene;

import com.badlogic.gdx.physics.box2d.*;

public abstract class Bord {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected PolygonShape shape;
    protected Body body;
    protected float width;
    protected float height;

    public Bord() {
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.shape = new PolygonShape();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
    }

    public Body getBody() {
        return this.body;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

}
