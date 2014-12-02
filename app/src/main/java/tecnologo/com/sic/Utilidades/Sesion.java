package tecnologo.com.sic.Utilidades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.LinkedHashMap;

import tecnologo.com.sic.Alta_Usuario;
import tecnologo.com.sic.Main;
import tecnologo.com.sic.SplashScreenActivity;

public class Sesion {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private String pref_nombre="Nombre";
    private String pref_apellido="Apellido";
    private String pref_email="Email";

    private String pref_nik="Nick";
    private String pref_pass="Password";
    private String pref_fecha="Nacimiento";

    private String pref_celular="Celular";
    private String pref_direccion="Direccion";
    private String pref_login="Login";
    private String pref_id="Id";
    private String pref_sexo="Sexo";

    // Constructor
    public Sesion(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String id,String nombre, String apellido,String sexo,String nik,String password, String email,String celular,
                                   String direccion,String fecha ,String tipo_login){
        // Guardo si esta Logueado
        editor.putBoolean(IS_LOGIN, true);

        // Guardo Datos Usuario
        editor.putString(pref_id,id);

        editor.putString(pref_nombre, nombre);
        editor.putString(pref_apellido, apellido);
        editor.putString(pref_sexo,sexo);
        editor.putString(pref_nik, nik);
        editor.putString(pref_pass, password);
        editor.putString(pref_email, email);

        editor.putString(pref_celular, celular);
        editor.putString(pref_direccion, direccion);
        editor.putString(pref_fecha, fecha);
        editor.putString(pref_login,tipo_login);


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SplashScreenActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    /**
     * Get stored session data
     *
     *
     *
     * */
    public String get_parametro (String preferencia){

        String retorno= ( pref.getString(preferencia,null));

        return retorno;
    }


     public LinkedHashMap <String, String> info_Usuario(){

        LinkedHashMap<String, String> usuario = new LinkedHashMap<String, String>();



        usuario.put(pref_nombre, pref.getString(pref_nombre, null));

        usuario.put(pref_apellido, pref.getString(pref_apellido, null));

        usuario.put(pref_sexo, pref.getString(pref_sexo, null));

        usuario.put(pref_nik, pref.getString(pref_nik, null));

        usuario.put(pref_email, pref.getString(pref_email, null));

        usuario.put(pref_celular, pref.getString(pref_celular, null));

        usuario.put(pref_direccion, pref.getString(pref_direccion, null));

        usuario.put(pref_fecha, pref.getString(pref_fecha, null));

        usuario.put(pref_login,pref.getString(pref_login,null));

        // return usuario
        return usuario;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity



        //Intent i = new Intent(_context, Main.class);
        // Closing all the Activities
     //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
       // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
       // _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}