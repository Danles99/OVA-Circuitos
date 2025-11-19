package com.tuempresa.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GeminiAPIClient {

    private static final String API_KEY = "AIzaSyCAx_kxzzPQT0wSuolOiEqNOlXEJF0nPI0"; 

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private static final int MAX_RETRIES = 3; 
    private static final long INITIAL_DELAY_MS = 1000; 

    public static String obtenerRespuesta(String prompt) {
        List<String> singleTurnHistory = new ArrayList<>();
        singleTurnHistory.add("user: " + prompt);
        return obtenerRespuestaConMemoria(singleTurnHistory);
    }

    public static String obtenerRespuestaConMemoria(List<String> history) {
        
        for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(API_URL);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);

                JSONArray contentsArray = new JSONArray();
                
                for (String turn : history) {
                    String[] parts = turn.split(": ", 2); 
                    if (parts.length < 2) continue;

                    String role = parts[0].trim().toLowerCase();
                    String text = parts[1].trim(); 

                    JSONObject textObj = new JSONObject();
                    textObj.put("text", text);

                    JSONArray partsArray = new JSONArray();
                    partsArray.put(textObj);
                    
                    JSONObject contentObj = new JSONObject();
                    contentObj.put("parts", partsArray);
                    
                    if(role.equals("system")) role = "user"; 
                    contentObj.put("role", role);

                    contentsArray.put(contentObj);
                }

                JSONObject requestBody = new JSONObject();
                requestBody.put("contents", contentsArray);
                
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = requestBody.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                
                if (responseCode >= 200 && responseCode < 300) {
                    try (InputStream is = conn.getInputStream();
                         BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                        
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line.trim());
                        }

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONArray candidates = jsonResponse.optJSONArray("candidates");

                        if (candidates != null && candidates.length() > 0) {
                            JSONObject candidate = candidates.getJSONObject(0);
                            JSONObject content = candidate.optJSONObject("content");
                            
                            if (content != null) {
                                JSONArray parts = content.optJSONArray("parts");
                                if (parts != null && parts.length() > 0) {
                                    return parts.getJSONObject(0).optString("text", "No se recibió texto de Gemini.");
                                }
                            }
                            
                            String finishReason = candidate.optString("finishReason", "");
                            if (finishReason.equals("SAFETY")) {
                                return "La respuesta fue bloqueada por razones de seguridad. Por favor, reformula tu pregunta.";
                            }
                        }
                        return "No se encontró respuesta válida del modelo.";
                    }
                } else if (responseCode == 503 || responseCode == 429) {
                    if (retryCount < MAX_RETRIES - 1) {
                        long delay = (long) (INITIAL_DELAY_MS * Math.pow(2, retryCount));
                        Thread.sleep(delay);
                        continue; 
                    } else {
                        return "Error de servidor (HTTP " + responseCode + "). Límite de reintentos excedido. El servicio puede estar temporalmente no disponible.";
                    }
                } else {
                    StringBuilder errorResponse = new StringBuilder();
                    try (InputStream is = conn.getErrorStream()) {
                        if (is != null) {
                            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    errorResponse.append(line.trim());
                                }
                            }
                        }
                    }
                    
                    String errorMsg = errorResponse.toString();
                    if (responseCode == 400) {
                        return "Error en la petición (HTTP 400). Verifica el formato de los mensajes. Detalles: " + errorMsg;
                    } else if (responseCode == 401 || responseCode == 403) {
                        return "Error de autenticación (HTTP " + responseCode + "). Verifica tu API Key. Detalles: " + errorMsg;
                    } else {
                        return "Error al conectar con Gemini (HTTP " + responseCode + "): " + errorMsg;
                    }
                }

            } catch (Exception e) {
                if (retryCount < MAX_RETRIES - 1) {
                    long delay = (long) (INITIAL_DELAY_MS * Math.pow(2, retryCount));
                    try { Thread.sleep(delay); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                return "Error crítico al conectar con Gemini: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            } finally {
                if (conn != null) conn.disconnect();
            }
        }
        
        return "Límite de reintentos excedido y sin respuesta satisfactoria.";
    }
}