package com.brightfuture.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.DialogAboutAppBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val dialog = BottomSheetDialog(requireContext()).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundResource(android.R.color.transparent)
            }
        }
        when (tag) {
            "about_app" -> {
                setupAboutApp(dialog)
            }
        }
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

      ///  statusAndNavigationBars(dialog)
        dialog.show()
        return dialog
    }

    private fun setupAboutApp(dialog: BottomSheetDialog) {
        val dialogView = DialogAboutAppBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(dialogView.root)
    }

//    private fun statusAndNavigationBars(dialog: BottomSheetDialog) {
//        val window: Window = dialog.window!!
//        window.navigationBarColor =
//            ContextCompat.getColor(requireContext(), R.color.bg_permissions_dialog)
//        val decorView: View = window.decorView
//        val wic = WindowInsetsControllerCompat(window, decorView)
//        wic.isAppearanceLightStatusBars = false
//        wic.isAppearanceLightNavigationBars = true
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//    }

}