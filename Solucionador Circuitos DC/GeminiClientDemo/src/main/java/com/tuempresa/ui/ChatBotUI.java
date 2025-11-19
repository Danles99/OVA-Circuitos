package com.tuempresa.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChatBotUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private List<String> history = new ArrayList<>(); 
    private String tipoCircuito;
    private String datosIniciales;

    public ChatBotUI(String tipo, String datos) {
        this.tipoCircuito = tipo;
        this.datosIniciales = datos;
        
        setTitle("Asistente IA - " + (tipo != null ? tipo : "General"));
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
        configurarEventos();
        
        mostrarMensajeInicial();
    }

    private void inicializarComponentes() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputField = new JTextField();
        sendButton = new JButton("Enviar");
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void mostrarMensajeInicial() {
        chatArea.append("🤖 **Gemini 2.5 Flash: Asistente de Electrónica**\n");
        chatArea.append("-----------------------------------------------------------------\n");
        
        if (datosIniciales != null) {
            chatArea.append("He cargado el contexto del circuito en " + tipoCircuito + ".\n");
            chatArea.append("Datos: " + datosIniciales + "\n");
            chatArea.append("Pregúntame sobre la Ley de Ohm, la potencia o las aplicaciones de este circuito.\n");
            
            history.add("system: El usuario está analizando un circuito " + tipoCircuito + " con estos resultados: " + datosIniciales);
        } else {
            chatArea.append("¡Hola! ¿En qué puedo ayudarte hoy con tus cálculos o teoría de circuitos?\n");
        }
        
        chatArea.append("-----------------------------------------------------------------\n\n");
    }

    private void configurarEventos() {
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userMessage = inputField.getText().trim();
                if (userMessage.isEmpty()) return;

                chatArea.append("👤 Tú: " + userMessage + "\n");
                inputField.setText("");
                
                history.add("user: " + userMessage);

                inputField.setEnabled(false);
                sendButton.setEnabled(false);
                
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return GeminiAPIClient.obtenerRespuestaConMemoria(history);
                    }

                    @Override
                    protected void done() {
                        try {
                            String aiResponse = get();
                            chatArea.append("🤖 Asistente IA: " + aiResponse + "\n\n");
                            
                            history.add("model: " + aiResponse);
                            
                            chatArea.setCaretPosition(chatArea.getDocument().getLength());
                            
                        } catch (Exception ex) {
                            chatArea.append("⚠️ ERROR: No se pudo obtener respuesta de la IA (" + ex.getMessage() + ").\n");
                            if (!history.isEmpty()) history.remove(history.size() - 1);
                        } finally {
                            inputField.setEnabled(true);
                            sendButton.setEnabled(true);
                            inputField.requestFocusInWindow();
                        }
                    }
                }.execute();
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }
}