package com.appclass.appclass;
//http://www.hermosaprogramacion.com/2014/10/android-listas-adaptadores/
public class Clase {
    private String codigo;
    private String nombre;
    private String correo;
    private int cantidadAlumnos;

    public static String genCodigo(int longitud) {
        String codigo="";
        char c;
        while(codigo.length()<longitud) {
            if(Math.random()<0.5) {
                c =(char) getChar(true);
            }
            else {
                c =(char) getChar(false);
            }
            codigo = codigo.concat( String.valueOf(c));
        }
        return codigo;
    }

    private static int getChar(boolean min) {
        int c = (int)(Math.random()*26);
        if( min ) {
            return c+97;
        }
        else {
            return c+65;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public void setCantidadAlumnos(int cantidadAlumnos) {
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public Clase() {

    }

    public Clase(String codigo, String nombre, String correo, int cantidadAlumnos) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.correo = correo;
        this.cantidadAlumnos = cantidadAlumnos;
    }

    @Override
    public String toString() {
        int max=30;
        int t = getNombre().length();
        return t>max?getNombre().substring(0,max/2-2)+"..."+getNombre().substring(t-(max/2),t):getNombre();
    }
}
