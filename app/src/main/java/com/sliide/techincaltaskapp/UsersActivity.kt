package com.sliide.techincaltaskapp

import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sliide.techincaltaskapp.data.User
import com.sliide.techincaltaskapp.utils.Status
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.add_new_user.view.*

@AndroidEntryPoint
class UsersActivity : AppCompatActivity() {

    val usersVM: UsersVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        initUi()
        setUiActionListeners()

        loadUsers()
    }

    private fun initUi() {
        rvUsers.adapter = UsersAdapter(usersVM)
    }

    private fun setUiActionListeners() {
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.removeUser -> {
                    removeSelectedUsers()
                    true
                }
                else -> false
            }
        }

        swRefresh.setOnRefreshListener {
            loadUsers()
        }

        rvUsers.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                removeUser(position)
            }

            override fun onSwipedRight(position: Int) {
                removeUser(position)
            }
        })

        addUser.setOnClickListener {
            val addUserView = LayoutInflater.from(this).inflate(R.layout.add_new_user, null)
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.add_new_user))
                .setPositiveButton(
                    getString(R.string.create),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                            addUser(
                                addUserView.etName.text.toString(),
                                addUserView.etEmail.text.toString()
                            )
                            dialogInterface?.dismiss()
                        }
                    })
                .setNegativeButton(
                    getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                            dialogInterface?.dismiss()
                        }
                    })
                .setView(addUserView)
                .create()
            dialog.show()
        }
    }

    fun removeSelectedUsers() {
        usersVM.selectedUsers.forEach {
            removeUser(usersVM.users.indexOf(it))
        }
    }

    fun removeUser(position: Int) {
        usersVM.removeUser(position).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    rvUsers.adapter?.notifyItemRemoved(it.data!!)
                    swRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    swRefresh.isRefreshing = true
                }
                Status.ERROR -> {
                    showAlertDialog("Error",it.message!!)
                    swRefresh.isRefreshing = false
                }
            }
        })
    }

    private fun loadUsers() {
        usersVM.loadUsers().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    rvUsers.adapter?.notifyDataSetChanged()
                    swRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    swRefresh.isRefreshing = true
                }
                Status.ERROR -> {
                    showAlertDialog("Error",it.message!!)
                    swRefresh.isRefreshing = false
                }
            }
        })
    }

    private fun addUser(name: String, email: String) {
        usersVM.addUser(User(name = name, email = email)).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    rvUsers.adapter?.notifyItemInserted(0)
                    rvUsers.smoothScrollToPosition(0)
                    swRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    swRefresh.isRefreshing = true
                }
                Status.ERROR -> {
                    showAlertDialog("Error",it.message!!)
                    swRefresh.isRefreshing = false
                }
            }
        })
    }

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show()
    }
}