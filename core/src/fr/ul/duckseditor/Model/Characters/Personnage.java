package fr.ul.duckseditor.Model.Characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static fr.ul.duckseditor.View.EditorScreen.*;

public abstract class Personnage {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected CircleShape shape;
    protected Body body;
    protected float positionInitX;
    protected float positionInitY;
    protected float rotationInit;
    protected float radius = Math.max(UMWIDTH, UMHEIGHT);

    public Personnage() {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(WORLDWIDTH/2f, WORLDHEIGHT/2f);
        this.positionInitX = WORLDWIDTH/2f;
        this.positionInitY = WORLDHEIGHT/2f;
        this.fixtureDef = new FixtureDef();
        this.shape = new CircleShape();
        this.shape.setRadius(this.radius / 2);
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.density = 0.8f;
        this.fixtureDef.restitution = 0.5f;
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
     * Retourne le diamètre de cet objet
     * @return Le diamètre de l'objet
     */
    public float getRadius() {
        return this.radius;
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
     * Retourne la rotation initiale d l'objet
     * @return La rotation initiale
     */
    public float getRotationInit() {
        return this.rotationInit;
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
