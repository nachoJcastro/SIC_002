package tecnologo.com.sic;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tecnologo.com.sic.Utilidades.Utilidad_Red;

public class PlaceholderFragment extends android.support.v4.app.ListFragment {


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lv = (ListView) getActivity().findViewById(android.R.id.list);
        catastrofesLsta = new ArrayList<HashMap<String, String>>();
        //Cargar catastrofes in Background Thread
        if (Utilidad_Red.obtener_Estado_Conexion(Main.main)!=0){
            new CargarCatastrofes().execute();
        }
        else {
            String mensaje = "No hay conexion";
            Main.main.mostrar_aviso(mensaje);
        }
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ActionBar actbar = getActivity().getActionBar();
        actbar.setTitle("Home");
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item;
        item = menu.findItem(R.id.ic_add_user);
        item.setVisible(false);
        item = menu.findItem(R.id.ic_user);
        item.setVisible(false);
        item = menu.findItem(R.id.ic_desaparecido);
        item.setVisible(false);
        item = menu.findItem(R.id.ic_donar);
        item.setVisible(false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("Dentro de", "On item click");
        String id_catast  = ((TextView) v.findViewById(R.id.id_catastrofes)).getText()
                .toString();
        String nombre = ((TextView) v.findViewById(R.id.nombre)).getText()
                .toString();

        Log.d("Id Catastrofe",id_catast);

        FragmentManager fm = Main.main.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        VerCatastrofeFragment verCatastrofe;
        verCatastrofe = new VerCatastrofeFragment();

        Bundle data = new Bundle();
        data.putString(TAG_ID, id_catast);
        data.putString(TAG_NOMBRE,nombre);
        verCatastrofe.setArguments(data);
        transaction.replace(R.id.container, verCatastrofe);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.d("Paso transaction",id_catast);
    }

    class CargarCatastrofes extends AsyncTask<String, String, String> {
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
                        String nombre = c.getString(TAG_NOMBRE);
                        String fecha = c.getString(TAG_FECHA);
                        String coor = c.getString(TAG_COOR);
                        String informacion = c.getString(TAG_INFO);
                        //String logo_string = c.getString(TAG_LOGO);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NOMBRE, nombre);
                        map.put(TAG_INFO, informacion);
                        map.put(TAG_FECHA, fecha);
                        map.put(TAG_COOR, coor);

                        // adding HashList to ArrayList
                        catastrofesLsta.add(map);
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

            SimpleAdapter adapter = new SimpleAdapter(
                    getActivity(), catastrofesLsta,
                    R.layout.listar_catastrofes, new String[]{TAG_ID,
                    TAG_NOMBRE},
                    new int[]{R.id.id_catastrofes, R.id.nombre});

            setListAdapter(adapter);


        }

    }



}