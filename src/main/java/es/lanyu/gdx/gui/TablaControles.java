package es.lanyu.gdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Value.Fixed;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import es.lanyu.audio.AudioConfigurable;

public class TablaControles extends Table {
    static Fixed padding = new Value.Fixed(10);
    private ImageButton ibtnSonido, ibtnMusica;

    public TablaControles(CustomSkin skin, AudioConfigurable configuracion) {
        super();

        // Agrego los Drawables que me haran falta para los botones
        Texture t = new Texture(Gdx.files.internal("data/graficos/Iconos.png"));
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int lado = 100;
        Sprite imgSonido = new Sprite(t, 0, lado, lado, lado);
        Sprite imgMusica = new Sprite(t, lado, lado, lado, lado);
        Sprite imgInfo = new Sprite(t, 2 * lado, lado, lado, lado);
        imgSonido.setSize(50, 50);
        imgMusica.setSize(50, 50);
        imgInfo.setSize(50, 50);
        skin.add("sonido", imgSonido);
        skin.add("musica", imgMusica);
        skin.add("info", imgInfo);

        // +BOTONES
        ibtnSonido = new BotonInterruptor(skin, "sonido", !configuracion.isSonidoActivado());
        ibtnSonido.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent arg0, Actor arg1) {
                configuracion.alternarSonidoActivado();
            }
        });

        ibtnMusica = new BotonInterruptor(skin, "musica", !configuracion.isMusicaActivada());
        ibtnMusica.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent arg0, Actor arg1) {
                configuracion.alternarMusicaActivada();
            }
        });

        establecerElementos();
    }

    // Sobrescribir para repartir los elementos de otra forma
    protected void establecerElementos() {
        // TABLA CONTROLES JUEGO
        // Estilo por defecto
        setFillParent(true);
        pad(padding).top().left();
        defaults().spaceRight(padding).spaceBottom(padding).left();

        // +Organizacion
        // Primera fila
        add(ibtnSonido);
        add(ibtnMusica);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (Gdx.input.isKeyJustPressed(Keys.SLASH))
            ibtnSonido.toggle();
        if (Gdx.input.isKeyJustPressed(Keys.STAR))
            ibtnMusica.toggle();
    }

}
