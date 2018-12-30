package sandip.example.com.github_login_repo.adapter

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.databinding.RepoItemBinding
import sandip.example.com.github_login_repo.objects.Repository
import sandip.example.com.github_login_repo.utils.DataBoundListAdapter
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors

class RepositoryAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Repository) -> Unit)?
) : DataBoundListAdapter<Repository, RepoItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.owner.login == newItem.owner.login
                    && oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): RepoItemBinding {
        val binding = DataBindingUtil
            .inflate<RepoItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.repo_item,
                parent,
                false,
                dataBindingComponent
            )
        binding.showWatcher.setOnClickListener {
            binding.repo?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: RepoItemBinding, item: Repository) {
        binding.repo = item
    }
}