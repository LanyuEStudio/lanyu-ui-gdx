package es.lanyu.gdx.gui;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EtiquetaLenta extends Label {
    CharSequence textoParaDesplegar;
    int letrasEscritas;
    float velocidad = .015f;
    float deltaTotal;
    List<Integer> posicionesAbrir = new LinkedList<Integer>();
    List<Integer> posicionesCerrar = new LinkedList<Integer>();
    int siguienteAbrir;
    int siguienteCerrar;
    int pos = 0;
    boolean finalizado;

    public int getLetrasEscritas() {
        return letrasEscritas;
    }

    public void setLetrasEscritas(int letrasEscritas) {
        if (letrasEscritas >= textoParaDesplegar.length()) {
            letrasEscritas = textoParaDesplegar.length();
            finalizado = true;
        }

        this.letrasEscritas = letrasEscritas;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public float getTiempoParaFinalizar() {
        return textoParaDesplegar.length() * velocidad;
    }

    public EtiquetaLenta(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
        setText(text);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isFinalizado()) {
            deltaTotal += delta / velocidad;
            int posicionActual = (int) deltaTotal;
            if (posicionActual > siguienteAbrir) {
                deltaTotal += siguienteCerrar - siguienteAbrir;
                actualizarSiguientes();
            }
            setLetrasEscritas((int) deltaTotal);
            super.setText(textoParaDesplegar.subSequence(0, getLetrasEscritas()));
        }
    }

    private void evaluarCorchetes() {
        String texto = textoParaDesplegar.toString();
        rellenarPosiciones(posicionesAbrir, '[', texto);
        rellenarPosiciones(posicionesCerrar, ']', texto);
        actualizarSiguientes();
    }

    private void rellenarPosiciones(List<Integer> lista, char caracter, String texto) {
        int posicion = -1;
        do {
            posicion = texto.indexOf(caracter, posicion + 1);
            if (posicion != -1) {
                lista.add(posicion);
            } else {
                break;
            }
        } while (posicion < texto.length());
    }

    private void actualizarSiguientes() {
        siguienteAbrir = posicionesAbrir.get(pos);
        siguienteCerrar = posicionesCerrar.get(pos);
        pos++;
        if (!(pos < posicionesAbrir.size())) {
            pos = posicionesAbrir.size() - 1;
        }
    }

    @Override
    public void setText(CharSequence newText) {
        textoParaDesplegar = newText;
        setLetrasEscritas(0);
        evaluarCorchetes();
        finalizado = false;
    }

}
