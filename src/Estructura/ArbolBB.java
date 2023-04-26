package Estructura;

import java.io.Serializable;
import java.util.ArrayList;
import reserva.varios.*;

public class ArbolBB implements Serializable {

    //Auxiliar pa eliminar
    private int profundidad = 0;
    //Auxiliar para el .text
    private ArrayList datos = new ArrayList();

    /**
     * @return the raiz
     */
    public NodoABB getRaiz() {
        return raiz;
    }

    /**
     * @param raiz the raiz to set
     */
    public void setRaiz(NodoABB raiz) {
        this.raiz = raiz;
    }
    private NodoABB raiz;

    //Constructor
    public ArbolBB() {
        this.raiz = null;
    }

    //Metodo ingresar nodo
    public void Ingresar(Estudiante p) {
        NodoABB nuevo = new NodoABB(p);

        if (this.raiz == null) {
            this.raiz = nuevo;
        } else {
            NodoABB aux = raiz;
            while (aux != null) {
                int bandera = (((Estudiante) aux.getInfo()).getCedula().compareTo(p.getCedula()));
                if (bandera < 0) {
                    if (aux.gethDer() == null) {
                        aux.sethDer(nuevo);
                        break;
                    }
                    aux = aux.gethDer();
                } else if (bandera > 0) {
                    if (aux.gethIzq() == null) {
                        aux.sethIzq(nuevo);
                        break;
                    }
                    aux = aux.gethIzq();
                } else {
                    break;
                }
            }
        }
    }

    //METODO PARA INGRESAR UN LIBRO 
    public void Ingresar(Libro p) {
        NodoABB nuevo = new NodoABB(p);

        if (this.raiz == null) {
            this.raiz = nuevo;
        } else {
            NodoABB aux = raiz;
            while (aux != null) {
                int bandera = (((Libro) aux.getInfo()).getCodigo().compareTo(p.getCodigo()));
                if (bandera < 0) {
                    if (aux.gethDer() == null) {
                        aux.sethDer(nuevo);
                        break;
                    }
                    aux = aux.gethDer();
                } else if (bandera > 0) {
                    if (aux.gethIzq() == null) {
                        aux.sethIzq(nuevo);
                        break;
                    }
                    aux = aux.gethIzq();
                } else {
                    break;
                }
            }
        }
    }
    //METODO PARA INGRESAR UNA RESERVA
    public void Ingresar(String[] p) {
        NodoABB nuevo = new NodoABB(p);

        if (this.raiz == null) {
            this.raiz = nuevo;
        } else {
            NodoABB aux = raiz;
            while (aux != null) {
                int bandera = (((String[]) aux.getInfo())[0].compareTo(p[0]));
                if (bandera < 0) {
                    if (aux.gethDer() == null) {
                        aux.sethDer(nuevo);
                        break;
                    }
                    aux = aux.gethDer();
                } else if (bandera > 0) {
                    if (aux.gethIzq() == null) {
                        aux.sethIzq(nuevo);
                        break;
                    }
                    aux = aux.gethIzq();
                } else {
                    break;
                }
            }
        }
    }

    //PREORDER
    public String Preorder(NodoABB r) {
        String res = "";
        Estudiante estudiante;
        if (r != null) {
            estudiante = (Estudiante) r.getInfo();
            res += estudiante.toString() + "\n";
            res += Preorder(r.gethIzq());
            res += Preorder(r.gethDer());
        }
        return res;
    }

    //INORDER
    public String Inorder(NodoABB r) {
        String res = "";
        Estudiante estudiante;
        if (r != null) {
            res += Inorder(r.gethIzq());
            estudiante = (Estudiante) r.getInfo();
            res += estudiante.toString() + "\n";
            res += Inorder(r.gethDer());
        }
        return res;
    }

    //INORDER para libros
    public String Inorderl(NodoABB r) {
        String res = "";
        Libro libro;
        if (r != null) {
            res += Inorderl(r.gethIzq());
            libro = (Libro) r.getInfo();
            res += libro.toString() + "\n";
            res += Inorderl(r.gethDer());
        }
        return res;
    }
    //INORDER para libros parametrizado

    public String InorderParametric(NodoABB r, String Categoria) {
        String res = "";
        Libro libro;
        if (r != null) {
            res += InorderParametric(r.gethIzq(),Categoria);
            libro = (Libro) r.getInfo();
            if (libro.getCategoria().equals(Categoria)||Categoria.equals("Mostrar Todos")) {
                 res += libro.toString() + "\n";
            }
            res += InorderParametric(r.gethDer(),Categoria);
        }
        return res;
    }

    //POSORDER
    public String Posorder(NodoABB r) {
        String res = "";
        Estudiante estudiante;
        if (r != null) {
            res += Posorder(r.gethIzq());
            res += Posorder(r.gethDer());
            estudiante = (Estudiante) r.getInfo();
            res += estudiante.toString() + "\n";
        }
        return res;
    }


