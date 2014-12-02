package tecnologo.com.sic;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class GetUsuario  extends AsyncTask<ArrayList<String>, Void, Boolean> {

    private static final String TAG_USUARIO = "Usuarios";
    private static String url_get_usuario = "http://www.gilums.com/android_proyecto_tenant/get_usuario.php";
    private static final String TAG_EXITO = "exito";
    private static final String TAG_MENSAJE = "mensaje";
    private JSONParser jsonParser = new JSONParser();
    private JSONObject usuario_retorno;

    private String nombre;
    private String apellido;
    private String nick;
    private String email;
    private String fechaNacimiento;
    private String sexo;
    private String celular;
    private String direccion;
    private String id;
    private String password;

    @Override
    protected Boolean doInBackground(ArrayList<String>... params) {
        ArrayList<String> usuario = params[0];
        Log.d("Usuario", usuario.toString());
        Boolean retorno = false;

        int exito;
        // Building Parameters
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        nick = usuario.get(0);
        password = usuario.get(1);

        Log.d("Nick", nick);
        Log.d("Password", password);

        param.add(new BasicNameValuePair("nick", nick));
        param.add(new BasicNameValuePair("password", password));

        Log.d("Parametros",param.toString());

        JSONObject json = jsonParser.makeHttpRequest(url_get_usuario, "GET", param);
        Log.d("Single Product Details", json.toString());

            // json exito tag
            try {
                exito = json.getInt(TAG_EXITO);
                Log.d("EXITO", String.valueOf(exito));
                if (exito == 1) {

                    JSONArray usuarObj = json
                            .getJSONArray(TAG_USUARIO); // JSON Array

                    // get first product object from JSON Array
                    usuario_retorno = usuarObj.getJSONObject(0);


                    id = usuario_retorno.getString("Id");
                    nombre = usuario_retorno.getString("Nombre");
                    apellido = usuario_retorno.getString("Apellido");
                    nick = usuario_retorno.getString("Nik");

                    email = usuario_retorno.getString("Email");

                    fechaNacimiento = usuario_retorno.getString("FechaNacimiento");

                    sexo = usuario_retorno.getString("Sexo");
                    celular = usuario_retorno.getString("Celular");
                    direccion = usuario_retorno.getString("Direccion");


           /* public void createLoginSession(String id,String nombre, String apellido,String sexo,String nik,String password, String email,String celular,
                    String direccion,String fecha ,String tipo_login){*/

                    SplashScreenActivity.splash.sesion.createLoginSession(id, nombre, apellido, sexo, nick, null, email, celular,
                            direccion, celular, "Usuario Registrado");

                    retorno = true;
                } else {
                    Log.d("ERROR", "No se encontro usuario");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        return retorno;
    }
}

