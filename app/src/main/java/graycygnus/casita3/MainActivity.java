package graycygnus.casita3;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import graycygnus.mygraphics.*;

/**
 * Created by Anai on 03/03/2016.
 * Actividad principal, la cual correra el proyecto
 */
public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener
{
    //Stage para manejo de Pausa y Optimizacion de Recursos
    private Stage stage;
    String[] strings = {"Vista","Trasladar",
        "Rotar", "Escalar", "Shearing"};
    String[] strings2 = {"Casa", "Arbol"};

    //String[] subs = {"Heaven of all working codes ","Google sub",
    //        "Microsoft sub", "Apple sub", "Yahoo sub","Samsung sub"};

    int arr_images[] = {
            R.drawable.camara,
            R.drawable.move,
            R.drawable.rotate,
            R.drawable.scale,
            R.drawable.shear };

    int arr_images2[] = {
            R.drawable.casa,
            R.drawable.arbol};
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //COMPORTAMIENTO GENERAL DE LA APLICACION

        //Full Screen ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Back Light On
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Aplicar el Full Screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //Cargar Escena para manejo
        setContentView(R.layout.main_layout);
        stage  = (Stage)findViewById(R.id.my_stage);

        /*
        //Creacion del Spinner de Opciones
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //Adaptador de Opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_dropdown_item);
        //Layout a usar
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Aplicar Adaptador
        spinner.setAdapter(adapter);
        */

        Spinner mySpinner = (Spinner)findViewById(R.id.spinner);
        mySpinner.setAdapter(new CustomSpinner(this, R.layout.row, strings, 0));
        mySpinner.setOnItemSelectedListener(this);

        Spinner mySpinner2 = (Spinner)findViewById(R.id.spinner2);
        mySpinner2.setAdapter(new CustomSpinner(this, R.layout.row2, strings2, 1));
        mySpinner2.setOnItemSelectedListener(this);

        final Button button = (Button) findViewById(R.id.button);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.squeak);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                stage.setAnimation(true);
                mp.start();
            }
        });

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stage.wireframe = isChecked;
            }
        });
        checkBox.setChecked(false);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Pausar la aplicacion
        stage.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Renaudar la aplicacion
        stage.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object o = parent.getItemAtPosition(position);
        if(parent.getCount() == 2)
        {
            //Selector arbol o casa
            stage.setSelectedPolygon(o.toString());
        }
        else
        {
            stage.setSelectedTool(o.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //nada
    }

    public class CustomSpinner extends ArrayAdapter<String> {

        public int idSpinner;

        public CustomSpinner(Context context, int textViewResourceId,   String[] objects, int idSpinner) {
            super(context, textViewResourceId, objects);
            this.idSpinner = idSpinner;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            if(idSpinner == 0)
            {
                View row=inflater.inflate(R.layout.row, parent, false);
                TextView label=(TextView)row.findViewById(R.id.company);
                label.setText(strings[position]);

                //TextView sub=(TextView)row.findViewById(R.id.sub);
                //sub.setText(subs[position]);

                ImageView icon=(ImageView)row.findViewById(R.id.image);
                icon.setImageResource(arr_images[position]);

                return row;
            }
            else {
                View row=inflater.inflate(R.layout.row2, parent, false);
                TextView label=(TextView)row.findViewById(R.id.company);
                label.setText(strings2[position]);

                //TextView sub=(TextView)row.findViewById(R.id.sub);
                //sub.setText(subs[position]);

                ImageView icon=(ImageView)row.findViewById(R.id.image);
                icon.setImageResource(arr_images2[position]);

                return row;
            }
        }
    }
}

