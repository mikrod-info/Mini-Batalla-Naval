package com.example.mini_batalla_naval.model;

import android.widget.TextView;

public class UpdaterTextView {
    private int valorRestante, valorMovimientos, valorAciertos;
    private TextView tvRestante, tvMovimientos, tvAciertos;

    public UpdaterTextView(TextView tvRestante, TextView tvMovimientos, TextView tvAciertos, int cantidadBarcos) {
        this.setTvRestante(tvRestante);
        this.setTvMovimientos(tvMovimientos);
        this.setTvAciertos(tvAciertos);
        inicializarContadores(cantidadBarcos);
        actualizarVistas();
    }

    private void setTvRestante(TextView tvRestante) {
        this.tvRestante = tvRestante;
    }

    private void setTvMovimientos(TextView tvMovimientos) {
        this.tvMovimientos = tvMovimientos;
    }

    private void setTvAciertos(TextView tvAciertos) {
        this.tvAciertos = tvAciertos;
    }

    private void inicializarContadores(int cantidadBarcos) {
        this.valorRestante = cantidadBarcos;
        this.valorMovimientos = 0;
        this.valorAciertos = 0;
    }

    private void actualizarVistas() {
        String strRestante = "Barcos restantes: " + this.valorRestante;
        String strMovimientos = "Movimientos: " + this.valorMovimientos;
        String strAciertos = "Aciertos: " + this.valorAciertos;

        this.tvRestante.setText(strRestante);
        this.tvMovimientos.setText(strMovimientos);
        this.tvAciertos.setText(strAciertos);
    }

    public void actualizar(){
        this.valorMovimientos++;
        actualizarVistas();
    }

    public void incrementarAcierto(){
        this.valorRestante--;
        this.valorAciertos++;
    }

}
