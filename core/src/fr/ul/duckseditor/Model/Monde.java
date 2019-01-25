package fr.ul.duckseditor.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.duckseditor.Model.Blocks.Beam;
import fr.ul.duckseditor.Model.Blocks.Block;
import fr.ul.duckseditor.Model.Blocks.Square;
import fr.ul.duckseditor.Model.Characters.Enemy;
import fr.ul.duckseditor.Model.Characters.Hero;
import fr.ul.duckseditor.Model.Characters.Personnage;
import fr.ul.duckseditor.Model.Characters.Prisonner;
import fr.ul.duckseditor.Model.Scene.*;

import java.util.ArrayList;

public class Monde {

    private World world;
    private ArrayList<Bord> bords;
    private ArrayList<Personnage> characters;
    private ArrayList<Block> blocks;
    private boolean gravityEnabled;

    public Monde() {
        this.world = new World(new Vector2(0, 0), false);
        this.world.setVelocityThreshold(5.0f);
        this.bords = new ArrayList<Bord>(); // Les bords du monde
        this.bords.add(new Cote(true, this.world));
        this.bords.add(new Cote(false, this.world));
        this.bords.add(new Sol(this.world));
        this.bords.add(new Haut(this.world));
        this.characters = new ArrayList<Personnage>(); // Les personnages du monde
        this.blocks = new ArrayList<Block>(); // Les blocs du monde
        this.gravityEnabled = false;
    }

    /**
     * Détermine si un body donné en paramètre est un body du monde
     * @param b Le body
     * @return Si b est un body du monde
     */
    public boolean possedeObjetMonde(Body b) {
        boolean trouve = false;
        for (Personnage p: this.characters) {
            if (p.getBody().equals(b)) {
                trouve = true;
            }
        }
        for (Block bl: this.blocks) {
            if (bl.getBody().equals(b)) {
                trouve = true;
            }
        }
        return trouve;
    }

    /**
     * Ajoute un objet dans le monde directement grace a ses coordonees et a son angle
     * @param className Le type de l'objet
     * @param x L'abscisse de l'objet
     * @param y L'ordonnee de l'objet
     * @param rot L'angle de l'objet
     */
    public void addObject(String className, float x, float y, float rot) {
        Body itemBody;
        if (className.equals("Enemy") || className.equals("Prisonner")) {
            Personnage p;
            if (className.equals("Enemy")) {
                p = new Enemy(this.world);
            }
            else {
                p = new Prisonner(this.world);
            }
            p.setPositionInit(x, y);
            p.setRotationInit(rot);
            this.characters.add(p);
            itemBody = p.getBody();
        }
        else {
            Block b;
            if (className.equals("Beam")) {
                b = new Beam(this.world);
            }
            else {
                b = new Square(this.world);
            }
            b.setPositionInit(x, y);
            b.setRotationInit(rot);
            this.blocks.add(b);
            itemBody = b.getBody();
        }
        itemBody.setTransform(x, y, rot);
    }

    public void addEnemy() {
        this.characters.add(new Enemy(this.world));
    } // Ajouter un ennemi

    public void addPrisonner() { // Ajouter un Prisonnier
        this.characters.add(new Prisonner(this.world));
    }

    public void addSquare() { // Ajouter un Bloc
        this.blocks.add(new Square(this.world));
    }

    public void addBeam() { // Ajouter un Rectangle
        this.blocks.add(new Beam(this.world));
    }

