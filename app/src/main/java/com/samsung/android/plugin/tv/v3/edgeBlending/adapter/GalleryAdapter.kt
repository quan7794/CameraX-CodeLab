package com.samsung.android.plugin.tv.v3.edgeBlending.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.samsung.android.architecture.base.adapter.BaseViewHolder
import com.samsung.android.architecture.base.adapter.SingleLayoutAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ItemGridPhotoBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.model.PhotoModel

class GalleryAdapter(private val maxSelectedPhoto: Int = 4,
                     val isShowTextNumber:Boolean = false,
                     private val onItemClick: (Int, PhotoModel.Data) -> Unit) :
    SingleLayoutAdapter<PhotoModel.Data, ItemGridPhotoBinding>(R.layout.item_grid_photo, emptyList(), onItemClick) {

    private var allPhotos: List<PhotoModel.Data> = ArrayList()
    lateinit var ctx: Context

    fun setData(photos: List<PhotoModel.Data>, filterPhotos: List<PhotoModel.Data>? = null) {
        allPhotos = photos

        if (filterPhotos == null) {
            items.clear()
            items.addAll(photos)
        } else {
            items.clear()
            items.addAll(filterPhotos)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PhotoModel.Data, ItemGridPhotoBinding> {
        ctx = parent.context
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<PhotoModel.Data, ItemGridPhotoBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (maxSelectedPhoto != 0) {
            if (getSelectedCount() >= maxSelectedPhoto) items.filterNot { it.isSelected }
                .forEach { it.isEnabled = false }
            else items.forEach { it.isEnabled = true }
        }
        with(holder.binding) {

            if (items[position].isEnabled) {
                root.alpha = 1.0f
                root.isEnabled = true
            } else {
                root.alpha = 0.3f
                root.isEnabled = false
            }

            root.setOnClickListener {
                selectedPhoto(position, this)
                onItemClick.invoke(position, items[position])
            }

            Glide.with(ctx).load(items[position].uri).centerCrop()
                .priority(Priority.IMMEDIATE).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.binding.photo)

            updateUI(position, this)
        }

    }

    private fun getSelectedCount(): Int = allPhotos?.count { it.isSelected }

    private fun updateUI(position: Int, binding: ItemGridPhotoBinding) {
        val photo = items[position]
        if (photo.isSelected) {
            binding.selectedLine.visibility = View.VISIBLE
            binding.selectedLine.setImageResource(R.drawable.outline_photo_select_normal)
            if (photo.numSelected == 0) {
                binding.selectNum.visibility = View.GONE
            } else {
                binding.selectNum.visibility = View.VISIBLE
                binding.selectNum.text = "${photo.numSelected}"
            }
            if (!isShowTextNumber){
                binding.checkbox.setImageDrawable(ctx.getDrawable(R.drawable.radio_btn_num_chk))
            }

        } else {
            photo.numSelected = 0
            binding.selectedLine.visibility = View.GONE
            binding.checkbox.setImageDrawable(ctx.getDrawable(R.drawable.radio_btn_num_nor))
            binding.selectNum.visibility = View.GONE

        }
    }

    private fun selectedPhoto(position: Int, binding: ItemGridPhotoBinding) {
        if (maxSelectedPhoto != 0) {
            when {
                getSelectedCount() <= maxSelectedPhoto -> {
                    if (items[position].isSelected) {
                        items[position].isSelected = false
                        updateUI(position, binding)
                        if (getSelectedCount() == (maxSelectedPhoto - 1) && !items[position].isSelected) {
                            items.forEach { it.isEnabled = true }
                            for ((index, item) in items.withIndex()) {
                                if (item.isEnabled && !item.isSelected) {
                                    notifyItemChanged(index)
                                }
                            }
                        }
                        if (isShowTextNumber && getSelectedCount() > 0) {
                            val selectedPhotos = allPhotos.filter { it.isSelected }
                            selectedPhotos.forEach {
                                val index = items.indexOf(it)
                                it.numSelected = selectedPhotos.indexOf(it).plus(1)
                                notifyItemChanged(index)
                            }
                        }
                    } else {
                        items[position].isSelected = true

                        if (isShowTextNumber) items[position].numSelected = getSelectedCount()

                        updateUI(position, binding)
                        if (getSelectedCount() == maxSelectedPhoto && items[position].isSelected) {
                            items.filterNot { it.isSelected }.forEach { it.isEnabled = false }
                            for ((index, item) in items.withIndex()) {
                                if (!item.isEnabled) notifyItemChanged(index)
                            }
                        }
                    }
                }
                getSelectedCount() > maxSelectedPhoto -> {
                    for (image in items) {
                        items.filter { it.isSelected && !it.isEnabled }
                            .forEach { it.isSelected = false }
                    }
                }
                else -> {
                }
            }
        } else {
            if (items[position].isSelected) {
                items[position].isSelected = false
                updateUI(position, binding)
                notifyItemChanged(position)
            } else {

                items[position].isSelected = true
                updateUI(position, binding)
                notifyItemChanged(position)
            }
        }
    }

}