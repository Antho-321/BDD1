package reserva.varios;

import java.util.Date;
import javax.swing.JOptionPane;
import Estructura.*;
import Serializacion.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trabajo01_Int extends javax.swing.JFrame {

    /*
    /////////////////////
    AQUI VA LA LISTA DE ESTUDIANTES EN GENERAL, Y LA LISTA DE LIBROS
    /////////////////////
     */
    ArbolBB listaEstudiantes = new ArbolBB();
    ArbolBB listaLibros = new ArbolBB();

    public Trabajo01_Int() {
        initComponents();
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

    //MÉTODO PARA VALIDAR EL NOMBRE Y APELLIDO
    public boolean nombreApellidoValido(String input) {
        // Compile the regex pattern
        Pattern pattern = Pattern.compile("^[A-ZÁÉÍÓÚÜÑ][a-záéíóúüñ]*$");
        // Create a matcher object from the input string
        Matcher matcher = pattern.matcher(input);
        // Return true if the input matches the pattern, false otherwise
        return matcher.matches();
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
            //////////////////
            PARTE DE VALIDACION DE DATOS, SOLO ESTÁ LA FECHA
            //////////////////
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
    public void verificarEstudiantes() {
        String cedula = txtCedulaRegEst.getText();
        if (!cedula.equals("")) {
            try {
                Estudiante e = (Estudiante) listaEstudiantes.Busqueda(cedula).getInfo();
                txtAreaRegEst.setText("El estudiante ya se encuentra resgistrado:\n" + e.toString());
            } catch (Exception e) {
                txtAreaRegEst.setText("Ningún Estudiante encontrado");
            }

        } else {

            JOptionPane.showMessageDialog(null, "Primero debes ingresar tu numero de cédula en el campo 'cedula' para saber si ya estas registrado");
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
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnVerificarEstudianteRegEst = new javax.swing.JButton();
        btnRegistrarEstudianteRegEst = new javax.swing.JButton();
        btnEliminarEstudianteRegEst = new javax.swing.JButton();
        txtDiaRegEst = new javax.swing.JTextField();
        txtMesRegEst = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaResLibro = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCedulaResLibro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombreLibroResLibro = new javax.swing.JTextField();
        btnBuscarLibroResLibro = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDatosEstudianteResLibro = new javax.swing.JTextArea();
        btnVerificarEstudianteResLibro = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDatosLibrosResLibro = new javax.swing.JTextArea();
        btnDevolverLibroResLibro = new javax.swing.JButton();
        btnReservarLibroResLibro = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        comboBoxCantidadLibrosResLibro = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Nombre");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 87, -1, -1));
        jPanel1.add(txtNombreRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel7.setText("Apellido");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));
        jPanel1.add(txtApellidoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 180, -1));

        jLabel8.setText("Cédula");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));
        jPanel1.add(txtCedulaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 180, -1));

        jLabel9.setText("Nacimiento");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));
        jPanel1.add(txtAñoRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, 70, -1));

        jLabel10.setText("Carrera");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, -1, -1));

        jLabel11.setText("Nivel");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, -1, -1));

        jLabel13.setText("Fecha de");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        comboBoxCarreraRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ing. Software", "Ing. Mecatrónica", "Ing. Electricidad", "Ing. Industrial", "Ing. Telecomunicaciones", "Ing. Textil" }));
        jPanel1.add(comboBoxCarreraRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 180, -1));

        comboBoxNivelRegEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
        jPanel1.add(comboBoxNivelRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 180, -1));

        txtAreaRegEst.setColumns(20);
        txtAreaRegEst.setRows(5);
        jScrollPane4.setViewportView(txtAreaRegEst);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 60, 500, 410));

        btnVerEstudiantesRegistradosRegEst.setText("Ver Estudiantes Registrados");
        btnVerEstudiantesRegistradosRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerEstudiantesRegistradosRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnVerEstudiantesRegistradosRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, 180, 30));

        btnModificarEstudianteRegEst.setText("Modificar Estudiante");
        btnModificarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 180, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Registrar Estudiante");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Datos Estudiante");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        btnVerificarEstudianteRegEst.setText("Verificar Estudiante");
        btnVerificarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnVerificarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 190, 30));

        btnRegistrarEstudianteRegEst.setText("Resgitrar Estudiante");
        btnRegistrarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 150, 30));

        btnEliminarEstudianteRegEst.setText("Eliminar Estudiante");
        btnEliminarEstudianteRegEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEstudianteRegEstActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminarEstudianteRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 150, 30));
        jPanel1.add(txtDiaRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 50, -1));
        jPanel1.add(txtMesRegEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 50, -1));

        jLabel26.setText("Año");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, -1, -1));

        jLabel27.setText("Día");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, -1, -1));

        jLabel28.setText("Mes");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, -1, -1));

        jTabbedPane2.addTab("Registrar Estudiantes", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Registrar Libro");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));

        txtAreaRegLibro.setColumns(20);
        txtAreaRegLibro.setRows(5);
        jScrollPane5.setViewportView(txtAreaRegLibro);

        jPanel2.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 60, 500, 400));
        jPanel2.add(txtNombreRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 180, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Datos Libro");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        jLabel17.setText("Nombre");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 87, -1, -1));

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

        jLabel22.setText("Número de Disponibles");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        btnVerificarLibroRegLibro.setText("Verificar Libro");
        jPanel2.add(btnVerificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 190, 30));

        btnEliminarLibroRegLibro.setText("Eliminar Libro");
        btnEliminarLibroRegLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarLibroRegLibroActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 160, 30));

        btnModificarLibroRegLibro.setText("Modificar Libro");
        jPanel2.add(btnModificarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 140, 30));

        jLabel23.setText("Número de Copias");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        comboBoxCategoriaRegLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        jPanel2.add(comboBoxCategoriaRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 140, -1));
        jPanel2.add(txtAutorRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 180, -1));
        jPanel2.add(txtAñoEdicionRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 110, -1));
        jPanel2.add(txtNumeroCopiasRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 110, -1));

        btnRegistrarLibroRegLibro.setText("Resgitrar Libro");
        jPanel2.add(btnRegistrarLibroRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 140, 30));

        btnVerLibrosRegistradosRegLibro.setText("Ver Libros Registrados");
        jPanel2.add(btnVerLibrosRegistradosRegLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, 160, 30));

        jTabbedPane2.addTab("Registrar Libros", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtAreaResLibro.setColumns(20);
        txtAreaResLibro.setRows(5);
        jScrollPane1.setViewportView(txtAreaResLibro);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 91, 490, 350));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Libros Disponibles");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 23, -1, -1));

        jLabel2.setText("Cédula");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, -1));
        jPanel3.add(txtCedulaResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 200, -1));

        jLabel3.setText("Cantidad de Libros");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));
        jPanel3.add(txtNombreLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 200, -1));

        btnBuscarLibroResLibro.setText("Buscar Libro");
        btnBuscarLibroResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnBuscarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 140, 30));

        txtAreaDatosEstudianteResLibro.setColumns(20);
        txtAreaDatosEstudianteResLibro.setRows(5);
        jScrollPane2.setViewportView(txtAreaDatosEstudianteResLibro);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 310, 90));

        btnVerificarEstudianteResLibro.setText("Verificar Estudiante");
        btnVerificarEstudianteResLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarEstudianteResLibroActionPerformed(evt);
            }
        });
        jPanel3.add(btnVerificarEstudianteResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 160, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar Todos", "Romance", "Ficción", "Comedia", "Ciencia", "Drama" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, -1, -1));

        txtAreaDatosLibrosResLibro.setColumns(20);
        txtAreaDatosLibrosResLibro.setRows(5);
        jScrollPane3.setViewportView(txtAreaDatosLibrosResLibro);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 310, 40));

        btnDevolverLibroResLibro.setText("Devolver Libro");
        jPanel3.add(btnDevolverLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 410, 140, 30));

        btnReservarLibroResLibro.setText("Reservar Libro");
        jPanel3.add(btnReservarLibroResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 140, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Buscar Libro");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Buscar Estudiante");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel24.setText("Nombre del Libro");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        comboBoxCantidadLibrosResLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel3.add(comboBoxCantidadLibrosResLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 380, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Categoría");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, -1, -1));

        jTabbedPane2.addTab("Reservar Libros", jPanel3);

        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarLibroResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroResLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLibroResLibroActionPerformed

    private void btnVerificarEstudianteResLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarEstudianteResLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerificarEstudianteResLibroActionPerformed

    private void btnEliminarLibroRegLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarLibroRegLibroActionPerformed
        // TODO add your handling code here:
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
        verificarEstudiantes();
    }//GEN-LAST:event_btnVerificarEstudianteRegEstActionPerformed

    private void btnModificarEstudianteRegEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarEstudianteRegEstActionPerformed
        // TODO add your handling code here:
        modificarEstudiante();
    }//GEN-LAST:event_btnModificarEstudianteRegEstActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Trabajo01_Int().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarLibroResLibro;
    private javax.swing.JButton btnDevolverLibroResLibro;
    private javax.swing.JButton btnEliminarEstudianteRegEst;
    private javax.swing.JButton btnEliminarLibroRegLibro;
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
    private javax.swing.JComboBox<String> comboBoxCantidadLibrosResLibro;
    private javax.swing.JComboBox<String> comboBoxCarreraRegEst;
    private javax.swing.JComboBox<String> comboBoxCategoriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxMateriaRegLibro;
    private javax.swing.JComboBox<String> comboBoxNivelRegEst;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTextField txtDiaRegEst;
    private javax.swing.JTextField txtMesRegEst;
    private javax.swing.JTextField txtNombreLibroResLibro;
    private javax.swing.JTextField txtNombreRegEst;
    private javax.swing.JTextField txtNombreRegLibro;
    private javax.swing.JTextField txtNumeroCopiasRegLibro;
    private javax.swing.JTextField txtNumeroDisponiblesRegLibro;
    // End of variables declaration//GEN-END:variables
}