    //METODO RECURSIVO PARA ENCONTRAR EL TAMAÑO DE UN ÁRBOL
    public int tamaño(NodoABB r) {
        int res = 0;
        if (r != null) {
            res++;
            res += tamaño(r.gethIzq());
            res += tamaño(r.gethDer());
        }
        return res;
    }
    //METODO PARA BÚSQUEDA

    public String getCedula(NodoABB nodo) {
        return ((Estudiante) nodo.getInfo()).getCedula();
    }

    public NodoABB Busqueda(String ced, NodoABB nodo) {
        String s = "";
        if (nodo == null) {
            return null;
        }
        s = getCedula(nodo);

        if (ced.equals(s)) {
            return nodo;
        } else {
            if (s.compareTo(ced) < 0) {
                return Busqueda(ced, nodo.gethDer());
            } else {
                return Busqueda(ced, nodo.gethIzq());
            }
        }
    }

    public NodoABB Busqueda(String ced) {
        return Busqueda(ced, this.raiz);
    }

    //METODO PARA BUSQUEDA EN LIBROS
    public String getCodigo(NodoABB nodo) {
        return ((Libro) nodo.getInfo()).getCodigo();
    }

    public NodoABB Busquedal(String cod, NodoABB nodo) {
        String s = "";
        if (nodo == null) {
            return null;
        }
        s = getCodigo(nodo);

        if (cod.equals(s)) {
            return nodo;
        } else {
            if (s.compareTo(cod) < 0) {
                return Busquedal(cod, nodo.gethDer());
            } else {
                return Busquedal(cod, nodo.gethIzq());
            }
        }
    }

    public NodoABB Busquedal(String cod) {
        return Busquedal(cod, this.raiz);
    }

    //MÉTODO PARA ALTURA
    public int Altura(NodoABB nodo) {

        if (nodo == null) {
            return 0;
        } else {
            int r, l;
            r = Altura(nodo.gethDer());
            l = Altura(nodo.gethIzq());
            if (r > l) {
                return r + 1;
            } else {
                return l + 1;
            }
        }
    }

    public int Altura(String cd) {
        return Altura(Busqueda(cd));
    }

    //Metodo Profundidad
    public int Profundidad(NodoABB r) {
        int aux = this.profundidad;
        this.profundidad = 0;
        return aux;
    }

    public int Profundidad(String ced, NodoABB nodo) {
        String s = "";
        if (nodo == null) {
            return -10;
        }
        s = getCedula(nodo);

        if (ced.equals(s)) {
            return 0;
        } else {
            if (s.compareTo(ced) < 0) {
                return Profundidad(ced, nodo.gethDer()) + 1;
            } else {
                return Profundidad(ced, nodo.gethIzq()) + 1;
            }
        }
    }

    public int prof(String ced) {
        return Profundidad(ced, this.raiz);
    }

    //Metodo para Eliminar
    public void EliminarNodo(String id, NodoABB raiz, NodoABB aux) {
        if (aux == null) {
            return;
        }
        if (raiz == aux && aux.gethIzq() == null && aux.gethDer() == null) {
            this.raiz = null;
        }
        int flag = Comparar(id, aux);

        if (flag < 0) {
            raiz = aux;
            aux = aux.gethIzq();
            EliminarNodo(id, raiz, aux);
        } else if (flag > 0) {
            raiz = aux;
            aux = aux.gethDer();
            EliminarNodo(id, raiz, aux);
        } else {
            boolean hoja = aux.gethIzq() == null && aux.gethDer() == null;
            if (hoja) {//caso 1
                if (raiz.gethIzq() == aux) {
                    raiz.sethIzq(null);
                } else {
                    raiz.sethDer(null);
                }
            } else {
                if (aux.gethIzq() != null && aux.gethDer() != null) {//caso 2
                    NodoABB info = MenorDeMayores(aux.gethDer());//siempre va a ser hoja
                    EliminarNodo(((Estudiante) info.getInfo()).getCedula(), aux, aux);//elimina el nodo repetido
                    aux.setInfo(info.getInfo());
                } else {
                    if (this.raiz != aux) {//caso 3
                        if (aux.gethIzq() != null) {
                            //Por cual enlace entra aux
                            if (raiz.gethIzq() == aux) {
                                raiz.sethIzq(aux.gethIzq());
                            } else {
                                raiz.sethDer(aux.gethIzq());
                            }
                        } else {
                            //Por donde entra
                            System.out.println("aa");
                            if (raiz.gethIzq() == aux) {
                                raiz.sethIzq(aux.gethDer());
                            } else {
                                raiz.sethDer(aux.gethDer());
                            }
                        }
                    } else {
                        //Caso especial cuando la raiz tiene una rama
                        if (aux.gethIzq() != null) {
                            this.raiz = aux.gethIzq();
                        } else {
                            this.raiz = aux.gethDer();
                        }
                    }
                }
            }
        }
    }

