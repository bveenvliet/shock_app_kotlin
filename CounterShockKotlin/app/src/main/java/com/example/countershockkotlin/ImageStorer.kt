package com.example.countershockkotlin

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageStorer(var context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(ShockUtils.SHOCK_SHARED_PREFS, Context.MODE_PRIVATE)

    private fun storeImages(images: List<ImageModel>) {
        val key = context.getString(R.string.key_stored_images)
        val editor = preferences.edit()
        editor.putString(key, Gson().toJson(images))
        editor.apply()
    }

    fun addImage(image: ImageModel) {
        val images = getStoredImages() as ArrayList<ImageModel>
        images.add(image)
        storeImages(images)
    }

    private fun getStoredImages(): List<ImageModel> {
        val imagesAsJson =
            preferences.getString(context.getString(R.string.key_stored_images), null)
        if (imagesAsJson.isNullOrEmpty()) {
            return ArrayList()
        }
        val type = object : TypeToken<List<ImageModel>>() {}.type
        return Gson().fromJson(imagesAsJson, type)
    }

    fun getAllImages(): List<ImageModel> {
        val assetImages = ArrayList<ImageModel>()
        assetImages.add(ImageModel(0, "bust_2", true))
        assetImages.add(ImageModel(1, "lama", true))
        assetImages.add(ImageModel(2, "man_1", true))
        assetImages.addAll(getStoredImages())
        return assetImages
    }

    fun getSelectedImage(): ImageModel {
        val images = getAllImages()
        val defaultImage = images[0]
        val imageId = preferences.getInt(context.getString(R.string.key_photo_id), 0)
        for (image in images) {
            if (image.id == imageId) {
                return image
            }
        }
        // Fall back on defaults
        val editor = preferences.edit()
        editor.putInt(context.getString(R.string.key_photo_id), 0)
        editor.apply()
        return defaultImage
    }

}