package com.agroscan.models;

public class HistoryEntry {
    public String histId;
    public String scanId;
    public String disease;
    public String treatment;
    public String observations;
    public String date;

    public HistoryEntry(String histId, String scanId, String disease,
                        String treatment, String observations, String date) {
        this.histId = histId;
        this.scanId = scanId;
        this.disease = disease;
        this.treatment = treatment;
        this.observations = observations;
        this.date = date;
    }
}
