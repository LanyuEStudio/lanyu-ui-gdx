package es.lanyu.gdx.app;

import es.lanyu.audio.AudioConfigurable;
import es.lanyu.audio.ReproductorMusica;

public interface GestorAudio {

    AudioConfigurable getConfiguracionAudio();

    default void pausarSonido() {
//        ReproductorSonido.pausa();
    }

    default void pausarMusica() {
        ReproductorMusica.pausa();
    }

    default void pausarAudio() {
        pausarSonido();
        pausarMusica();
    }

    default void detenerSonido() {
        getConfiguracionAudio().setSonidoActivado(false);
    }

    default void detenerMusica() {
        ReproductorMusica.stop();
        getConfiguracionAudio().setMusicaActivada(false);
    }

    default void detenerAudio() {
        detenerSonido();
        detenerMusica();
    }

    default void reanudarSonido() {
        getConfiguracionAudio().setSonidoActivado(true);
//        ReproductorSonido.reanudar();
    }

    default void reanudarMusica() {
        getConfiguracionAudio().setMusicaActivada(true);
        ReproductorMusica.reanudar();
    }

    default void reanudarAudio() {
        reanudarSonido();
        reanudarMusica();
    }
}
