package com.example.mini_batalla_naval.model

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.mini_batalla_naval.R

class DialogManager(
    private val context: Context,
    private val dialogListener: DialogListener
) {
    fun mostrarDialogoVictoria() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title_victory))
            .setMessage(context.getString(R.string.dialog_message_victory_no_ranking))
            .setPositiveButton(context.getString(R.string.dialog_button_try_again))
            { _, _ -> dialogListener.onDialogRestartGame() }
            .setNegativeButton(context.getString(R.string.dialog_button_go_home_screen))
            {_, _ -> dialogListener.onDialogBackToHomeScreen() }
            .setCancelable(false)
            .show()
    }

    fun mostrarDialogoVictoriaLeaderboard(puntuacion: Puntuacion) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title_victory))
            .setMessage(context.getString(R.string.dialog_message_victory_ranking))
            .setPositiveButton(context.getString(R.string.dialog_button_play_again))
            { _, _ -> dialogListener.onDialogRestartGame() }
            .setNegativeButton(context.getString(R.string.dialog_button_view_ranking))
            { _, _ -> dialogListener.onDialogShowLeaderboard() }
            .setNeutralButton(context.getString(R.string.dialog_button_share))
            { _, _ -> dialogListener.onDialogShareScore(puntuacion)}
            .setCancelable(false)
            .show()
    }

    fun mostrarDialogoDerrota(){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title_defeat))
            .setMessage(context.getString(R.string.dialog_message_defeat))
            .setPositiveButton(context.getString(R.string.dialog_button_try_again))
            { _, _ -> dialogListener.onDialogRestartGame()}
            .setNegativeButton(context.getString(R.string.dialog_button_go_home_defeat))
            { _, _ -> dialogListener.onDialogBackToHomeScreen()}
            .setCancelable(false)
            .show()
    }
}