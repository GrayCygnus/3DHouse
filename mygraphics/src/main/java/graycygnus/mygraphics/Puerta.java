package graycygnus.mygraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Anai on 08/03/2016.
 */
public class Puerta {

    /** The buffer holding the vertices */
    private FloatBuffer vertexBuffer;
    /** The buffer holding the texture coordinates */
    private FloatBuffer textureBuffer;
    /** The buffer holding the indices */
    private ByteBuffer indexBuffer;

    /** Our texture pointer */
    private int[] textures = new int[1];

    /**
     * The initial vertex definition
     *
     * Note that each face is defined, even
     * if indices are available, because
     * of the texturing we want to achieve
     */
    private float vertices[] = {
            //Vertices according to faces
            //Ventana
            //Frente
            -1.0f, -1.0f, 1.001f, //Vertex 0
            0.0f, -1.0f, 1.001f,  //v1
            -1.0f, 0.5f, 1.001f,  //v2
            0.0f, 0.5f, 1.001f,   //v3

            //Derecha
            0.0f, -1.0f, 1.001f,
            0.0f, -1.0f, 0.95f,
            0.0f, 0.5f, 1.001f,
            0.0f, 0.5f, 0.95f,

            //fondo
            0.0f, -1.0f, 0.95f, //Vertex 0
            -1.0f, -1.0f, 0.95f,  //v1
            0.0f, 0.5f, 0.95f,  //v2
            -1.0f, 0.5f, 0.95f,   //v3

            //izquierda
            -1.0f, -1.0f, 0.95f,
            -1.0f, -1.0f, 1.001f,
            -1.0f, 0.5f, 0.95f,
            -1.0f, 0.5f, 1.001f,

            //bottom
            -1.0f, -1.0f, 0.95f,
            0.0f, -1.0f, 0.95f,
            -1.0f, -1.0f, 1.001f,
            0.0f, -1.0f, 1.001f,

            //top
            -1.0f, 0.5f, 1.001f,
            0.0f, 0.5f, 1.001f,
            -1.0f, 0.5f, 0.95f,
            0.0f, 0.5f, 0.95f,
    };

    /** The initial texture coordinates (u, v) */
    private float texture[] = {
            //Mapping coordinates for the vertices

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    /** The initial indices definition */
    private byte indices[] = {
            //Faces definition
            0,1,2, 2,1,3,   //Face front
            4,5,6, 6,5,7,   //face left
            8,9,10, 10,9,11,  //face far
            12,13,14, 14,13,15, //face right
            16,17,18,18,17,19, //face bottom
            20,21,22,22,21,23,//face top
    };

    /**
     * The Cube constructor.
     *
     * Initiate the buffers.
     */
    public Puerta() {
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //
        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        //
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl - The GL Context
     */
    public void draw(GL10 gl, boolean wireframe) {
        //Bind our only previously generated texture in this case
        gl.glPushMatrix();

        //Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //Set the face rotation
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        //gl.glFrontFace(GL10.);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        //Enable the vertex and texture state
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);


        //Draw the vertices as triangles, based on the Index Buffer information
        if(wireframe)
        {
            gl.glDrawElements(GL10.GL_LINE_LOOP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        }
        else
        {
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        }

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glPopMatrix();
    }

    /**
     * Load the textures
     *
     * @param gl - The GL Context
     * @param context - The Activity context
     */
    public void loadGLTexture(GL10 gl, Context context) {
        //Get the texture from the Android resource directory
        InputStream is = context.getResources().openRawResource(+R.drawable.door);
        Bitmap bitmap = null;
        try {
            //BitmapFactory is an Android graphics utility for images
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                is.close();
                is = null;
            } catch (IOException e) {
            }
        }

        //Generate one texture pointer...
        gl.glGenTextures(1, textures, 0);
        //...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        //Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);


        //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //Clean up
        bitmap.recycle();
    }
}
