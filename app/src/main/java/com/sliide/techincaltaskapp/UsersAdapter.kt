package com.sliide.techincaltaskapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sliide.techincaltaskapp.data.User
import com.sliide.techincaltaskapp.databinding.UserItemBinding
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter(var userViewModel: UsersVM) : RecyclerView.Adapter<UserVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        return UserVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_item, parent,false))
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = userViewModel.users[position]
        holder.bind(user)
        with(holder.itemView.cvUser){
            isChecked = userViewModel.selectedUsers.contains(user)
            setOnClickListener {
                isChecked = !isChecked
                userViewModel.setSelected(user, isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
        return userViewModel.users.size
    }
}

class UserVH(val itemBinding: UserItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(user: User) {
        itemBinding.user = user
        itemBinding.executePendingBindings()
    }
}