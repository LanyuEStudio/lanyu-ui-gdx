package es.lanyu.gdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.lanyu.gdx.app.AbstractJuego;
import es.lanyu.gdx.app.EstadoApp;
import es.lanyu.gdx.app.Pantalla;

public class PantallaCarga implements Pantalla {
    private CustomSkin skin;
    private Stage stage;

    public PantallaCarga(AbstractJuego juego) {
        super();
        create(juego);
    }

    public void create(AbstractJuego juego) {
        stage = new Stage(new ScreenViewport());
        skin = juego.getSkin();

        getStage().addActor(new TablaCarga(juego));

        ShaderProgram sProgram = DistanceFieldFont.createDistanceFieldShader();
        getStage().getBatch().setShader(sProgram);
    }

    @Override
    public void render(float delta) {
        Color colorFondo = skin.getColorFondo();
        Gdx.gl.glClearColor(colorFondo.r, colorFondo.g, colorFondo.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        getStage().act(delta);
        getStage().draw();
    }

    public void dispose() {
        getStage().dispose();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setEstado(EstadoApp estado) {
        // TODO Auto-generated method stub
    }

}
