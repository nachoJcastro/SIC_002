package tecnologo.com.sic;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;

import android.graphics.Color;

import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nacho on 16/11/2014.
 */
public class VerCatastrofeFragment  extends android.support.v4.app.Fragment implements ActionBar.OnNavigationListener {

    TextView texto_nombre;
    TextView texto_info;
    TextView texto_coor;
    TextView texto_fecha;
    ImageView img_logo;

    String id_catast;
    String id_catastrofe;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String url_detalle_catastrofe = "http://www.gilums.com/android_proyecto/get_detalle_catastrofe.php";
    private static String url_pedir_ayuda = "http://www.gilums.com/android_proyecto_tenant/pedir_ayuda.php";
    // JSON Node names
    JSONArray comentarios = null;
    private JSONObject catastrofe;

    private String id;

    // id de la tabla
    private static final String TAG_EXITO = "exito";
    private static final String TAG_CATASTROFE = "catastrofe";

    // campos
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_FECHA = "FechaCreacion";
    private static final String TAG_ID = "idCatastrofe";
    private static final String TAG_INFO = "Informacion";
    private static final String TAG_COOR = "ZonaAfectada";
    private static final String TAG_LOGO = "Logo";
    private static final String TAG_MENSAJE = "mensaje";

    // Campos para actualizr
    private String coor;
    private String info;
    private String nombre;
    private String fecha;
    private Bitmap logo;
    private String logo_string;
    private  SupportMapFragment mapFragment;
    private  GoogleMap map;
    private DisplayMetrics metrics;
    private LinearLayout.LayoutParams mapViewParameters;


    private Marker mMarker;
    private Location userLocation;
    private String nombre_cat;
    private ImageButton btnPedirAyuda;
    private Location loc;
    private FragmentManager frag_mananger;
    private View parentLayout;


    private String pref_nombre="nombre_usuario";

    private SharedPreferences pref;
    private  VerCatastrofeFragment myFragment;
    private AsyncTask<String, String, Boolean> carga_Catastrofe;
    private String[] array_coord;

    private String[] coord1;
    private String[] coord2;
    private String[] coord3;
    private String[] coord4;


   /* private Main main_context;

    @Override
    public void onAttach(Activity activity) {
        main_context=(Main) activity;
        super.onAttach(activity);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
          // parentLayout = inflater.inflate(R.layout.visualizar_catastrofes, container, false);
          //((SupportMapFragment)parentLayout.getParent()).(mapFragment);
         try {
            parentLayout = inflater.inflate(R.layout.visualizar_catastrofes, container, false);
        } catch (InflateException e) {

            Log.d("error",e.toString());
        }
        if (parentLayout != null) {
            ViewGroup parent = (ViewGroup) parentLayout.getParent();

            if (parent != null) {
                View vista = parent.findViewById(R.id.map);
               parent.removeAllViews();
               // parent.removeView(vista);
               Log.d("HIJOS",String.valueOf( parent.getChildCount()));
               // parent.removeView(vista);
                if (mapFragment!=null){
                    Toast.makeText(getActivity().getBaseContext(),"NO ES NULO MAP FRAGMENT",Toast.LENGTH_LONG).show();
                }
                else  Toast.makeText(getActivity().getBaseContext(),"ES NULO MAP FRAGMENT",Toast.LENGTH_LONG).show();
            }
            /* try {
                parentLayout = inflater.inflate(R.layout.visualizar_catastrofes, container);
            } catch (InflateException e) {

                Log.d("error",e.toString());
            }*/
            Bundle bundle = this.getArguments();
            id = bundle.getString(TAG_ID, id_catast);
            nombre_cat= bundle.getString(TAG_NOMBRE,nombre);

            ActionBar actbar = getActivity().getActionBar();
            actbar.setTitle(nombre_cat);
            Log.d("TAG_ID PASADO", id);
            Log.d("NOMBRE PASADO", nombre_cat);
            Log.d("Fragment", "thread = " + Thread.currentThread().getName());

            texto_nombre = (TextView) parentLayout.findViewById(R.id.nombre_cat);
            texto_info = (TextView) parentLayout.findViewById(R.id.informacion_cat);
            texto_fecha = (TextView) parentLayout.findViewById(R.id.fecha_cat);
             //texto_coor = (TextView) parentLayout.findViewById(R.id.);
            img_logo = (ImageView) parentLayout.findViewById(R.id.logo_cat);

