package tecnologo.com.sic.Utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Nacho_Local on 18/11/2014.
 */
public class Utilidad_Red {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_SIN_CONEXION = 0;


    public static int obtener_Estado_Conexion(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_SIN_CONEXION;
    }

    public static String estado_Conexion(Context context) {
        int conn = Utilidad_Red.obtener_Estado_Conexion(context);
        String status = null;
        if (conn == Utilidad_Red.TYPE_WIFI) {
            status = "Wifi habilitada";
        } else if (conn == Utilidad_Red.TYPE_MOBILE) {
            status = "Conexión de datos habilitada";
        } else if (conn == Utilidad_Red.TYPE_SIN_CONEXION) {
            status = "Sin conexión a Internet";
        }
        return status;
    }
}