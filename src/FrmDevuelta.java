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

    JComboBox cmbDenominacion;
    int[] denominaciones = { 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50 };
    int[] existencias = new int[denominaciones.length];
    JTextField txtExistencia, txtDevuelta;
    String[] encabezados = { "Cantidad", "Presentación", "Denominación" };
    JTable tblDevuelta;

    // metodo constructor
    public FrmDevuelta() {
        setSize(500, 400);
        setTitle("Cálculo Devuelta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblDenominacion = new JLabel("Denominación");
        lblDenominacion.setBounds(10, 10, 100, 25);
        add(lblDenominacion);

        cmbDenominacion = new JComboBox();
        cmbDenominacion.setBounds(220, 10, 100, 25);
        add(cmbDenominacion);

        for (int denominacion : denominaciones) {
            cmbDenominacion.addItem(denominacion);
        }

        JButton btnExistencia = new JButton("Actualizar Existencia");
        btnExistencia.setBounds(10, 40, 200, 25);
        add(btnExistencia);

        txtExistencia = new JTextField();
        txtExistencia.setBounds(220, 40, 50, 25);
        add(txtExistencia);

        JLabel lblDevuelta = new JLabel("Valor a Devolver");
        lblDevuelta.setBounds(10, 70, 200, 25);
        add(lblDevuelta);

        txtDevuelta = new JTextField();
        txtDevuelta.setBounds(220, 70, 100, 25);
        add(txtDevuelta);

        JButton btnDevuelta = new JButton("Calcular");
        btnDevuelta.setBounds(330, 70, 100, 25);
        add(btnDevuelta);

        tblDevuelta = new JTable();
        JScrollPane spDevuelta = new JScrollPane(tblDevuelta);
        spDevuelta.setBounds(10, 100, 470, 150);

        add(spDevuelta);

        DefaultTableModel dtm = new DefaultTableModel(null, encabezados);
        tblDevuelta.setModel(dtm);

        // eventos
        btnExistencia.addActionListener(e -> {
            actualizarExistencia();
        });

        btnDevuelta.addActionListener(e -> {
            calcularDevuelta();
        });

        cmbDenominacion.addActionListener(e -> {
            consultarExistencia();
        });

    }

    private void consultarExistencia() {
        if (cmbDenominacion.getSelectedIndex() >= 0) {
            txtExistencia.setText(String.valueOf(existencias[cmbDenominacion.getSelectedIndex()]));
        }
    }

    private void actualizarExistencia() {

        if (cmbDenominacion.getSelectedIndex() >= 0) {
            existencias[cmbDenominacion.getSelectedIndex()] = Integer.parseInt(txtExistencia.getText());
        }
    }

    private void calcularDevuelta() {
        // obtener el valor a devolver
        int devuelta = Integer.parseInt(txtDevuelta.getText());

        int[] cantidadesDevuelta = new int[denominaciones.length];

        for (int i = 0; i < denominaciones.length; i++) {
            if (devuelta >= denominaciones[i]) {
                int cantidadNecesaria = (int) (devuelta / denominaciones[i]);
                /*
                 * if (existencias[i] >= cantidadNecesaria) {
                 * cantidadesDevuelta[i] = cantidadNecesaria;
                 * } else {
                 * cantidadesDevuelta[i] = existencias[i];
                 * }
                 */
                cantidadesDevuelta[i] = existencias[i] >= cantidadNecesaria ? cantidadNecesaria : existencias[i];
                devuelta -= cantidadesDevuelta[i] * denominaciones[i];
                existencias[i] -= cantidadesDevuelta[i];
                if (devuelta == 0) {
                    break;
                }
            }
        }

        // contar las denominaciones utilizadas
        int totalDenominaciones = 0;
        for (int i = 0; i < denominaciones.length; i++) {
            if (cantidadesDevuelta[i] > 0)
                totalDenominaciones++;
        }

        // calcular la matriz de resultados
        String[][] resultado = new String[totalDenominaciones][3];
        int fila = 0;
        for (int i = 0; i < denominaciones.length; i++) {
            if (cantidadesDevuelta[i] > 0) {
                resultado[fila][0] = String.valueOf(cantidadesDevuelta[i]);
                resultado[fila][1] = denominaciones[i] >= 2000 ? "Billete" : "Moneda";
                resultado[fila][2] = String.valueOf(denominaciones[i]);
                fila++;
            }
        }

        // mostrar el resultado
        DefaultTableModel dtm = new DefaultTableModel(resultado, encabezados);
        tblDevuelta.setModel(dtm);

        if (devuelta > 0) {
            JOptionPane.showMessageDialog(null, "Quedó pendiente $" + devuelta);
        }
    }

}
