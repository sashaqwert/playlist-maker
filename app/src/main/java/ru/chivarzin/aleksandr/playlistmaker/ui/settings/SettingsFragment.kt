package ru.chivarzin.aleksandr.playlistmaker.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.settings.SettingsViewModel
import kotlin.getValue

class SettingsFragment : Fragment() {
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings_dark_theme = view.findViewById<SwitchMaterial>(R.id.settings_dark_theme)
        settingsViewModel.observeIsDarkTheme().observe(viewLifecycleOwner) {
            settings_dark_theme.isChecked = it
        }
        settings_dark_theme.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.setThame(checked)
        }

        val action_share = view.findViewById<Button>(R.id.action_share)
        action_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.course_url))
            startActivity(intent)
        }

        val action_support = view.findViewById<Button>(R.id.action_support)
        action_support.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = "mailto:".toUri()
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            startActivity(shareIntent)
        }

        val action_user_agreement = view.findViewById<Button>(R.id.action_user_agreement)
        action_user_agreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(getString(R.string.user_agreement_url).toUri())
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}