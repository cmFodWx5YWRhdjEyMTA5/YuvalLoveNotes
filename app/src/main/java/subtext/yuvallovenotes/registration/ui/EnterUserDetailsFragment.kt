package subtext.yuvallovenotes.registration.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import org.koin.java.KoinJavaComponent.get
import subtext.yuvallovenotes.R
import subtext.yuvallovenotes.crossapplication.viewmodel.LoveItemsViewModel
import subtext.yuvallovenotes.databinding.FragmentEnterUserDetailsBinding

class EnterUserDetailsFragment : Fragment() {

    lateinit var binding : FragmentEnterUserDetailsBinding
    lateinit var sharedPrefs : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        binding = FragmentEnterUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userNameInputEditText.requestFocus()
        setupUserNameEditText()
        setOnDoneButtonClickListener()
    }

    private fun setOnDoneButtonClickListener() {
        binding.userNameContinueBtn.setOnClickListener {
            if (binding.userNameInputEditText.text.isNotBlank()) {
                sharedPrefs.edit().putString(resources.getString(R.string.pref_key_user_name), binding.userNameInputEditText.text.toString()).apply()
                findNavController().navigate(EnterUserDetailsFragmentDirections.navigateToLoverName())
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.error_no_user_name_inserted), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupUserNameEditText() {
        binding.userNameInputEditText.requestFocus()
        val userName =  sharedPrefs.getString(resources.getString(R.string.pref_key_user_name), "")
        binding.userNameInputEditText.setText(userName)
    }
}