package es.cm.dam.dos.pmdm.mismapas;

public class Parada {

    String latitud;
    String longitud;
    String direccion;
    String hora;

    public Parada(String latitud, String longitud, String direccion, String hora) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.hora = hora;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


}
