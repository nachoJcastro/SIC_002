package tecnologo.com.sic.Utilidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Nacho_Local on 18/11/2014.
 */
public class Manejo_Red  extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

            String status = Utilidad_Red.estado_Conexion(context);

            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}