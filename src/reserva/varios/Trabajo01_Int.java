package reserva.varios;

import java.util.Date;
import javax.swing.JOptionPane;
import Estructura.*;
import Serializacion.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Estructura.ListaLineal;
import Estructura.ArbolBB;
import Estructura.NodoABB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;

public class Trabajo01_Int extends javax.swing.JFrame {

    /*
    /////////////////////
    AQUI VA LA LISTA DE ESTUDIANTES EN GENERAL, Y LA LISTA DE LIBROS
    /////////////////////
     */
    ArbolBB listaEstudiantes = new ArbolBB();
    ArbolBB listaLibros = new ArbolBB();
    
    //Mis variables para serializar
    String rutaArchivo;
    ListaLineal listaNiveles;
    Serializador serializador;
    //Variable para abrir menu
    javax.swing.JFileChooser fcMenu = new javax.swing.JFileChooser();
    
    
    public Trabajo01_Int() {
        initComponents();
        
        listaNiveles = new ListaLineal();
        rutaArchivo = "";
        serializador = new Serializador();
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Guardar cambios?", "", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    fcMenu.showSaveDialog(fcMenu);
                    rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
                    listaEstudiantes.Niveles(listaNiveles);
                    System.out.println(serializador.SerializarEstudiante(rutaArchivo, listaNiveles));
                }
            }
        });
    }

    //-------------------------------------------------------
    //MÉTODOS PARA TODOS LOS BOTONES SEGÚN SU FUNCIONALIDAD
    //-------------------------------------------------------
    //PARTE DE INGRESO DE ESTUDIANTES EN EL FORMULARIO
    //MÉTODO PARA VALIDAR UNA CÉDULA
    public boolean cedulaValida(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        int total = 0;
        int digitoVerificador = Integer.parseInt(cedula.substring(9));
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};

        for (int i = 0; i < coeficientes.length; i++) {
            int val = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
            total += val > 9 ? val - 9 : val;
        }
        int verificadorObtenido = total % 10 == 0 ? 0 : 10 - total % 10;
        return digitoVerificador == verificadorObtenido;
    }
