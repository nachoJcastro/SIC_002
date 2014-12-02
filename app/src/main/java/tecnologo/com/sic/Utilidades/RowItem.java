package tecnologo.com.sic.Utilidades;

import android.graphics.Bitmap;


public class RowItem {

    private String id;
    private String nombre;
    private String apellido;
    private String edad;
    private String sexo;
    private String fecha;
    private String contacto;
    private String telf;
    private Bitmap foto;

    public RowItem(String id , String nombre, String apellido, String edad, String sexo,String fecha,String contacto,
                   String telf, Bitmap foto) {

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.fecha = fecha;
        this.contacto = contacto;
        this.telf = telf;
        this.foto = foto;

    }
    //********************************************************
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
    //*********************************************************
    public String getapellido() {
        return apellido;
    }
    public void setapellido(String apellido) {
        this.apellido = apellido;
    }
    //*********************************************************
    public String getnombre() {
        return nombre;
    }
    public void setnombre(String nombre) {
        this.nombre = nombre;
    }
    //*********************************************************
    public String getedad() {
        return edad;
    }
    public void setedad(String edad) {
        this.edad = edad;
    }
    //*********************************************************
    public String getsexo() {
        return sexo;
    }
    public void setsexo(String sexo) {
        this.sexo = sexo;
    }
    //*********************************************************
    public String getfecha() {
        return fecha;
    }
    public void setfecha(String fecha) {
        this.fecha = fecha;
    }
    //*********************************************************
    public String getcontacto() {
        return contacto;
    }
    public void setcontacto(String contacto) {
        this.contacto = contacto;
    }
    //*********************************************************
    public String gettelf() {
        return telf;
    }
    public void settelf(String telf) {
        this.telf = telf;
    }
    //************************************************************
    public Bitmap getfoto() {
        return foto;
    }
    public void setfoto(Bitmap foto) {
        this.foto = foto;
    }
    //*************************************************************
}