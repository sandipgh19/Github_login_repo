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
import com.google.gson.Gson
import sandip.example.com.github_login_repo.MainActivity
import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.adapter.WatcherAdapter
import sandip.example.com.github_login_repo.binding.FragmentDataBindingComponent
import sandip.example.com.github_login_repo.database.PreferencesHelper
import sandip.example.com.github_login_repo.databinding.FragmentRepoWatcherBinding
import sandip.example.com.github_login_repo.di.Injectable
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors
import sandip.example.com.github_login_repo.utils.helperUtils.autoCleared
import sandip.example.com.github_login_repo.utils.remoteUtils.Status
import sandip.example.com.github_login_repo.viewModel.RepoWatcherViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class RepoWatcherFragment : Fragment(), Injectable {

    var binding by autoCleared<FragmentRepoWatcherBinding>()

    private lateinit var viewModel: RepoWatcherViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    lateinit var executors: AppExecutors

    var adapter by autoCleared<WatcherAdapter>()

    private val OWNER_NAME = "owner_name"
    private val REPO_NAME = "repo_name"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OWNER_NAME, owner)
        outState.putString(REPO_NAME, repo)
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
            R.layout.fragment_repo_watcher,
            container,
            false
        )

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.title = getString(R.string.watcher)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RepoWatcherViewModel::class.java)

        owner = savedInstanceState?.getString(OWNER_NAME) ?: RepoWatcherFragmentArgs.fromBundle(arguments).owner
        repo = savedInstanceState?.getString(REPO_NAME) ?: RepoWatcherFragmentArgs.fromBundle(arguments).repo

        viewModel.init(owner = owner, repo = repo)

        adapter = WatcherAdapter(dataBindingComponent = dataBindingComponent, appExecutors = executors) { item->

        }

        binding.let {
            it.setLifecycleOwner(this)
            it.adapter = adapter
            it.count = 1
        }


        initRepoList(viewModel)

    }

    private fun initRepoList(viewModel: RepoWatcherViewModel) {
        viewModel.result.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            Log.e("Observer", "Data : ${Gson().toJson(listResource)}")
            binding.resource = listResource
            binding.count = listResource?.data?.size
            endProgress()
            when (listResource?.status) {
                Status.SUCCESS -> {
                    endProgress()
                    adapter.submitList(listResource.data)
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
        var repo: String = "-1"
    }


}
