package Estructura;

public class ListaLineal {

    private Nodo inicio;

    public ListaLineal() {
        this.inicio = null;
    }

    //Agrega un incio a la lista, dejando los demas nodos
    public void IngresarInicio(Nodo elemento) {
        if (Vacia()) {
            this.inicio = elemento;
        } else {
            Nodo aux = this.inicio;
            this.inicio = elemento;
            elemento.setSig(aux);
        }
    }

    public void IngresarInicio(Object o){
        Nodo n = new Nodo(o);
        IngresarInicio(n);
    }
    
    //Ingresar un nodo al final de la lista
    public void IngresarFinal(Nodo fin) {
        Nodo aux;
        if (this.inicio == null) {
            this.inicio = fin;
        } else {
            aux = this.inicio;
            while (aux.getSig() != null) {
                aux = aux.getSig();
            }
            aux.setSig(fin);
        }
    }

    public void IngresarFinal(Object o){
        Nodo n = new Nodo(o);
        IngresarFinal(n);
    }
    
    //Quita el inicio de la lista y deja los demas nodos
    public Nodo Retirar() {
        Nodo aux;
        aux = inicio;
        inicio = inicio.getSig();
        return aux;
    }

    public Boolean Vacia() {
        return inicio == null;
    }

    public int NumElementos() {
        int cont = 0;
        Nodo aux = this.inicio;
        while (aux != null) {
            cont++;
            aux = aux.getSig();
        }
        return cont;
    }

    public String Reporte() {
        String s = "";
        Nodo aux = this.inicio;
        while (aux != null) {
            s += aux.getInfo() + " ";
            aux = aux.getSig();
        }
        if(s.equals(""))
            s = "Lista vacía";
        return s;
    }

    public Nodo getInicio() {
        return inicio;
    }
    
    public void Clear(){
        this.inicio = null;
    }
    
}
