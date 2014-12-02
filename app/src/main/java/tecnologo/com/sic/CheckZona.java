package tecnologo.com.sic;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CheckZona extends AsyncTask<String, String, String> {

    // Progress Dialog
    private ProgressDialog pDialog;
    private ListView lv;
    private View parentLayout;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private JSONObject info ;

    ArrayList<HashMap<String, String>> catastrofesLsta;

    // url para obtener los temas
    private static String url_listar_catastrofes = "http://www.gilums.com/android_proyecto/get_all_catastrofes.php";
/*
        private static final String url_detalle_menu = "http://www.gilums.com/android_app/get_detalle_menu.php";
        private String id;
        String fecha;
        String nombre;
*/

    JSONParser jsonParser = new JSONParser();

    // JSON Node nombres
    private static final String TAG_EXITO = "exito";
    private static final String TAG_CATASTROFES = "catastrofe";
    private static final String TAG_ID = "idCatastrofe";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_FECHA = "FechaCreacion";
    private static final String TAG_INFO = "Informacion";
    private static final String TAG_COOR = "ZonaAfectada";
    private static final String TAG_LOGO = "logo";

    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;


    // temas JSONArray
    JSONArray catastrofes = null;

    private AdapterView.OnItemClickListener listenes;
    private View.OnCreateContextMenuListener context;


                /* @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog.setMessage("Cargando catástrofes. Por favor espere...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
                */

    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_listar_catastrofes, "GET", params);

        // Check your log cat for JSON reponse
        Log.d("All desaparecidos: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_EXITO);

            if (success == 1) {
                // cat found
                // Getting Array of Products
                catastrofes = json.getJSONArray(TAG_CATASTROFES);

                // looping through All Products
                for (int i = 0; i < catastrofes.length(); i++) {
                    JSONObject c = catastrofes.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);

                    String coor = c.getString(TAG_COOR);

                }
            } else {

                Log.d("ERROR", "No se encontraron temas");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {

        //String logo1 = TAG_LOGO;
        //byte[] byteData = Base64.decode(logo1, Base64.DEFAULT);
        //Bitmap logo = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);




    }

}


