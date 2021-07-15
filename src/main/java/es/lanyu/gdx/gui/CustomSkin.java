package es.lanyu.gdx.gui;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CustomSkin extends Skin {
    private String nombre;
    private Color colorFuenteOver, colorFuenteAbajo, colorFuenteDesactivado;
    protected Color colorPantalla;
    // TODO VER SI SE QUITA ESTO CALCULANDO DE ALGUNA FORMA
    // Establecer esta propiedad hace que se vea mejor el texto de marcado (tinte sobre blanco)
//    private Color colorTextoSalidas = Color.WHITE;
    private Cursor cursor;
    protected boolean conImagenFondo = true;
    private float tooltipWrap;

    public String getNombre() {
        return nombre;
    }

    public Color getColorPantalla() {
        return colorPantalla;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(String imagen, int posX, int posY) {
        this.cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("data/cursores/" + imagen)), posX, posY);
    }

    public boolean isConImagenFondo() {
        return conImagenFondo;
    }

    public void setTooltipWrap(float tooltipWrap) {
        this.tooltipWrap = tooltipWrap;
        nuevoEstilo(TextTooltipStyle.class, getColorFondo(), getColorFuente());
    }

    // TODO Eliminar esto y pasarlo a JSON
    private float getTooltipWrap() {
        return tooltipWrap;
    }

    public BitmapFont getFuentePorDefecto() {
        return optional("default-font", BitmapFont.class);
    }

    public Color getColorFuente() {
        BitmapFont fuente = getFuentePorDefecto();
        return (fuente != null) ? fuente.getColor() : null;
    }

    public void setColorFuente(Color color) {
        getFuentePorDefecto().setColor(color);
        actualizarColores();
    }

    public Color getColorFondo() {
        return optional("fondo", Color.class);// getColor("fondo");
    }

    public void setColorFondo(Color color) {
        Color colorFondo = getColorFondo();
        if (colorFondo != null) {
            colorFondo.set(color);
        } else {
            add("fondo", color);
        }
        // getColorFondo().set(color);
        actualizarColores();
    }

    void actualizarColores() {
        Color colorFuente = getColorFuente();
        Color colorFondo = getColorFondo();
        if (colorFuente != null && colorFondo != null) {
            nuevoEstilo(LabelStyle.class, "default", colorFuente, null);
            nuevoEstilo(TextButtonStyle.class, "default", colorFuente, colorFondo, colorFondo, colorFuente);
            nuevoEstilo(SelectBoxStyle.class, "default", colorFuente, colorFondo);
            nuevoEstilo(SliderStyle.class, "default-horizontal", colorFuente, colorFondo);
            nuevoEstilo(ProgressBarStyle.class, "default-horizontal", colorFuente, colorFondo);
            nuevoEstilo(TextTooltipStyle.class, "default", colorFondo, colorFuente);
            nuevoEstilo(CheckBoxStyle.class, "default", colorFuente, colorFondo);
            nuevoEstilo(TextFieldStyle.class, "default", colorFuente, colorFondo);
        }
    }

    public Drawable getFondoCustomizado(String nombreEstilo, Color color) {
        return newDrawable(nombreEstilo, color);
    }

    public Drawable getFondoCustomizado(Color color) {
        return getFondoCustomizado("default-round", color);
    }

    // Por defecto elige "default"
    protected <T> T nuevoEstilo(Class<T> clase, Color colorFrente, Color colorFondo) {
        return nuevoEstilo(clase, "default", colorFrente, colorFondo);
    }

    // Por defecto elige colorFrente para colorFuenteOver y colorFondo para colorFondoOver
    protected <T> T nuevoEstilo(Class<T> clase, String nombre, Color colorFrente, Color colorFondo) {
        return nuevoEstilo(clase, nombre, colorFrente, colorFondo, colorFrente, colorFondo);
    }

    protected <T> T nuevoEstilo(Class<T> clase, String nombre, Color colorFuente, Color colorFondo,
            Color colorFuenteOver, Color colorFondoOver) {
        T nuevoEstilo = optional(nombre, clase);
        if (nuevoEstilo == null) {
            String nombreEstilo = "default";
            if (clase != ImageButtonStyle.class) {// No hay boton de imagen por defecto
                nuevoEstilo = get(nombreEstilo, clase);
            }
        }

        try {
            if (nuevoEstilo != null) {
                nuevoEstilo = clase.getDeclaredConstructor(clase).newInstance(nuevoEstilo);
            } else {
                nuevoEstilo = clase.newInstance();
            }

            Color frente = new Color(colorFuente);
            Color fondo = (colorFondo != null) ? new Color(colorFondo) : null;
            Color frenteOver = new Color(colorFuenteOver);
            Color fondoOver = (colorFondoOver != null) ? new Color(colorFondoOver) : fondo;

            // LLAMAR METODO PARTICULAR SEGUN CLASE DE ESTILO
            modificarEstilo(nuevoEstilo, nombre, frente, fondo, frenteOver, fondoOver);

            add(nombre, nuevoEstilo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nuevoEstilo;
    }

    // TODO SE PUEDE PASAR AL JSON TAMBIEN
    private <T> T modificarEstilo(T nuevoEstilo, String nombre, Color frente, Color fondo,
            Color frenteOver, Color fondoOver) {
        if (nuevoEstilo instanceof LabelStyle) {
            LabelStyle estiloModificado = (LabelStyle) nuevoEstilo;
            estiloModificado.fontColor = frente;
            if (fondo != null) {
                estiloModificado.background = getFondoCustomizado(fondo);
            }
        }
        if (nuevoEstilo instanceof TextButtonStyle) {
            TextButtonStyle estiloModificado = (TextButtonStyle) nuevoEstilo;
            estiloModificado.fontColor = frente;
            estiloModificado.downFontColor = estiloModificado.overFontColor = frenteOver;
            estiloModificado.up = getFondoCustomizado(fondo);
            estiloModificado.down = estiloModificado.over = getFondoCustomizado(fondoOver);
            Color desactivado = new Color(fondo);
            desactivado.a = .4f;
            estiloModificado.disabled = getFondoCustomizado(desactivado);
            Color fuenteDesactivado = new Color(getMayorContraste());
            fuenteDesactivado.a = .5f;
            estiloModificado.disabledFontColor = fuenteDesactivado;
        }
        if (nuevoEstilo instanceof SelectBoxStyle) {
            SelectBoxStyle estiloModificado = (SelectBoxStyle) nuevoEstilo;
            estiloModificado.fontColor = estiloModificado.listStyle.fontColorUnselected = frente;
            estiloModificado.listStyle.fontColorSelected = fondo;
            estiloModificado.background = estiloModificado.backgroundOpen
                    = estiloModificado.listStyle.background
                    = getFondoCustomizado(fondo);
            estiloModificado.listStyle.selection = getFondoCustomizado(frente);
        }
        if (nuevoEstilo instanceof SliderStyle) {
            SliderStyle estiloModificado = (SliderStyle) nuevoEstilo;
            estiloModificado.knob = getFondoCustomizado("default-slider-knob", fondo);
            estiloModificado.knobDown = estiloModificado.knobOver
                    = estiloModificado.knobBefore
                    = getFondoCustomizado("default-slider-knob", frente);
            estiloModificado.knobBefore = getFondoCustomizado("default-slider", frente);
            estiloModificado.knobAfter = estiloModificado.background
                    = estiloModificado.disabledBackground
                    = getFondoCustomizado("default-slider", fondo);
        }
        if (nuevoEstilo instanceof ProgressBarStyle) {
            ProgressBarStyle estiloModificado = (ProgressBarStyle) nuevoEstilo;
            estiloModificado.knob = getFondoCustomizado("default-slider-knob", fondo);
            estiloModificado.knobBefore = getFondoCustomizado("default-slider-knob", frente);
            estiloModificado.knobBefore = getFondoCustomizado("default-slider", frente);
            estiloModificado.knobAfter = estiloModificado.background
                    = estiloModificado.disabledBackground
                    = getFondoCustomizado("default-slider", fondo);
        }
        if (nuevoEstilo instanceof TextTooltipStyle) {
            TextTooltipStyle estiloModificado = (TextTooltipStyle) nuevoEstilo;
            estiloModificado.label.fontColor = frente;
            estiloModificado.background = getFondoCustomizado(fondo);
            estiloModificado.wrapWidth = getTooltipWrap();
        }
        if (nuevoEstilo instanceof ImageButtonStyle) {
            ImageButtonStyle estiloModificado = (ImageButtonStyle) nuevoEstilo;
            estiloModificado.up = estiloModificado.over = estiloModificado.checkedOver = newDrawable(nombre, frente);
            Color desactivado = new Color(frente);
            desactivado.a = .4f;
            estiloModificado.down = estiloModificado.disabled
                    = estiloModificado.checked
                    = newDrawable(nombre, desactivado);
        }
        if (nuevoEstilo instanceof CheckBoxStyle) {
            CheckBoxStyle estiloModificado = (CheckBoxStyle) nuevoEstilo;
            estiloModificado.fontColor = estiloModificado.overFontColor = estiloModificado.downFontColor = fondo;
            estiloModificado.up = estiloModificado.over = estiloModificado.down = null;
            estiloModificado.checkboxOff = getFondoCustomizado("check-off", frente);
            estiloModificado.checkboxOn = getFondoCustomizado("check-on", frente);
            // estiloModificado.checkedOffsetX = 20;
        }
        if (nuevoEstilo instanceof TextFieldStyle) {
            TextFieldStyle estiloModificado = (TextFieldStyle) nuevoEstilo;
            estiloModificado.fontColor = frente;
            estiloModificado.focusedFontColor = fondo;
            estiloModificado.background = getFondoCustomizado(fondo);
            estiloModificado.focusedBackground = getFondoCustomizado(frente);
            estiloModificado.cursor = newDrawable(estiloModificado.cursor, fondo);
        }

        return nuevoEstilo;
    }

    public Color getColorFuenteOver() {
        return (colorFuenteOver == null) ? getColorFuenteAbajo() : colorFuenteOver;
    }

    public void setColorFuenteOver(Color colorFuenteOver) {
        this.colorFuenteOver = colorFuenteOver;
    }

    public Color getColorFuenteAbajo() {
        return (colorFuenteAbajo == null) ? getColorFondo() : colorFuenteAbajo;
    }

    public void setColorFuenteAbajo(Color colorFuenteAbajo) {
        this.colorFuenteAbajo = colorFuenteAbajo;
    }

    public Color getColorFuenteDesactivado() {
        return (colorFuenteDesactivado == null) ? getColorFondo() : colorFuenteDesactivado;
    }

    public void setFuente(BitmapFont fuente) {
        add("default-font", fuente, BitmapFont.class);
        actualizarFuente(fuente);
    }

    public CustomSkin(FileHandle skinFile, TextureAtlas atlas, BitmapFont fuentePorDefecto, String nombre) {
        super(skinFile, atlas);
        this.nombre = nombre;
        setColorFuente(getColor("black"));
        setColorFondo(getColor("white"));
        actualizarColores();
        setTooltipWrap(600);
        
        // Permite agregar nuevos estilos antes de establecerles la fuente
        crearEstilosPersonalizados();
        
        setFuente(fuentePorDefecto);
    }

    protected void crearEstilosPersonalizados() {}

    protected void actualizarFuente(BitmapFont fuente) {
        get("default", LabelStyle.class).font
                = get("default", TextButtonStyle.class).font
                = get("default", TextTooltipStyle.class).label.font
                = get("default", CheckBoxStyle.class).font
                = get("default", TextFieldStyle.class).font
                = fuente;
        SelectBoxStyle sBoxDefecto = get("default", SelectBoxStyle.class);
        sBoxDefecto.font = fuente;
        sBoxDefecto.listStyle.font = fuente;
    }

    // Metodos para comparar los coloes y elegir el mejor entre fuente y fondo
    public Color getMayorContraste() {
        if (getColorPantalla() == null) {
            return getColorMasFuerte();
        } else {
            float sumaPantalla = sumaValores(getColorPantalla());
            return (Math.abs(sumaValores(getColorFuente()) - sumaPantalla) < Math.abs(sumaValores(getColorFondo()) - sumaPantalla))
                    ? getColorFuente() : getColorFondo();
        }
    }

    public Color getColorMasDebil() {
        return (fuerzaDeColor.compare(getColorFuente(), getColorFondo()) * -1 < 0) ? getColorFuente() : getColorFondo();
    }

    public Color getColorMasFuerte() {
        return (fuerzaDeColor.compare(getColorFuente(), getColorFondo()) < 0) ? getColorFuente() : getColorFondo();
    }

    protected Float sumaValores(Color color) {
        return (color.r + color.g + color.b) * color.a;
    }

    Comparator<Color> fuerzaDeColor = new Comparator<Color>() {
        @Override
        public int compare(Color o1, Color o2) {
            return sumaValores(o1).compareTo(sumaValores(o2));
        }
    };

    @Override
    public String toString() {
        return nombre;
    }

    public LabelStyle nuevoEstiloEtiqueta(String nombre, Color colorFuente, Color colorFondo) {
        return nuevoEstilo(LabelStyle.class, nombre, colorFuente, colorFondo, colorFuente, colorFondo);
    }

    public ImageButtonStyle nuevoEstiloBotonImagen(String nombre, Color colorFrente) {
        return nuevoEstilo(ImageButtonStyle.class, nombre, colorFrente, null, colorFrente, null);
    }

}
