package com.example.mini_batalla_naval.model;

public class Tablero {
    private int cantidadBarcos;
    private int filas;
    private int columnas;
    private final Celda[][] celdas;

    public Tablero(int filas, int columnas){
        //podría pedir solo dimensión y hacer una matriz cuadrada. pero al pedir tanto filas y
        // columnas da la opción de posiblemente generar otras dimensiones de matrices a futuro
        this.setFilas(filas);
        this.setColumnas(columnas);
        //random para rango de [10, 15]. fórmula: (Math.random() * (B-A+1)) + A)
        this.setCantidadBarcos((int) (Math.random() * 6) + 10);
        celdas = new Celda[this.filas][this.columnas];
        inicializarMatriz();
        cargarBarcos();
    }

    private void setFilas(int filas) {
        this.filas = filas;
    }

    private void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    private void setCantidadBarcos(int cantidadBarcos) {
        this.cantidadBarcos = cantidadBarcos;
    }

    private void cargarBarcos() {
        int barcosCargados = 0;
        while(barcosCargados < this.cantidadBarcos){
            //misma fórmula random. el rango acá es de [0,5] en matriz de 6*6
            int filaRandom = (int)(Math.random()*this.filas);
            int columnaRandom = (int)(Math.random()*this.columnas);

            if(!celdas[filaRandom][columnaRandom].estaOcupado()){
                celdas[filaRandom][columnaRandom].setOcupado();
                barcosCargados++;
            }
        }
    }

    private void inicializarMatriz() {
        for (int i = 0; i < this.filas; i++){
            for (int j = 0; j < this.columnas; j++){
                celdas[i][j] = new Celda();
            }
        }
    }

    public Celda getCelda(int fila, int columna){
        return this.celdas[fila][columna];
    }

    public int getCantidadBarcos() {
        return this.cantidadBarcos;
    }
}
