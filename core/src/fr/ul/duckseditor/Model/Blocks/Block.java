package fr.ul.duckseditor.Model.Blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static fr.ul.duckseditor.View.EditorScreen.*;

public abstract class Block {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected PolygonShape shape;
    protected Body body;
    protected float positionInitX;
    protected float positionInitY;
    protected float rotationInit;
    protected float width;
    protected float height;

    public Block() {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(WORLDWIDTH/2f, WORLDHEIGHT/2f);
        this.positionInitX = WORLDWIDTH/2f;
        this.positionInitY = WORLDHEIGHT/2f;
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = 1f;
        this.fixtureDef.restitution = 0.3f;
        this.shape = new PolygonShape();
        this.fixtureDef.isSensor = true;
    }

    /**
     * Retourne le body de cet objet
     * @return Le body de l'objet
     */
    public Body getBody() {
        return this.body;
    }

    /**
     * Set l'angle initial de l'objet
     * @param rotation La rotation initiale
     */
    public void setRotationInit(float rotation) {
        this.rotationInit = rotation;
    }

    /**
     * Set les coordonnées initiales de l'objet
     * @param x La position initiale X de l'objet
     * @param y La position initiale Y de l'objet
     */
    public void setPositionInit(float x, float y) {
        this.positionInitX = x;
        this.positionInitY = y;
    }

    /**
     * Retourne la rotation initiale d l'objet
     * @return La rotation initiale
     */
    public float getRotationInit() {
        return this.rotationInit;
    }

    /**
     * Retourne l'abscisse initiale d l'objet
     * @return L'abscisse initiale
     */
    public float getPositionInitX() {
        return this.positionInitX;
    }

    /**
     * Retourne l'ordonnée initiale d l'objet
     * @return L'ordonnée initiale
     */
    public float getPositionInitY() {
        return this.positionInitY;
    }

    /**
     * Active ou désactive les colisions de l'objet
     */
    public void toggleSensor() {
        Array<Fixture> fixtures = this.body.getFixtureList();
        for (Fixture fixture : fixtures) {
            if (fixture.isSensor()) {
                fixture.setSensor(false);
            } else {
                fixture.setSensor(true);
            }
        }
    }

    public abstract void draw(SpriteBatch sb);

}
