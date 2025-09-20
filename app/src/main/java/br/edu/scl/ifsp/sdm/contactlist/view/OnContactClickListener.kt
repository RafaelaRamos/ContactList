package br.edu.scl.ifsp.sdm.contactlist.view

import android.icu.text.Transliterator.Position

sealed interface OnContactClickListener {
    fun onContactClick(position: Int)
    fun onRemoveContactMenuItemClick(position: Int)
    fun onEditContactMenuEditClick(position: Int)
}