package tecnologo.com.sic;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tecnologo.com.sic.Utilidades.CustomListAdapter;
import tecnologo.com.sic.Utilidades.RowItem;
import tecnologo.com.sic.Utilidades.Utilidad_Red;


public class DesaparecidosFragment extends android.support.v4.app.ListFragment {

    // Progress Dialog
    private ProgressDialog pDialog;
    private ListView lv;
    private View parentLayout;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private JSONObject info ;

    ArrayList<RowItem> lista_desaparecidos;
    private static final String TAG_ID = "idDesaparecidos";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_APELLIDO = "Apellido";
    private static final String TAG_EDAD = "Edad";
    private static final String TAG_SEXO = "Sexo";
    private static final String TAG_FECHA = "FechaDesaparicion";
    private static final String TAG_CONTACTO = "NombreContacto";
    private static final String TAG_TELF = "TelefonoContacto";
    private static final String TAG_FOTO = "Foto";

    // url para obtener los temas
    private static String url_listar_desaparecidos = "http://www.gilums.com/android_proyecto_tenant/get_all_desaparecidos.php";
    JSONParser jsonParser = new JSONParser();

    // JSON Node nombres
    private static final String TAG_EXITO = "exito";
    private static final String TAG_DESAPARECIDOS = "Desaparecidos";
    // temas JSONArray
    JSONArray desaparecidos = null;

    private String Fecha_formateada;
    public Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lv = (ListView) getActivity().findViewById(android.R.id.list);
        lista_desaparecidos = new ArrayList<RowItem>();
        //Cargar desaparecidos in Background Thread
        if (Utilidad_Red.obtener_Estado_Conexion(Main.main)!=0){
            new CargarDesaparecidos().execute();
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
        try{
        actbar.setTitle("Listado de Desaparecidos");
        }
        catch(Exception e){
            Log.e("Error: ", e.toString());
        }
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

      /*  @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);

            Log.d("Dentro de", "On item click");
            String id_catast  = ((TextView) v.findViewById(R.id.id_desaparecidos)).getText()
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
        }*/

    class CargarDesaparecidos extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Cargando lista de desaparecidos. Por favor espere...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_listar_desaparecidos, "GET", params);
            // Check your log cat for JSON reponse
            Log.d("All Desaparecidos: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_EXITO);

                if (success == 1) {
                    // cat found
                    // Getting Array of Products
                    desaparecidos = json.getJSONArray(TAG_DESAPARECIDOS);

                    // looping through All Products
                    for (int i = 0; i < desaparecidos.length(); i++) {
                        JSONObject c = desaparecidos.getJSONObject(i);
                        String idDesaparecidos = c.getString(TAG_ID);
                        String Nombre =c.getString(TAG_NOMBRE);
                        String Apellido= c.getString(TAG_APELLIDO);
                        String Edad="Edad: "+c.getString(TAG_EDAD);
                        Log.d("Edad",Edad);
                        String Sexo= "Sexo: "+c.getString(TAG_SEXO);
                        Log.d("Sexo",Sexo);
                        //String UltimoParadero=c.getString(TAG_ULTIMO);
                        //String UltimoParaderoLatLong= c.getString(TAG_ULTIMO_LAT_LONG);
                        //String EstadoBusqueda=c.getString(TAG_ESTADO_BUSQUEDA);
                        String NombreContacto="Contacto: "+c.getString(TAG_CONTACTO);
                        String TelefonoContacto="Telf: " +c.getString(TAG_TELF);
                        Log.d("Contacto",NombreContacto);
                        Log.d("Telefono",TelefonoContacto);
                        //String RelacionContacto=c.getString(TAG_RELACION);
                        //String IdUsuarioReportado= c.getString(TAG_ID_REPORTADO);
                        //String TIpoUsuioReportado=c.getString(TAG_TIPO_ID_REPORTADO);
                        String logo_string=c.getString(TAG_FOTO);

                        byte[] byteData = Base64.decode(logo_string, Base64.DEFAULT);
                        Bitmap Foto = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);

                        String FechaDesaparicion=c.getString(TAG_FECHA);
                        Log.d("Fecha antes",FechaDesaparicion);
                        try {
                         date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").parse(FechaDesaparicion);
                         Fecha_formateada = "Desaparecido el: "+ new SimpleDateFormat("dd/MM/yyyy").format(date);
                        }
                        catch (ParseException e){
                            Log.e("Error: ",e.toString());
                        }
                        Log.d("Fecha",Fecha_formateada);
                        RowItem item = new  RowItem(idDesaparecidos,Nombre,Apellido,Edad, Sexo,Fecha_formateada,NombreContacto,
                                TelefonoContacto, Foto);
                        lista_desaparecidos.add(item);
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
            //listView = (ListView) parentLayout.findViewById(R.id.list_desaparecidos);
            /*CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.list_item, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);*/

            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity(),
                    R.layout.listar_desaparecidos, lista_desaparecidos);

            setListAdapter(adapter);
            pDialog.dismiss();
        }
    }
}