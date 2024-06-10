package com.brightfuture.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brightfuture.dictionary.databinding.DialogAboutAppBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        when (tag) {
            "about_app" -> {
                setupAboutApp(dialog)
            }
        }
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.show()
        return dialog
    }

    private fun setupAboutApp(dialog: Dialog) {
        val dialogView = DialogAboutAppBinding.inflate(layoutInflater, null, false)
        dialog.setCancelable(true)
        dialog.setContentView(dialogView.root)
    }
}