 package reserva.varios;

import javax.swing.JOptionPane;
import Estructura.*;
import Serializacion.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Estructura.ListaLineal;
import Estructura.ArbolBB;
import Estructura.NodoABB;

public class Trabajo01_Int extends javax.swing.JFrame {

    /*
    /////////////////////
    AQUI VA LA LISTA DE ESTUDIANTES EN GENERAL, Y LA LISTA DE LIBROS
    /////////////////////
     */
    ArbolBB listaEstudiantes = new ArbolBB();
    ArbolBB listaLibros = new ArbolBB();
    ArbolBB listaReservas = new ArbolBB();
    
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
    public boolean nombreValido(String input) {
        return StringVálido("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+([- ][A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$", input);
    }
    public boolean apellidoValido(String input) {
        return StringVálido("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+([- ][A-ZÁÉÍÓÚÑ]*[a-záéíóúñ]+)*$", input);
    }
    //MÉTODO PARA VALIDAR LAS PALABRAS QUE SE INGRESEN EN LA VENTANA DE REGISTRO DE LIBROS
    public boolean tituloRegLibrosValido(String input) {
        return StringVálido("[0-9\\s]*[A-ZÁÉÍÓÚÑ\\s]*[a-záéíóúñ\\s]+([A-ZÁÉÍÓÚÑ\\s]*[a-záéíóúñ\\s]+)*", input);
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
            if (nombreValido(txtNombreRegEst.getText())) {
                nombre = txtNombreRegEst.getText();
            } else {
                throw new NullPointerException("nombreInv");
            }
            if (apellidoValido(txtApellidoRegEst.getText())) {
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
            if (apellidoValido(txtAutorRegLibro.getText())) {
                autor = txtAutorRegLibro.getText();
            } else {
                throw new NullPointerException("apellidoInv");
            }

            int añoEdicion = Integer.parseInt(txtAñoEdicionRegLibro.getText());
            int numeroCopias = Integer.parseInt(txtNumeroCopiasRegLibro.getText());
            int numeroDisponibles = Integer.parseInt(txtNumeroDisponiblesRegLibro.getText());
            if(!(añoEdicion>=1000&&añoEdicion<=2023)){
                throw new NumberFormatException("añoInv");
            }
            if(numeroDisponibles<1){
                throw new NumberFormatException("menor1");
            }
            if(numeroCopias<=14){
                throw new NumberFormatException("menor14");
            }
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
            String msj=e.getMessage();
            switch(msj){
                case "menor1":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un número disponible mayor que cero");
                    break;
                case "menor14":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un número de copias mayor o igual a 15");
                    break;
                case "añoInv":
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un año de edición válido");
                    break;
                default:
            JOptionPane.showMessageDialog(null, "Por favor registre números en la parte numérica");
            /////////////////////////////ESTE CATCH SE DEBE CORREGIR E IMPLEMENTAR PARA LIBROS//////////////////
        }} catch (NullPointerException e) {
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
        cod += nombre.substring(nombre.length()-3, nombre.length());
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
            if (nombreValido(txtNombreRegLibro.getText())) {
                nombre = txtNombreRegLibro.getText();
            } else {
                throw new NullPointerException("nombreInv");
            }
            if (apellidoValido(txtAutorRegLibro.getText())) {
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
    public void reservar(String codigo, int requeridos, String cedula, boolean abrirArchivo) {
        String codigof=cedula+codigo;
        Estudiante e;
        Libro l;
        try {
            e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();   
            l = (Libro) listaLibros.Busquedal(codigo).getInfo();
            if(abrirArchivo==false){
                if(l.getNumeroDisponibles() >= requeridos&&listaReservas.BusquedaReserva(codigof) == null){
                    ejecReserva(l,requeridos,abrirArchivo,codigof,cedula);
                }else{
                    JOptionPane.showMessageDialog(null, "No existen copias suficientes disponibles, o ya reservó este libro");
                }
            }else{
                if (l.getNumeroDisponibles() >= requeridos) {
                    ejecReserva(l,requeridos,abrirArchivo,codigof,cedula);
                }else{
                    JOptionPane.showMessageDialog(null, "No existen copias suficientes disponibles, o ya reservó este libro");
                }
            }
           
            
        } catch (Exception ext) {
            JOptionPane.showMessageDialog(null, "Fallo en la operación, ingrese y verifique bien los campos, asegurese que el libro este disponible");
        }

    }
    
    public void ejecReserva(Libro l, int requeridos, boolean abrirArchivo, String codigof, String cedula){
        l.setNumeroDisponibles(l.getNumeroDisponibles() - requeridos);
                l.setNumeroPrestamos(+requeridos);
                if(abrirArchivo==false){
                    Reserva r= new Reserva(l, requeridos,codigof, cedula);
                    listaReservas.IngresarReserva(r);
                    JOptionPane.showMessageDialog(null, "Libro reservado correctamente!!!");
                }
                String item = (String) cboxCatLibroResLibro.getSelectedItem(); // get the selected item as an Object
                reporteLibrosParametrico(item);
    }

    //Devolver libro
    public void devolver() {
        String codigo, cedula;
        codigo = txtCodLibroDevLibro.getText();
        cedula = txtCedulaDevLibro.getText();
        String codigof= cedula+codigo;
        int requeridos = Integer.parseInt((String) comboBoxCantidadLibrosDevLibro.getSelectedItem());
        Estudiante e;
        Libro l;
        Reserva r;
        try {
            e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
            l = (Libro) listaLibros.Busquedal(codigo).getInfo();
            r = (Reserva) listaReservas.BusquedaReserva(codigof).getInfo();
            System.out.println("test1: "+r);
            
            if (requeridos <= r.getCantidad()) {
                l.setNumeroDisponibles(l.getNumeroDisponibles() + requeridos);
                l.setNumeroPrestamos(l.getNumeroPrestamos()-requeridos);
                r.setCantidad(r.getCantidad() - requeridos);
                System.out.println("test2: "+r);
                if (r.getCantidad() == 0) {
                    listaReservas.EliminarNodoReserva(codigo, listaReservas.getRaiz(), listaReservas.getRaiz());
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
    public String listaLibrosEstudiante(String cedula) {
        try {

            return ("_______________________________________________________________________"
                    + "\nLista de libros registrados:\n" + listaReservas.InorderReservaSub(listaReservas.getRaiz(),cedula));

        } catch (Exception e) {
            System.out.println(e);
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
            listaNiveles.Clear();
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaEstudiantes.Niveles(listaNiveles);
                               
            serializador.SerializarEstudiante(rutaArchivo, listaNiveles);
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
    
    public void abrirReservas(){
        try{
            listaReservas.setRaiz(null);
            fcMenu.showOpenDialog(fcMenu);
            if(fcMenu.getSelectedFile() != null){
                this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();

                serializador.DeserializarReservas(rutaArchivo, listaReservas);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    //Metodo para guardar/serializar arbol libros
    public void guardarLibros(){
        try{
            listaNiveles.Clear();
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaLibros.Niveles(listaNiveles);
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
    //Metodo para guardar/serializar arbol reservas
    public void guardarReservas(){
        try{
            listaNiveles.Clear();
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaReservas.Niveles(listaNiveles);      
            serializador.SerializarReserva(rutaArchivo, listaNiveles);
        }
        catch(Exception e){
            System.out.println(e);
        } 
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel54 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCedulaResLibro1 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        btnVerificarEstudianteResLibro1 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtAreaDatosEstudianteResLibro1 = new javax.swing.JTextArea();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        txtCodLibroResLibro1 = new javax.swing.JTextField();
        btnBuscarLibroResLibro1 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtAreaDatosLibrosResLibro1 = new javax.swing.JTextArea();
        jLabel59 = new javax.swing.JLabel();
        comboBoxCantidadLibrosResLibro1 = new javax.swing.JComboBox<>();
        btnReservarLibroResLibro1 = new javax.swing.JButton();
        btnDevolverLibroResLibro1 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtAreaResLibro1 = new javax.swing.JTextArea();
        jLabel60 = new javax.swing.JLabel();
        cboxCatLibro1 = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        btnVerEstudiantesRegistradosRegEst = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaRegEst = new javax.swing.JTextArea();
        txtNombreRegEst = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCedulaRegEst = new javax.swing.JTextField();
        txtApellidoRegEst = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDiaRegEst = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtMesRegEst = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtAñoRegEst = new javax.swing.JTextField();
        comboBoxCarreraRegEst = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        comboBoxNivelRegEst = new javax.swing.JComboBox<>();
        btnRegistrarEstudianteRegEst = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        btnVerificarEstudianteRegEst = new javax.swing.JButton();
        btnEliminarEstudianteRegEst = new javax.swing.JButton();
        btnModificarEstudianteRegEst = new javax.swing.JButton();
        btnAbrirEstudiantesRegEst = new javax.swing.JButton();
        btnGuardarEstudiantesRegEst = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtNombreRegLibro = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtAutorRegLibro = new javax.swing.JTextField();
        comboBoxCategoriaRegLibro = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        comboBoxMateriaRegLibro = new javax.swing.JComboBox<>();
        txtAñoEdicionRegLibro = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtNumeroCopiasRegLibro = new javax.swing.JTextField();
        txtNumeroDisponiblesRegLibro = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        btnRegistrarLibroRegLibro = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtCodLibro = new javax.swing.JTextField();
        btnEliminarLibroRegLibro = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtAreaRegLibro = new javax.swing.JTextArea();
        btnVerLibrosRegistradosRegLibro = new javax.swing.JButton();
        btnVerificarLibroRegLibro = new javax.swing.JButton();
        btnModificarLibroRegLibro = new javax.swing.JButton();
        btnAbrirLibrosRegLibro = new javax.swing.JButton();
        btnGuardarLibrosRedLibro = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        cboxCatLibroResLibro = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtAreaResLibro = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtAreaDatosEstudianteResLibro = new javax.swing.JTextArea();
        btnVerificarEstudianteResLibro = new javax.swing.JButton();
        txtCedulaResLibro = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtCodLibroResLibro = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        btnBuscarLibroResLibro = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtAreaDatosLibrosResLibro = new javax.swing.JTextArea();
        jLabel42 = new javax.swing.JLabel();
        comboBoxCantidadLibrosResLibro = new javax.swing.JComboBox<>();
        btnReservarLibroResLibro = new javax.swing.JButton();
        btnGuardarReservas = new javax.swing.JButton();
        btnAbrirReservas = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtAreaDatosEstudianteDevLibro = new javax.swing.JTextArea();
        btnVerificarEstudianteDevLibro = new javax.swing.JButton();
        txtCedulaDevLibro = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        txtCodLibroDevLibro = new javax.swing.JTextField();
        btnBuscarLibroDevLibro2 = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtAreaDatosLibrosDevLibro = new javax.swing.JTextArea();
        comboBoxCantidadLibrosDevLibro = new javax.swing.JComboBox<>();
        jLabel69 = new javax.swing.JLabel();
        btnDevolverLibroDevLibro2 = new javax.swing.JButton();
        btnGuardarReservas1 = new javax.swing.JButton();

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel54.setText("Reservar Libros");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("Buscar Estudiante");

        jLabel55.setText("Cédula");

        btnVerificarEstudianteResLibro1.setText("Verificar Estudiante");
        btnVerificarEstudianteResLibro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteResLibro1ActionPerformed(evt);
            }
        });

        txtAreaDatosEstudianteResLibro1.setColumns(20);
        txtAreaDatosEstudianteResLibro1.setRows(5);
        jScrollPane10.setViewportView(txtAreaDatosEstudianteResLibro1);

        jLabel56.setText("__________________________________________________________________");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel57.setText("Buscar Libro");

        jLabel58.setText("Codigo del Libro");

        btnBuscarLibroResLibro1.setText("Buscar Libro");
        btnBuscarLibroResLibro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroResLibro1ActionPerformed(evt);
            }
        });

        txtAreaDatosLibrosResLibro1.setColumns(20);
        txtAreaDatosLibrosResLibro1.setRows(5);
        jScrollPane11.setViewportView(txtAreaDatosLibrosResLibro1);

        jLabel59.setText("Cantidad de Libros");

        comboBoxCantidadLibrosResLibro1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));

        btnReservarLibroResLibro1.setText("Reservar Libro");
        btnReservarLibroResLibro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarLibroResLibro1ActionPerformed(evt);
            }
        });

        btnDevolverLibroResLibro1.setText("Devolver Libro");
        btnDevolverLibroResLibro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverLibroResLibro1ActionPerformed(evt);
            }
        });

        txtAreaResLibro1.setColumns(20);
        txtAreaResLibro1.setRows(5);
        jScrollPane12.setViewportView(txtAreaResLibro1);

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setText("Categoría");

        cboxCatLibro1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar Todos", "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        cboxCatLibro1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCatLibro1ItemStateChanged(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel61.setText("Libros Disponibles");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane2.setForeground(new java.awt.Color(102, 102, 102));
        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(234, 236, 238));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Eras Bold ITC", 1, 40)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(41, 128, 185));
        jLabel2.setText("Bienvenido");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel1.setFont(new java.awt.Font("Eras Bold ITC", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(22, 160, 133));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 400, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/books.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 120, -1, -1));

