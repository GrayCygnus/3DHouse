package graycygnus.mygraphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Anai on 03/03/2016.
 * Pantalla Principal
 */
public class Stage extends GLSurfaceView
{

    //Stage width
    private float w, h;
    //Screen width
    private int screenWidth, screenHeight;

    //Casa
    private Casa casa;
    //Arbol
    private Arbol arbol;
    //Puerta de la casa
    private Puerta puerta;
    //Suelo
    private Suelo suelo;
    //Mundo
    private Mundo mundo;
    //Cielo
    private Cielo cielo;

    //parametros de Vista
    private float xrot;
    private float yrot;
    private float zrot;
    private float scaleVista = 0.25f;

    //parametros de touch
    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    //Herramienta seleccionada
    private String selectedTool = "Vista";
    //Figura a editar
    private String selectedPolygon = "Casa";
    //Boolean Wire
    public boolean wireframe = false;

    //Variables de Estado de Arbol
    private float translateArbolX = 5.0f;
    private float translateArbolY;
    private float translateArbolZ;
    private float rotateArbolX;
    private float rotateArbolY;
    private float rotateArbolZ;
    private float shearArbolX;
    private float shearArbolY;
    private float shearArbolZ;
    private float scaleArbol = 1.0f;
    //Variables de Estado de Casa
    private float translateCasaX;
    private float translateCasaY;
    private float translateCasaZ;
    private float rotateCasaX;
    private float rotateCasaY;
    private float rotateCasaZ;
    private float shearCasaX;
    private float shearCasaY;
    private float shearCasaZ;
    private float scaleCasa = 1.0f;
    //Variables de Estado de Puerta
    private boolean animation = false;
    private float rotacionPuerta = 0.0f;
    private float translatePuertaX = 0.0f;
    private float translatePuertaY = -1.0f;
    private float translatePuertaZ = 1.0001f;


