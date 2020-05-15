package com.example.countershockkotlin

import java.io.Serializable

class ImageModel(val id: Int, val imgFilename: String, val isAsset: Boolean) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ImageModel
        if (id != other.id) return false
        if (imgFilename != other.imgFilename) return false
        if (isAsset != other.isAsset) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + imgFilename.hashCode()
        result = 31 * result + isAsset.hashCode()
        return result
    }

}