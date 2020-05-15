package com.example.countershockkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.dialog_media_picker.*

class ImagePickerDialogFragment : DialogFragment(), ImagePickerAdapter.Callback {
    private lateinit var adapter: ImagePickerAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_media_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = ImageStorer(context!!).getAllImages()
        adapter = ImagePickerAdapter(items, this)
        recyclerView.adapter = adapter
        gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = gridLayoutManager
    }

    override fun itemSelected(item: ImageModel) {
        val preferences =
            context!!.getSharedPreferences(ShockUtils.SHOCK_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(getString(R.string.key_photo_id), item.id)
        editor.apply()
        dismiss()
        LocalBroadcastManager.getInstance(context!!)
            .sendBroadcast(Intent(ShockUtils.MEDIA_UPDATED_ACTION))
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

}