    public Stage(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        //CASA
        casa = new Casa();
        //ARBOL
        arbol = new Arbol();
        //PUERTA
        puerta = new Puerta();
        //SUELO
        suelo = new Suelo();
        //Mundo
        mundo = new Mundo();
        //Cielo
        cielo = new Cielo();

        xrot = 0.0f;
        yrot = 0.0f;

        //Setear R, G, B, A, Depth, Stencil
        //RGBA tendran 8 bits cada uno
        setEGLConfigChooser(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new MyRenderer());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        //Sportar maximo dos Punteros
        if(e.getPointerCount()<=2)
        {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mPreviousX = x;
                    mPreviousY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    mPreviousX = x;
                    mPreviousY = y;
                    break;
                case MotionEvent.ACTION_MOVE:

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

                    if(selectedTool.equals("Vista"))
                    {
                        //Debe haber dos punteros
                        if(e.getPointerCount()==2 && e.getHistorySize() > 1)
                        {
                            float distanciaInicial = (float) Math.sqrt((double)((e.getX(0)-e.getX(1))*(e.getX(0)-e.getX(1)) + (e.getY(0)-e.getY(1))*(e.getY(0)-e.getY(1))));
                            float distanciaFinal = (float) Math.sqrt((double)((e.getHistoricalX(0, 0)-e.getHistoricalX(1, 0))*(e.getHistoricalX(0, 0)-e.getHistoricalX(1, 0)) + (e.getHistoricalY(0, 0)-e.getHistoricalY(1, 0))*(e.getHistoricalY(0, 0)-e.getHistoricalY(1, 0))));


                            if(distanciaInicial > distanciaFinal)
                            {
                                scaleVista = scaleVista*(distanciaInicial/distanciaFinal)*1.02f;
                            }
                            else if(distanciaInicial < distanciaFinal)
                            {
                                scaleVista = scaleVista*(distanciaInicial/distanciaFinal)*0.98f;
                            }
                            else
                            {
                                scaleVista = scaleVista*(distanciaInicial/distanciaFinal);
                            }
                        }
                        else
                        {
                            xrot += dy * TOUCH_SCALE_FACTOR;
                            yrot += dx * TOUCH_SCALE_FACTOR;
                        }
                    }
                    else if(selectedTool.equals("Trasladar"))
                    {
                        //Usar x, y
                        if(selectedPolygon.equals("Casa"))
                        {
                            //casa
                            translateCasaX += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            translateCasaY -= dy*Math.cos((xrot * (Math.PI / 180.0)))* TOUCH_SCALE_FACTOR/16;
                            translateCasaZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;

                            //translateArbolX += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            //translateArbolY -= dy*Math.cos((xrot * (Math.PI / 180.0)))* TOUCH_SCALE_FACTOR/16;
                            //translateArbolZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                        }
                        else
                        {
                            //arbol
                            translateArbolX += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            translateArbolY -= dy*Math.cos((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            translateArbolZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                        }
                    }
                    else if(selectedTool.equals("Rotar"))
                    {
                        //Usar x, y
                        if(selectedPolygon.equals("Casa"))
                        {
                            //casa
                            rotateCasaY += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4;
                            rotateCasaX += dy*Math.cos((xrot * (Math.PI / 180.0)))* TOUCH_SCALE_FACTOR/4;
                            rotateCasaZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4;
                        }
                        else
                        {
                            //arbol
                            rotateArbolY += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4;
                            rotateArbolX += dy*Math.cos((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4;
                            rotateArbolZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/4;
                        }

                    }
                    else if(selectedTool.equals("Escalar"))
                    {
                        //Debe haber dos punteros exactamente
                        if(e.getPointerCount()==2 && e.getHistorySize() >1)
                        {
                            float distanciaInicial = (float) Math.sqrt((double)((e.getX(0)-e.getX(1))*(e.getX(0)-e.getX(1)) + (e.getY(0)-e.getY(1))*(e.getY(0)-e.getY(1))));
                            float distanciaFinal = (float) Math.sqrt((double)((e.getHistoricalX(0, 0)-e.getHistoricalX(1, 0))*(e.getHistoricalX(0, 0)-e.getHistoricalX(1, 0)) + (e.getHistoricalY(0, 0)-e.getHistoricalY(1, 0))*(e.getHistoricalY(0, 0)-e.getHistoricalY(1, 0))));

                            if(getSelectedPolygon().equals("Casa"))
                            {
                                if(distanciaInicial > distanciaFinal)
                                {
                                    scaleCasa = scaleCasa*(distanciaInicial/distanciaFinal)*1.1f;
                                }
                                else if(distanciaInicial < distanciaFinal)
                                {
                                    scaleCasa = scaleCasa*(distanciaInicial/distanciaFinal)*0.9f;
                                }
                                else
                                {
                                    scaleCasa = scaleCasa*(distanciaInicial/distanciaFinal);
                                }
                            }
                            else
                            {
                                if(distanciaInicial > distanciaFinal)
                                {
                                    scaleArbol = scaleArbol*(distanciaInicial/distanciaFinal)*1.1f;
                                }
                                else if(distanciaInicial < distanciaFinal)
                                {
                                    scaleArbol = scaleArbol*(distanciaInicial/distanciaFinal)*0.9f;
                                }
                                else
                                {
                                    scaleArbol = scaleArbol*(distanciaInicial/distanciaFinal);
                                }
                            }
                        }
                    }
                    else if(selectedTool.equals("Shearing"))
                    {
                        if(selectedPolygon.equals("Casa"))
                        {
                            //casa
                            shearCasaX += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            shearCasaY -= dy*Math.cos((xrot * (Math.PI / 180.0)))* TOUCH_SCALE_FACTOR/16;
                            shearCasaZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                        }
                        else
                        {
                            //arbol
                            shearArbolX += dx*Math.cos((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            shearArbolY -= dy*Math.cos((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                            shearArbolZ += dx*Math.sin((yrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16 + dy * Math.sin((xrot*(Math.PI/180.0)))* TOUCH_SCALE_FACTOR/16;
                        }
                    }
                    break;
                /*float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                    xRot = xRot + dx * TOUCH_SCALE_FACTOR;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                    yRot = yRot + dy * TOUCH_SCALE_FACTOR;
                }*/
            }
            mPreviousX = x;
            mPreviousY = y;
        }
        return true;
    }

    public String getSelectedTool() {
        return selectedTool;
    }

    public void setSelectedTool(String selectedTool) {
        this.selectedTool = selectedTool;
    }

    public String getSelectedPolygon() {
        return selectedPolygon;
    }

    public void setSelectedPolygon(String selectedPolygon) {
        this.selectedPolygon = selectedPolygon;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    private class MyRenderer implements GLSurfaceView.Renderer
    {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //Load the texture for the cube once during Surface creation

            //CARGAR TEXTURA DE TODOS

            casa.loadGLTexture(gl, getContext()); //carga sus texturas
            arbol.loadGLTexture(gl, getContext());
            puerta.loadGLTexture(gl, getContext());
            suelo.loadGLTexture(gl, getContext());
            mundo.loadGLTexture(gl, getContext());
            cielo.loadGLTexture(gl, getContext());


            gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
            gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading

            gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
            gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
            gl.glClearDepthf(1.0f); 					//Depth Buffer Setup

            //Really Nice Perspective Calculations
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //Setear fondo de pantalla a NEGRO (RGBA)

            gl.glClearColor(0, 0, 0, 1);
            //Verificar orientacion de pantalla
            //ver si esta vertical u horizontal para desplegar acordemente
            if(width > height) {
                h = 600;
                w = width * h / height;
            } else {
                w = 600;
                h = height * w / width;
            }
            screenWidth = width;
            screenHeight = height;
            //Setear la View de 0,0 a screenWidth,screenHight o sea su totalidad
            gl.glViewport(0, 0, screenWidth, screenHeight);
            //Setear Modo de Matriz
            //En este caso a modo PROJECTION
            //MODO PROJECTION
                //Para proyectar los puntos en la "camara"
            //MODO VIEW
                //Para transformar los puntos
            gl.glMatrixMode(GL10.GL_PROJECTION);
            //Cargar matriz de Identidad La que tiene el modelo
            //Reset antes de llamar la Proyeccion
            gl.glLoadIdentity();
            //Tipo de Matriz de Proyeccion
            //Otrogonoal, o sea sin Perspectiva!!!!!!
            //MOdificar para obtener otra forma de Perspectiva!!!!
            //Otra...
            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
            //gl.glOrthof(0, w, h, 0, -1, 1);
            //Modo View para transformar
            //Ya no se esta cambiando la camara
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //Cargar la matriz ya con la proyeccion

            gl.glLoadIdentity();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //Clear Screen And Depth Buffer
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();					//Reset The Current Modelview Matrix

            //Drawing
            gl.glTranslatef(0.0f, 0.0f, -5.0f);		//Move 5 units into the screen
            gl.glScalef(scaleVista, scaleVista, scaleVista); 			//Scale to 25 percent,

            //Rotate around the axis based on the rotation matrix (rotation, x, y, z)
            gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
            gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);    //Y
            gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z

            //Dibujar suelo y mundo
            suelo.draw(gl, false);
            mundo.draw(gl, false);
            cielo.draw(gl, false);
            /**
             * Bloque de Transformaciones de Arbol
             */
            gl.glPushMatrix();
            gl.glTranslatef(translateArbolX, translateArbolY, translateArbolZ);

            gl.glRotatef(rotateArbolX, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotateArbolY, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(rotateArbolZ, 0.0f, 0.0f, 1.0f);

            gl.glMultMatrixf(shearMatrix(shearArbolX, shearArbolY, shearArbolZ), 0);

            gl.glScalef(scaleArbol, scaleArbol, scaleArbol);

            arbol.draw(gl, wireframe);

            gl.glPopMatrix();
            gl.glPushMatrix();

            /**
             * Bloque de Transformaciones de Casa
             */
            gl.glTranslatef(translateCasaX, translateCasaY, translateCasaZ);

            gl.glRotatef(rotateCasaX, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotateCasaY, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(rotateCasaZ, 0.0f, 0.0f, 1.0f);

            gl.glMultMatrixf(shearMatrix(shearCasaX, shearCasaY, shearCasaZ), 0);

            gl.glScalef(scaleCasa, scaleCasa, scaleCasa);

            casa.draw(gl, wireframe);

            gl.glPopMatrix();

            /**
             * Bloque de Matrices de Puerta
             * */
            gl.glPushMatrix();

            gl.glTranslatef(translateCasaX/*-((1.0f)*(rotacionPuerta/90.0f))*/, translateCasaY/*+((1.5f)*(rotacionPuerta/90.0f))*/, translateCasaZ/*+((1.0f)*(rotacionPuerta/90.0f))*/);

            gl.glRotatef(rotateCasaX, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotateCasaY, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(rotateCasaZ, 0.0f, 0.0f, 1.0f);

            gl.glMultMatrixf(shearMatrix(shearCasaX, shearCasaY, shearCasaZ), 0);

            gl.glScalef(scaleCasa, scaleCasa, scaleCasa);

            //abrir o cerrar
            gl.glTranslatef(translatePuertaX, translatePuertaY, translatePuertaZ);
            gl.glRotatef(rotacionPuerta, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(-translatePuertaX, -translatePuertaY, -translatePuertaZ);

            puerta.draw(gl, wireframe);

            gl.glPopMatrix();

            if(isAnimation())
            {
                //animar
                if(casa.isDoorOpen())
                {
                    //cerrar
                    if(rotacionPuerta > 0.0f)
                    {
                        //trasladar
                        //gl.glRotatef(rotacionPuerta, 0.0f, 1.0f, 0.0f);
                        //puerta.draw(gl)
                        //puerta.draw(gl);
                        rotacionPuerta -= 1.0f;
                    }
                    else
                    {
                        casa.setDoorOpen(false);
                        setAnimation(false);
                    }
                }
                else
                {
                    //abrir
                    if(rotacionPuerta < 90.0f)
                    {
                        //Tralsadar?
                        //gl.glRotatef(rotacionPuerta, 0.0f, 1.0f, 0.0f);
                        //puerta.draw(gl)
                        //puerta.draw(gl);
                        rotacionPuerta += 1.0f;
                    }
                    else
                    {
                        casa.setDoorOpen(true);
                        setAnimation(false);
                    }
                }
            }

            /*---------------------------------------------*/
            //PRUEBAS
            /*---------------------------------------------*/
            //rotateArbolX += 0.5f;
            //rotateCasaY += 0.5f;
            //translateArbolX += 0.01f;
            //translateCasaY += 0.01f;
            //scaleArbol += 0.05f;
            //scaleCasa += 0.05f;
            //Change rotation factors (nice rotation)
            //xrot += 0.5f;
            //yrot += 0.5f;
            //zrot += 0.5f;
        }

        public float rgbTOfloat(int rgb)
        {
            return (float) ((float)rgb/255.0);
        }

        public float[] shearMatrix(float shearX, float shearY, float shearZ)
        {
            float matrix[] = {
                    1, shearY, shearZ, 0,
                    shearX, 1, shearZ, 0,
                    shearX, shearY, 1, 0,
                    0,0,0,1
            };
            return matrix;
        }

        //Triangle Fan
        /**
         * a    c       e
         *
         *    b     d        f
         */

        //Triangle Strip
        //Especial para Cilindros y Esferas
        /**
         *
         * a        i
         *                  h
         *                      g
         *                      f
         *      b   c   d   e
         */
    }
}
