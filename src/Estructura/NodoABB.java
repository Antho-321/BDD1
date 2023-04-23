/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructura;

import java.io.Serializable;
import reserva.varios.Estudiante;

/**
 *
 * @author bd678
 */
public class NodoABB implements Serializable {
    private Object info;
    private NodoABB hIzq;
    private NodoABB hDer;
    
    
    //Constructor
    public NodoABB(){
        this.info = null;
    }
    //Constructor con objeti
    public NodoABB(Object info){
        this.info = info;
    }

    /**
     * @return the info
     */
    public Object getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(Object info) {
        this.info = info;
    }

    /**
     * @return the hIzq
     */
    public NodoABB gethIzq() {
        return hIzq;
    }

    /**
     * @param hIzq the hIzq to set
     */
    public void sethIzq(NodoABB hIzq) {
        this.hIzq = hIzq;
    }

    /**
     * @return the hDer
     */
    public NodoABB gethDer() {
        return hDer;
    }

    /**
     * @param hDer the hDer to set
     */
    public void sethDer(NodoABB hDer) {
        this.hDer = hDer;
    }
    //Método para ver cual es mayor en comparación de nodos
    public boolean Mayorque(NodoABB nodo){
        String esteinfo;
        String infoingresado;
        if (this.getInfo() instanceof Estudiante) {
            esteinfo = ((Estudiante)this.getInfo()).getCedula();
            infoingresado = ((Estudiante)nodo.getInfo()).getCedula();
        }else{
            esteinfo = ""+this.getInfo();
            infoingresado = ""+nodo.getInfo();
        }
        return esteinfo.compareTo(infoingresado)>=1;
    }
    //Método para ver cual es menor en comparación de nodos
    public boolean Menorque(NodoABB nodo){
        String esteinfo;
        String infoingresado;
        if (this.getInfo() instanceof Estudiante) {
            esteinfo = ((Estudiante)this.getInfo()).getCedula();
            infoingresado = ((Estudiante)nodo.getInfo()).getCedula();
        }else{
            esteinfo = ""+this.getInfo();
            infoingresado = ""+nodo.getInfo();
        }
        return esteinfo.compareTo(infoingresado)<=-1;
    }
}
