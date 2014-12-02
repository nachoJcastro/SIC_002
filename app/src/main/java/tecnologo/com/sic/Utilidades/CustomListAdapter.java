package tecnologo.com.sic.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tecnologo.com.sic.R;

public class CustomListAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView foto;
        TextView txt_id;
        TextView txt_nombre;
        TextView txt_edad;
        TextView txt_apellido;
        TextView txt_telf;
        TextView txt_contacto;
        TextView txt_sexo;
        TextView txt_fecha;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listar_desaparecidos, null);
            holder = new ViewHolder();

            holder.txt_id = (TextView) convertView.findViewById(R.id.id_desaparecido);
            holder.txt_nombre = (TextView) convertView.findViewById(R.id.nombre);
            holder.txt_apellido = (TextView) convertView.findViewById(R.id.apellido);
            holder.txt_edad = (TextView) convertView.findViewById(R.id.edad);
            holder.txt_sexo = (TextView) convertView.findViewById(R.id.sexo);
            holder.txt_fecha = (TextView) convertView.findViewById(R.id.fecha);
            holder.txt_contacto = (TextView) convertView.findViewById(R.id.contacto);
            holder.txt_telf = (TextView) convertView.findViewById(R.id.telf);
            holder.foto = (ImageView) convertView.findViewById(R.id.foto);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txt_id.setText(rowItem.getid());
        holder.txt_nombre.setText(rowItem.getnombre());
        holder.txt_apellido.setText(rowItem.getapellido());
        holder.txt_sexo.setText(rowItem.getsexo());
        holder.txt_edad.setText(rowItem.getedad());
        holder.txt_fecha.setText(rowItem.getfecha());
        holder.txt_contacto.setText(rowItem.getcontacto());
        holder.txt_telf.setText(rowItem.gettelf());
        holder.foto.setImageBitmap(rowItem.getfoto());





        return convertView;
    }

}