import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmDevuelta extends JFrame {

    // variables globales
    private JComboBox cmbDenominacion;
    private int[] denominaciones = { 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50 };
    private int[] existencias = new int[denominaciones.length];
    JTextField txtExistencia;
    JTextField txtDevuelta;
    JTable tblDevuelta;
    String[] encabezados = { "Cantidad", "Presentación", "Denominación" };

    // metodo constructor
    public FrmDevuelta() {
        // dimension de la ventana
        setSize(400, 400);
        // asignar el titulo
        setTitle("Cálculo de Devuelta");
        // definir operecion de salida al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // anular la distribución de elementos en la ventana
        setLayout(null);

        // Agregar etiqueta de las denominaciones
        JLabel lblDenominacion = new JLabel("Denominación");
        lblDenominacion.setBounds(10, 10, 100, 25);
        add(lblDenominacion);

        // agregar la lista desplegable con las denominaciones
        cmbDenominacion = new JComboBox();
        cmbDenominacion.setBounds(120, 10, 100, 25);
        add(cmbDenominacion);

        // ciclo para que recorre las denominaciones
        for (int denominacion : denominaciones) {
            cmbDenominacion.addItem(denominacion);
        }

        // agregar botón para actualizar existencia
        JButton btnExistencia = new JButton("Actualizar Existencia");
        btnExistencia.setBounds(10, 45, 180, 25);
        add(btnExistencia);

        // agergar caja de texto para consultar y editar cada existencia
        txtExistencia = new JTextField();
        txtExistencia.setBounds(200, 45, 100, 25);
        add(txtExistencia);

        // Agregar etiqueta del valor a devolver
        JLabel lblDevuelta = new JLabel("Valor a devolver");
        lblDevuelta.setBounds(10, 80, 100, 25);
        add(lblDevuelta);

        // agregar caja de texto para leer el valor a devolver
        txtDevuelta = new JTextField();
        txtDevuelta.setBounds(120, 80, 100, 25);
        add(txtDevuelta);

        // agregar boton para calcular la forma de devolver
        JButton btnDevuelta = new JButton("Calcular Devuelta");
        btnDevuelta.setBounds(230, 80, 150, 25);
        add(btnDevuelta);

        // Agregar tabla donde se mostrará la forma de devolver
        tblDevuelta = new JTable();
        JScrollPane spDevuelta = new JScrollPane(tblDevuelta);
        spDevuelta.setBounds(10, 125, 365, 200);
        add(spDevuelta);

        // definir el modelo de datos inicial de la tabla

        DefaultTableModel dtm = new DefaultTableModel(null, encabezados);
        tblDevuelta.setModel(dtm);

        // declaración de eventos
        cmbDenominacion.addActionListener(evento -> {
            consultarExistencia();
        });

        btnExistencia.addActionListener(evento -> {
            actualizarExistencia();
        });

        btnDevuelta.addActionListener(evento -> {
            calcularDevuelta();
        });

    }

    private void consultarExistencia() {
        // validar que el usuario haya seleccionado una DENOMINACION
        if (cmbDenominacion.getSelectedIndex() >= 0) {
            // mostrar en la caja de texto el valor de la EXISTENCIA que corresponde a la
            // DENOMINACION
            txtExistencia.setText(String.valueOf(existencias[cmbDenominacion.getSelectedIndex()]));
        }
    }

    private void actualizarExistencia() {
        if (cmbDenominacion.getSelectedIndex() >= 0) {
            try {
                existencias[cmbDenominacion.getSelectedIndex()] = Integer.parseInt(txtExistencia.getText());
            } catch (Exception ex) {
                txtExistencia.setText("");
                txtExistencia.requestFocus();
                JOptionPane.showMessageDialog(null, "La existencia debe ser un número entero");
            }
        }
    }

    private void calcularDevuelta() {
        try {
            int valorDevuelta = Integer.parseInt(txtDevuelta.getText());

            int[] devuelta = new int[denominaciones.length];

            int indiceDenominacion = 0;
            int totalFilas = 0;
            while (valorDevuelta > 0 && indiceDenominacion < denominaciones.length) {

                if (valorDevuelta >= denominaciones[indiceDenominacion]) {
                    int cantidadNecesaria = valorDevuelta / denominaciones[indiceDenominacion];
                    if (existencias[indiceDenominacion] < cantidadNecesaria) {
                        cantidadNecesaria = existencias[indiceDenominacion];
                    }
                    if (cantidadNecesaria > 0) {
                        devuelta[indiceDenominacion] = cantidadNecesaria;
                        // actualizar el valor a devolver
                        valorDevuelta -= cantidadNecesaria * denominaciones[indiceDenominacion];
                        totalFilas++;
                    }
                }
                indiceDenominacion++;
            }
            if (totalFilas > 0) {
                // mostrar el resultado en la tabla
                String[][] datos = new String[totalFilas][encabezados.length];

                totalFilas = 0;
                for (int i = 0; i < denominaciones.length; i++) {
                    if (devuelta[i] > 0) {
                        datos[totalFilas][0] = String.valueOf(devuelta[i]);
                        datos[totalFilas][1] = denominaciones[i] >= 2000 ? "Billete" : "Moneda";
                        datos[totalFilas][2] = String.valueOf(denominaciones[i]);
                        totalFilas++;
                    }
                }

                DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
                tblDevuelta.setModel(dtm);
            } else {
                JOptionPane.showMessageDialog(null, "No se puede devolver");
            }

        } catch (Exception ex) {
            txtDevuelta.setText("");
            txtDevuelta.requestFocus();
            JOptionPane.showMessageDialog(null, "El valor a devolver debe ser un número entero");
        }
    }
}
