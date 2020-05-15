package com.example.countershockkotlin

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AudioStorer(var context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(ShockUtils.SHOCK_SHARED_PREFS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun addAudio(audio: AudioModel) {
        val audios = getStoredAudios() as ArrayList<AudioModel>
        audios.add(audio)
        storeAudios(audios)
    }

    private fun storeAudios(audios: List<AudioModel>) {
        editor.putString(context.getString(R.string.key_stored_audios), Gson().toJson(audios))
        editor.commit()
    }

    private fun getStoredAudios(): List<AudioModel> {
        val audiosAsString =
            preferences.getString(context.getString(R.string.key_stored_audios), null)
        if (audiosAsString.isNullOrEmpty()) {
            return ArrayList()
        }
        val type = object : TypeToken<List<AudioModel>>() {}.type
        return Gson().fromJson(audiosAsString, type)
    }

    fun getAllAudios(): List<AudioModel> {
        val assetAudios = ArrayList<AudioModel>()
        assetAudios.add(AudioModel(0, "scream2", "Scream 2", true))
        assetAudios.add(AudioModel(1, "see_you", "Seeing You", true))
        assetAudios.add(AudioModel(2, "behind_you", "Behind you now", true))
        assetAudios.addAll(getStoredAudios())
        return assetAudios
    }

    fun getSelectedAudio(): AudioModel {
        val audios = getAllAudios()
        val defaultAudio = audios[0]
        val audioId = preferences.getInt(context.getString(R.string.key_audio_id), 0)
        for (audio in audios) {
            if (audio.id == audioId) {
                return audio
            }
        }

        // fall back
        editor.putInt(context.getString(R.string.key_audio_id), 0)
        editor.commit()

        return defaultAudio
    }


}