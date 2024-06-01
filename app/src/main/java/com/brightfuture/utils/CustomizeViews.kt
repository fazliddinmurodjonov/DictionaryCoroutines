package com.brightfuture.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import com.brightfuture.fragments.BottomSheetDialogFragment
import com.brightfuture.fragments.DialogFragment


object CustomizeViews {
    fun appearanceStatusNavigationBars(window: Window, mode: Int) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
        if (mode == 0) {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = false
            wic.isAppearanceLightNavigationBars = true
        } else {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = false
            wic.isAppearanceLightNavigationBars = false
        }
    }
    fun statusNavigationBarsColor(
        window: Window,
        context: Context,
        colorStatus: Int,
        colorNavigation: Int
    ) {
        window.statusBarColor = ContextCompat.getColor(context, colorStatus)
        window.navigationBarColor = ContextCompat.getColor(context, colorNavigation)
    }
    fun shareApp(context: Context) {
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "Dictionary"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }

    fun showDialog(fragmentManager: FragmentManager, tag: String) {
        val dialog = DialogFragment()
        dialog.show(fragmentManager, tag)
    }
    fun showBottomSheetDialog(fragmentManager: FragmentManager, tag: String) {
        val dialog = BottomSheetDialogFragment()
        dialog.show(fragmentManager, tag)
    }
    fun pixelToDp(px: Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return Math.round(px / (displayMetrics.densityDpi / 160f))
    }


}