package tecnologo.com.sic;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tecnologo.com.sic.Utilidades.LocationAddress;
import tecnologo.com.sic.Utilidades.Sesion;
import tecnologo.com.sic.Utilidades.Utilidad_Red;


public class Main extends FragmentActivity implements LocationListener,GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    static Main main;

    Sesion sesion;

    private LocationManager locationManager;
    private Location loc;
    private int estado;
    // private int estado;
    static AlertDialog dialogo;

    private LocationClient mLocationClient;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    SharedPreferences datos_usuario;

    public static final String MyPREFERENCES = "Preferencias" ;
    public static final String nombre_usuario = "nombre_usuario";
    public static final String clave_usuario = "clave_usuario";

    private static final String TAG_EXITO = "exito";
    private static final String TAG_CATASTROFES = "catastrofe";
    private static final String TAG_ID = "idCatastrofe";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_FECHA = "FechaCreacion";
    private static final String TAG_INFO = "Informacion";
    private static final String TAG_COOR = "ZonaAfectada";

    private String ubicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_main);

        //*************************************************
        datos_usuario=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        //sesion = new Sesion(getApplicationContext());
        main=this;
        mLocationClient = new LocationClient(this, this,this);
        estado = Utilidad_Red.obtener_Estado_Conexion(this);
        if(estado!=0) {
            // new CargarCatastrofes().execute();}
            Toast.makeText(this, Utilidad_Red.estado_Conexion(this), Toast.LENGTH_LONG).show();
        }
        // ubicacion ----------------------------------------------------------
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        //Si el GPS no está habilitado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {
            // mostrarAvisoGpsDeshabilitado();
            Log.d("GPS APAGADO","APAGADO");
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER )){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10,this);
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    Log.d("Latitud", String.valueOf(loc.getLatitude()));
                    Log.d("Longitud", String.valueOf(loc.getLongitude()));
                    Toast.makeText(getApplicationContext(),
                            "Ubicacion por Red", Toast.LENGTH_SHORT);
                }
            }

        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10,this);
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                Log.d("Latitud", String.valueOf(loc.getLatitude()));
                Log.d("Longitud", String.valueOf(loc.getLongitude()));
                Toast.makeText(getApplicationContext(),
                        "Ubicacion por GPS", Toast.LENGTH_SHORT);
            }
        }
        //*******************************************************************
        if (savedInstanceState == null) {

            if (zona_catastrofe()) {
                VerCatastrofeFragment verCatastrofe;
                verCatastrofe = new VerCatastrofeFragment();
                 Bundle data = new Bundle();
                data.putString(TAG_ID, "1");
                data.putString(TAG_NOMBRE,"Incendio");
                verCatastrofe.setArguments(data);
                /*transaction.replace(R.id.container, verCatastrofe);
                transaction.addToBackStack(null);
                transaction.commit();*/
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, verCatastrofe)
                        .commit();
            }
            //*********************************************
            else{
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new PlaceholderFragment())
                        .commit();}

        }

        // ubicacion ----------------------------------------------------------
    }
    //****************************************************************
    public boolean zona_catastrofe(){


        return false;
    }

   //******Coordenadas***********************************************
    public Location obtener_Coordenadas(){
        return this.loc;
    }
    //******Ubicacion***********************************************
    public String obtener_Ubicacion(){
        return this.ubicacion;
    }
    //****************************************************************************
    @Override
    public void onLocationChanged(Location location) {
        //  txtLat = (TextView) findViewById(R.id.textview1);
        // txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        // Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
    }
    //*****************************************************************************
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
        // Toast.makeText(getApplicationContext(), "No hayGPS", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");

    }

    /*
     try{

        Location loc=((Main) getActivity()).obtener_Coordenadas();
        String longitud;
        String latitud;
        String proveedor;
        if (loc==null){

           longitud="";
           latitud="";
           proveedor="Sin GPS";
        }
        else {
            proveedor=loc.getProvider();
            longitud=String.valueOf(loc.getLongitude());
            latitud=String.valueOf(loc.getLatitude());
               Log.d("UBICACION","LONG"+longitud+"LATITUD"+latitud+"PROVEEDOR"+proveedor);
            longitud=String.valueOf(loc.getLongitude());
            latitud=String.valueOf(loc.getLatitude());

          }


     */



    //******Coordenadas***********************************************

   /* @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0 ) {
            salir();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    public void salir(){

        // Salir
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               this);

        // set title
        alertDialogBuilder.setTitle("Stuck UY!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Realmente desea salir?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Main.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        // Salir
    }*/




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       /* int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/

      /* Fragment fragmento = null;
        // Control de cuando se presionan elementos de la barra de acciones
        switch (item.getItemId()) {

            // Usuario
            case R.id.ic_add_user:
                fragmento = new Alta_Usuario();

                startActivity(new Intent(this,  Login_Facebook.class));
                break;


            // Donacion
            case R.id.ic_donar:
              //  fragmento = new DonarFragment();
                break;

            // Desaparecidos
            case R.id.ic_desaparecido:
                fragmento = new DesaparecidosFragment();
                break;

            default:
                //Toast.makeText(getApplicationContext(), "Opcion no disponible!", Toast.LENGTH_SHORT).show();
                fragmento = new PlaceholderFragment();
                break;
        }

        //Validamos si el fragment no es nulo
        if (fragmento != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.container, fragmento).commit();
            return true;
        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment" + item.getTitle().toString());
            return false;
        }
*/
       return false;

    }

    /*
        CONEXION ***************************************************************
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        //Location location = mLocationClient.getLastLocation();
        LocationManager locationManager = (LocationManager) Main.main.getSystemService(Context.LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Toast.makeText(this, "Ubicacion por GPS!", Toast.LENGTH_SHORT).show();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());
         } else {
            Toast.makeText(main, "Sin servicio de Ubicacion!", Toast.LENGTH_SHORT).show();
        }
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
           ubicacion=locationAddress;
        }
    }
    //**********************************************************************************
     @Override
    public void onDisconnected() {
        Toast.makeText(this, "Desconectado.", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {

        if (datos_usuario.contains(nombre_usuario))
        {
            if(datos_usuario.contains(clave_usuario)){
                Intent i = new Intent(this,tecnologo.com.sic.Main.class);
                startActivity(i);
            }
        }
        super.onResume();
    }
    //*************************************************************************************
     @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Google Play services can resolve some errors it detects. If the error
        //has a resolution, try sending an Intent to start a Google Play
        // services activity that can resolve error.

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

                // Thrown if Google Play services canceled the original
                //    PendingIntent

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,
                    "Servicio de Ubicacion no disponible", Toast.LENGTH_LONG).show();
        }

    }
    //***********************************************************************************************
    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Main.main);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
           // Log.d("Google Play services esta disponible.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                VerCatastrofeFragment.ErrorDialogFragment errorFragment = new VerCatastrofeFragment.ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(Main.main.getSupportFragmentManager(), "Actualizacion de Ubicacion");
            }

            return false;
        }
    }
    //*********************************************************************************************************
    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        if (isGooglePlayServicesAvailable()) {

            Toast.makeText(this,"Hay servicio Google play",
                    Toast.LENGTH_SHORT).show();
            mLocationClient.connect();
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
            }*/


        }
        else Toast.makeText(this,"No hay servicio Google play",
                Toast.LENGTH_SHORT).show();

    }
    //*******************************************************************************************
    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

        //****************************************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(this,"Conexion OK",
                                Toast.LENGTH_SHORT).show();
                        mLocationClient.connect();
                        break;
                }

        }
    }
    /*
        CONEXION ***************************************************************
     */

    //---------------FRAGMENT------------------------------------------------------------------


    //---------------------------FRAGMENT----------------------------------------------------------------------
    public void mostrar_aviso (String mensaje){

        //-----------------------------------
        dialogo= new AlertDialog.Builder(main)
                .setTitle("Aviso")
                .setMessage(mensaje)
                    /*.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })*/
                   /* .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })*/
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        //-------------------------------------------

    }


    public String codificar_password(String password) throws NoSuchAlgorithmException {

        StringBuffer hexString = new StringBuffer();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Digest(in hex format):: " + sb.toString());

        //convert the byte to hex format method 2

        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();

    }


}
