package es.lanyu.gdx.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;

public class IoUtilsLibGdx {

    public static InputStream crearInputStream(String ruta) throws FileNotFoundException {
        return Gdx.files.internal(ruta).read();
    }

    public static OutputStream crearOutputStream(String ruta) throws FileNotFoundException {
        return Gdx.files.local(ruta).write(false);
    }

}
