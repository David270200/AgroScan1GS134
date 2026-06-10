package com.agroscan.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MLKitHelper simulates plant disease detection.
 * In a real implementation, this would use Google ML Kit Image Labeling
 * or a custom TensorFlow Lite model.
 */
public class MLKitHelper {

    public static class DiagnosisResult {
        public String diseaseName;
        public String description;
        public String causes;
        public String treatment;
        public boolean isHealthy;

        public DiagnosisResult(String diseaseName, String description,
                               String causes, String treatment, boolean isHealthy) {
            this.diseaseName = diseaseName;
            this.description = description;
            this.causes = causes;
            this.treatment = treatment;
            this.isHealthy = isHealthy;
        }
    }

    private static final DiagnosisResult[] DISEASES = {
            new DiagnosisResult(
                    "Tizón tardío (Phytophthora)",
                    "Enfermedad fúngica que afecta hojas y frutos con manchas oscuras acuosas y borde blanquecino.",
                    "Humedad excesiva, temperaturas entre 10-25°C, mal drenaje del suelo.",
                    "Aplicar fungicida cúprico cada 7 días. Eliminar partes afectadas. Mejorar ventilación y drenaje.",
                    false
            ),
            new DiagnosisResult(
                    "Oídio (Powdery Mildew)",
                    "Capa de polvo blanco en hojas y tallos causada por hongos de la familia Erysiphaceae.",
                    "Condiciones cálidas y secas con humedad relativa alta. Plantas con exceso de nitrógeno.",
                    "Aplicar azufre mojable o fungicida sistémico. Podar hojas afectadas. Reducir fertilización nitrogenada.",
                    false
            ),
            new DiagnosisResult(
                    "Antracnosis",
                    "Manchas hundidas de color marrón a negro en hojas, tallos y frutos.",
                    "Hongo Colletotrichum. Se propaga por agua de lluvia y riego por aspersión.",
                    "Fungicida a base de mancozeb o clorotalonil. Evitar riego por aspersión. Rotar cultivos.",
                    false
            ),
            new DiagnosisResult(
                    "Mosaico viral",
                    "Deformación y mosaico amarillo-verde en hojas, crecimiento atrofiado.",
                    "Virus transmitido por insectos (áfidos, trips). No tiene cura directa.",
                    "Eliminar plantas infectadas. Controlar vectores insectiles con insecticida sistémico. Usar semillas certificadas.",
                    false
            ),
            new DiagnosisResult(
                    "Planta sana",
                    "La planta no presenta síntomas visibles de enfermedad.",
                    "N/A",
                    "Mantener riego adecuado y fertilización preventiva. Monitorear periódicamente.",
                    true
            )
    };

    /**
     * Analyzes a plant image (simulated).
     * Pass the plant name as context for a more realistic result.
     */
    public static DiagnosisResult analyze(String plantName) {
        // Simulate processing: 70% chance healthy, 30% disease
        Random rnd = new Random();
        int roll = rnd.nextInt(10);
        if (roll >= 7) {
            return DISEASES[DISEASES.length - 1]; // healthy
        }
        int diseaseIdx = rnd.nextInt(DISEASES.length - 1);
        return DISEASES[diseaseIdx];
    }
}
