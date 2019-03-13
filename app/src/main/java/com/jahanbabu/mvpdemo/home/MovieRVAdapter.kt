package com.jahanbabu.mvpdemo.home

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jahanbabu.mvpdemo.data.Movie
import com.jahanbabu.mvpdemo.R
import kotlinx.android.synthetic.main.row_item.view.*

class MovieRVAdapter(private val mContext: Context, private val list: MutableList<Movie>): RecyclerView.Adapter<MovieRVAdapter.ViewHolder>() {
    internal lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = list[position]

        holder.titleTextView!!.text = holder.mItem.title
        holder.descriptionTextView!!.text = holder.mItem.description

        Log.e("thumb", holder.mItem.thumb)
        val mDefaultBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.resources.getDrawable(R.drawable.ic_image_placeholder, null)
        } else {
            mContext.resources.getDrawable(R.drawable.ic_image_placeholder)
        }

        Glide.with(mContext).load(holder.mItem.thumb)
            .placeholder(R.drawable.ic_image_placeholder).dontAnimate()
            .error(mDefaultBackground)
            .into(holder.thumbImageView)

        holder.mView.setOnClickListener {
            itemClickListener.onItemClicked(position, holder.mItem.id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        internal var titleTextView = mView.titleTextView
        internal var descriptionTextView = mView.descriptionTextView
        internal var thumbImageView = mView.thumbImageView

        lateinit var mItem: Movie
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.itemClickListener = clickListener
    }

    interface ItemClickListener {
        fun onItemClicked(position: Int, id: String)
    }
}