        jLabel15.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(58, 189, 76));
        jLabel15.setText("modificación de tanto libros como estudiantes.");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jLabel17.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(58, 189, 76));
        jLabel17.setText("cada una realiza las funciones antes descritas.");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        jLabel19.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(235, 152, 78));
        jLabel19.setText("•   Rosero Jorge");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, -1, -1));

        jLabel20.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(235, 152, 78));
        jLabel20.setText("Proyecto realizado por:");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));

        jLabel21.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(235, 152, 78));
        jLabel21.setText("•   de la Cruz Brayan");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));

        jLabel22.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(235, 152, 78));
        jLabel22.setText("•   Luna Anthony");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        jLabel25.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(58, 189, 76));
        jLabel25.setText("de libros hecho para estudiantes.");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel29.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(58, 189, 76));
        jLabel29.setText("Este es un sistema diseñado para ayudar a la gestión de reserva y devolución de ");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel30.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(58, 189, 76));
        jLabel30.setText("El cual cuenta con las funciones de registro, edición y  ");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel31.setFont(new java.awt.Font("Eras Bold ITC", 0, 20)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(58, 189, 76));
        jLabel31.setText("En la parte superior se encuentran las ventanas donde ");
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        jTabbedPane2.addTab("Home", jPanel1);

        jPanel2.setBackground(new java.awt.Color(240, 230, 140));
        jPanel2.setForeground(new java.awt.Color(222, 184, 135));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel33.setText("Registrar Estudiante");
        jPanel2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        btnVerEstudiantesRegistradosRegEst.setText("Ver Estudiantes Registrados");
        btnVerEstudiantesRegistradosRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerEstudiantesRegistradosRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnVerEstudiantesRegistradosRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 310, 30));

        txtAreaRegEst.setColumns(20);
        txtAreaRegEst.setRows(5);
        jScrollPane4.setViewportView(txtAreaRegEst);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 500, 400));
        jPanel2.add(txtNombreRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Datos Estudiante");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        jLabel6.setText("Nombre");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel7.setText("Apellido");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));

        jLabel8.setText("Cédula");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));
        jPanel2.add(txtCedulaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 180, -1));
        jPanel2.add(txtApellidoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 180, -1));

        jLabel9.setText("Nacimiento");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        jLabel13.setText("Fecha de");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));
        jPanel2.add(txtDiaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 50, -1));

        jLabel27.setText("Día");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));
        jPanel2.add(txtMesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 50, -1));

        jLabel28.setText("Mes");
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, -1, -1));

        jLabel26.setText("Año");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, -1, -1));
        jPanel2.add(txtAñoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 70, -1));

        comboBoxCarreraRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ing. Software", "Ing. Mecatrónica", "Ing. Electricidad", "Ing. Industrial", "Ing. Telecomunicaciones", "Ing. Textil" }));
        jPanel2.add(comboBoxCarreraRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 180, -1));

        jLabel10.setText("Carrera");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        jLabel11.setText("Nivel");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        comboBoxNivelRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        jPanel2.add(comboBoxNivelRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, 180, -1));

        btnRegistrarEstudianteRegEst.setText("Registrar Estudiante");
        btnRegistrarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 190, 30));

        jLabel34.setText("_______________________________________________________________");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));

        btnVerificarEstudianteRegEst.setText("Verificar Estudiante");
        jPanel2.add(btnVerificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 310, 30));

        btnEliminarEstudianteRegEst.setText("Eliminar Estudiante");
        btnEliminarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 310, 30));

        btnModificarEstudianteRegEst.setText("Modificar Estudiante");
        btnModificarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnModificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 310, 30));

        btnAbrirEstudiantesRegEst.setText("Abrir Archivo Estudiantes");
        btnAbrirEstudiantesRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirEstudiantesRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnAbrirEstudiantesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 490, 240, 30));

        btnGuardarEstudiantesRegEst.setText("Guardar Archivo Estudiantes");
        btnGuardarEstudiantesRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEstudiantesRegEstActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardarEstudiantesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 490, 250, 30));

        jTabbedPane2.addTab("Registrar Estudiantes", jPanel2);

        jPanel3.setBackground(new java.awt.Color(217, 196, 245));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel43.setText("Registrar Libro");
        jPanel3.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel44.setText("Datos Libro");
        jPanel3.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));
        jPanel3.add(txtNombreRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel45.setText("Nombre");
        jPanel3.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel46.setText("Autor");
        jPanel3.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));
        jPanel3.add(txtAutorRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 180, -1));

        comboBoxCategoriaRegLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        jPanel3.add(comboBoxCategoriaRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 140, -1));

        jLabel47.setText("Categoría");
        jPanel3.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel48.setText("Materia");
        jPanel3.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        comboBoxMateriaRegLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entretenimiento", "Literatura", "Ciencia" }));
        jPanel3.add(comboBoxMateriaRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 140, -1));
        jPanel3.add(txtAñoEdicionRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 110, -1));

        jLabel49.setText("Año de Edición");
        jPanel3.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        jLabel50.setText("Número de Copias");
        jPanel3.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));
        jPanel3.add(txtNumeroCopiasRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 110, -1));
        jPanel3.add(txtNumeroDisponiblesRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 110, -1));

        jLabel51.setText("Número de Disponibles");
        jPanel3.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        btnRegistrarLibroRegLibro.setText("Registrar Libro");
        btnRegistrarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnRegistrarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 140, 30));

        jLabel52.setText("_______________________________________________________________");
        jPanel3.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        jLabel53.setText("Código del Libro");
        jPanel3.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));
        jPanel3.add(txtCodLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 160, -1));

        btnEliminarLibroRegLibro.setText("Eliminar Libro");
        btnEliminarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 450, 150, 30));

        txtAreaRegLibro.setColumns(20);
        txtAreaRegLibro.setRows(5);
        jScrollPane9.setViewportView(txtAreaRegLibro);

        jPanel3.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 500, 400));

        btnVerLibrosRegistradosRegLibro.setText("Ver Libros Registrados");
        btnVerLibrosRegistradosRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerLibrosRegistradosRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnVerLibrosRegistradosRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 230, 30));

        btnVerificarLibroRegLibro.setText("Verificar Libro");
        btnVerificarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnVerificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 150, 30));

        btnModificarLibroRegLibro.setText("Modificar Libro");
        btnModificarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnModificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 150, 30));

        btnAbrirLibrosRegLibro.setText("Abrir Archivo de Libros");
        btnAbrirLibrosRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirLibrosRegLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnAbrirLibrosRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 490, 240, 30));

        btnGuardarLibrosRedLibro.setText("Guardar Archivo de Libros");
        btnGuardarLibrosRedLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarLibrosRedLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnGuardarLibrosRedLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 490, 240, 30));

        jTabbedPane2.addTab("Registrar Libros", jPanel3);

        jPanel4.setBackground(new java.awt.Color(182, 250, 191));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Buscar Estudiante");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel35.setText("Reservar Libros");
        jPanel4.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setText("Categoría");
        jPanel4.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, -1, -1));

        cboxCatLibroResLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar Todos", "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        cboxCatLibroResLibro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCatLibroResLibroItemStateChanged(evt);
            }
        });
        jPanel4.add(cboxCatLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, -1, -1));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel37.setText("Libros Disponibles");
        jPanel4.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 23, -1, -1));

        txtAreaResLibro.setColumns(20);
        txtAreaResLibro.setRows(5);
        jScrollPane6.setViewportView(txtAreaResLibro);

        jPanel4.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 91, 480, 390));

        txtAreaDatosEstudianteResLibro.setColumns(20);
        txtAreaDatosEstudianteResLibro.setRows(5);
        jScrollPane7.setViewportView(txtAreaDatosEstudianteResLibro);

        jPanel4.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 330, 130));

        btnVerificarEstudianteResLibro.setText("Verificar Estudiante");
        btnVerificarEstudianteResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteResLibroActionPerformed(evt);
            }
        });
        jPanel4.add(btnVerificarEstudianteResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 160, 30));
        jPanel4.add(txtCedulaResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 250, -1));

        jLabel38.setText("Cédula");
        jPanel4.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, -1));

        jLabel39.setText("__________________________________________________________________");
        jPanel4.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel40.setText("Buscar Libro");
        jPanel4.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, -1, -1));
        jPanel4.add(txtCodLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, 200, -1));

        jLabel41.setText("Codigo del Libro");
        jPanel4.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        btnBuscarLibroResLibro.setText("Buscar Libro");
        btnBuscarLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroResLibroActionPerformed(evt);
            }
        });
        jPanel4.add(btnBuscarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 140, 30));

        txtAreaDatosLibrosResLibro.setColumns(20);
        txtAreaDatosLibrosResLibro.setRows(5);
        jScrollPane8.setViewportView(txtAreaDatosLibrosResLibro);

        jPanel4.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 320, 100));

        jLabel42.setText("Cantidad de Libros");
        jPanel4.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));

        comboBoxCantidadLibrosResLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel4.add(comboBoxCantidadLibrosResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, -1, -1));

        btnReservarLibroResLibro.setText("Reservar Libro");
        btnReservarLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarLibroResLibroActionPerformed(evt);
            }
        });
        jPanel4.add(btnReservarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 490, 230, 30));

        btnGuardarReservas.setText("Guardar archivo");
        btnGuardarReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarReservasActionPerformed(evt);
            }
        });
        jPanel4.add(btnGuardarReservas, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 500, -1, -1));

        btnAbrirReservas.setText("Abrir archivo");
        btnAbrirReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirReservasActionPerformed(evt);
            }
        });
        jPanel4.add(btnAbrirReservas, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 500, -1, -1));

        jTabbedPane2.addTab("Reservar Libros", jPanel4);

        jPanel5.setBackground(new java.awt.Color(237, 209, 197));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel62.setText("Buscar Libro");
        jPanel5.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, -1, -1));

        txtAreaDatosEstudianteDevLibro.setColumns(20);
        txtAreaDatosEstudianteDevLibro.setRows(5);
        jScrollPane13.setViewportView(txtAreaDatosEstudianteDevLibro);

        jPanel5.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 350, 130));

        btnVerificarEstudianteDevLibro.setText("Verificar Estudiante");
        btnVerificarEstudianteDevLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteDevLibroActionPerformed(evt);
            }
        });
        jPanel5.add(btnVerificarEstudianteDevLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 160, 30));
        jPanel5.add(txtCedulaDevLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 250, -1));

        jLabel64.setText("Cédula");
        jPanel5.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("Buscar Estudiante");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, -1, -1));

        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel65.setText("Devolver Libros");
        jPanel5.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));
        jPanel5.add(txtCodLibroDevLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 200, -1));

        btnBuscarLibroDevLibro2.setText("Buscar Libro");
        btnBuscarLibroDevLibro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroDevLibro2ActionPerformed(evt);
            }
        });
        jPanel5.add(btnBuscarLibroDevLibro2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 140, 30));

        jLabel68.setText("Codigo del Libro");
        jPanel5.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, -1, -1));

        txtAreaDatosLibrosDevLibro.setColumns(20);
        txtAreaDatosLibrosDevLibro.setRows(5);
        jScrollPane15.setViewportView(txtAreaDatosLibrosDevLibro);

        jPanel5.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, 350, 130));

        comboBoxCantidadLibrosDevLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel5.add(comboBoxCantidadLibrosDevLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 390, -1, -1));

        jLabel69.setText("Cantidad de Libros");
        jPanel5.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, -1, -1));

        btnDevolverLibroDevLibro2.setText("Devolver Libro");
        btnDevolverLibroDevLibro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverLibroDevLibro2ActionPerformed(evt);
            }
        });
        jPanel5.add(btnDevolverLibroDevLibro2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 230, 30));

        btnGuardarReservas1.setText("Guardar archivo");
        btnGuardarReservas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarReservas1ActionPerformed(evt);
            }
        });
        jPanel5.add(btnGuardarReservas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 360, -1, -1));

        jTabbedPane2.addTab("Devolver Libros", jPanel5);

        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroResLibroActionPerformed
        // TODO add your handling code here:
        txtAreaDatosLibrosResLibro.setText(verificarLibro(txtCodLibroResLibro.getText()));
    }//GEN-LAST:event_btnBuscarLibroResLibroActionPerformed

    private void btnVerificarEstudianteResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteResLibroActionPerformed
        // TODO add your handling code here:
        String librosEstudiante = listaLibrosEstudiante(txtCedulaResLibro.getText());
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

    
    
    private void cboxCatLibroResLibroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCatLibroResLibroItemStateChanged
        // TODO add your handling code here:

        String item = (String) cboxCatLibroResLibro.getSelectedItem(); // get the selected item as an Object
        reporteLibrosParametrico(item);
    }//GEN-LAST:event_cboxCatLibroResLibroItemStateChanged

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

    private void btnVerificarEstudianteResLibro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteResLibro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerificarEstudianteResLibro1ActionPerformed

    private void btnBuscarLibroResLibro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroResLibro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLibroResLibro1ActionPerformed

    private void btnReservarLibroResLibro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarLibroResLibro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReservarLibroResLibro1ActionPerformed

    private void btnDevolverLibroResLibro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverLibroResLibro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDevolverLibroResLibro1ActionPerformed

    private void cboxCatLibro1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCatLibro1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxCatLibro1ItemStateChanged

    private void btnVerificarEstudianteDevLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteDevLibroActionPerformed
        // TODO add your handling code here:        
        String librosEstudiante = listaLibrosEstudiante(txtCedulaDevLibro.getText());
        txtAreaDatosEstudianteDevLibro.setText(verificarEstudiantes(txtCedulaDevLibro.getText()) + librosEstudiante);
    }//GEN-LAST:event_btnVerificarEstudianteDevLibroActionPerformed

    private void btnBuscarLibroDevLibro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroDevLibro2ActionPerformed
        // TODO add your handling code here:
        txtAreaDatosLibrosDevLibro.setText(verificarLibro(txtCodLibroDevLibro.getText()));
    }//GEN-LAST:event_btnBuscarLibroDevLibro2ActionPerformed

    private void btnDevolverLibroDevLibro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverLibroDevLibro2ActionPerformed
        // TODO add your handling code here:
        devolver();
    }//GEN-LAST:event_btnDevolverLibroDevLibro2ActionPerformed

    private void btnReservarLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarLibroResLibroActionPerformed
        // TODO add your handling code here:
        String codigo = txtCodLibroResLibro.getText();
        int requeridos = Integer.parseInt((String) comboBoxCantidadLibrosResLibro.getSelectedItem());
        String cedula = txtCedulaResLibro.getText();
        reservar(codigo, requeridos, cedula, false);
    }//GEN-LAST:event_btnReservarLibroResLibroActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
        String item = (String) cboxCatLibroResLibro.getSelectedItem(); // get the selected item as an Object
        reporteLibrosParametrico(item);
    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void btnGuardarReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarReservasActionPerformed
        try{
            listaNiveles.Clear();
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaReservas.Niveles(listaNiveles);
                               
            serializador.SerializarReservas(rutaArchivo, listaNiveles);
        }
        catch(Exception e){
            System.out.println(e);
        } 
    }//GEN-LAST:event_btnGuardarReservasActionPerformed

    private void btnAbrirReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirReservasActionPerformed
        try{
            listaReservas.setRaiz(null);
            fcMenu.showOpenDialog(fcMenu);
            if(fcMenu.getSelectedFile() != null){
                this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();

                serializador.DeserializarReservas(rutaArchivo, listaReservas);
            }
            InorderReservar(listaReservas.getRaiz());
        }
        catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_btnAbrirReservasActionPerformed

    private void btnGuardarReservas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarReservas1ActionPerformed
       try{
            listaNiveles.Clear();
            fcMenu.showSaveDialog(fcMenu);
            this.rutaArchivo = fcMenu.getSelectedFile().getAbsolutePath();
            listaReservas.Niveles(listaNiveles);
                               
            serializador.SerializarReservas(rutaArchivo, listaNiveles);
        }
        catch(Exception e){
            System.out.println(e);
        } 
    }//GEN-LAST:event_btnGuardarReservas1ActionPerformed

    public String InorderReservar(NodoABB r) {
        String res = "";
        Reserva reserva;
        if (r != null) {
            res += InorderReservar(r.gethIzq());
            reserva = (Reserva) r.getInfo();
            reservar(reserva.getLibro().getCodigo(), reserva.getCantidad(), reserva.getCedula(), true);
            res += reserva.toString() + "\n";
            res += InorderReservar(r.gethDer());
        }
        return res;
    }
    
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
    private javax.swing.JButton btnAbrirReservas;
    private javax.swing.JButton btnBuscarLibroDevLibro2;
    private javax.swing.JButton btnBuscarLibroResLibro;
    private javax.swing.JButton btnBuscarLibroResLibro1;
    private javax.swing.JButton btnDevolverLibroDevLibro2;
    private javax.swing.JButton btnDevolverLibroResLibro1;
    private javax.swing.JButton btnEliminarEstudianteRegEst;
    private javax.swing.JButton btnEliminarLibroRegLibro;
    private javax.swing.JButton btnGuardarEstudiantesRegEst;
    private javax.swing.JButton btnGuardarLibrosRedLibro;
    private javax.swing.JButton btnGuardarReservas;
    private javax.swing.JButton btnGuardarReservas1;
    private javax.swing.JButton btnModificarEstudianteRegEst;
    private javax.swing.JButton btnModificarLibroRegLibro;
    private javax.swing.JButton btnRegistrarEstudianteRegEst;
    private javax.swing.JButton btnRegistrarLibroRegLibro;
    private javax.swing.JButton btnReservarLibroResLibro;
    private javax.swing.JButton btnReservarLibroResLibro1;
    private javax.swing.JButton btnVerEstudiantesRegistradosRegEst;
    private javax.swing.JButton btnVerLibrosRegistradosRegLibro;
    private javax.swing.JButton btnVerificarEstudianteDevLibro;
    private javax.swing.JButton btnVerificarEstudianteRegEst;
    private javax.swing.JButton btnVerificarEstudianteResLibro;
    private javax.swing.JButton btnVerificarEstudianteResLibro1;
    private javax.swing.JButton btnVerificarLibroRegLibro;
    private javax.swing.JComboBox<String> cboxCatLibro1;
    private javax.swing.JComboBox<String> cboxCatLibroResLibro;
    private javax.swing.JComboBox<String> comboBoxCantidadLibrosDevLibro;
    private javax.swing.JComboBox<String> comboBoxCantidadLibrosResLibro;
    private javax.swing.JComboBox<String> comboBoxCantidadLibrosResLibro1;
    private javax.swing.JComboBox<String> comboBoxCarreraRegEst;
    private javax.swing.JComboBox<String> comboBoxCategoriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxMateriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxNivelRegEst;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField txtApellidoRegEst;
    private javax.swing.JTextArea txtAreaDatosEstudianteDevLibro;
    private javax.swing.JTextArea txtAreaDatosEstudianteResLibro;
    private javax.swing.JTextArea txtAreaDatosEstudianteResLibro1;
    private javax.swing.JTextArea txtAreaDatosLibrosDevLibro;
    private javax.swing.JTextArea txtAreaDatosLibrosResLibro;
    private javax.swing.JTextArea txtAreaDatosLibrosResLibro1;
    private javax.swing.JTextArea txtAreaRegEst;
    private javax.swing.JTextArea txtAreaRegLibro;
    private javax.swing.JTextArea txtAreaResLibro;
    private javax.swing.JTextArea txtAreaResLibro1;
    private javax.swing.JTextField txtAutorRegLibro;
    private javax.swing.JTextField txtAñoEdicionRegLibro;
    private javax.swing.JTextField txtAñoRegEst;
    private javax.swing.JTextField txtCedulaDevLibro;
    private javax.swing.JTextField txtCedulaRegEst;
    private javax.swing.JTextField txtCedulaResLibro;
    private javax.swing.JTextField txtCedulaResLibro1;
    private javax.swing.JTextField txtCodLibro;
    private javax.swing.JTextField txtCodLibroDevLibro;
    private javax.swing.JTextField txtCodLibroResLibro;
    private javax.swing.JTextField txtCodLibroResLibro1;
    private javax.swing.JTextField txtDiaRegEst;
    private javax.swing.JTextField txtMesRegEst;
    private javax.swing.JTextField txtNombreRegEst;
    private javax.swing.JTextField txtNombreRegLibro;
    private javax.swing.JTextField txtNumeroCopiasRegLibro;
    private javax.swing.JTextField txtNumeroDisponiblesRegLibro;
    // End of variables declaration//GEN-END:variables
}
