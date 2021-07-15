package es.lanyu.gdx.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;

public class Fuentes {

    private static final Collection<BitmapFont> FUENTES_DISPONIBLES;

    public static String getDirectorioFuentes() {
        return "data/fuentes/";
    }

    public static Collection<BitmapFont> getFuentes() {
        return FUENTES_DISPONIBLES;
    }

    public static BitmapFont getFuente(String nombre) {
        return FUENTES_DISPONIBLES.stream().filter(f -> nombre.equals(f.getData().name)).findFirst().orElseGet(null);
    }

    static {
        FUENTES_DISPONIBLES = new ArrayList<>();
        File dirFuentes = new File(getDirectorioFuentes());
        Stream.of(dirFuentes.listFiles()).map(f -> f.getName().substring(0, f.getName().lastIndexOf("."))).distinct()
                .forEach(f -> {
                    try {
                        FUENTES_DISPONIBLES.add(crearDistanceFieldFont(f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private static DistanceFieldFont crearDistanceFieldFont(String nombreArchivo) {
        DistanceFieldFont fuente = new DistanceFieldFont(
                Gdx.files.internal(getDirectorioFuentes() + nombreArchivo + ".fnt"),
                Gdx.files.internal(getDirectorioFuentes() + nombreArchivo + ".png"), false);
        // Activo el lenguaje de marcado para los colores esta fuente
        fuente.getData().markupEnabled = true;

        return fuente;
    }
}
