package Estructura;

public class Nodo {

    private Object info;
    private Nodo sig;
    
    public Nodo(Object info){
        this.info = info;
        this.sig = null;
    }

    public Object getInfo() {
        return info;
    }

    public Nodo getSig() {
        return sig;
    }

    public void setSig(Nodo sig) {
        this.sig = sig;
    }
    
    @Override
    public String toString(){
        return this.info.toString();
    }
    
    
    
}
