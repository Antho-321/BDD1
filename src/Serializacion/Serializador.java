package Serializacion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Estructura.ListaLineal;
import Estructura.ArbolBB;
import Estructura.NodoABB;
import reserva.varios.Persona;
import reserva.varios.Estudiante;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import reserva.varios.Libro;


public class Serializador {
    final private Gson gson;
    private File archivo;

    public Serializador() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public boolean SerializarEstudiante(String ruta, ListaLineal lista){
        
        try {
            archivo = new File(ruta);
        } catch (Exception e) {
            return false;
        }
        
        Object a;
        String jSonString = "";
        
        try (var pw = new PrintWriter(archivo)) {
            
            while(!lista.Vacia()){
                a = (NodoABB)lista.Retirar().getInfo();
                jSonString = TransformarJSon(gson, (Persona)((NodoABB)a).getInfo());
                EscribirJson(pw, jSonString);
            }  
        }catch (Exception e) {
            return false;
        }      
        
        return true;
        
    }
    
    public boolean DeserializarEstudiante(String ruta, ArbolBB arbol){
        String stringJson = "";
        String linea;
        
        try {
            FileReader fileReader = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fileReader);
            //System.out.println("Archivo encontrado");
            
            Estudiante es;
            while ((linea = br.readLine()) != null) {                
                stringJson += linea;
                if(linea.compareTo("}") == 0){
                    es = gson.fromJson(stringJson, Estudiante.class);
                    
                    arbol.Ingresar(es);
                   
                    stringJson = "";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    
    //PARTE DE SERIALIZACION DE LOS LIBROS
    public boolean SerializarLibro(String ruta, ListaLineal lista){
        try {
            archivo = new File(ruta);
        } catch (Exception e) {
            return false;
        }
        Object a;
        String jSonString = "";
        try (var pw = new PrintWriter(archivo)) {
            
            while(!lista.Vacia()){
                a = (NodoABB)lista.Retirar().getInfo();
                jSonString = TransformarJSon(gson, (Libro)((NodoABB)a).getInfo());
                EscribirJson(pw, jSonString);
            }  
        }catch (Exception e) {
            return false;
        }
        return true;
    }
    //PARTE DE SERIALIZACION DE LAS RESERVAS
    public boolean SerializarReserva(String ruta, ListaLineal lista){
        try {
            archivo = new File(ruta);
        } catch (Exception e) {
            return false;
        }
        Object a;
        String jSonString = "";
        try (var pw = new PrintWriter(archivo)) {
            
            while(!lista.Vacia()){
                a = (NodoABB)lista.Retirar().getInfo();
                jSonString = TransformarJSon(gson, (String[])((NodoABB)a).getInfo());
                EscribirJson(pw, jSonString);
            }  
        }catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean DeserializarLibro(String ruta, ArbolBB arbol){
        String stringJson = "";
        String linea;
        
        try {
            FileReader fileReader = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fileReader);
            //System.out.println("Archivo encontrado");
            Libro lib;
            while ((linea = br.readLine()) != null) {                
                stringJson += linea;
                if(linea.compareTo("}") == 0){
                    lib = gson.fromJson(stringJson, Libro.class);
                    
                    arbol.Ingresar(lib);
                   
                    stringJson = "";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    public boolean DeserializarReserva(String ruta, ArbolBB arbol){
        String stringJson = "";
        String linea;
        
        try {
            FileReader fileReader = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fileReader);
            //System.out.println("Archivo encontrado");
            String[] lib;
            while ((linea = br.readLine()) != null) {                
                stringJson += linea;
                if(linea.compareTo("}") == 0){
                    lib = gson.fromJson(stringJson, String[].class);
                    
                    arbol.Ingresar(lib);
                    stringJson = "";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    
    public static String TransformarJSon(Gson gson, Object o){
        return gson.toJson(o);
    }
    public static void EscribirJson(PrintWriter pr,String s){
        pr.write(s+"\n");
    }
    
}
