package es.lanyu.gdx.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class BotonInterruptor extends ImageButton {

    public BotonInterruptor(CustomSkin skin, String styleName) {
        super(skin.nuevoEstilo(ImageButtonStyle.class, styleName, skin.getMayorContraste(), null,
                skin.getMayorContraste(), null));
    }

    public BotonInterruptor(CustomSkin skin, String styleName, boolean estadoInicialPulsado) {
        this(skin, styleName);
        // Para que no se active el evento se desactiva momentaneamente
        setProgrammaticChangeEvents(false);
        setChecked(estadoInicialPulsado);
        // Y se vuelve a activar
        setProgrammaticChangeEvents(true);
    }

}
