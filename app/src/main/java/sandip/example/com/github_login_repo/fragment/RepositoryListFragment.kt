package sandip.example.com.github_login_repo.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import sandip.example.com.github_login_repo.MainActivity

import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.adapter.RepositoryAdapter
import sandip.example.com.github_login_repo.binding.FragmentDataBindingComponent
import sandip.example.com.github_login_repo.database.PreferencesHelper
import sandip.example.com.github_login_repo.databinding.FragmentRepositoryListBinding
import sandip.example.com.github_login_repo.di.Injectable
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.helperUtils.autoCleared
import sandip.example.com.github_login_repo.utils.remoteUtils.Status
import sandip.example.com.github_login_repo.viewModel.LoginViewModel
import sandip.example.com.github_login_repo.viewModel.RepositoryListViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class RepositoryListFragment : Fragment(), Injectable {

    var binding by autoCleared<FragmentRepositoryListBinding>()
    private lateinit var viewModel: RepositoryListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    lateinit var executors: AppExecutors

    var adapter by autoCleared<RepositoryAdapter>()

    private val OWNER_NAME = "owner_name"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OWNER_NAME, owner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_repository_list,
            container,
            false
        )

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.title = getString(R.string.repo_list)


        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RepositoryListViewModel::class.java)

        owner = savedInstanceState?.getString(OWNER_NAME) ?: RepositoryListFragmentArgs.fromBundle(arguments).owner

        viewModel.init(owner = owner)

        adapter = RepositoryAdapter(dataBindingComponent = dataBindingComponent, appExecutors = executors) { item->

            Log.e("item: ", "Value: ${Gson().toJson(item)}")
            val watcherAction = RepositoryListFragmentDirections.repoWatcherFragment()
            watcherAction.setOwner(item.owner.login)
            watcherAction.setRepo(item.name)
            navController().navigate(watcherAction)

        }

        binding.let {
            it.setLifecycleOwner(this)
            it.adapter = adapter
        }


        initRepoList(viewModel)

    }

    private fun initRepoList(viewModel: RepositoryListViewModel) {
        viewModel.result.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            Log.e("Observer", "Data : ${Gson().toJson(listResource)}")
            binding.resource = listResource
            binding.count = listResource?.data?.size
            endProgress()
            when (listResource?.status) {
                Status.SUCCESS -> {
                    viewModel.apiCall = false
                    adapter.submitList(listResource.data)
                    endProgress()
                }

                Status.ERROR -> {
                    endProgress()
                    Toast.makeText(requireContext(), getString(R.string.generalError), Toast.LENGTH_LONG).show()
                }

                Status.LOADING ->{startProgress()}
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {

                PreferencesHelper(requireContext()).authToken = ""
                requireActivity().finish()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)

            }
        }
        return true
    }


    companion object {
        var owner: String = "-1"
    }

}
