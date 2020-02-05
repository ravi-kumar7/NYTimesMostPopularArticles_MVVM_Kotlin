package com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.main.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nytimesmostpopulararticles_mvvm_kotlin.data.model.db.Article
import com.example.nytimesmostpopulararticles_mvvm_kotlin.databinding.ItemFavoritesEmptyViewBinding
import com.example.nytimesmostpopulararticles_mvvm_kotlin.databinding.ItemFavoritesViewBinding
import com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.base.BaseViewHolder
import com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.main.favorites.FavoritesItemViewModel.FavoritesItemViewModelListener

class FavoritesAdapter(private val articles: MutableList<Article>?) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var mListener: FavoritesAdapterListener? = null

    override fun getItemCount(): Int {
        return if (articles != null && articles.size > 0) articles.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (articles != null && articles.isNotEmpty()) VIEW_TYPE_NORMAL else VIEW_TYPE_EMPTY
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                FavoritesViewHolder(
                    ItemFavoritesViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
            else -> {
                EmptyViewHolder(
                    ItemFavoritesEmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
        }
    }

    fun addItems(articles: List<Article?>?) {
        articles?.forEach { article ->
            article?.let { this.articles?.add(it) }
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        articles?.clear()
    }

    fun setListener(listener: FavoritesAdapterListener?) {
        mListener = listener
    }

    interface FavoritesAdapterListener {
        fun onItemClick(article: Article?)
    }

    inner class FavoritesViewHolder(private val mBinding: ItemFavoritesViewBinding) :
        BaseViewHolder(mBinding.root), FavoritesItemViewModelListener {
        override fun onBind(position: Int) {
            val article = articles?.get(position)
            mBinding.viewModel = article?.let { FavoritesItemViewModel(it, this) }
            mBinding.executePendingBindings()
        }

        override fun onItemClick(article: Article?) {
            if (article != null) {
                mListener?.onItemClick(article)
            }
        }

    }

    inner class EmptyViewHolder(private val mBinding: ItemFavoritesEmptyViewBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            mBinding.viewModel = FavoritesEmptyItemViewModel()
        }
    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }

}