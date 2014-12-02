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


public class CheckUsuario extends AsyncTask<String, Void, Boolean> {

    private static final String TAG_USUARIO = "Usuarios";
    private static String url_check_usuario = "http://www.gilums.com/android_proyecto_tenant/check_usuario.php";
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
    protected Boolean doInBackground(String... params) {
        String usuario = params[0];
        Log.d("Usuario", usuario.toString());
        Boolean retorno = false;

        int exito;
        // Building Parameters
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        nick = usuario;


        Log.d("Nick", nick.toString());
//        Log.d("Password", password);

        param.add(new BasicNameValuePair("nick", nick));


        Log.d("Parametros",param.toString());

        JSONObject json = jsonParser.makeHttpRequest(url_check_usuario, "GET", param);
        Log.d("Single Product Details", json.toString());

            // json exito tag
            try {
                exito = json.getInt(TAG_EXITO);
                Log.d("EXITO", String.valueOf(exito));
                if (exito == 1) {

                    retorno = true;
                } else {
                    retorno = false;
                    Log.d("ERROR", "No se encontro usuario");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        return retorno;
    }
}

