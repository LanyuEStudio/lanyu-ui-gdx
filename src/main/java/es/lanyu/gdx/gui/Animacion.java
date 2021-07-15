package es.lanyu.gdx.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import es.lanyu.commons.graficos.Textureable;

public class Animacion implements Textureable<TextureRegion> {
    static final String RUTA_GRAFICOS = "data/graficos/";
    static AssetManager assetManager;
    public static float escalaX, escalaY;
    // TODO agregar varios Sprites para Animar. Puede hacerse con Interfaz
    transient TextureRegion textura;
    String archivo;

    public static void setAssetManager(AssetManager assetManager) {
        Animacion.assetManager = assetManager;
    }

    public TextureRegion getTextura() {
        if (textura == null && !getRutaArchivo().equals("")) {
            textura = new TextureRegion(assetManager.get(getRutaArchivo(), Texture.class));
        }

        return textura;
    }

    // Tengo que eliminarlo por camino AssetManager
    public void setTextura(String rutaArchivo) {
        this.textura = new TextureRegion(new Texture(Animacion.RUTA_GRAFICOS + rutaArchivo));
    }

    public void setTextura(TextureRegion textura) {
        this.textura = textura;
    }

    @Override
    public String getRutaArchivo() {
        return RUTA_GRAFICOS + archivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.archivo = rutaArchivo;
    }

    public Animacion() {
    }

    public Animacion(String rutaArchivo) {
        this();
        archivo = rutaArchivo;
        setTextura(new TextureRegion(new Texture(getRutaArchivo())));
    }

    public Animacion(TextureRegion textura) {
        super();
        this.textura = textura;
        // FileTextureData ftd = (FileTextureData)textura.getTexture().getTextureData();
        // archivo = ftd.getFileHandle().name();
        // System.out.println(archivo);
    }

}
