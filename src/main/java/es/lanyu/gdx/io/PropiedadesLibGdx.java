package es.lanyu.gdx.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import es.lanyu.commons.config.Propiedades;

public class PropiedadesLibGdx extends Propiedades {

    private static final long serialVersionUID = 6840862188350948127L;

    // Se sobreescribe para que la manipulacion de archivo se haga
    // con libgdx en vez de el nativo de Java
    @Override
    protected InputStream getInputStream(String ruta) {
        InputStream input = null;
        try {
            input = IoUtilsLibGdx.crearInputStream(ruta);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    protected OutputStream getOutputStream(String ruta) {
        OutputStream output = null;
        try {
            output = IoUtilsLibGdx.crearOutputStream(ruta);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    public PropiedadesLibGdx(String ruta) {
        cargarPropiedades(ruta);
    }

}
