/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reserva.varios;

/**
 *
 * @author bd678
 */
public class Reserva {
    private Libro libro;
    private int cantidad;
    private String Codigo;
    private String cedula;
    
    public Reserva(Libro libro, int cant,String codigof, String cedula){
        this.libro = libro;
        this.cantidad= cant;
        this.Codigo= codigof;
        this.cedula=cedula;
    }

    /**
     * @return the libro
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * @param libro the libro to set
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }
    
    /**
     * @return the cedula
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.libro.setNumeroPrestamos(cantidad);
    }
    
    
    @Override
    public String toString(){
        return "_______________________________________________________________________"+"\n"+
                "Cantidad de libros poseidos: "+this.cantidad+"\n"+this.libro.toString();
    }

    /**
     * @return the Codigo
     */
    public String getCodigo() {
        return Codigo;
    }

    /**
     * @param Codigo the Codigo to set
     */
    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }
}
