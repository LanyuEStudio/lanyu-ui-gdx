package es.lanyu.gdx.gui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import es.lanyu.commons.graficos.Dibujable;
import es.lanyu.gdx.asset.CustomAssetManager;

public interface DibujableLibGDX extends Dibujable<Animacion> {

    default AssetDescriptor<Texture> getDescriptorTextura() {
        return CustomAssetManager.getDescriptorTextura(getSprite().getRutaArchivo());
    }

}
