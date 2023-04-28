package reserva.varios;

import Estructura.ArbolBB;

public class Estudiante extends Persona{
    private String carrera;
    private int nivel;
    public Estudiante() {
        super();
    }

    public Estudiante(String cedula, String nombre, String apellido, Fecha fechaNacimiento, String carrera, int nivel) {
        this.cedula = cedula;
        this.carrera = carrera;
        this.nivel = nivel;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    /*
    COLOCAR ABSTRACCION DEL LIBRO, UTILIZAR LISTA AGAPITO 
    ///////////////////////
    lista libros (prestados)
    /////////////////////
    ///////////////////////
    /////////////////////////
    */

    public String getCarrera() {
        return carrera;
    }

    public int getNivel() {
        return nivel;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellido;
    }

    public Fecha getEdad() {
        return fechaNacimiento;
    }
    
    //Setter
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public void setNivel(int nivel) {
        this.nivel= nivel;
    }

    public void setCedula(String cedula) {
        this.cedula= cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellido= apellidos;
    }

    public void setEdad(Fecha edad) {
        this.fechaNacimiento= edad;
    }
    

    @Override
    public String toString(){
        return "_______________________________________________________________________"+"\n"+
                "Nombre: "+ this.nombre+"\n"+
                "Apellido: "+ this.apellido+"\n"+
                "Cédula: "+ this.cedula+"\n"+
                "Fecha de Nacimiento: "+ this.fechaNacimiento.getDía()+"/"+this.fechaNacimiento.getMes()+"/"+this.fechaNacimiento.getAño()+"\n"+
                "Carrera: "+ this.carrera+"\n"+
                "Nivel: "+ this.nivel+"\n";
    }
  
}
