# ⚡ Asistente Lógico: OVA para Circuitos Eléctricos con IA

> **Reto 36 – 2025 | Universidad Santo Tomás**
> *Una herramienta interactiva que combina simulación de circuitos con la potencia de Google Gemini.*

![Java](https://img.shields.io/badge/Java-SE%2011%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-1B6AC6?style=for-the-badge&logo=java&logoColor=white)
![Gemini API](https://img.shields.io/badge/AI-Gemini%20Flash%202.5-8E75B2?style=for-the-badge&logo=google&logoColor=white)
![Status](https://img.shields.io/badge/Estado-Prototipo%20Funcional-success?style=for-the-badge)

## 📖 Descripción

**Asistente Lógico** es una aplicación de escritorio desarrollada en Java que permite a estudiantes de ingeniería simular y analizar circuitos resistivos básicos (Serie y Paralelo). 

Lo que hace único a este proyecto es su integración con la **API de Gemini 2.5 Flash**, la cual actúa como un tutor virtual capaz de:
* Explicar el "porqué" de los resultados matemáticos.
* Responder dudas conceptuales (Ley de Ohm, Kirchhoff).
* Mantener el contexto de la conversación recordando los valores del circuito simulado.

## 🚀 Características Principales

### 1. Motor de Cálculo (`CalculadoraCircuitos.java`)
* Resolución automática de **Circuitos en Serie y Paralelo**.
* Cálculo de Voltaje (V), Corriente (I) y Resistencia Equivalente (Req).
* **Detección automática de casos:** Calcula variables faltantes basándose en las entradas (ej. si ingresas V y R, calcula I).
* **Transformación de Fuentes:** Identifica y sugiere transformaciones entre fuentes de voltaje y corriente cuando aplica.

### 2. Asistente IA Contextual (`GeminiAPIClient.java`)
* Conexión directa a `gemini-2.5-flash` mediante peticiones HTTP.
* **Memoria conversacional:** El bot recuerda los datos del último circuito calculado para dar respuestas precisas.
* Manejo de errores y reintentos automáticos ante fallos de red.

### 3. Interfaz Gráfica (`ui` package)
* Diseño limpio usando Java Swing.
* Validación de entradas numéricas para evitar errores de ejecución.
* Tablas de resultados detallados componente por componente.

---

## 🛠️ Instalación y Puesta en Marcha

### Prerrequisitos
* **Java JDK 11** o superior.
* **IDE:** NetBeans, IntelliJ IDEA o Eclipse.
* **Conexión a Internet** (Indispensable para el módulo de IA).

### Pasos para ejecutar
1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Danles99/OVA-Circuitos.git](https://github.com/Danles99/OVA-Circuitos.git)
    ```
2.  **Abrir el proyecto:**
    * Carga la carpeta del proyecto en tu IDE de preferencia.
3.  **Gestión de Dependencias:**
    * El proyecto utiliza la librería `org.json` para parsear las respuestas de Google. Asegúrate de que el `.jar` de JSON esté incluido en el *ClassPath* o librerías del proyecto.
4.  **⚠️ Configuración de la API Key:**
    * Abre el archivo `src/com/tuempresa/ui/GeminiAPIClient.java`.
    * Ubica la variable `API_KEY`.
    * Reemplázala con tu propia clave de Google AI Studio si la actual ha caducado.
    ```java
    private static final String API_KEY = "TU_API_KEY_AQUI";
    ```
5.  **Ejecutar:**
    * Corre el archivo `MenuPrincipal.java` para iniciar la aplicación.

---

## 📂 Estructura del Proyecto

```text
com.tuempresa
├── logica
│   └── CalculadoraCircuitos.java  // Lógica matemática pura (Ley de Ohm/Kirchhoff)
├── ui
│   ├── MenuPrincipal.java         // Pantalla de inicio y selección
│   ├── CircuitoSerieUI.java       // Interfaz para análisis serie
│   ├── CircuitoParaleloUI.java    // Interfaz para análisis paralelo
│   ├── ChatBotUI.java             // Ventana del chat con el asistente
│   └── GeminiAPIClient.java       // Cliente HTTP para conectar con Google Gemini
└── resources
    └── imagenes                   // Diagramas de circuitos (png)
