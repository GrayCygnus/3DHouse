package graycygnus.mygraphics;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Anai on 08/03/2016.
 */
public class Arbol {

    private Hojas hojas;
    private Tronco tronco;

    public Arbol(){
        hojas = new Hojas();
        tronco = new Tronco();
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl - The GL Context
     */
    public void draw(GL10 gl, boolean wireframe) {
        //Dibujar todos sus componentes en el orden adecuado
        tronco.draw(gl, wireframe);
        hojas.draw(gl, wireframe);
    }

    /**
     * Load the textures
     *
     * @param gl - The GL Context
     * @param context - The Activity context
     */
    public void loadGLTexture(GL10 gl, Context context) {
        //Cargar todas las texturas de la casa
        hojas.loadGLTexture(gl, context);
        tronco.loadGLTexture(gl, context);
    }

    /**
     * Funcion para Rotar el arbol
     * Gira en grados sobre los ejes correspondientes
     * Gira el Tronco y las Hojas
     *
     * */
    public void rotate(GL10 gl, Context context, float grados, float x, float y, float z){



    }
    /**
     * Funcion para Trasladar el arbol
     * Mover en nidades sobre los ejes correspondientes
     * Mueve el Tronco y las Hojas
     *
     * */
    public void translate(GL10 gl, Context context, float x, float y, float z){

        //PRUEBA
        //TRASLADAR un poquito a la derecha
        //LLamar Draw de Tronco y Hojas
        tronco.translate(gl, context, x, y, z);
        hojas.translate(gl, context, x, y, z);
        //hojas.translate();
        //tronco.draw(gl);

    }
    /**
     * Funcion para escalar el arbol
     * escalar en factor sobre los ejes correspondientes
     * escala el Tronco y las Hojas
     *
     * */
    public void scale(GL10 gl, Context context, float factor, float x, float y, float z){

    }

}
