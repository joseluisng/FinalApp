package com.techng.joseluisng.finalapp.dialogues

import android.support.v7.app.AlertDialog
import android.support.v4.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.techng.joseluisng.finalapp.R
import com.techng.joseluisng.finalapp.models.NewRateEvent
import com.techng.joseluisng.finalapp.models.Rate
import com.techng.joseluisng.finalapp.toast
import com.techng.joseluisng.finalapp.utils.RxBus
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)

        return AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.dialog_title))
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                    val textRate = view.editTextRateFeedback.text.toString()
                    if(textRate.isNotEmpty()){
                        val imgUrl = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run { "" }
                        val rate = Rate(textRate, view.ratingBarFeedback.rating, Date(), imgUrl)
                        RxBus.publish(NewRateEvent(rate))
                    }else{
                        activity!!.toast("Por favor llena el campo de texto")
                    }
                }
                .setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
                    activity!!.toast("Cancel")
                }
                .create()

    }

}