    /**
     * Change l'état du monde entre l'état endormi qui est l'état où on peut exécuter des actions depuis l'éditeur et où
     * tous les objets du monde sont endormi et l'état actif qui est le mode jeu du monde avec tous les objets actif.
     */
    public void setState() { // Change l'état de la gravité du monde et de ses composants en conséquent
        if (this.gravityEnabled) { // Bascule depuis l'état actif vers l'état inactif
            this.world.setGravity(new Vector2(0, 0));
            for (Block b : this.blocks) {
                b.getBody().setAwake(false);
                b.getBody().setFixedRotation(true);
                b.getBody().setTransform(b.getPositionInitX(), b.getPositionInitY(), b.getRotationInit());
                b.toggleSensor();
            }
            for (Personnage p : this.characters) {
                p.getBody().setAwake(false);
                p.getBody().setFixedRotation(true);
                p.getBody().setTransform(p.getPositionInitX(), p.getPositionInitY(), p.getRotationInit());
                p.toggleSensor();
            }
            this.gravityEnabled = false;
        }
        else { // Bascule depuis l'état inactif vers l'état actif
            this.world.setGravity(new Vector2(0, -40.0f));
            for (Block b : this.blocks) {
                b.getBody().setAwake(true);
                b.getBody().setFixedRotation(false);
                b.toggleSensor();
            }
            for (Personnage p : this.characters) {
                p.getBody().setAwake(true);
                p.getBody().setFixedRotation(false);
                p.toggleSensor();
            }
            this.gravityEnabled = true;
        }
    }

    /**
     * Change la position d'un body dans le monde
     * @param body Le body dont la position doit changer
     */
    public void setPositionInit(Body body) {
        for (Block b : this.blocks) {
            if (b.getBody().equals(body)) {
                b.setPositionInit(body.getPosition().x, body.getPosition().y);
            }
        }
        for (Personnage p : this.characters) {
            if (p.getBody().equals(body)) {
                p.setPositionInit(body.getPosition().x, body.getPosition().y);
            }
        }
    }

    /**
     * Supprime un body du monde ainsi que l'objet qui lui est associé
     * @param body Le body à supprimer
     */
    public void deleteBody(Body body) {
        boolean del = false;
        Block btemp = null;
        Personnage ptemp = null;
        for (Block b : this.blocks) {
            if (b.getBody().equals(body)) {
                btemp = b;
                del = true;
            }
        }
        if (!del) { // Le body a supprimer n'est pas un bloc
            for (Personnage p : this.characters) {
                if (p.getBody().equals(body)) {
                    ptemp = p;
                }
            }
        }
        if (del) {
            this.blocks.remove(btemp);
        }
        else {
            this.characters.remove(ptemp);
        }
        this.world.destroyBody(body);
    }

    /**
     * Set la rotaion initiale d'un des objets du monde
     * @param body Le body du monde associé à l'objet du mon qui doit changer
     */
    public void setRotationInit(Body body) {
        for (Block b : this.blocks) {
            if (b.getBody().equals(body)) {
                b.setRotationInit(body.getAngle());
            }
        }
        for (Personnage p : this.characters) {
            if (p.getBody().equals(body)) {
                p.setRotationInit(body.getAngle());
            }
        }
    }

    /**
     * Retourne le monde physique
     * @return le monde physique
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Retourne l'état actuel du monde
     * @return l'état du monde
     */
    public boolean hasGravityEnabled() {
        return this.gravityEnabled;
    }

    /**
     * Supprime tous les objets du monde existant
     */
    public void resetLevel() {
        while(!this.characters.isEmpty()) {
            this.deleteBody(this.characters.get(0).getBody());
        }
        while(!this.blocks.isEmpty()) {
            this.deleteBody(this.blocks.get(0).getBody());
        }
    }

    public void render(SpriteBatch sb) {
        world.step(Gdx.graphics.getDeltaTime(), 3, 6);
        for (Block b : this.blocks) {
            b.draw(sb);
        }
        for (Personnage p : this.characters) {
            p.draw(sb);
        }
    }

    public String toString() {
        StringBuilder string = new StringBuilder("");
        string.append("(characters)\n");
        for (Personnage p: this.characters) {
            string.append(p.getClass().getSimpleName() + ":");
            string.append(" " + p.getPositionInitX() + " - " + p.getPositionInitY() + " - " + p.getRotationInit() + ";\n");
        }
        string.append("EOB\n");
        string.append("(blocks)\n");
        for (Block b: this.blocks) {
            string.append(b.getClass().getSimpleName() + ":");
            string.append(" " + b.getPositionInitX() + " - " + b.getPositionInitY() + " - " + b.getRotationInit() + ";\n");
        }
        string.append("EOB\n");
        return string.toString();
    }

}
