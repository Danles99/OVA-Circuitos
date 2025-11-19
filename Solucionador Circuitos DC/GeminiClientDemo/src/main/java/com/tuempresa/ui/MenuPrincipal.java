package com.tuempresa.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPrincipal extends JFrame {

    private JLabel lblTitulo;
    private JButton btnSerie, btnParalelo, btnIA, btnSalir;
    private JLabel lblImgSerie, lblImgParalelo;

    public MenuPrincipal() {
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(15, 15));

        // --- Título ---
        lblTitulo = new JLabel("Simulador de Circuitos Resistivos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- Panel central ---
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // =========================================================================
        // === RUTA DE IMAGENES AJUSTADA: Probar sin el prefijo '/resources' ===
        // =========================================================================
        lblImgSerie = new JLabel(cargarImagen("/imagenes/circuito_serie.png"), SwingConstants.CENTER);
        lblImgParalelo = new JLabel(cargarImagen("/imagenes/circuito_paralelo.png"), SwingConstants.CENTER);
        
        // Si no se encuentra la imagen, mantenemos el texto de advertencia
        if (lblImgSerie.getIcon() == null) {
            lblImgSerie.setText("[¡Error! Imagen de circuito en serie no encontrada]");
        }
        if (lblImgParalelo.getIcon() == null) {
            lblImgParalelo.setText("[¡Error! Imagen de circuito en paralelo no encontrada]");
        }
        // =========================================================================
        
        btnSerie = new JButton("1. Circuito en Serie");
        btnParalelo = new JButton("2. Circuito en Paralelo");

        panelCentral.add(lblImgSerie);
        panelCentral.add(lblImgParalelo);
        panelCentral.add(btnSerie);
        panelCentral.add(btnParalelo);

        add(panelCentral, BorderLayout.CENTER);

        // --- Panel inferior con IA y salir ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        btnIA = new JButton("Asistente IA (Chat) 💬");
        btnSalir = new JButton("Salir 🚪");

        panelInferior.add(btnIA);
        panelInferior.add(btnSalir);

        add(panelInferior, BorderLayout.SOUTH);
        
        // Estilos de botones
        btnSerie.setFont(new Font("Arial", Font.BOLD, 14));
        btnParalelo.setFont(new Font("Arial", Font.BOLD, 14));
        btnIA.setBackground(new Color(52, 152, 219));
        btnIA.setForeground(Color.WHITE);
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
    }
    
    // --- MÉTODO AUXILIAR PARA CARGAR Y ESCALAR IMAGENES ---
    private ImageIcon cargarImagen(String ruta) {
        try {
            // Se intenta cargar el recurso desde el classpath
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL != null) {
                // Escalar la imagen a 200x150
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image img = originalIcon.getImage();
                Image scaledImg = img.getScaledInstance(200, 150, Image.SCALE_SMOOTH); 
                return new ImageIcon(scaledImg);
            } else {
                System.err.println("Archivo de imagen no encontrado: " + ruta);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + ruta + " | " + e.getMessage());
            return null;
        }
    }
    // ------------------------------------------

    private void configurarVentana() {
        setTitle("Menú Principal - Simulador de Circuitos");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void configurarEventos() {
        // --- Abrir ventana de circuito en serie ---
        btnSerie.addActionListener(e -> {
            try {
                new CircuitoSerieUI().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al iniciar Circuito en Serie: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Abrir ventana de circuito en paralelo ---
        btnParalelo.addActionListener(e -> {
            try {
                new CircuitoParaleloUI().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al iniciar Circuito en Paralelo: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Abrir Chatbot SIN contexto (con try-catch para debugging) ---
        btnIA.addActionListener(e -> {
            try {
                ChatBotUI chatWindow = new ChatBotUI(null, null); 
                chatWindow.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "ERROR CRÍTICO AL ABRIR EL CHAT: " + ex.getMessage() + "\nRevisa la consola para el stack trace completo.", 
                    "Error de Inicialización del Chat", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Salir de la aplicación ---
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}