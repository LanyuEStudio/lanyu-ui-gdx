package es.lanyu.gdx.utils;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import es.lanyu.commons.identificable.Nombrable;
import es.lanyu.gdx.gui.Animacion;
import es.lanyu.gdx.gui.DibujableLibGDX;
import es.lanyu.meta.Describible;
import es.lanyu.meta.Enlazable;

public class Utils {

    public static void addMensajeLog(Object origen, String mensaje) {
        String nombreClase = origen.getClass().getName();
        try {
            nombreClase = nombreClase.substring(nombreClase.lastIndexOf(".") + 1, nombreClase.lastIndexOf("$"));
        } catch (Exception e) {
            nombreClase = nombreClase.substring(nombreClase.lastIndexOf(".") + 1);
        }

        addMensajeLog(nombreClase, mensaje);
    }

    public static void addMensajeLog(String origen, String mensaje) {
        Gdx.app.log(origen, mensaje);
    }

    // nivel 1 sirve para acortar a la mitad la distancia hasta el blanco. Mayor mas blanco
    // nivel 0 se queda color original.
    // Tambien sirven valores negativos y decimales
    public static Color saturandoColor(Color color, float nivel) {
        double potencia = Math.pow(2, nivel);
        return new Color(
                (float)(1 + (color.r - 1) / potencia),
                (float)(1 + (color.g - 1) / potencia),
                (float)(1 + (color.b - 1) / potencia),
                1);
    }

    public static String leerDatosColor(Color color) {
        return String.format("hex:%s | r:%f, g:%f, b:%f, a:%f", color, color.r, color.g, color.b, color.a);
    }

    public static Image crearImagen(TextureRegion textura, boolean flip, Scaling escalado) {
        // Si hay que darle la vuelta se hace sobre una nueva textura por si se usa a la vez sin voltear
        if (flip) {
            textura = new TextureRegion(textura);
            textura.flip(flip, false);
        }
        Image imagen = new Image(textura);
        imagen.setScaling(escalado);

        return imagen;
    }

    public static <T extends Nombrable & DibujableLibGDX> void cargarImagenEnTabla(Table tabla, T dibujableConNombre,
            boolean flip, String nombreActor, Scaling escalado) {
        cargarImagenEnTabla(tabla, dibujableConNombre, flip, nombreActor, escalado);
    }

    public static <T extends Nombrable, K extends DibujableLibGDX> void cargarImagenEnTabla(Table tabla, T nombrable,
            K dibujable, boolean flip, String nombreActor, Scaling escalado) {
        Actor actorNuevo = null;
        Animacion animacion = dibujable.getSprite();
        if (animacion != null) {
            TextureRegion textura = animacion.getTextura();
            if (textura != null) {
                Image imagen = crearImagen(textura, flip, escalado);
                actorNuevo = imagen;
            } else {
                // paqueteIdioma.format("sinImagen", nombrable.getNombre(), dibujable.getSprite().getArchivo());
                String texto = "No se encuentra " + animacion.getRutaArchivo();
                Label noImagen = crearEtiqueta(texto, tabla.getSkin());
                noImagen.setColor(Color.RED);
                actorNuevo = noImagen;
            }
            actorNuevo.setName(nombreActor);
        }

        reemplazarActor(tabla, nombreActor, actorNuevo);
    }

    public static Label crearEtiqueta(String texto, Skin skin) {
        Label noImagen = new Label(texto, skin);
        noImagen.setAlignment(Align.right);
        noImagen.setWrap(true);

        return noImagen;
    }

    public static void reemplazarActor(Table tabla, String nombreActor, Actor actorNuevo) {
        Actor a = tabla.findActor(nombreActor);
        if (a != null) {
            tabla.getCell(a).setActor(actorNuevo);
        } else {
            tabla.add(actorNuevo);
        }
    }

    public static <T extends Nombrable, K extends DibujableLibGDX> void reemplazarRetratos(Table tabla, T nombrable,
            K dibujable, boolean flip, String nombre, Scaling escalado, float tiempoTransicion) {
        float tiempo = tiempoTransicion / 2;
        float distancia = tabla.getWidth() / 3;
        MoveToAction salir = moveTo(distancia * (flip ? -1 : 1), 0, tiempo);
        MoveToAction entrar = moveTo(0, 0, tiempo);
        salir.setInterpolation(Interpolation.fade);
        entrar.setInterpolation(Interpolation.fade);
        AlphaAction desvanecer = alpha(0, tiempo);
        AlphaAction aparecer = alpha(1, tiempo);
        desvanecer.setInterpolation(Interpolation.fade);
        aparecer.setInterpolation(Interpolation.fade);

        tabla.addAction(sequence(salir, entrar));
        tabla.addAction(sequence(
                desvanecer,
                run(() -> cargarImagenEnTabla(tabla, nombrable, dibujable, flip, nombre, escalado)),
                aparecer));
    }

    public static <T extends Describible & Enlazable> boolean describirYEnlazar(T objeto, Actor contenedor,
            RunnableAction accion, Skin skin) {
        boolean enlazado = false;
        if (objeto.getDescripcion() != null && !"".equals(objeto.getDescripcion())) {
            contenedor.addListener(new TextTooltip(objeto.getDescripcion(), skin));
            enlazado = true;
        }
        if (objeto.getEnlace() != null && !"".equals(objeto.getEnlace())) {
            accion.setRunnable(() -> Gdx.net.openURI(objeto.getEnlace()));
            enlazado = true;
        }

        return enlazado;
    }

    public static TextButton crearBotonMulticolor(String texto, TextButtonStyle estilo, float tiempoTransicion) {
        List<Color> coloresElegidos = Arrays.asList(
                new Color(Color.RED), new Color(Color.CYAN), new Color(Color.GREEN), new Color(Color.YELLOW));
        TextButton btnApoyo = new TextButton(texto, estilo) {
            boolean reset = false;
            float delay = 0;

            @Override
            public void act(float delta) {
                super.act(delta);
                delay += delta;
                if (delay >= tiempoTransicion) {
                    delay -= tiempoTransicion;
                    cambiarColorBoton(this, tiempoTransicion, coloresElegidos, reset);
                    reset = !reset;
                }
            }
        };

        return btnApoyo;
    }

    // reset sirve para ponerlo a blanco, sino se pasa a uno de los otros colores
    public static void cambiarColorBoton(Button boton, float tiempo, List<Color> coloresElegidos, boolean reset) {
        Color color = reset ? Color.WHITE : coloresElegidos.get((int) (Math.random() * coloresElegidos.size()));
        boton.addAction(Actions.color(color, tiempo));
    }
}
