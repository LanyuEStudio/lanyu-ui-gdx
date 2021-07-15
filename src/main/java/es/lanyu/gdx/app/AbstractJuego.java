package es.lanyu.gdx.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter;
import com.badlogic.gdx.utils.I18NBundle;

import es.lanyu.audio.AudioConfigurable;
import es.lanyu.audio.ConfiguracionAudio;
import es.lanyu.audio.Musica;
import es.lanyu.audio.ReproductorMusica;
import es.lanyu.audio.Sonido;
import es.lanyu.commons.config.Configurable;
import es.lanyu.commons.config.ConfiguracionBase;
import es.lanyu.commons.config.Propiedades;
import es.lanyu.commons.identificable.GestorIdentificables;
import es.lanyu.gdx.asset.CustomAssetManager;
import es.lanyu.gdx.gui.CustomSkin;
import es.lanyu.gdx.gui.PantallaCarga;

public abstract class AbstractJuego extends ApplicationAdapter implements GestorAudio {
    private AssetManager assetManager;
    private CustomSkin skin;
    private GestorIdentificables gestorIdentificables;
    private Locale locale = Locale.getDefault();
    private Configurable configuracion;
    private AudioConfigurable cfgAudio;
    private EstadoApp estado;
    private Pantalla pantallaActual;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public CustomSkin getSkin() {
        return skin;
    }

    public void setSkin(CustomSkin skin) {
        this.skin = skin;
    }

    public GestorIdentificables getGestorIdentificables() {
        return gestorIdentificables;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Configurable getConfiguracion() {
        return configuracion;
    }

    @Override
    public AudioConfigurable getConfiguracionAudio() {
        return cfgAudio;
    }

    public EstadoApp getEstado() {
        return estado;
    }

    protected void setEstado(EstadoApp estado) {
        this.estado = estado;
    }

    public Pantalla getPantallaActual() {
        return pantallaActual;
    }

    public void setPantallaActual(Pantalla pantallaActual) {
        this.pantallaActual = pantallaActual;
    }

    public AbstractJuego(String[] args) {
        super();
    }

    @Override
    public void create() {

        cargarConfiguracion();

        gestorIdentificables = new GestorIdentificables();

        cargarAssetsBase();

        // En clase hija agregar en cargarAssetsBase():
//        setSkin(CustomSkin.COMIC);
        // Asegurarse de que todos los recursos esten accesibles:
        // Fuentes, Atlas, Texturas, etc...

        // @Override para cargar otros Assets especificos
        cargarAssetsEspecificos();

        // Establezco la Pantalla de Carga (Cargando...)
        setPantallaActual(new PantallaCarga(this));

        getConfiguracionAudio().actualizarVolumenes();
    }

    protected <T extends Configurable> T cargarConfiguracion(Propiedades propiedades, Class<T> tipoConfigurable) {
        T configuracion = null;
        try {
            configuracion = tipoConfigurable.getConstructor(Propiedades.class).newInstance(propiedades);
            cfgAudio = cargarAudio(configuracion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.configuracion = configuracion;

        return configuracion;
    }

    protected Configurable cargarConfiguracion() {
        configuracion = new ConfiguracionBase();
        cfgAudio = cargarAudio(getConfiguracion());

        return configuracion;
    }

    protected AudioConfigurable cargarAudio(Configurable configuracion) {
        AudioConfigurable cfgAudio = new ConfiguracionAudio(configuracion.getPropiedades());
        ReproductorMusica.setConfiguracion(cfgAudio);

        return cfgAudio;
    }

    protected AssetManager cargarAssetsBase() {
        // Creo un CustomAssetsManager con notificacion al finalizar la carga
        assetManager = new CustomAssetManager() {
            @Override
            protected boolean cargaFinalizada() {
                if (getEstado() == EstadoApp.CARGANDO)
                    setEstado(EstadoApp.CARGADO);

                return super.cargaFinalizada();
            }

            @Override
            public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
                System.out.println("Cargando " + fileName);
                super.load(fileName, type, parameter);
            }
        };

        // Establezco el AssetManager para el audio
        Musica.setAssetManager(getAssetManager());
        Sonido.setAssetManager(getAssetManager());

        // Obligo a cargar el paquete de idioma del menu para leer "Cargando..."
        I18NBundleParameter parametrosIdioma = new I18NBundleParameter(getLocale(), "UTF-8");
        getAssetManager().load("data/i18n/menu", I18NBundle.class, parametrosIdioma);
        getAssetManager().finishLoadingAsset("data/i18n/menu");

        return assetManager;
    }

    protected abstract void cargarAssetsEspecificos();

    protected abstract void arrancarContenidoJugable();
    // arrancarMotor();
    // empezarRonda();

    protected void setEntradaUsuario() {
        // Hay que establecer la pantalla especifica del juego antes agregar los inputs
//        setPantallaActual(new JuegoGUI(this));

        InputMultiplexer multiplexer = new InputMultiplexer();
        for (InputProcessor input : getInputs()) {
            multiplexer.addProcessor(input);
        }
        Gdx.input.setInputProcessor(multiplexer);

        // Si se presiona mayusculas izquierdo se pone en DEBUG
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            System.out.println("*** Modo Debug ***");
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Actualiza el mundo
        update(delta);

        // Pintado de la GUI
        getPantallaActual().render(delta);
    }

    // Usado para actualizar el mundo
    protected void update(float delta) {
        // Actualiza la carga de recursos
        if (!getAssetManager().update()) {
            // TODO Podria poner directamente una pantalla de carga si es CARGANDO
            setEstado(EstadoApp.CARGANDO);
        }

        // Acaba de terminar la carga de recursos
        if (getEstado() == EstadoApp.CARGADO) {
            // Establece los elementos necesarios para el contenido jugable
            arrancarContenidoJugable();
            // Se establece la entrada del usuario (Incluida pantalla)
            setEntradaUsuario();
            // Pasa a modo normal de juego
            resume();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (getPantallaActual() != null) {
            getPantallaActual().resize(width, height);
        }
    }

    @Override
    public void pause() {
        super.pause();
        setEstado(EstadoApp.PAUSA);

        if (getPantallaActual() != null) {
            getPantallaActual().setEstado(EstadoApp.PAUSA);
        }
//        pausarAudio();
    }

    @Override
    public void resume() {
        super.resume();
        setEstado(EstadoApp.CORRIENDO);

        if (getPantallaActual() != null) {
            getPantallaActual().setEstado(EstadoApp.CORRIENDO);
        }
//        reanudarAudio();
    }

    @Override
    public void dispose() {
        getConfiguracion().guardarConfiguracion();
        super.dispose();
    }

    public void salir() {
        Gdx.app.exit();
    }

    protected Collection<InputProcessor> getInputs() {
        Collection<InputProcessor> inputs = new ArrayList<>();
        inputs.add(getPantallaActual().getStage());
        inputs.add(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // Entrar y salir de la pausa
                if (keycode == Keys.ESCAPE) {
                    if (getEstado() != EstadoApp.PAUSA) {
                        pause();
                    } else {
                        resume();
                    }

                    return true;
                }

                if (getEstado() == EstadoApp.PAUSA) {
                    return true;
                }

                // TODO SI ESTOY EN PAUSA ACTIVAR DEMAS TECLAS

                return super.keyDown(keycode);
            }

        });

        return inputs;
    }

}