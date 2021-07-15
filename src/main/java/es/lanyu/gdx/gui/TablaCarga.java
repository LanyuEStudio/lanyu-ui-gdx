package es.lanyu.gdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import es.lanyu.gdx.app.AbstractJuego;

public class TablaCarga extends Table {
    private AbstractJuego juego;
    ProgressBar barraProgreso;

    public TablaCarga(AbstractJuego juego) {
        super();
        this.juego = juego;
        float ancho = Gdx.graphics.getWidth() * .4f;
        float alto = Gdx.graphics.getHeight() * .02f;

        setFillParent(true);
        defaults().center();

        CustomSkin skin = juego.getSkin();
        Label lblCargando = new Label(
                juego.getAssetManager().get("data/i18n/menu", I18NBundle.class).format("cargando").toUpperCase(), skin);
        lblCargando.setAlignment(Align.center);
        add(lblCargando);
        row();
        barraProgreso = new ProgressBar(0, 1, .1f, false, skin) {
            @Override
            public float getPrefWidth() {
                return ancho;
            }
        };
        barraProgreso.getStyle().knob = null;
        barraProgreso.getStyle().background.setMinHeight(alto);
        barraProgreso.getStyle().knobBefore.setMinHeight(alto);
        add(barraProgreso);

    }

    @Override
    public void act(float arg0) {
        super.act(arg0);
        barraProgreso.setValue(juego.getAssetManager().getProgress());
    }

}
