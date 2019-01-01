package sandip.example.com.github_login_repo.adapter

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import sandip.example.com.github_login_repo.R
import sandip.example.com.github_login_repo.databinding.WatcherItemBinding
import sandip.example.com.github_login_repo.objects.RepoWatching
import sandip.example.com.github_login_repo.utils.DataBoundListAdapter
import sandip.example.com.github_login_repo.utils.helperUtils.AppExecutors

class WatcherAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((RepoWatching) -> Unit)?
) : DataBoundListAdapter<RepoWatching, WatcherItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<RepoWatching>() {
        override fun areItemsTheSame(oldItem: RepoWatching, newItem: RepoWatching): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoWatching, newItem: RepoWatching): Boolean {
            return oldItem.login == newItem.login
                    && oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): WatcherItemBinding {
        val binding = DataBindingUtil
            .inflate<WatcherItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.watcher_item,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener {
            binding.item?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: WatcherItemBinding, item: RepoWatching) {
        binding.item = item
    }
}