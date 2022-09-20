package com.samsung.android.architecture.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.samsung.android.architecture.BR
import com.samsung.android.architecture.R


abstract class BasePagingAdapter<T : ItemComparable, B : ViewDataBinding>(
    private var itemBindingId: Int = BR.item,
    var onBind: B.(Int) -> Unit = {},
    private val onItemClicked: ((Int,(T)) -> Unit)? = null

) :
    PagingDataAdapter<T, BaseViewHolder<T, B>>(PagingComparator()) {


    /**
     * get item at given position
     */


    /**
     * abstract function to decide which layout should be shown at given position.
     * This will be useful for multi layout adapters. for single layout adapter it can only returns
     * a static layout resource id.
     *
     * @return relevant layout resource id based on given position
     *
     */
    abstract fun getLayoutId(position: Int): Int

    /**
     * Instead of returning viewType, this method will return layout id at given position provided
     * by [getLayoutId] and will be used in [onCreateViewHolder].
     *
     * @see [RecyclerView.Adapter.getItemViewType]
     */
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is ItemSeperator) TYPE_SEPARATOR
        else getLayoutId(position)
    }
    override fun onBindViewHolder(holder: BaseViewHolder<T, B>, position: Int) {
        getItem(position)?.let { holder.bind(itemBindingId, it as T) }
        holder.binding.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, B> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return BaseViewHolder(binding, onItemClicked)
    }


    @SuppressLint("DiffUtilEquals")
   private class PagingComparator<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.toString() == newItem.toString()


        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean  =
            oldItem == newItem
    }


    companion object {
        private const val TYPE_PAGING_MODEL = 0
        private  val TYPE_SEPARATOR = R.layout.item_paging_separator

    }

}