    //Metodo para Eliminar liibro
    public void EliminarNodol(String id, NodoABB raiz, NodoABB aux) {
        if (aux == null) {
            return;
        }
        if (raiz == aux && aux.gethIzq() == null && aux.gethDer() == null) {
            this.raiz = null;
        }
        int flag = Compararl(id, aux);

        if (flag < 0) {
            raiz = aux;
            aux = aux.gethIzq();
            EliminarNodo(id, raiz, aux);
        } else if (flag > 0) {
            raiz = aux;
            aux = aux.gethDer();
            EliminarNodo(id, raiz, aux);
        } else {
            boolean hoja = aux.gethIzq() == null && aux.gethDer() == null;
            if (hoja) {//caso 1
                if (raiz.gethIzq() == aux) {
                    raiz.sethIzq(null);
                } else {
                    raiz.sethDer(null);
                }
            } else {
                if (aux.gethIzq() != null && aux.gethDer() != null) {//caso 2
                    NodoABB info = MenorDeMayores(aux.gethDer());//siempre va a ser hoja
                    EliminarNodo(((Libro) info.getInfo()).getCodigo(), aux, aux);//elimina el nodo repetido
                    aux.setInfo(info.getInfo());
                } else {
                    if (this.raiz != aux) {//caso 3
                        if (aux.gethIzq() != null) {
                            //Por cual enlace entra aux
                            if (raiz.gethIzq() == aux) {
                                raiz.sethIzq(aux.gethIzq());
                            } else {
                                raiz.sethDer(aux.gethIzq());
                            }
                        } else {
                            //Por donde entra
                            System.out.println("aa");
                            if (raiz.gethIzq() == aux) {
                                raiz.sethIzq(aux.gethDer());
                            } else {
                                raiz.sethDer(aux.gethDer());
                            }
                        }
                    } else {
                        //Caso especial cuando la raiz tiene una rama
                        if (aux.gethIzq() != null) {
                            this.raiz = aux.gethIzq();
                        } else {
                            this.raiz = aux.gethDer();
                        }
                    }
                }
            }
        }
    }

    //Retornar nodo
    public NodoABB retornarNododePersona(String cedula) {
        profundidad = 0;
        NodoABB nodo_aux = this.getRaiz();
        Estudiante persona_aux = new Estudiante(cedula, "", "", new Fecha(0, 0, 0), "", 0);
        NodoABB nodo_persona = new NodoABB(persona_aux);
        while (!cedula.equals(((Estudiante) nodo_aux.getInfo()).getCedula())) {
            profundidad++;
            //System.out.println("nodo_aux: "+nodo_aux.getInfo());
            if (nodo_aux.Mayorque(nodo_persona)) {
                nodo_aux = nodo_aux.gethIzq();
            } else {
                nodo_aux = nodo_aux.gethDer();
            }
            if (nodo_aux == null) {
                return null;
            }
        }
        return nodo_aux;
    }

    //Busca el mayor de los menores
    public NodoABB MayorDeMenores(NodoABB subraiz) {
        NodoABB aux1 = subraiz.gethDer();
        NodoABB aux2 = subraiz.gethIzq();
        if (aux2 == null) {
            return aux1;
        }
        while (aux2.gethDer() != null) {
            aux2 = aux2.gethDer();
        }
        aux2.sethDer(aux1);
        return aux2;
    }

    //Menor de mayores
    public NodoABB MenorDeMayores(NodoABB raiz_der) {
        if (raiz_der.gethIzq() == null) {
            return raiz_der;
        } else {
            return MenorDeMayores(raiz_der.gethIzq());
        }
    }
    //Método para comparar

    public int Comparar(String id, NodoABB A) {
        return id.compareTo(((Estudiante) A.getInfo()).getCedula());
    }

    //Método para comparar Libro
    public int Compararl(String id, NodoABB A) {
        return id.compareTo(((Libro) A.getInfo()).getCodigo());
    }

    //Métodos para generar el .text
    public ListaLineal Niveles(ListaLineal lista) {
        lista.Clear();
        lista.IngresarInicio(this.raiz);
        Nodo n = lista.getInicio();

        while (n != null) {
            if (((NodoABB) n.getInfo()).gethIzq() != null) {
                lista.IngresarFinal(((NodoABB) n.getInfo()).gethIzq());
            }
            if (((NodoABB) n.getInfo()).gethDer() != null) {
                lista.IngresarFinal(((NodoABB) n.getInfo()).gethDer());
            }
            n = n.getSig();

        }

        return lista;
    }
    ////////////////////PRUEBAAAAAAAAAAAAAAAAAAA///////////////////////////////////////////

}
