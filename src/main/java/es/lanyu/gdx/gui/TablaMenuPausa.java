package es.lanyu.gdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Value.Fixed;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import es.lanyu.audio.ConfiguracionAudio;
import es.lanyu.gdx.app.AbstractJuego;
import es.lanyu.gdx.asset.CustomAssetManager;

public class TablaMenuPausa extends Table {
    public CustomSkin skin;
    Fixed padding = new Value.Fixed(10);
    int anchoMenu = 240;
    String formatoEtiquetasSlider = "%s (%.0f)";

    public TablaMenuPausa(AbstractJuego juego) {
        AssetManager assetManager = juego.getAssetManager();
        skin = juego.getSkin();
        setSkin(skin);
        I18NBundle paqueteIdioma = assetManager.get("data/i18n/menu");

        String archivoLogo = juego.getConfiguracion().getPropiedades().getProperty("archivoLogo");
        AssetDescriptor<Texture> descriptorTexturaLogo = CustomAssetManager.getDescriptorTextura(archivoLogo);
        assetManager.load(descriptorTexturaLogo);

        String textoLabelSonido = paqueteIdioma.format("sonido");
        String textoLabelMusica = paqueteIdioma.format("musica");
        String textoReanudar = paqueteIdioma.format("reanudar").toUpperCase();
        String textoSalir = paqueteIdioma.format("salir").toUpperCase();

        // ETIQUETAS
        Label lblMenuPausa = new Label(paqueteIdioma.format("juego")
                + System.lineSeparator() + paqueteIdioma.format("menuPausa"), skin);
        Label lblSonido = new Label(textoLabelSonido, skin);
        Label lblMusica = new Label(textoLabelMusica, skin);
        lblMenuPausa.setColor(skin.getMayorContraste());
        lblSonido.setColor(skin.getMayorContraste());
        lblMusica.setColor(skin.getMayorContraste());
        lblMenuPausa.setAlignment(Align.center);
        lblSonido.setAlignment(Align.center);
        lblMusica.setAlignment(Align.center);

        ConfiguracionAudio cfgAudio = new ConfiguracionAudio(juego.getConfiguracion().getPropiedades());

        // +SLIDES
        Slider sldVolumenSonido = new Slider(0, 1f, .1f, false, skin);
        sldVolumenSonido.setValue(cfgAudio.getVolSonido());
        // Se establece el valor antes del listener para que no dispare el evento
        sldVolumenSonido.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent arg0, Actor arg1) {
                cfgAudio.setVolSonido(sldVolumenSonido.getValue());
                lblSonido.setText(
                        String.format(formatoEtiquetasSlider, textoLabelSonido, sldVolumenSonido.getValue() * 10));
            }
        });
        Slider sldVolumenMusica = new Slider(0, 1f, .1f, false, skin);
        sldVolumenMusica.setValue(cfgAudio.getVolMusica());
        sldVolumenMusica.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent arg0, Actor arg1) {
                cfgAudio.setVolMusica(sldVolumenMusica.getValue());
                lblMusica.setText(
                        String.format(formatoEtiquetasSlider, textoLabelMusica, sldVolumenMusica.getValue() * 10));
            }
        });

        // +BOTONES
        Button btnSalir = nuevoBotonMenuPausa(textoSalir);
        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Button btnReanudar = nuevoBotonMenuPausa(textoReanudar);
        btnReanudar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Sonido.reproducirSonido(Sonido.getSonido("Punetazo"));
                juego.resume();
            }
        });

        assetManager.finishLoadingAsset(descriptorTexturaLogo.fileName);
        Texture texLogo = assetManager.get(descriptorTexturaLogo);
        Image logo = new Image(texLogo);
        logo.setScaling(Scaling.fill);

        // TABLA MENU PAUSA
        setFillParent(true);
        // pad(padding).top();
        center();
        // Estilo por defecto
        defaults().width(anchoMenu).space(padding).spaceBottom(0).center();

        // +Organizacion
        // Controles de volumen
        add(logo);
        row();
        add(lblMenuPausa);
        row();
        add(lblSonido);
        row().spaceTop(0);
        add(sldVolumenSonido);
        row();
        add(lblMusica);
        row().spaceTop(0);
        add(sldVolumenMusica);
        row();
        // Tabla para agragar otros Slides
        Table tablaVacia = new Table(skin);
        tablaVacia.setName("TablaParaRellenar");
        add(tablaVacia).fill();
        // Botonera
        defaults().height(40);
        row();
        // TODO Implementar boton guardar
        // tablaMenuPausa.add(btnGuardar);
        // tablaMenuPausa.row();
        add(btnReanudar);
        row();
        add(btnSalir);

    }

    public Button nuevoBotonMenuPausa(String texto) {
        return new TextButton(texto, skin);
    }
}
