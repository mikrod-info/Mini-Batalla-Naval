package com.example.mini_batalla_naval.model;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CeldaClickListener implements View.OnClickListener{
    private Celda celda;
    private UpdaterTextView updater;

    public CeldaClickListener(Celda celda, UpdaterTextView updater) {
        this.setCelda(celda);
        this.setUpdater(updater);
    }

    private void setUpdater(UpdaterTextView updater) {
        this.updater = updater;
    }

    private void setCelda(Celda celda) {
        this.celda = celda;
    }

    //comportamiento que implementa el listener de botón que viene del view.onclicklistener.
    @Override
    public void onClick(View v) {
        //downcasting del contexto v
        Button boton = (Button) v;

        if (this.celda.estaOcupado()){
            boton.setText("\uD83D\uDEF3");//"🛳"
            //para solucionar: Toast se apila y es lento. conviene quitarlo?
            Toast.makeText(v.getContext(), "Tocado!", Toast.LENGTH_SHORT).show();
            updater.incrementarAcierto();
        } else {
            boton.setText("\uD83C\uDF0A");//"🌊"
            Toast.makeText(v.getContext(), "Agua!", Toast.LENGTH_SHORT).show();
        }

        updater.actualizar();

        boton.setEnabled(false);
    }
}
