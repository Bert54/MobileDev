package fr.ul.duckseditor.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.ul.duckseditor.DataFactory.TextureFactory;

import static fr.ul.duckseditor.View.EditorScreen.*;

public class EditorPanel{

    protected float width = CAMERAWIDTH/3.5f;
    protected float height = CAMERAHEIGHT;

    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getInstance().getEditPanel(), 0, 0, this.width, this.height);
    }

}
