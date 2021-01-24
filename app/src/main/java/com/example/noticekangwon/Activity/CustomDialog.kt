package com.example.noticekangwon.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.noticekangwon.R
import com.example.noticekangwon.R.style.AnimationPopupStyle
import com.example.noticekangwon.defaultClass.ThemeSet
import kotlinx.android.synthetic.main.dialog_custom.view.*

class CustomDialog : DialogFragment() {

    var title: String? = "테마"
    var con: Context? = null

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_custom, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val lpwindow = WindowManager.LayoutParams()
        lpwindow.windowAnimations = R.style.AnimationPopupStyle
        dialog?.window?.attributes = lpwindow
        return view.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.apply {
            findViewById<TextView>(R.id.text_title)?.text = title

            var shared: SharedPreferences? = con?.getSharedPreferences("themeSetSP", 0)
            var edit = shared?.edit()
            var mode = shared?.getString("themeMode", ThemeSet.LIGHT_MODE)

            when (mode) {
                ThemeSet.LIGHT_MODE ->  {
                    radioBtn1.isChecked = true}
                ThemeSet.DARK_MODE -> {
                    radioBtn2.isChecked = true}
                ThemeSet.DEFAULT_MODE -> {
                    radioBtn3.isChecked = true}
            }

            rdG1.setOnCheckedChangeListener { _, checkId ->
                when (checkId) {
                    R.id.radioBtn1 -> {
                        edit?.clear()
                        edit?.putString("themeMode", ThemeSet.LIGHT_MODE)
                        ThemeSet.applyTheme(ThemeSet.LIGHT_MODE)
                        radioBtn1.highlightColor = resources.getColor(R.color.rdBtnColor)
                        radioBtn3.highlightColor = resources.getColor(R.color.rdBtnColor)
                        edit?.commit()
                    }
                    R.id.radioBtn2 -> {
                        edit?.clear()
                        edit?.putString("themeMode", ThemeSet.DARK_MODE)
                        ThemeSet.applyTheme(ThemeSet.DARK_MODE)
                        edit?.commit()
                    }
                    R.id.radioBtn3 -> {
                        edit?.clear()
                        edit?.putString("themeMode", ThemeSet.DEFAULT_MODE)
                        ThemeSet.applyTheme(ThemeSet.DEFAULT_MODE)
                        radioBtn1.highlightColor = resources.getColor(R.color.rdBtnColor)
                        radioBtn3.highlightColor = resources.getColor(R.color.rdBtnColor)
                        edit?.commit()
                    }
                }
            }
        }
    }


    class CustomDialogBuilder {

        private val dialog = CustomDialog()

        fun setTitle(title: String, context: Context): CustomDialogBuilder {
            dialog.title = title
            dialog.con = context
            return this
        }

        fun create(): CustomDialog {
            return dialog
        }
    }
}
