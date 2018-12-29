package sandip.example.com.github_login_repo.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import sandip.example.com.github_login_repo.MainActivity
import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.database.PreferencesHelper
import sandip.example.com.github_login_repo.databinding.LoginFragmentBinding
import sandip.example.com.github_login_repo.di.Injectable
import sandip.example.com.github_login_repo.utils.helperUtils.autoCleared
import sandip.example.com.github_login_repo.utils.remoteUtils.Status
import sandip.example.com.github_login_repo.viewModel.LoginViewModel
import java.io.UnsupportedEncodingException
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    private lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<LoginFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.title = getString(R.string.title_activity_login)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        binding.authenticate.setOnClickListener {

            if (binding.username.text.isNullOrEmpty()) {
                binding.username.error = getString(R.string.error_field_required)
            } else if (binding.password.text.isNullOrEmpty()) {
                binding.password.error = getString(R.string.error_field_required)
            } else {
                val authorization =
                    "Basic " + getBase64String("${binding.username.text.toString()}:${binding.password.text.toString()}")
                PreferencesHelper(requireContext()).authToken = authorization
                viewModel.login(
                    userName = binding.username.text.toString(),
                    password = binding.password.text.toString()
                )

            }

        }

        initEntryList(viewModel)

    }

    private fun initEntryList(viewModel: LoginViewModel) {
        viewModel.result.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            Log.e("Observer", "Data : ${Gson().toJson(listResource)}")
            binding.resource = listResource
            endProgress()
            when (listResource?.status) {
                Status.SUCCESS -> {
                    endProgress()
                    if (listResource.data?.login != null) {
                        val repolistAction = LoginFragmentDirections.repoListFragment()
                        repolistAction.setOwner(listResource.data.login)
                        navController().navigate(repolistAction)

                    }

                }

                Status.ERROR -> {
                    endProgress()
                    Toast.makeText(requireContext(), getString(R.string.unauthorised), Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {
                    startProgress()
                }
            }
        })
    }

    @Throws(UnsupportedEncodingException::class)
    fun getBase64String(value: String): String {
        return Base64.encodeToString(value.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
    }

    private fun startProgress() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun endProgress() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun navController() = findNavController()

}
