package com.pruebatecnica.poliza.controllers;

public class CoreMockRequest {
    private String evento;
    private String polizaId;

    public CoreMockRequest() {
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getPolizaId() {
        return polizaId;
    }

    public void setPolizaId(String polizaId) {
        this.polizaId = polizaId;
    }
}
