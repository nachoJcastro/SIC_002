package tecnologo.com.sic;



import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.security.MessageDigest;


import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import tecnologo.com.sic.Utilidades.Sesion;

public class Alta_Usuario  extends Fragment  implements View.OnTouchListener, View.OnFocusChangeListener
{

    private boolean resultado;

    public Alta_Usuario() {
    }

    Fragment alta =this;

    // Dialogo progreso
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    EditText input_nombre;
    EditText input_apellido;
    EditText input_nik;

    EditText input_email;
    EditText input_password;

    AutoCompleteTextView input_sexo;
    EditText input_celular;
    EditText input_direccion;

    EditText input_fecha;

    private View parentLayout;




    // url para crear tema en la bd

    private static String url_crear_usuario = "http://www.gilums.com/android_proyecto_tenant/crear_usuario.php";
    //private static String url_crear_tags = "http://www.gilums.com/android_app/crear_tags.php";
    // JSON
    private static final String TAG_EXITO = "exito";
    private static final String TAG_MENSAJE  = "mensaje";



    private Button btnAltaUsuario;


    private String pref_nombre="nombre_usuario";
    private String pref_apellido="apellido_usuario";
    private String pref_email="email_usuario";

    private String pref_nik="nik_usuario";
    private String pref_pass="password_usuario";
    private String pref_fecha="fecha_usuario";