            //***************************************************************************
            metrics = new DisplayMetrics();
            metrics.heightPixels=600;
            mapViewParameters = (LinearLayout.LayoutParams) parentLayout.findViewById(
                    R.id.map).getLayoutParams();
            mapViewParameters.height = metrics.heightPixels;
            parentLayout.findViewById(R.id.map).setLayoutParams(mapViewParameters);
            //*****************************************************************************
            //if (map==null) {

                Log.d("Mapa nulo", "mapa");
            // }
            parentLayout.setVisibility(View.VISIBLE);
            carga_Catastrofe= new  GetDetalleCatastrofe().execute();
            try {
                Boolean resultado =carga_Catastrofe.get();
                if (carga_Catastrofe.get())
                    iniciar_Mapa();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }




            // Boton Pedir Ayuda
            btnPedirAyuda = (ImageButton) parentLayout.findViewById(R.id.btn_pedirayuda);
            btnPedirAyuda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("PedidoAyuda: ", "tramitando");
                    //pDialog.setMessage("Click");
                    // creo el tema en un background thread
                    new PedirAyuda().execute();
                }
            });
        }
        return parentLayout;
    }

    private void iniciar_Mapa() {

//        Toast.makeText(getActivity(),"Inicio Mapa",Toast.LENGTH_LONG).show();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {

            Toast.makeText(getActivity().getApplicationContext(),"Map Fragment nulo",Toast.LENGTH_LONG).show();
           // mapFragment = SupportMapFragment.newInstance();
             mapFragment = new SupportMapFragment() {
                @Override
                public void onActivityCreated(Bundle savedInstanceState) {
                    super.onActivityCreated(savedInstanceState);
                     map = mapFragment.getMap();
                    if (map != null) {
                        cargo_datos_mapa();
                    }
                }
            };
            //fragmentTransaction.remove(mapFragment);
            //fragmentTransaction.remove(mapFragment);
           // fragmentTransaction.addToBackStack(null);
          //  fragmentTransaction.commit();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        else {

            if (map!=null){
                Toast.makeText(getActivity().getApplicationContext(),"Mapa no nulo",Toast.LENGTH_LONG).show();
                cargo_datos_mapa();
               // fragmentTransaction.replace(R.id.map, mapFragment).commit();
            }

            else{
                Toast.makeText(getActivity().getApplicationContext(),"Mapa nulo",Toast.LENGTH_LONG).show();
                map = mapFragment.getMap();
                cargo_datos_mapa();
                Log.d("Mapa nulo", "mapa");

            }
            //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //ft.remove(mapFragment);
            //ft.commit();
             //iniciar_Mapa();
            Toast.makeText(getActivity().getApplicationContext(),"Map Fragment no es nulo",Toast.LENGTH_LONG).show();
            /* FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(mapFragment);
            ft.commit();*/
             //params.height = 400;
            // map = mapFragment.getMap();
//            map.setMyLocationEnabled(true);
            //map.setOnMyLocationChangeListener(myLocationChangeListener);

           /* if (map != null) {
                map.setMyLocationEnabled(true);
                userLocation =Main.main.obtener_Coordenadas();
                Log.d("Location",userLocation.toString());

                if(userLocation==null){

                    Toast.makeText(getActivity().getApplicationContext(),
                            "NO HAY USER LOCATION", Toast.LENGTH_SHORT).show();
                }
                //else  Toast.makeText(getActivity(),userLocation.toString(), Toast.LENGTH_SHORT);

                LatLng myLocation = null;
                if (userLocation != null) {
                    myLocation = new LatLng(userLocation.getLatitude(),
                            userLocation.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                            map.getMaxZoomLevel() - 5));
                 }*/
                /*map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {

                    }
                });*/
          //  }
        // else  {

              /*  getSupportFragmentManager().beginTransaction()
                        .add(detailFragment, "detail")
                                // Add this transaction to the back stack
                        .addToBackStack()
                        .commit();*/

              /*  fragmentTransaction.add(R.id.map, mapFragment);
                //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                Toast.makeText(getActivity().getApplicationContext().getApplicationContext(),
                        "MAP NULO", Toast.LENGTH_SHORT).show();*/
            //}
        }
       /*else Toast.makeText(getActivity().getApplicationContext().getApplicationContext(),
                "MAP FRAGMENT NULL", Toast.LENGTH_SHORT).show();*/
    }
  /*  @Override
    public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
        super.onInflate(arg0, arg1, arg2);
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  map = mapFragment.getMap();
    }

    public void cargo_datos_mapa (){
        // cargo datos mapa
        map.setMyLocationEnabled(true);
        userLocation =Main.main.obtener_Coordenadas();
//                        Log.d("Location",userLocation.toString());
        loc=map.getMyLocation();
        if(userLocation==null){
            Toast.makeText(getActivity().getApplicationContext(),
                    "NO HAY USER LOCATION", Toast.LENGTH_SHORT).show();
        }
        else  Toast.makeText(getActivity().getApplicationContext(),userLocation.toString(), Toast.LENGTH_SHORT);
        LatLng myLocation = null;
        if (userLocation != null) {
            myLocation = new LatLng(userLocation.getLatitude(),
                    userLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    map.getMaxZoomLevel() - 5));

            //  String Coordenadas = coor;

            //   Log.d("COORDENADAS", coor);

            PolygonOptions opcionesPoligono =
                    new PolygonOptions()

                           /* "[["22.388174382531723 113.8238525390625"," +
                                    ""22.24715764789753 113.91036987304688"," +
                                    ""22.391983666602783 113.97628784179688"," +
                                    ""22.388174382531723 113.8238525390625"]," +
                                    "["22.388174382531723 113.8238525390625"," +
                                    ""22.24715764789753 113.91036987304688"," +
                                    ""22.391983666602783 113.97628784179688"," +
                                    ""22.388174382531723 113.8238525390625"]," +
                                    "["22.388174382531723 113.8238525390625"," +
                                    ""22.24715764789753 113.91036987304688"," +
                                    ""22.391983666602783 113.97628784179688"," +
                                    ""22.388174382531723 113.8238525390625"]]" */

          /*  coord1=array_coord[0].split(" ");
            coord2=array_coord[1].split(" ");
            coord3=array_coord[2].split(" ");
            coord4=array_coord[3].split(" ");*/

                                    .add(new LatLng(Double.parseDouble(coord1[0]), Double.parseDouble(coord1[1])))
                                    .add(new LatLng(Double.parseDouble(coord2[0]), Double.parseDouble(coord2[1])))
                                    .add(new LatLng(Double.parseDouble(coord3[0]), Double.parseDouble(coord3[1])))
                                    .add(new LatLng(Double.parseDouble(coord4[0]) ,Double.parseDouble(coord4[1])))
                                            //  .add(new LatLng(22.391983666602783, 113.97628784179688))
                                            // .add(new LatLng( -4124487.7054055994,-7319875.7341199))
                                            //  .add(new LatLng(-2.811371193331128, -115.6640625))
                                            //  .strokeWidth(4)
                                    .strokeColor(Color.RED)
                                    .fillColor(0x7F00FF00);
            map.clear();
            // poligono.setFillColor(Color.BLUE); // Relleno del polígono
            // poligono.setStrokeColor(Color.RED); // Bordes del polígono
            Polygon poligono = map.addPolygon(opcionesPoligono);
            Log.d("POLIGONO", poligono.toString());
             Location locacion = Main.main.obtener_Coordenadas();

            if (locacion != null) {

                LatLng punto = new LatLng(locacion.getLatitude(), locacion.getLongitude());
                map.addMarker(new MarkerOptions().position(punto).title(
                        "Zona de Catastrofe")).showInfoWindow();
            }
        }
        // cargo datos mapa
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
           // mapFragment.onSaveInstanceState();
          //Log.v(TAG, "In frag's on save instance state ");
         // outState.putSerializable("starttime", mapFragment);

        }
    @Override
    public void onResume() {
        Toast.makeText(getActivity().getApplicationContext(),"Resumen",Toast.LENGTH_LONG).show();
        //mapFragment.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        Toast.makeText(getActivity().getApplicationContext(),"En Pausa",Toast.LENGTH_LONG).show();
       // Log.e("DEBUG", "OnPause of loginFragment");
       // mapFragment.onPause();
       // map=null;
       // mapFragment=null;
        //map.getUiSettings()

        super.onPause();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_mananger =Main.main.getSupportFragmentManager();
        setRetainInstance(true);
        ActionBar actbar = getActivity().getActionBar();
       // actbar.setTitle("Ver Catastrofe");
        setHasOptionsMenu(true);
       //setHasOptionsMenu(true);
        // mLocationClient = new LocationClient(Main.main, this,this);
        SharedPreferences.Editor editor = Main.main.datos_usuario.edit();
        if (editor!=null){
            pref = getActivity().getApplicationContext().getSharedPreferences(pref_nombre, Context.MODE_PRIVATE);
            if (pref.getString(pref_nombre,null)!=null) {
                Log.d("USUARIO LOGUEADO", pref.getString(pref_nombre, null));
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item;

       // item = menu.findItem(R.id.ic_add_user);
      //  item.setVisible(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragmento = null;
        switch (item.getItemId()) {

            case R.id.ic_add_user:
                 if (SplashScreenActivity.splash.sesion.isLoggedIn()){
                    Toast.makeText(getActivity().getApplicationContext(),"ESTAS LOGUEADO", Toast.LENGTH_LONG).show();
                    LinkedHashMap <String, String> perfil = SplashScreenActivity.splash.sesion.info_Usuario();
                    String  mensajes=new String();
                    if (perfil!=null) {

                        for (String key : perfil.keySet()) {
                            mensajes = mensajes + key + ":  " + perfil.get(key) + "\n";
                        }
                    }
                    else mensajes="nulo";
                    final AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
                    dialogo.setTitle("Perfil del Usuario ");
                    dialogo.setMessage(mensajes);
                    dialogo.setPositiveButton("Log out", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // do something when the OK button is clicked
                            SplashScreenActivity.splash.sesion.logoutUser();
                            if (Session.getActiveSession() != null)
                                Session.getActiveSession().closeAndClearTokenInformation();

                        }
                    });
                    dialogo.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    dialogo.show();
                    /*  for (Map.Entry entry : perfil.entrySet()) {
                       // System.out.println(entry.getKey() + ", " + entry.getValue());
                         menu.add(entry.getKey().toString() + " " + entry.getValue().toString());
                    }*/
                }
                else {
                     startActivity(new Intent(getActivity(), Login.class));
                    //fragmento = new Alta_Usuario();
                }
                 break;

            case R.id.ic_user:
                break;
            case R.id.ic_donar:
                AlertDialog.Builder alertDialogDonate = new AlertDialog.Builder(getActivity());
                // Setting Dialog Message
                alertDialogDonate.setTitle("Donar");
                alertDialogDonate.setMessage("Tu donación ayuda a las personas afectadas por esta catastrofe. \nDesde ya muchas gracias!");
                alertDialogDonate.setCancelable(true);
                alertDialogDonate.setPositiveButton("DONATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Uri.Builder uriBuilder = new Uri.Builder();
                        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
                        uriBuilder.appendQueryParameter("cmd", "_donations");

                        uriBuilder.appendQueryParameter("business", "gbg933@hotmail.com");
                        uriBuilder.appendQueryParameter("lc", "US");
                        uriBuilder.appendQueryParameter("item_name", "SIC");
                        uriBuilder.appendQueryParameter("no_note", "1");
                        uriBuilder.appendQueryParameter("no_shipping", "1");
                        uriBuilder.appendQueryParameter("currency_code", "USD");
                        Uri payPalUri = uriBuilder.build();

                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, payPalUri);
                        startActivity(viewIntent);
                    }
                });
                alertDialogDonate.show();
                break;
            case R.id.ic_desaparecido:
                fragmento = new DesaparecidosFragment();
                break;
            default:
                break;
         }
            //Validamos si el fragment no es nulo
            if (fragmento != null) {

                FragmentTransaction transaction =frag_mananger.beginTransaction();
                //getFragmentManager().popBackStack();
                transaction.replace(R.id.container, fragmento);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
            else {
                //Si el fragment es nulo mostramos un mensaje de error.
                Log.e("Error  ", "MostrarFragment " + item.getTitle().toString());
                return false;
            }
 }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    class GetDetalleCatastrofe extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando detalles del Tema. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }
        protected  Boolean doInBackground (String... params) {

            Boolean retorno =false;
            Log.d("Fragment", "thread = " + Thread.currentThread().getName());
            int exito;
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("id",id));

            Log.d("id", id);



            JSONObject json = jsonParser.makeHttpRequest(
                    url_detalle_catastrofe, "GET", param);

            Log.d("Single Product Details", json.toString());

            // json exito tag
            try {
                exito = json.getInt(TAG_EXITO);
                Log.d("EXITO",String.valueOf(exito));
                if (exito == 1) {

                    JSONArray temaObj = json
                            .getJSONArray(TAG_CATASTROFE); // JSON Array

                    // get first product object from JSON Array
                    catastrofe = temaObj.getJSONObject(0);

                    id_catastrofe = catastrofe.getString(TAG_ID);

                    Log.d("Id Tema", id_catastrofe);

                    nombre = catastrofe.getString(TAG_NOMBRE);
                    Log.d("Nombre CAT", nombre);
                    publishProgress(nombre);
                    info = catastrofe.getString(TAG_INFO);
                    Log.d("Info CAT", info);
                    publishProgress(info);
                    fecha = catastrofe.getString(TAG_FECHA);
                    Log.d("Fecha CAT", fecha);
                    publishProgress(fecha);

                    coor = catastrofe.getString(TAG_COOR);

                    coor = coor.replace("[", "");
                    coor = coor.replace("]", "");

                     array_coord = coor.split(",");
                    array_coord[0]=array_coord[0].replaceAll("\"", "");
                    array_coord[1]=array_coord[1].replaceAll("\"", "");
                    array_coord[2]=array_coord[2].replaceAll("\"", "");
                    array_coord[3]=array_coord[3].replaceAll("\"", "");

                    coord1=array_coord[0].split(" ");
                    coord2=array_coord[1].split(" ");
                    coord3=array_coord[2].split(" ");
                    coord4=array_coord[3].split(" ");



                    Log.d("Coordenada 1 : ", array_coord[0]);
                    Log.d("Coordenada 2 : ", array_coord[1]);
                    Log.d("Coordenada 3 : ", array_coord[2]);
                    Log.d("Coordenada 4 : ", array_coord[3]);

                    Log.d("Coordenada 1 : ", coord1[0]);
                    Log.d("Coordenada 1 : ", coord1[1]);
                    Log.d("Coordenada 2 : ", coord2[0]);
                    Log.d("Coordenada 2 : ", coord2[1]);
                    Log.d("Coordenada 3 : ", coord3[0]);
                    Log.d("Coordenada 3 : ", coord3[1]);
                    Log.d("Coordenada 4 : ", coord4[0]);
                    Log.d("Coordenada 4 : ", coord4[1]);



                    Log.d("Coor CAT", coor);
                    publishProgress(coor);
                    logo_string = catastrofe.getString(TAG_LOGO);

                    byte[] byteData = Base64.decode(logo_string, Base64.DEFAULT);
                    logo = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                    retorno=true;
                }
                else{
                    Log.d("ERROR","No se encontraron catastrofes");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return retorno;
        }

        protected void onPostExecute(String values) {
            texto_nombre.setText(nombre);
            texto_fecha.setText(fecha);
            texto_info.setText(info);
           // texto_coor.setText(coor);
            img_logo.setImageBitmap(logo);

            Log.d("Imagen",img_logo.toString());
           // pDialog.dismiss();

        }
    }
    /*\\
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        //Location location = mLocationClient.getLastLocation();
        LocationManager locationManager = (LocationManager) Main.main.getSystemService(Context.LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();


        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
  /*  @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }
*/
    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
   /* @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

		 //Google Play services can resolve some errors it detects. If the error
		  //has a resolution, try sending an Intent to start a Google Play
		 // services activity that can resolve error.

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(Main.main,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

				 // Thrown if Google Play services canceled the original
				 //    PendingIntent

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }*/

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

    }





   /* @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        if (isGooglePlayServicesAvailable()) {

            Toast.makeText(getActivity(),"HAY SERVICIO GOOGLE PLAY",
                    Toast.LENGTH_SHORT).show();
            .connect();
            /*if(mapFragment!=null){

                map = mapFragment.getMap();
                if(map!=null){
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    map.setMyLocationEnabled(false);


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(12.12122,
                                    17.22323), ZOOM_LEVEL);
                    map.animateCamera(cameraUpdate);
                }
            }


        }
        else Toast.makeText(getActivity(),"NO HAY SERVICIO GOOGLE PLAY",
                Toast.LENGTH_SHORT).show();

    }*/

   /* @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        if(mapFragment!=null){

            map = mapFragment.getMap();
            if(map!=null){
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(false);


                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(12.12122,
                                17.22323), ZOOM_LEVEL);
                map.animateCamera(cameraUpdate);
            }
        }
    }*/




   /* @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }*/

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			//
			// If the result code is Activity.RESULT_OK, try to connect again

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(),"Conexion OK",
                                Toast.LENGTH_SHORT).show();
                        mLocationClient.connect();
                        break;
                }

        }
    }*/

    /*private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Main.main);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(Main.main.getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }*/

    @Override
    public void onDestroyView() {
// TODO Auto-generated method stub
        super.onDestroyView();


       if (parentLayout != null) {
            ViewGroup parentViewGroup = (ViewGroup) parentLayout.getParent();
            if (parentViewGroup != null) {

               /* FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.remove(mapFragment);
                ft.commit();*/
                //mapFragment.onDestroyView();
                parentViewGroup.removeAllViews();

            }
        }


     /*   ViewGroup parent = (ViewGroup) parentLayout.getParent();
        if (parent != null)

            parent.removeView(parent);


        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();*/



        /*if (map != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).commit();
            map = null;
        }*/


        //super.onDestroyView();
        /*try {
            SupportMapFragment fragment = (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(
                            R.id.map);
            if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();

        } catch (IllegalStateException e) {
            //handle this situation because you are necessary will get
            //an exception here :-(
        }*/
    }



    /*private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = map.addMarker(new MarkerOptions().position(loc));
            if(map != null){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };*/

    /***********************************************************************************************************************/
    /***********************************************************************************************************************/
    class PedirAyuda extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Solicitando Ayuda. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected  String doInBackground (String... params) {

            String msj = null;

            String Canal="1";
            String Mensaje="Enviado desde boton de panico";
            String Ubicacion="";
            String UbicacionLatLong="";

            Location loc=((Main) getActivity()).obtener_Coordenadas();

            String longitud=null;
            String latitud=null;

            if(loc!=null) {
                longitud = String.valueOf(loc.getLongitude());
                latitud = String.valueOf(loc.getLatitude());
                Ubicacion=Main.main.obtener_Ubicacion();
                UbicacionLatLong = longitud+","+latitud;
            }
           // else Toast.makeText(getActivity().getApplicationContext(),"No se mando ubicacion",Toast.LENGTH_LONG).show();




            List<NameValuePair> params_pedido = new ArrayList<NameValuePair>();
            //params_pedido.add(new BasicNameValuePair("TipoSolicitor", TipoSolicitor));
            //params_pedido.add(new BasicNameValuePair("idSolicitor", idSolicitor));
            String id=null;
            String tipo_solicitor;

            if(SplashScreenActivity.splash.sesion.isLoggedIn()){

               id= SplashScreenActivity.splash.sesion.get_parametro("Id");
               tipo_solicitor="2";
            }
            else tipo_solicitor="1";

            params_pedido.add(new BasicNameValuePair("IdSolicitor", id));
            params_pedido.add(new BasicNameValuePair("TipoSolicitor", tipo_solicitor));
            params_pedido.add(new BasicNameValuePair("Canal", Canal));
            params_pedido.add(new BasicNameValuePair("Mensaje", Mensaje));
            params_pedido.add(new BasicNameValuePair("Ubicacion", Ubicacion));
            params_pedido.add(new BasicNameValuePair("UbicacionLatLong", UbicacionLatLong));

            JSONObject json_pedido = jsonParser.makeHttpRequest(url_pedir_ayuda,
                    "POST",params_pedido);
            Log.d("Pedido", params_pedido.toString());

            try {
                int exito = json_pedido.getInt(TAG_EXITO);
                Log.d("EXITO", String.valueOf(exito));

               // publishProgress(json_pedido.getString(TAG_MENSAJE));
                if (exito == 1) {
                    msj = json_pedido.getString(TAG_MENSAJE);
                    Log.d("Mensaje: ", msj);
                }

                else {
                    msj = json_pedido.getString(TAG_MENSAJE);
                    Log.d("Mensaje: ", msj);
                }


            }catch (JSONException e) {
                e.printStackTrace();
            }
            return msj;
        }

        protected void onPostExecute(String mensaje) {

            // dismiss the dialog once done
            pDialog.dismiss();

            Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
        }

    }
}

