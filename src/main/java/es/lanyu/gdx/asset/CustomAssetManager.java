package es.lanyu.gdx.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.I18NBundle;

public class CustomAssetManager extends AssetManager {

    public static AssetDescriptor<Texture> getDescriptorTextura(String ruta) {
        TextureParameter param = new TextureParameter();
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        param.genMipMaps = true;

        return new AssetDescriptor<Texture>(ruta, Texture.class, param);
    }

    public CustomAssetManager() {
        this(new InternalFileHandleResolver());
    }

    public CustomAssetManager(FileHandleResolver resolver) {
        super(resolver, false);
        setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
        setLoader(Music.class, new MusicLoader(resolver));
        setLoader(Pixmap.class, new PixmapLoader(resolver));
        setLoader(Sound.class, new SoundLoader(resolver));
//        setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        setLoader(Texture.class, new TextureLoader(resolver));
//        setLoader(Skin.class, new SkinLoader(resolver));
//        setLoader(ParticleEffect.class, new ParticleEffectLoader(resolver));
//        setLoader(com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class,
//                new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver));
//        setLoader(PolygonRegion.class, new PolygonRegionLoader(resolver));
        setLoader(I18NBundle.class, new I18NBundleLoader(resolver));
//        setLoader(Model.class, ".g3dj", new G3dModelLoader(new JsonReader(), resolver));
//        setLoader(Model.class, ".g3db", new G3dModelLoader(new UBJsonReader(), resolver));
//        setLoader(Model.class, ".obj", new ObjLoader(resolver));
//        setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
    }

    @Override
    public synchronized boolean update() {
        return super.update() ? cargaFinalizada() : false;
    }

    // Usado para notificar al finalizar la carga
    protected boolean cargaFinalizada() {
        return true;
    }

}
