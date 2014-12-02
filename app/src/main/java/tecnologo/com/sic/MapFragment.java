package tecnologo.com.sic;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment
         {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationClient mLocationClient;
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
             private View root;

             @Override
             public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
             {
                // super.onCreateView(inflater, container, savedInstanceState);

                 if(root!=null){
                     ViewGroup parent = (ViewGroup) root.getParent();
                     if (parent != null)
                         parent.removeView(root);


                 }


                  root = inflater.inflate(R.layout.mapa, container, false);


                 
                 
                 iniciar_Mapa();
                 return root;
             }

             private void iniciar_Mapa() {

                 mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
                 if (mapFragment == null) {
                     FragmentManager fragmentManager = getFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     mapFragment = SupportMapFragment.newInstance();
                     fragmentTransaction.replace(R.id.map, mapFragment).commit();
                 }
                 if (mapFragment != null)
                 {
                     map = mapFragment.getMap();
                     if (map != null)
                         map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                         {
                             @Override
                             public void onMapClick(LatLng point){

                             }
                         });
                 }
             }

             @Override
             public void onDestroyView() {
            // TODO Auto-generated method stub
                 super.onDestroyView();
                 if (map != null) {
                     getActivity().getSupportFragmentManager().beginTransaction()
                             .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).commit();
                     map = null;
                 }
             }
}