//MÉTODO PARA VALIDAR UN STRING SEGÚN UNA EXPRESIÓN REGULAR
    public boolean StringVálido(String expRegular, String strAEvaluar){
        Pattern pattern = Pattern.compile(expRegular);
        // Create a matcher object from the input string
        Matcher matcher = pattern.matcher(strAEvaluar);
        // Return true if the input matches the pattern, false otherwise
        return matcher.matches();
    }
    //MÉTODO PARA VALIDAR EL NOMBRE Y APELLIDO
    public boolean nombreApellidoValido(String input) {
        return StringVálido("^[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]*$", input);
    }
    //MÉTODO PARA VALIDAR LAS PALABRAS QUE SE INGRESEN EN LA VENTANA DE REGISTRO DE LIBROS
    public boolean tituloRegLibrosValido(String input) {
        return StringVálido("[A-ZÁÉÍÓÚÜÑ0-9][a-záéíóúüñ\\s]*", input);
    }
    public boolean nombreRegLibrosValido(String input) {
        return StringVálido("[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ\\s]*", input);
    }

    //Metodo para ingreso de estudiantes
    public void ingresarEstudiante() {
        String nombre = null;
        String apellido = null;

        String carrera = (String) comboBoxCarreraRegEst.getSelectedItem();
        int nivel = Integer.parseInt((String) comboBoxNivelRegEst.getSelectedItem());

        try {
            if (nombreApellidoValido(txtNombreRegEst.getText())) {
                nombre = txtNombreRegEst.getText();
            } else {
                throw new NullPointerException("nombreInv");
            }
            if (nombreApellidoValido(txtApellidoRegEst.getText())) {
                apellido = txtApellidoRegEst.getText();
            } else {
                throw new NullPointerException("apellidoInv");
            }
            String cedula = null;
            if (cedulaValida(txtCedulaRegEst.getText())) {
                cedula = txtCedulaRegEst.getText();
            } else {
                throw new NullPointerException("cedulaInv");
            }
            int dia = Integer.parseInt(txtDiaRegEst.getText());
            int mes = Integer.parseInt(txtMesRegEst.getText());
            int año = Integer.parseInt(txtAñoRegEst.getText());

            Fecha fn = new Fecha(dia, mes, año);
            Estudiante e = new Estudiante(cedula, nombre, apellido, fn, carrera, nivel);

            listaEstudiantes.Ingresar(e);
            /*
            /////////////////
            PARTE DE VALIDACION DE DATOS, SOLO ESTÁ LA FECHA
            /////////////////
             */

            JOptionPane.showMessageDialog(null, "Estudiante Registrado Correctamente");

            txtAreaRegEst.setText("Estudiante Registrado:\n" + e);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre números en la fecha de nacimiento");
        } catch (NullPointerException e) {
            switch (e.getMessage()) {
                case "nombreInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre válido");
                    break;
                case "apellidoInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un apellido válido");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Por favor registre un número de cédula válido");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre una fecha de nacimiento válida");
        }
    }

    //Método para reporte de estudiantes (inorder para mejor comprension)
    public void verEstudiantesRegistrados() {
        txtAreaRegEst.setText("Lista de estudiantes registrados:\n" + listaEstudiantes.Inorder(listaEstudiantes.getRaiz()));
    }

    //Metodo para verificar estudiantes
    public String verificarEstudiantes(String cedula) {
        if (!cedula.equals("")) {
            try {
                Estudiante e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
                String pantalla = "Datos del estudiante solicitado:\n" + e.toString();
                return pantalla;
            } catch (Exception e) {
                return "Ningún Estudiante encontrado";
            }

        } else {

            JOptionPane.showMessageDialog(null, "Primero debes ingresar tu numero de cédula en el campo 'cedula' para saber si ya estas registrado");
            return "";
        }

    }

    //Metodo para Eliminar estudiante
    public void eliminarEstudiante() {
        String cedula = txtCedulaRegEst.getText();
        if (!cedula.equals("")) {
            try {
                Estudiante e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
                String Info = e.toString();
                txtAreaRegEst.setText("Este estudiante se ha eliminado:\n" + Info);
                listaEstudiantes.EliminarNodo(cedula, listaEstudiantes.getRaiz(), listaEstudiantes.getRaiz());
            } catch (Exception e) {
                txtAreaRegEst.setText("Ningún Estudiante encontrado");
            }

        } else {

            JOptionPane.showMessageDialog(null, "Primero debes ingresar tu numero de cédula en el campo 'cedula' para saber si ya estas registrado");
        }
    }

    //Metodo para modificar estudiante
    public void modificarEstudiante() {
        String cedula = txtCedulaRegEst.getText();
        if (!cedula.equals("")) {
            try {
                //Guarda la información antigua para ser mostrada después
                Estudiante e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
                String oldData = "Datos antiguos:\n" + e.toString() + "\n";
                //Aqui se asignan los nuevos valores
                String nombre = txtNombreRegEst.getText();
                String apellido = txtApellidoRegEst.getText();
                String carrera = (String) comboBoxCarreraRegEst.getSelectedItem();
                int nivel = Integer.parseInt((String) comboBoxNivelRegEst.getSelectedItem());
                int dia = Integer.parseInt(txtDiaRegEst.getText());
                int mes = Integer.parseInt(txtMesRegEst.getText());
                int año = Integer.parseInt(txtAñoRegEst.getText());
                Fecha fn = new Fecha(dia, mes, año);
                //Aqui se modifica
                e.setNombre(nombre);
                e.setApellidos(apellido);
                e.setCarrera(carrera);
                e.setNivel(nivel);
                e.setEdad(fn);
                //Muestra los datos modificados
                String newData = "---------------------------------------------------\n" + "Datos modificados:\n" + e.toString();
                String pantalla = oldData + newData;
                txtAreaRegEst.setText(pantalla);
            } catch (Exception e) {
                txtAreaRegEst.setText("Ningún Estudiante encontrado, o datos mal ingresados en modificación");
            }

        } else {

            JOptionPane.showMessageDialog(null, "Primero debes ingresar tu numero de cédula en el campo 'cedula' para saber si ya estas registrado");
        }
    }

    //PARTE DE INGRESO DE LIBROS EN EL FORMULARIO
    //Ingresar un libro
    public void ingresarLibro() {
        String nombre = null;
        String autor = null;
        String categoria = (String) comboBoxCategoriaRegLibro.getSelectedItem();
        String materia = (String) comboBoxMateriaRegLibro.getSelectedItem();

        try {
            if (tituloRegLibrosValido(txtNombreRegLibro.getText())) {
                nombre = txtNombreRegLibro.getText();
            } else {
                throw new NullPointerException("nombreInv");
            }
            if (nombreRegLibrosValido(txtAutorRegLibro.getText())) {
                autor = txtAutorRegLibro.getText();
            } else {
                throw new NullPointerException("apellidoInv");
            }

            int añoEdicion = Integer.parseInt(txtAñoEdicionRegLibro.getText());
            int numeroCopias = Integer.parseInt(txtNumeroCopiasRegLibro.getText());
            int numeroDisponibles = Integer.parseInt(txtNumeroDisponiblesRegLibro.getText());

            String codigo = generarCodigoLibro(nombre, añoEdicion, numeroCopias);

            Libro lib = new Libro(categoria, codigo, nombre, autor, materia, materia, añoEdicion, numeroCopias, numeroDisponibles);

            listaLibros.Ingresar(lib);

            /*
            //////////////////
            PARTE DE VALIDACION DE DATOS, SOLO ESTÁ LA PARTE NUMÉRICA
            //////////////////
             */
            JOptionPane.showMessageDialog(null, "Libro Registrado Correctamente");

            txtAreaRegLibro.setText("Libro Registrado:\n" + lib);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre números en la parte numérica");
            /////////////////////////////ESTE CATCH SE DEBE CORREGIR E IMPLEMENTAR PARA LIBROS//////////////////
        } catch (NullPointerException e) {
            switch (e.getMessage()) {
                case "nombreInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre válido");
                    break;
                case "apellidoInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un autor válido");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Por favor registre un número de cédula válido");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre una fecha de nacimiento válida");
        }
    }

    //Método para generar código en un libro
    public String generarCodigoLibro(String nombre, int año, int numCopias) {
        String cod = nombre.substring(0, 2);
        return cod + año + numCopias;
    }

    //Método para reporte de estudiantes (inorder para mejor comprension)
    public void verLibrosRegistrados() {
        txtAreaRegLibro.setText("Lista de libros registrados:\n" + listaLibros.Inorderl(listaLibros.getRaiz()));
    }

    //Metodo para Eliminar libro
    public void eliminarLibro() {
        String cod = txtCodLibro.getText();
        if (!cod.equals("")) {
            try {
                Libro e = (Libro) listaLibros.Busquedal(cod).getInfo();
                String Info = e.toString();
                txtAreaRegLibro.setText("Este libro se ha eliminado:\n" + Info);
                listaLibros.EliminarNodol(cod, listaLibros.getRaiz(), listaLibros.getRaiz());
            } catch (Exception e) {
                txtAreaRegLibro.setText("Ningún libro encontrado");
            }

        } else {

            JOptionPane.showMessageDialog(null, "Primero debes ingresar el codigo del libro a eliminar");
        }
    }

    //Método para verificar un libro
    public String verificarLibro(String codigo) {
        try {
            Libro e = (Libro) listaLibros.Busquedal(codigo).getInfo();
            String pantalla = "Libro encontrado!:\n" + e.toString() + "\n";
            return pantalla;
        } catch (Exception e) {
            return "No se ha encontrado ningún libro con este código";
        }

    }

    //Méotdo para modificar libro 
    public void modificarLibro() {

        String nombre = null;
        String autor = null;
        String categoria = (String) comboBoxCategoriaRegLibro.getSelectedItem();
        String materia = (String) comboBoxMateriaRegLibro.getSelectedItem();
        String codigo = txtCodLibro.getText();
        try {
            //Guarda la información antigua para ser mostrada después
            Libro e = (Libro) listaLibros.Busquedal(codigo).getInfo();
            String oldData = "Datos antiguos:\n" + e.toString() + "\n";
            //Guarda la información antigua para ser mostrada después
            /////////////////////////////////////////////////////////////
            if (nombreApellidoValido(txtNombreRegLibro.getText())) {
                nombre = txtNombreRegLibro.getText();
            } else {
                throw new NullPointerException("nombreInv");
            }
            if (nombreApellidoValido(txtAutorRegLibro.getText())) {
                autor = txtAutorRegLibro.getText();
            } else {
                throw new NullPointerException("apellidoInv");
            }

            int añoEdicion = Integer.parseInt(txtAñoEdicionRegLibro.getText());
            int numeroCopias = Integer.parseInt(txtNumeroCopiasRegLibro.getText());
            int numeroDisponibles = Integer.parseInt(txtNumeroDisponiblesRegLibro.getText());

            //Aqui se asignan los nuevos valores
            e.setAutor(autor);
            e.setAñoEdicion(añoEdicion);
            e.setCategoria(categoria);
            e.setMateria(materia);
            e.setNombre(nombre);
            e.setNumeroDisponibles(numeroDisponibles);
            e.setNumeroCopias(numeroCopias);
            /*
            //////////////////
            PARTE DE VALIDACION DE DATOS, SOLO ESTÁ LA PARTE NUMÉRICA
            //////////////////
             */
            JOptionPane.showMessageDialog(null, "Libro Modificado Correctamente");
            //Muestra los datos modificados
            String newData = "---------------------------------------------------\n" + "Datos modificados:\n" + e.toString();
            String pantalla = oldData + newData;
            txtAreaRegLibro.setText(pantalla);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre números en la parte numérica");
            /////////////////////////////ESTE CATCH SE DEBE CORREGIR E IMPLEMENTAR PARA LIBROS//////////////////
        } catch (NullPointerException e) {
            switch (e.getMessage()) {
                case "nombreInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre válido");
                    break;
                case "apellidoInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un autor válido");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Por favor registre un número de cédula válido");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Por favor registre una fecha de nacimiento válida");
        }
    }

    //ESTA ES LA PARTE DE METODOS PARA EL MEDIO DE REDSERVA
    //Reservar libro
    public void reservar() {
        String codigo, cedula;
        codigo = txtCodLibroResLibro.getText();
        cedula = txtCedulaResLibro.getText();
        int requeridos = Integer.parseInt((String) comboBoxCantidadLibrosResLibro.getSelectedItem());
        Estudiante e;
        Libro l, l2;
        try {
            e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
            l = (Libro) listaLibros.Busquedal(codigo).getInfo();
            if (l.getNumeroDisponibles() > requeridos && e.getLibrosEstudiante().Busquedal(codigo) == null) {
                //Se le asigna la cantidad de libros reservados
                l2 = (Libro) l.clone();
                l2.setNumeroDisponibles(requeridos);
                l2.setNumeroPrestamos(requeridos);
                ///////////////////////////////////////////////
                l.setNumeroDisponibles(l.getNumeroDisponibles() - requeridos);
                l.setNumeroPrestamos(+requeridos);
                e.getLibrosEstudiante().Ingresar(l2);
                JOptionPane.showMessageDialog(null, "Libro reservado correctamente!!!");
            } else {
                JOptionPane.showMessageDialog(null, "No existen copias suficientes disponibles, o ya reservó este libro");
            }
        } catch (Exception ext) {
            JOptionPane.showMessageDialog(null, "Fallo en la operación, ingrese y verifique bien los campos, asegurese que el libro este disponible");
        }

    }

    //Devolver libro
    public void devolver() {
        String codigo, cedula;
        codigo = txtCodLibroResLibro.getText();
        cedula = txtCedulaResLibro.getText();
        int requeridos = Integer.parseInt((String) comboBoxCantidadLibrosResLibro.getSelectedItem());
        Estudiante e;
        Libro l, l2;
        try {
            e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
            l = (Libro) listaLibros.Busquedal(codigo).getInfo();
            l2 = (Libro) e.getLibrosEstudiante().Busquedal(codigo).getInfo();

            if (requeridos <= l2.getNumeroDisponibles()) {
                l.setNumeroDisponibles(l.getNumeroDisponibles() + requeridos);
                l.setNumeroPrestamos(-requeridos);
                l2.setNumeroDisponibles(l2.getNumeroDisponibles() - requeridos);
                l2.setNumeroPrestamos(l2.getNumeroPrestamos() - requeridos);
                if (l2.getNumeroDisponibles() == 0) {
                    e.getLibrosEstudiante().EliminarNodol(codigo, e.getLibrosEstudiante().getRaiz(), e.getLibrosEstudiante().getRaiz());
                }
                JOptionPane.showMessageDialog(null, "Libro devuelto correctamente!!!");
            } else {
                JOptionPane.showMessageDialog(null, "La persona no reservó tantos libros");
            }
        } catch (Exception ext) {
            JOptionPane.showMessageDialog(null, "Fallo en la operación, ingrese y verifique bien los campos, asegurese que el libro este disponible");
        }
    }

    //Método para elistar todos los libros de un solo estudiante
    public String listaLibrosEstudiante() {
        String cedula = txtCedulaResLibro.getText();
        try {
            Estudiante e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
            return ("_______________________________________________________________________"
                    + "\nLista de libros registrados:\n" + e.getLibrosEstudiante().Inorderl(e.getLibrosEstudiante().getRaiz()));

        } catch (Exception e) {
        }
        return "";
    }

    //Metodo que muestra los libros por categoría
    public void reporteLibrosParametrico(String Categoría) {
        txtAreaResLibro.setText("Lista de libros registrados:\n" + listaLibros.InorderParametric(listaLibros.getRaiz(), Categoría));
    }

    //Metodo para guardar/serializar arbol estudiantes
    public void guardarEstudiantes(){
        try{
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaEstudiantes.Niveles(listaNiveles);
            System.out.println(serializador.SerializarEstudiante(rutaArchivo, listaNiveles));
        }
        catch(Exception e){
            System.out.println(e);
        } 
    }
    
    //Metodo para abrir/deserializar arbol estudiantes
    public void abrirEstudiantes(){
        try{
            listaEstudiantes.setRaiz(null);
            fcMenu.showOpenDialog(fcMenu);
            if(fcMenu.getSelectedFile() != null){
                this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();

                serializador.DeserializarEstudiante(rutaArchivo, listaEstudiantes);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    //Metodo para guardar/serializar arbol libros
    public void guardarLibros(){
        try{
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaLibros.Niveles(listaNiveles);
            System.out.println(serializador.SerializarLibro(rutaArchivo, listaNiveles));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    //Metodo para abrir/deserializar arbol libros
    public void abrirLibros(){
        try{
            listaLibros.setRaiz(null);
            fcMenu.showOpenDialog(fcMenu);
            if(fcMenu.getSelectedFile() != null){
                this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();

                serializador.DeserializarLibro(rutaArchivo, listaLibros);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreRegEst = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtApellidoRegEst = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCedulaRegEst = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAñoRegEst = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        comboBoxCarreraRegEst = new javax.swing.JComboBox<>();
        comboBoxNivelRegEst = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaRegEst = new javax.swing.JTextArea();
        btnVerEstudiantesRegistradosRegEst = new javax.swing.JButton();
        btnModificarEstudianteRegEst = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnVerificarEstudianteRegEst = new javax.swing.JButton();
        btnRegistrarEstudianteRegEst = new javax.swing.JButton();
        btnEliminarEstudianteRegEst = new javax.swing.JButton();
        txtDiaRegEst = new javax.swing.JTextField();
        txtMesRegEst = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnAbrirEstudiantesRegEst = new javax.swing.JButton();
        btnGuardarEstudiantesRegEst = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtAreaRegLibro = new javax.swing.JTextArea();
        txtNombreRegLibro = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtNumeroDisponiblesRegLibro = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        comboBoxMateriaRegLibro = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        btnVerificarLibroRegLibro = new javax.swing.JButton();
        btnEliminarLibroRegLibro = new javax.swing.JButton();
        btnModificarLibroRegLibro = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        comboBoxCategoriaRegLibro = new javax.swing.JComboBox<>();
        txtAutorRegLibro = new javax.swing.JTextField();
        txtAñoEdicionRegLibro = new javax.swing.JTextField();
        txtNumeroCopiasRegLibro = new javax.swing.JTextField();
        btnRegistrarLibroRegLibro = new javax.swing.JButton();
        btnVerLibrosRegistradosRegLibro = new javax.swing.JButton();
        txtCodLibro = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        btnGuardarLibrosRedLibro = new javax.swing.JButton();
        btnAbrirLibrosRegLibro = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaResLibro = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCedulaResLibro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodLibroResLibro = new javax.swing.JTextField();
        btnBuscarLibroResLibro = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDatosEstudianteResLibro = new javax.swing.JTextArea();
        btnVerificarEstudianteResLibro = new javax.swing.JButton();
        cboxCatLibro = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDatosLibrosResLibro = new javax.swing.JTextArea();
        btnDevolverLibroResLibro = new javax.swing.JButton();
        btnReservarLibroResLibro = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        comboBoxCantidadLibrosResLibro = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Nombre");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));
        jPanel1.add(txtNombreRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel7.setText("Apellido");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));
        jPanel1.add(txtApellidoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 180, -1));

        jLabel8.setText("Cédula");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));
        jPanel1.add(txtCedulaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 180, -1));

        jLabel9.setText("Nacimiento");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));
        jPanel1.add(txtAñoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 70, -1));

        jLabel10.setText("Carrera");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        jLabel11.setText("Nivel");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jLabel13.setText("Fecha de");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        comboBoxCarreraRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ing. Software", "Ing. Mecatrónica", "Ing. Electricidad", "Ing. Industrial", "Ing. Telecomunicaciones", "Ing. Textil" }));
        jPanel1.add(comboBoxCarreraRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 180, -1));

        comboBoxNivelRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        jPanel1.add(comboBoxNivelRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, 180, -1));

        txtAreaRegEst.setColumns(20);
        txtAreaRegEst.setRows(5);
        jScrollPane4.setViewportView(txtAreaRegEst);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 500, 400));

        btnVerEstudiantesRegistradosRegEst.setText("Ver Estudiantes Registrados");
        btnVerEstudiantesRegistradosRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerEstudiantesRegistradosRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnVerEstudiantesRegistradosRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 310, 30));

        btnModificarEstudianteRegEst.setText("Modificar Estudiante");
        btnModificarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 310, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Datos Estudiante");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        btnVerificarEstudianteRegEst.setText("Verificar Estudiante");
        btnVerificarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnVerificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 310, 30));

        btnRegistrarEstudianteRegEst.setText("Resgitrar Estudiante");
        btnRegistrarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 190, 30));

        btnEliminarEstudianteRegEst.setText("Eliminar Estudiante");
        btnEliminarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 310, 30));
        jPanel1.add(txtDiaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 50, -1));
        jPanel1.add(txtMesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 50, -1));

        jLabel26.setText("Año");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, -1, -1));

        jLabel27.setText("Día");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));

        jLabel28.setText("Mes");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, -1, -1));

        btnAbrirEstudiantesRegEst.setText("Abrir Archivo Estudiantes");
        btnAbrirEstudiantesRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirEstudiantesRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnAbrirEstudiantesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 490, 240, 30));

        btnGuardarEstudiantesRegEst.setText("Guardar Archivo Estudiantes");
        btnGuardarEstudiantesRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEstudiantesRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardarEstudiantesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 490, 250, 30));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel33.setText("Registrar Estudiante");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        jLabel34.setText("_______________________________________________________________");
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));

        jTabbedPane2.addTab("Registrar Estudiantes", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Registrar Libro");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        txtAreaRegLibro.setColumns(20);
        txtAreaRegLibro.setRows(5);
        jScrollPane5.setViewportView(txtAreaRegLibro);

        jPanel2.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 500, 400));
        jPanel2.add(txtNombreRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Datos Libro");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        jLabel17.setText("Nombre");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel18.setText("Autor");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));
        jPanel2.add(txtNumeroDisponiblesRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 110, -1));

        jLabel19.setText("Categoría");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel20.setText("Materia");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        jLabel21.setText("Año de Edición");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        comboBoxMateriaRegLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entretenimiento", "Literatura", "Ciencia" }));
        jPanel2.add(comboBoxMateriaRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 140, -1));

        jLabel22.setText("Código del Libro");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        btnVerificarLibroRegLibro.setText("Verificar Libro");
        btnVerificarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnVerificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 150, 30));

        btnEliminarLibroRegLibro.setText("Eliminar Libro");
        btnEliminarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 450, 150, 30));

        btnModificarLibroRegLibro.setText("Modificar Libro");
        btnModificarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnModificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 150, 30));

        jLabel23.setText("Número de Copias");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        comboBoxCategoriaRegLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        jPanel2.add(comboBoxCategoriaRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 140, -1));
        jPanel2.add(txtAutorRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 180, -1));
        jPanel2.add(txtAñoEdicionRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 110, -1));
        jPanel2.add(txtNumeroCopiasRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 110, -1));

        btnRegistrarLibroRegLibro.setText("Resgitrar Libro");
        btnRegistrarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 140, 30));

        btnVerLibrosRegistradosRegLibro.setText("Ver Libros Registrados");
        btnVerLibrosRegistradosRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerLibrosRegistradosRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnVerLibrosRegistradosRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 230, 30));
        jPanel2.add(txtCodLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 160, -1));

        jLabel29.setText("_______________________________________________________________");
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        jLabel30.setText("Número de Disponibles");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        btnGuardarLibrosRedLibro.setText("Guardar Archivo de Libros");
        btnGuardarLibrosRedLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarLibrosRedLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardarLibrosRedLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 490, 240, 30));

        btnAbrirLibrosRegLibro.setText("Abrir Archivo de Libros");
        btnAbrirLibrosRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirLibrosRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnAbrirLibrosRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 490, 240, 30));

        jTabbedPane2.addTab("Registrar Libros", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtAreaResLibro.setColumns(20);
        txtAreaResLibro.setRows(5);
        jScrollPane1.setViewportView(txtAreaResLibro);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 91, 480, 390));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Libros Disponibles");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 23, -1, -1));

        jLabel2.setText("Cédula");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, -1));
        jPanel3.add(txtCedulaResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 200, -1));

        jLabel3.setText("Cantidad de Libros");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));
        jPanel3.add(txtCodLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, 200, -1));

        btnBuscarLibroResLibro.setText("Buscar Libro");
        btnBuscarLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnBuscarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 140, 30));

        txtAreaDatosEstudianteResLibro.setColumns(20);
        txtAreaDatosEstudianteResLibro.setRows(5);
        jScrollPane2.setViewportView(txtAreaDatosEstudianteResLibro);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 330, 130));

        btnVerificarEstudianteResLibro.setText("Verificar Estudiante");
        btnVerificarEstudianteResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnVerificarEstudianteResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 160, 30));

        cboxCatLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar Todos", "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        cboxCatLibro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCatLibroItemStateChanged(evt);
            }
        });
        jPanel3.add(cboxCatLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, -1, -1));

        txtAreaDatosLibrosResLibro.setColumns(20);
        txtAreaDatosLibrosResLibro.setRows(5);
        jScrollPane3.setViewportView(txtAreaDatosLibrosResLibro);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 320, 100));

        btnDevolverLibroResLibro.setText("Devolver Libro");
        btnDevolverLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverLibroResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnDevolverLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 490, 230, 30));

        btnReservarLibroResLibro.setText("Reservar Libro");
        btnReservarLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarLibroResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnReservarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 490, 230, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Buscar Libro");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Buscar Estudiante");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel24.setText("Codigo del Libro");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        comboBoxCantidadLibrosResLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel3.add(comboBoxCantidadLibrosResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Categoría");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, -1, -1));

        jLabel31.setText("__________________________________________________________________");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel32.setText("Reservar Libros");
        jPanel3.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        jTabbedPane2.addTab("Reservar Libros", jPanel3);

        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroResLibroActionPerformed
        // TODO add your handling code here:
        txtAreaDatosLibrosResLibro.setText(verificarLibro(txtCodLibroResLibro.getText()));
    }//GEN-LAST:event_btnBuscarLibroResLibroActionPerformed

    private void btnVerificarEstudianteResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteResLibroActionPerformed
        // TODO add your handling code here:
        String librosEstudiante = listaLibrosEstudiante();
        txtAreaDatosEstudianteResLibro.setText(verificarEstudiantes(txtCedulaResLibro.getText()) + librosEstudiante);
        
    }//GEN-LAST:event_btnVerificarEstudianteResLibroActionPerformed

    private void btnEliminarLibroRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarLibroRegLibroActionPerformed
        // TODO add your handling code here:
        eliminarLibro();
    }//GEN-LAST:event_btnEliminarLibroRegLibroActionPerformed

    private void btnRegistrarEstudianteRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarEstudianteRegEstActionPerformed
        // TODO add your handling code here:
        ingresarEstudiante();
    }//GEN-LAST:event_btnRegistrarEstudianteRegEstActionPerformed

    private void btnVerEstudiantesRegistradosRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerEstudiantesRegistradosRegEstActionPerformed
        // TODO add your handling code here:
        verEstudiantesRegistrados();
    }//GEN-LAST:event_btnVerEstudiantesRegistradosRegEstActionPerformed

    private void btnEliminarEstudianteRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEstudianteRegEstActionPerformed
        // TODO add your handling code here:
        eliminarEstudiante();
    }//GEN-LAST:event_btnEliminarEstudianteRegEstActionPerformed

    private void btnVerificarEstudianteRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteRegEstActionPerformed
        // TODO add your handling code here:
        txtAreaRegEst.setText(verificarEstudiantes(txtCedulaRegEst.getText()));
    }//GEN-LAST:event_btnVerificarEstudianteRegEstActionPerformed

    private void btnModificarEstudianteRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarEstudianteRegEstActionPerformed
        // TODO add your handling code here:
        modificarEstudiante();
    }//GEN-LAST:event_btnModificarEstudianteRegEstActionPerformed

    private void btnRegistrarLibroRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLibroRegLibroActionPerformed
        // TODO add your handling code here:
        ingresarLibro();
    }//GEN-LAST:event_btnRegistrarLibroRegLibroActionPerformed

    private void btnVerLibrosRegistradosRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerLibrosRegistradosRegLibroActionPerformed
        // TODO add your handling code here:
        verLibrosRegistrados();
    }//GEN-LAST:event_btnVerLibrosRegistradosRegLibroActionPerformed

    private void btnModificarLibroRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarLibroRegLibroActionPerformed
        // TODO add your handling code here:
        modificarLibro();
    }//GEN-LAST:event_btnModificarLibroRegLibroActionPerformed

    private void btnVerificarLibroRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarLibroRegLibroActionPerformed
        // TODO add your handling code here:
        txtAreaRegLibro.setText(verificarLibro(txtCodLibro.getText()));
    }//GEN-LAST:event_btnVerificarLibroRegLibroActionPerformed

    private void btnReservarLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarLibroResLibroActionPerformed
        // TODO add your handling code here:
        reservar();
    }//GEN-LAST:event_btnReservarLibroResLibroActionPerformed

    private void btnDevolverLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverLibroResLibroActionPerformed
        // TODO add your handling code here:
        devolver();
    }//GEN-LAST:event_btnDevolverLibroResLibroActionPerformed

    
    
    private void cboxCatLibroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCatLibroItemStateChanged
        // TODO add your handling code here:

        String item = (String) cboxCatLibro.getSelectedItem(); // get the selected item as an Object
        reporteLibrosParametrico(item);
    }//GEN-LAST:event_cboxCatLibroItemStateChanged

    private void btnAbrirEstudiantesRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirEstudiantesRegEstActionPerformed
        // TODO add your handling code here:
        abrirEstudiantes();
    }//GEN-LAST:event_btnAbrirEstudiantesRegEstActionPerformed

    private void btnGuardarEstudiantesRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEstudiantesRegEstActionPerformed
        // TODO add your handling code here:
        guardarEstudiantes();
    }//GEN-LAST:event_btnGuardarEstudiantesRegEstActionPerformed

    private void btnAbrirLibrosRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirLibrosRegLibroActionPerformed
        // TODO add your handling code here:
        abrirLibros();
    }//GEN-LAST:event_btnAbrirLibrosRegLibroActionPerformed

    private void btnGuardarLibrosRedLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarLibrosRedLibroActionPerformed
        // TODO add your handling code here:
        guardarLibros();
    }//GEN-LAST:event_btnGuardarLibrosRedLibroActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Trabajo01_Int().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirEstudiantesRegEst;
    private javax.swing.JButton btnAbrirLibrosRegLibro;
    private javax.swing.JButton btnBuscarLibroResLibro;
    private javax.swing.JButton btnDevolverLibroResLibro;
    private javax.swing.JButton btnEliminarEstudianteRegEst;
    private javax.swing.JButton btnEliminarLibroRegLibro;
    private javax.swing.JButton btnGuardarEstudiantesRegEst;
    private javax.swing.JButton btnGuardarLibrosRedLibro;
    private javax.swing.JButton btnModificarEstudianteRegEst;
    private javax.swing.JButton btnModificarLibroRegLibro;
    private javax.swing.JButton btnRegistrarEstudianteRegEst;
    private javax.swing.JButton btnRegistrarLibroRegLibro;
    private javax.swing.JButton btnReservarLibroResLibro;
    private javax.swing.JButton btnVerEstudiantesRegistradosRegEst;
    private javax.swing.JButton btnVerLibrosRegistradosRegLibro;
    private javax.swing.JButton btnVerificarEstudianteRegEst;
    private javax.swing.JButton btnVerificarEstudianteResLibro;
    private javax.swing.JButton btnVerificarLibroRegLibro;
    private javax.swing.JComboBox<String> cboxCatLibro;
    private javax.swing.JComboBox<String> comboBoxCantidadLibrosResLibro;
    private javax.swing.JComboBox<String> comboBoxCarreraRegEst;
    private javax.swing.JComboBox<String> comboBoxCategoriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxMateriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxNivelRegEst;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField txtApellidoRegEst;
    private javax.swing.JTextArea txtAreaDatosEstudianteResLibro;
    private javax.swing.JTextArea txtAreaDatosLibrosResLibro;
    private javax.swing.JTextArea txtAreaRegEst;
    private javax.swing.JTextArea txtAreaRegLibro;
    private javax.swing.JTextArea txtAreaResLibro;
    private javax.swing.JTextField txtAutorRegLibro;
    private javax.swing.JTextField txtAñoEdicionRegLibro;
    private javax.swing.JTextField txtAñoRegEst;
    private javax.swing.JTextField txtCedulaRegEst;
    private javax.swing.JTextField txtCedulaResLibro;
    private javax.swing.JTextField txtCodLibro;
    private javax.swing.JTextField txtCodLibroResLibro;
    private javax.swing.JTextField txtDiaRegEst;
    private javax.swing.JTextField txtMesRegEst;
    private javax.swing.JTextField txtNombreRegEst;
    private javax.swing.JTextField txtNombreRegLibro;
    private javax.swing.JTextField txtNumeroCopiasRegLibro;
    private javax.swing.JTextField txtNumeroDisponiblesRegLibro;
    // End of variables declaration//GEN-END:variables
}
