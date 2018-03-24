package graycygnus.mygraphics;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Anai on 08/03/2016.
 */
public class Casa {

    private Pared pared;
    private Techo techo;
    //ventana
    private Ventana ventana;
    //negro
    private Negro negro;
    private boolean doorOpen = false;

    public Casa(){
        pared = new Pared();
        techo = new Techo();
        ventana = new Ventana();
        negro = new Negro();
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl - The GL Context
     */
    public void draw(GL10 gl, boolean wireframe) {
        pared.draw(gl, wireframe);
        ventana.draw(gl, wireframe);
        techo.draw(gl, wireframe);
        negro.draw(gl, wireframe);
    }

    /**
     * Load the textures
     *
     * @param gl - The GL Context
     * @param context - The Activity context
     */
    public void loadGLTexture(GL10 gl, Context context) {
        //Cargar todas las texturas de la casa
        pared.loadGLTexture(gl, context);
        techo.loadGLTexture(gl, context);
        ventana.loadGLTexture(gl, context);
        negro.loadGLTexture(gl, context);
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }
}