    private String pref_celular="celular_usuario";
    private String pref_direccion="direccion_usuario";
    private String pref_login="tipo_login_usuario";
    private android.support.v4.app.FragmentManager frag_mananger;
    private int anio;
    private int mes;
    private int dia;
    private Button btn_Calendario;


/*
    Editor editor = Main.main.datos_usuario.edit();
    editor.putString(pref_nombre, nombre);
    editor.putString(pref_apellido, apellido);
    editor.putString(pref_nik, nik);
    editor.putString(pref_pass, password);
    editor.putString(pref_email, email);

    editor.putString(pref_celular, celular);
    editor.putString(pref_direccion, direccion);
    editor.putString(pref_fecha, fecha);
    editor.putString(pref_login,"login_pagina");*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentLayout = inflater.inflate(R.layout.alta_usuario, container, false);


      /*  titulo_item= (TextView) parentLayout.findViewById(R.id.titulo_item);

        if (titulo_item!=null){

            Log.d("TITULO FRAGMENT", titulo_item.getText().toString());
        }

        else Log.d("TITULO FRAGMENT","NULO");*/
        // view.titulo_item= (TextView) convertView.findViewById(R.id.titulo_item);
        // view.titulo_item.setText(itm.getTitulo());

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                R.layout.drop_down, SEXO);


        input_nombre=(EditText) parentLayout.findViewById(R.id.input_nombre);
        input_apellido= (EditText) parentLayout.findViewById(R.id.input_apellido);
        input_email= (EditText) parentLayout.findViewById(R.id.input_email);
        input_fecha = (EditText) parentLayout.findViewById(R.id.input_fecha);
        //input_sexo= (AutoCompleteTextView) parentLayout.findViewById(R.id.input_sexo);
        input_celular= (EditText) parentLayout.findViewById(R.id.input_celular);
        input_direccion= (EditText) parentLayout.findViewById(R.id.input_direccion);
        input_nik= (EditText) parentLayout.findViewById(R.id.input_nik);
        input_password= (EditText) parentLayout.findViewById(R.id.input_password);

        input_sexo = (AutoCompleteTextView) parentLayout.findViewById(R.id.input_sexo);
        input_sexo.setAdapter(adapter);

        input_nombre.setOnTouchListener(this);
        input_apellido.setOnTouchListener(this);
        input_email.setOnTouchListener(this);
        input_fecha.setOnTouchListener(this);
        input_celular.setOnTouchListener(this);
        input_direccion.setOnTouchListener(this);
        input_nik.setOnTouchListener(this);
        input_password.setOnTouchListener(this);
        input_sexo.setOnTouchListener(this);


       /* input_nik.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //TextView responseText = (TextView) findViewById(R.id.responseText);
                EditText metatags = (EditText) parentLayout.findViewById(R.id.input_nik);



                if ( keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||

                        event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {


                    if (!event.isShiftPressed()) {
                        int position;

                        if(KeyEvent.ACTION_DOWN == event.getAction()){
                            metatags.setText(metatags.getText() + ";");
                            position = metatags.length();
                            metatags.setSelection(position);

                        }
                        return true;
                    }

                }
                return false; // pass on to other listeners.
            }
        });*/


        // Boton crear Usuario
        btnAltaUsuario = (Button) parentLayout.findViewById(R.id.btnAltaUsuario);
        btnAltaUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Log.d ( "Unknown: ","desconocido");
                //pDialog.setMessage("Click");
                // creo el tema en un background thread
              if( input_nombre.getText().toString().length() == 0 ){
                  input_nombre.setError( "Nombre es obligatorio!" );
                  mostrar_aviso("Nombre es obligatorio!");
              }
              else{
                  if( input_apellido.getText().toString().length() == 0 ){
                        input_apellido.setError( "Apellido es obligatorio!");
                        mostrar_aviso("Apellido es obligatorio!");}
                  else{
                      if( input_email.getText().toString().length() == 0 ){
                          input_email.setError( "Email es obligatorio!");
                          mostrar_aviso("Email es obligatorio!");}
                      else{
                          if( input_nik.getText().toString().length() == 0 ){
                              input_nik.setError( "El nick es obligatorio!" );
                              mostrar_aviso("Nick es obligatorio!");}
                          else{
                                    Boolean existe=chequear_usuario(input_nik.getText().toString());
                                    Log.d("existe", existe.toString());
                                    if (existe){

                                         input_nik.setError( "El nick existe!" );
                                     }
                                    else{
                                          if( input_password.getText().toString().length() == 0 ){
                                          input_password.setError( "El password es obligatorio!" );
                                          // mostrar_aviso("Password es obligatorio!");
                                          }
                                      else
                                          {  new CrearNuevoUsuario().execute();
                                             frag_mananger.beginTransaction().remove(alta).commit();
                                        //.getSupportFragmentManager();
                                            Login.login.onBackPressed();}
                                    }
                                }

                          }
                      }

                  }


                }



        });


        btn_Calendario = (Button) parentLayout.findViewById(R.id.btnCalendario);
        btn_Calendario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                         // Process to get Current Date
                        final Calendar c = Calendar.getInstance();
                        anio = c.get(Calendar.YEAR);
                        mes = c.get(Calendar.MONTH);
                        dia = c.get(Calendar.DAY_OF_MONTH);

                        // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox
                                        input_fecha.setText(year + "-"
                                                + (monthOfYear + 1) + "-" + dayOfMonth);

                                    }
                                }, anio, mes, dia);
                        dpd.show();
                    }
 });
    return parentLayout;

    }

    private Boolean chequear_usuario(String s) {
        Boolean retorno = false;
        AsyncTask<String,Void,Boolean> get_usuario = new CheckUsuario().execute(s);
        try {
            resultado = get_usuario.get();
            if ( resultado)
              retorno =true;
            else
                retorno= false;
        }
        catch(Exception e){
            retorno =false;

        }

       return  retorno;
    }


    /*//@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCrearUsuario:
                new CrearNuevoUsuario().execute();

            default:
                Log.d ( "Unknown: ","desconocido");
                break;
        }
    }*/


    private static final String[] SEXO = new String[] {
            "Masculino", "Femenino", "N/C"
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        //MenuItem item = menu.findItem(R.id.action_search);
        //MenuItem  titulo_item = menu.findItem(R.id.titulo_item);
         //item.setVisible(false);
        // item = menu.findItem(R.id.menu_nuevotema);
        // item.setVisible(false);

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        frag_mananger =getActivity().getSupportFragmentManager();



      // ActionBar actbar = getActionBar();
        //actbar.setTitle("Alta Usuario");
        //ab.setSubtitle("sub-title");
        // setContentView(R.layout.crear_tema);



        // Campos edit para el usuario
       /* input_email = (EditText) parentLayout.findViewById(R.id.input_email);
        input_apellido = (EditText) parentLayout.findViewById(R.id.input_apellido);
        input_nik = (EditText) parentLayout.findViewById(R.id.input_nik);*/
        /*input_nik.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //TextView responseText = (TextView) findViewById(R.id.responseText);
                EditText metatags = (EditText) parentLayout.findViewById(R.id.input_nik);



            if ( keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||

                    event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {


                if (!event.isShiftPressed()) {
                    int position;

                    if(KeyEvent.ACTION_DOWN == event.getAction()){
                        metatags.setText(metatags.getText() + ";");
                        position = metatags.length();
                        metatags.setSelection(position);

                    }
                    return true;
                }

            }
            return false; // pass on to other listeners.
        }
    });

    input_email = (EditText) parentLayout.findViewById(R.id.inputNombre);
    input_password = (EditText) parentLayout.findViewById(R.id.inputCorreo);
    */
        // Boton crearTema
        // Button btnCrearUsuario = (Button) parentLayout.findViewById(R.id.btnCrearUsuario);

        // evento click boton
       /* */
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v instanceof EditText) {

               if(!hasFocus && ((EditText) v).getText().length()==0)
               {
                   // ((EditText) v).setText("Campo Obligatorio");
                  // ((EditText) v).setTextColor(Color.RED);
                   // String mensaje ="Campo Obligatorio";
                  // mostrar_aviso  (mensaje);

                 //  Toast.makeText(getActivity().getApplicationContext(),"Obligatorio",Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof EditText) {
            v.setOnFocusChangeListener(this); // User touched edittext
        }


        return false;
    }

    public void mostrar_aviso ( String mensaje){

        new AlertDialog.Builder(Login.login)
                .setTitle("Aviso")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }



    /*@Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {



    }*/

    /**
     * Tarea Async para crear el Tema
     * */
    class CrearNuevoUsuario extends AsyncTask<String, String, String> {

        /**
         * Mostrar Progreso
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /*pDialog = new ProgressDialog(getActivity());
           pDialog.setMessage("Dando de alta Usuario..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Creando Tema
         * */
        protected String doInBackground(String... args) {

           /* String titulo =input_email.getText().toString();
            String texto = input_apellido.getText().toString();
            String metaTags = input_nik.getText().toString();

            String nombre= input_email.getText().toString();
            String correo=input_password.getText().toString();*/

            String nombre =input_nombre.getText().toString();
            String apellido = input_apellido.getText().toString();
            String nik =input_nik.getText().toString();


             String fecha =input_fecha.getText().toString();

            if(fecha==null){
                fecha ="2009-09-09";

            }

            Log.d("FECHA",input_fecha.getText().toString());
            String email = input_email.getText().toString();
            String password_s_encrypt = input_password.getText().toString();
            String password=null;
           try {
               password = Main.main.codificar_password(password_s_encrypt);
           }
           catch (NoSuchAlgorithmException e){

               Log.d("Log d",e.toString());
           }


            String sexo =input_sexo.getText().toString();
            String celular = input_celular.getText().toString();
            String direccion = input_direccion.getText().toString();


           /*tring titulo ="TITULO NUEVO";
            String texto = "TEXTO NUEVO";
            String metaTags = "TITULO NUEVO";

            String nombre="TITULO NUEVO";
            String correo="TITULO NUEVO";
            */
             // Parametros crear Usuario
            List<NameValuePair> params_User = new ArrayList<NameValuePair>();

            params_User.add(new BasicNameValuePair("nombre", nombre));
            params_User.add(new BasicNameValuePair("apellido", apellido));
            params_User.add(new BasicNameValuePair("fecha",fecha));

            params_User.add(new BasicNameValuePair("nik", nik));
            params_User.add(new BasicNameValuePair("email", email));
            params_User.add(new BasicNameValuePair("password", password));

            params_User.add(new BasicNameValuePair("sexo", sexo));
            params_User.add(new BasicNameValuePair("celular", celular));
            params_User.add(new BasicNameValuePair("direccion", direccion));

            JSONObject json_usuario = jsonParser.makeHttpRequest(url_crear_usuario,
                    "POST", params_User);
            Log.d("Creando Respuesta Usuario", json_usuario.toString());

            // chequeo si se creo exitosamente
            try {
                int exito = json_usuario.getInt(TAG_EXITO);

                if (exito == 1) {

                       String id= String.valueOf( json_usuario.getInt(TAG_MENSAJE));
                      Log.d("Id usuario ingresado",id);
                  /*  Toast.makeText(getActivity().getApplicationContext(), fecha, Toast.LENGTH_SHORT).show();
            */
                    SplashScreenActivity.splash.sesion.createLoginSession( id,nombre,  apellido,sexo, nik, password_s_encrypt,  email, celular,
                             direccion, fecha ,"Login SIC");

                   // Log.d("Parametro sesion id=",Main.main.sesion.get_parametro("Id"));


                    /*session.createLoginSession(, "anroidhive@gmail.com");


                    Editor editor = Main.main.datos_usuario.edit();
                    editor.putString(pref_nombre, nombre);
                    editor.putString(pref_apellido, apellido);
                    editor.putString(pref_nik, nik);
                    editor.putString(pref_pass, password);
                    editor.putString(pref_email, email);

                    editor.putString(pref_celular, celular);
                    editor.putString(pref_direccion, direccion);
                    editor.putString(pref_fecha, fecha);
                    editor.putString(pref_login,"login_pagina");*/


                    // vuelvo al anterior
                    /*android.support.v4.app.FragmentTransaction transaction =frag_mananger.beginTransaction();

                    transaction.replace(R.id.container, fragmento);
                    transaction.addToBackStack(null);
                    transaction.commit();*/

                    //super.onBackPressed();

                    //FragmentManager fm = getActivity().getFragmentManager();
                    //fm.popBackStack();



                } else {
                    // fallo
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
           // pDialog.dismiss();





            //FragmentManager fm=getSupportFragmentManager();
           // android.support.v4.app.FragmentTransaction ft=fm.beginTransaction().remove(alta);




            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();



           /* previousFragment=(SherlockFragment)getSupportFragmentManager()
                    .findFragmentByTag(""+currentTagNum);
            getSupportFragmentManager().beginTransaction()
                    .show(mFragment)
                    .commit();*/



           /* VerCatastrofeFragment listar ;
            listar = new VerCatastrofeFragment();
            FragmentTransaction transaction = getActivity().().beginTransaction();
            //getFragmentManager().popBackStack();

            //transaction.replace(R.id.container, listar);
            //transaction.addToBackStack(null);
            transaction.commit();*/

        }

    }



}