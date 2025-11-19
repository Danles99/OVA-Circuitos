package com.tuempresa.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CircuitoSerieUI extends JFrame {
    private JTextField txtVoltaje, txtCorriente, txtCantidadResistencias;
    private JPanel panelResistencias;
    private List<JTextField> camposResistencias = new ArrayList<>();
    
    private JTextArea txtResultado;
    private double ultimoVoltaje = 0, ultimaCorriente = 0;
    private double ultimaResistenciaTotal = 0; 

    // --- CONSTANTES DE ESTILO BÁSICO ---
    private static final int MAX_RESISTENCIAS = 10;
    private static final Color COLOR_PRIMARIO = new Color(30, 70, 115); 
    private static final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 13);
    private static final Font FONT_RESULTADO = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_BOTON_MODULO = new Font("Dialog", Font.BOLD, 14);
    private static final Color COLOR_FONDO_CONTROLES = new Color(245, 245, 245);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    public CircuitoSerieUI() {
        setTitle("Módulo de Análisis de Circuitos en Serie");
        setSize(750, 750); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelControl = crearPanelControl();
        add(panelControl, BorderLayout.NORTH);

        panelResistencias = new JPanel();
        panelResistencias.setLayout(new GridBagLayout());
        panelResistencias.setBackground(Color.WHITE);
        JScrollPane scrollResistencias = new JScrollPane(panelResistencias);
        scrollResistencias.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            "DEFINICIÓN DE PARÁMETROS RESISTIVOS (Opcional si V e I son conocidos)", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            FONT_LABEL.deriveFont(Font.BOLD), COLOR_PRIMARIO));
        
        txtResultado = crearAreaResultados();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollResistencias, new JScrollPane(txtResultado));
        splitPane.setResizeWeight(0.5); 
        add(splitPane, BorderLayout.CENTER);

        JPanel panelAcciones = crearPanelAcciones();
        add(panelAcciones, BorderLayout.SOUTH);

        configurarEventos(panelAcciones);
        
        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.6));
    }
    
    // --- NUEVO MÉTODO PARA EL PANEL DE ADVERTENCIA ---
    private JPanel crearPanelAdvertencia() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(255, 255, 204)); // Amarillo pálido
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 1)); 
        
        JLabel label = new JLabel("⚠️ REGLA DE CÁLCULO: Solo necesita ingresar 2 de 3 parámetros para calcular el tercero (V, I, o Rt).");
        label.setFont(FONT_LABEL.deriveFont(Font.BOLD, 12f));
        label.setForeground(new Color(153, 102, 0));
        panel.add(label);
        return panel;
    }

    private JPanel crearPanelControl() {
        // Nuevo contenedor que usará BorderLayout para el título y la advertencia
        JPanel panelContenedor = new JPanel(new BorderLayout()); 
        panelContenedor.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
            "DEFINICIÓN DE PARÁMETROS INICIALES (V, I)", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            FONT_LABEL.deriveFont(Font.BOLD), COLOR_PRIMARIO));
        
        // El panel de GridLayout que contiene las cajas de texto V, I, Cant. Resistencias
        JPanel panelInputs = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInputs.setBackground(COLOR_FONDO_CONTROLES);
        panelInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añadir padding

        panelInputs.add(new JLabel("Voltaje de Fuente (V):"));
        txtVoltaje = new JTextField();
        panelInputs.add(txtVoltaje);

        panelInputs.add(new JLabel("Corriente Total (A):"));
        txtCorriente = new JTextField();
        panelInputs.add(txtCorriente);

        panelInputs.add(new JLabel("Cant. Resistencias (0-" + MAX_RESISTENCIAS + "):"));
        txtCantidadResistencias = new JTextField(); // Permite 0
        panelInputs.add(txtCantidadResistencias);

        // Añadir advertencia y inputs al contenedor principal
        panelContenedor.add(crearPanelAdvertencia(), BorderLayout.NORTH);
        panelContenedor.add(panelInputs, BorderLayout.CENTER);

        return panelContenedor;
    }
    
    private JTextArea crearAreaResultados() {
        JTextArea area = new JTextArea(8, 40);
        area.setEditable(false);
        area.setFont(FONT_RESULTADO);
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
            "REPORTE DE RESULTADOS", 
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL.deriveFont(Font.BOLD)));
        return area;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(COLOR_FONDO_CONTROLES);

        JButton btnGenerarCampos = new JButton("Generar Campos (R)"); 
        JButton btnCalcular = new JButton("Ejecutar Cálculo"); 
        JButton btnIA = new JButton("Asistente (IA)"); 
        JButton btnRegresar = new JButton("Retornar al Menú Principal"); 

        btnGenerarCampos.setFont(FONT_BOTON_MODULO);
        btnCalcular.setFont(FONT_BOTON_MODULO);
        btnIA.setFont(FONT_BOTON_MODULO);
        btnRegresar.setFont(FONT_BOTON_MODULO); 

        btnGenerarCampos.setCursor(HAND_CURSOR);
        btnCalcular.setCursor(HAND_CURSOR);
        btnIA.setCursor(HAND_CURSOR);
        btnRegresar.setCursor(HAND_CURSOR);

        panel.add(btnGenerarCampos);
        panel.add(btnCalcular);
        panel.add(btnIA); 
        panel.add(btnRegresar);
        return panel;
    }
    
    private void generarCamposResistencias() {
        panelResistencias.removeAll();
        camposResistencias.clear();
        txtResultado.setText(""); 

        try {
            int cantidad = Integer.parseInt(txtCantidadResistencias.getText());

            if (cantidad < 0 || cantidad > MAX_RESISTENCIAS) {
                JOptionPane.showMessageDialog(this, 
                    "Parámetro de cantidad fuera de rango (0-" + MAX_RESISTENCIAS + ").", 
                    "Error de Parámetro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            panelResistencias.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.weightx = 0.5;

            for (int i = 0; i < cantidad; i++) {
                JLabel rLabel = new JLabel("Resistencia R" + (i + 1) + " (Ohm):");
                rLabel.setFont(FONT_LABEL);
                JTextField rField = new JTextField(10);
                rField.setFont(FONT_LABEL);
                
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.4;
                panelResistencias.add(rLabel, gbc);
                
                gbc.gridx = 1;
                gbc.gridy = i;
                gbc.weightx = 0.6;
                panelResistencias.add(rField, gbc);
                
                camposResistencias.add(rField);
            }

            // Mensaje si no se generaron campos
            if (cantidad == 0) {
                 JLabel msg = new JLabel("Se ingresó 0 resistencias. Se calculará Rt mediante V/I.");
                 msg.setForeground(Color.BLUE);
                 gbc.gridx = 0;
                 gbc.gridy = 0;
                 gbc.gridwidth = 2;
                 panelResistencias.add(msg, gbc);
            }


            panelResistencias.revalidate();
            panelResistencias.repaint();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error de formato: Ingrese un valor entero para la cantidad de resistencias (0-" + MAX_RESISTENCIAS + ").", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarEventos(JPanel panelAcciones) {
        JButton btnGenerarCampos = (JButton) panelAcciones.getComponent(0);
        JButton btnCalcular = (JButton) panelAcciones.getComponent(1);
        JButton btnIA = (JButton) panelAcciones.getComponent(2);

        btnGenerarCampos.addActionListener(e -> generarCamposResistencias());
        
        ((JButton) panelAcciones.getComponent(3)).addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });


        btnCalcular.addActionListener(e -> {
            try {
                // Leer V, I y verificar si están vacíos
                boolean v_conocido = !txtVoltaje.getText().isEmpty();
                boolean i_conocida = !txtCorriente.getText().isEmpty();

                double voltaje = v_conocido ? Double.parseDouble(txtVoltaje.getText()) : 0;
                double corriente = i_conocida ? Double.parseDouble(txtCorriente.getText()) : 0;
                
                List<Double> resistenciasValores = new ArrayList<>();
                double resistenciaTotal_entradas = 0;
                
                if (!camposResistencias.isEmpty()) {
                    for (int i = 0; i < camposResistencias.size(); i++) {
                        String textoR = camposResistencias.get(i).getText();
                        double valorR = Double.parseDouble(textoR);
                        if (valorR <= 0) {
                            JOptionPane.showMessageDialog(this, "La Resistencia R" + (i + 1) + " debe ser un valor positivo.", "Error de Parámetros", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        resistenciasValores.add(valorR);
                    }
                    resistenciaTotal_entradas = resistenciasValores.stream().mapToDouble(Double::doubleValue).sum();
                }

                double resistenciaTotal_calculada;
                String mensajeTransformacion = "";
                
                // CASO 1: V y I conocidos -> CALCULAR Rt (Permitido incluso sin Ri)
                if (v_conocido && i_conocida) {
                    if (corriente == 0) {
                        JOptionPane.showMessageDialog(this, "La corriente no puede ser cero para calcular Rt.", "Error Matemático", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    resistenciaTotal_calculada = voltaje / corriente;
                // CASO 2: V conocido y (I o R desconocida) -> Necesita Rt de las entradas
                } else if (v_conocido && !i_conocida) {
                    if (resistenciaTotal_entradas == 0) {
                         JOptionPane.showMessageDialog(this, "Error: Necesita definir las resistencias o la Corriente (I).", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                         return;
                    }
                    corriente = voltaje / resistenciaTotal_entradas;
                    resistenciaTotal_calculada = resistenciaTotal_entradas;
                    
                    // Lógica de Transformación de Fuente (usando R1)
                    if (!resistenciasValores.isEmpty()) {
                        double R1 = resistenciasValores.get(0);
                        mensajeTransformacion = String.format(
                            "*** Se realizó una TRANSFORMACIÓN DE FUENTE: ***\n" +
                            "Fuente de Voltaje (V=%.6f V en serie con R1=%.6f Ω)\n" +
                            "Convertida a Fuente de Corriente (I=%.6f A en paralelo con R1=%.6f Ω).\n\n",
                            voltaje, R1, corriente, R1);
                    }
                // CASO 3: I conocido y (V o R desconocida) -> Necesita Rt de las entradas
                } else if (i_conocida && !v_conocido) {
                    if (resistenciaTotal_entradas == 0) {
                         JOptionPane.showMessageDialog(this, "Error: Necesita definir las resistencias o el Voltaje (V).", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                         return;
                    }
                    voltaje = corriente * resistenciaTotal_entradas;
                    resistenciaTotal_calculada = resistenciaTotal_entradas;
                
                // CASO 4: Muy pocos datos (Solo V, Solo I, o solo resistencias)
                } else {
                     JOptionPane.showMessageDialog(this, "Error: Necesita definir al menos 2 parámetros para calcular (V, I, o las resistencias para Rt).", "Error de Datos", JOptionPane.WARNING_MESSAGE);
                     return;
                }

                // 3. Almacenar resultados finales
                ultimoVoltaje = voltaje;
                ultimaCorriente = corriente;
                ultimaResistenciaTotal = resistenciaTotal_calculada;

                // 4. Formato de reporte
                StringBuilder sb = new StringBuilder();
                
                if (!mensajeTransformacion.isEmpty()) {
                    sb.append(mensajeTransformacion);
                }
                
                sb.append("⚡ PRINCIPIO SERIE: La corriente es la misma en todos los elementos del circuito.\n");

                sb.append("--- REPORTE DETALLADO DE COMPONENTES ---\n");
                
                if (resistenciasValores.isEmpty()) {
                    sb.append(String.format("CÁLCULO DE RESISTENCIA TOTAL (Rt) ÚNICAMENTE:\nRt = V / I = %.6f Ω\n", ultimaResistenciaTotal));
                
                } else {
                    if (v_conocido && i_conocida && Math.abs(resistenciaTotal_calculada - resistenciaTotal_entradas) > 1e-6) {
                        sb.append(String.format("⚠️ Advertencia: Rt calculada (V/I=%.6f Ω) difiere de la suma de Ri (%.6f Ω).\n", 
                            resistenciaTotal_calculada, resistenciaTotal_entradas));
                        sb.append("   Se utilizó la Rt calculada (V/I) para el análisis.\n");
                    }
                    
                    sb.append(String.format("%-10s | %-12s | %-12s\n", "Componente", "Resistencia (Ω)", "Voltaje (V)"));
                    sb.append("------------------------------------------\n");

                    for (int i = 0; i < resistenciasValores.size(); i++) {
                        double Ri = resistenciasValores.get(i);
                        double Vi = ultimaCorriente * Ri; 
                        sb.append(String.format("R%-9d| %-12.6f | %-12.6f\n", (i + 1), Ri, Vi));
                    }
                    sb.append("------------------------------------------\n");
                }


                sb.append("\n--- RESUMEN DE CÁLCULO (LEY DE OHM) ---\n");
                sb.append(String.format("Resistencia Equivalente (Rt): %.6f Ω\n", ultimaResistenciaTotal));
                sb.append(String.format("Voltaje de Fuente (V): %.6f V\n", ultimoVoltaje));
                sb.append(String.format("Corriente Total (I): %.6f A\n", ultimaCorriente));
                
                txtResultado.setText(sb.toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error de formato: Asegúrese de que todos los campos contengan valores numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de Ejecución: " + ex.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Listener para el botón IA
        btnIA.addActionListener(e -> {
            if (ultimaCorriente == 0 && ultimaResistenciaTotal == 0 && ultimoVoltaje == 0) {
                JOptionPane.showMessageDialog(this, "Ejecute un cálculo previo para habilitar la consulta contextual.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                String datosCircuito = String.format(
                    "Circuito Serie: Voltaje=%.6f V, Corriente=%.6f A, Resistencia total=%.6f Ω.", 
                    ultimoVoltaje, ultimaCorriente, ultimaResistenciaTotal);

                ChatBotUI chatWindow = new ChatBotUI("Serie", datosCircuito);
                chatWindow.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el módulo IA: " + ex.getMessage(), "Error de Módulo", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CircuitoSerieUI().setVisible(true));
    }
}