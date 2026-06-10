package com.agroscan.models;

public class PlantRecord {
    public String scanId;
    public String plantName;
    public String cropType;
    public String symptoms;
    public String diagnosis;
    public String date;
    public boolean isHealthy;

    public PlantRecord(String scanId, String plantName, String cropType,
                       String symptoms, String diagnosis, String date, boolean isHealthy) {
        this.scanId = scanId;
        this.plantName = plantName;
        this.cropType = cropType;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.date = date;
        this.isHealthy = isHealthy;
    }
}
