package tecnologo.com.sic;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Crear_Usuario extends AsyncTask<ArrayList<String>, Void, String> {



    JSONParser jsonParser = new JSONParser();



    private View parentLayout;
    private static String url_crear_usuario = "http://www.gilums.com/android_proyecto_tenant/crear_usuario.php";
    private static final String TAG_EXITO = "exito";
    private static final String TAG_MENSAJE  = "mensaje";



    @Override
    protected String doInBackground(ArrayList<String>... params) {

        ArrayList<String> lista=params[0];


        // Parametros crear Usuario
        List<NameValuePair> params_User = new ArrayList<NameValuePair>();



        params_User.add(new BasicNameValuePair("nombre",lista.get(0)));
        params_User.add(new BasicNameValuePair("apellido",lista.get(1)));
        params_User.add(new BasicNameValuePair("fecha",null));

        params_User.add(new BasicNameValuePair("nik", lista.get(2)));
        params_User.add(new BasicNameValuePair("email", null));
        params_User.add(new BasicNameValuePair("password", null));

        params_User.add(new BasicNameValuePair("sexo", lista.get(3)));
        params_User.add(new BasicNameValuePair("celular", null));
        params_User.add(new BasicNameValuePair("direccion", null));

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
                SplashScreenActivity.splash.sesion.createLoginSession( id,lista.get(0),  lista.get(1), lista.get(3),lista.get(2), null,  null, null,
                        null, null ,"Facebook");

                //Log.d("Parametro sesion id=",Main.main.sesion.get_parametro("Id"));


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

                //frag_mananger.beginTransaction().remove(alta).commit();
                //.getSupportFragmentManager();
                //frag_mananger.popBackStack();
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


}