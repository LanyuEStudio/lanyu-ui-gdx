package es.lanyu.gdx.app;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface Pantalla {

    void render(float delta);

    void setEstado(EstadoApp estado);

    Stage getStage();

    default void resize(int width, int height) {
        getStage().getViewport().update(width, height, true);
    }

}
