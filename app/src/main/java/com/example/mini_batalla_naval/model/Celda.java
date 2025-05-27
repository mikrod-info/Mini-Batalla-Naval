package com.example.mini_batalla_naval.model;

public class Celda {
    private boolean ocupado;

    public Celda(){
        this.ocupado = false;
    }

    public boolean estaOcupado(){
        return this.ocupado;
    }
    public void setOcupado() {
        this.ocupado = true;
    }
}
