package com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.main.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nytimesmostpopulararticles_mvvm_kotlin.data.model.api.ArticlesResponse
import com.example.nytimesmostpopulararticles_mvvm_kotlin.databinding.ItemArticleEmptyViewBinding
import com.example.nytimesmostpopulararticles_mvvm_kotlin.databinding.ItemArticleViewBinding
import com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.base.BaseViewHolder
import com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.main.article.ArticleEmptyItemViewModel.ArticleEmptyItemViewModelListener
import com.example.nytimesmostpopulararticles_mvvm_kotlin.ui.main.article.ArticleItemViewModel.ArticleItemViewModelListener

class ArticleAdapter(private val articles: MutableList<ArticlesResponse.Article>?) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var mListener: ArticleAdapterListener? = null
    override fun getItemCount(): Int {
        return if (articles != null && articles.size > 0) {
            articles.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (articles != null && articles.isNotEmpty()) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val articleViewBinding = ItemArticleViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                ArticleViewHolder(articleViewBinding)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding =
                    ItemArticleEmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                EmptyViewHolder(
                    emptyViewBinding
                )
            }
            else -> {
                val emptyViewBinding =
                    ItemArticleEmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                EmptyViewHolder(
                    emptyViewBinding
                )
            }
        }
    }

    fun addItems(articles: List<ArticlesResponse.Article?>?) {
        if (articles != null) {
            for (article in articles) {
                article?.let { this.articles!!.add(it) }
            }
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        articles!!.clear()
    }

    fun setListener(listener: ArticleAdapterListener?) {
        mListener = listener
    }

    interface ArticleAdapterListener {
        fun onRetryClick()
        fun onItemClick(article: ArticlesResponse.Article?)
    }

    inner class ArticleViewHolder(private val mBinding: ItemArticleViewBinding) :
        BaseViewHolder(mBinding.root), ArticleItemViewModelListener {
        private var marticleItemViewModel: ArticleItemViewModel? = null
        override fun onBind(position: Int) {
            val article = articles!![position]
            marticleItemViewModel = ArticleItemViewModel(article, this)
            mBinding.viewModel = marticleItemViewModel
            mBinding.executePendingBindings()
        }

        override fun onItemClick(article: ArticlesResponse.Article?) {
            if (article != null) {
                mListener!!.onItemClick(article)
            }
        }

    }

    inner class EmptyViewHolder(private val mBinding: ItemArticleEmptyViewBinding) :
        BaseViewHolder(mBinding.root), ArticleEmptyItemViewModelListener {
        override fun onBind(position: Int) {
            val emptyItemViewModel = ArticleEmptyItemViewModel(this)
            mBinding.viewModel = emptyItemViewModel
        }

        override fun onRetryClick() {
            mListener!!.onRetryClick()
        }

    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }

}