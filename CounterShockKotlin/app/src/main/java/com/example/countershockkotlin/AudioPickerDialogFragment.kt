package com.example.countershockkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_media_picker.*

class AudioPickerDialogFragment : DialogFragment(), AudioPickerAdapter.Callback {
    private lateinit var preferences: SharedPreferences
    private lateinit var adapter: AudioPickerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences =
            context!!.getSharedPreferences(ShockUtils.SHOCK_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_media_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = AudioStorer(context!!).getAllAudios()
        adapter = AudioPickerAdapter(items, this)
        recyclerView.adapter = adapter
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun itemSelected(item: AudioModel) {
        val editor = preferences.edit()
        editor.putInt(getString(R.string.key_audio_id), item.id)
        editor.apply()
        dismiss()
        LocalBroadcastManager.getInstance(context!!)
            .sendBroadcast(Intent(ShockUtils.MEDIA_UPDATED_ACTION))
    }

}