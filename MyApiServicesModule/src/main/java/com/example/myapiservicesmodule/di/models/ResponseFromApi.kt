package com.example.myapiservicesmodule.di.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class ResponseFromApi (
    @SerializedName("path")
    @Expose
    var image_path: String
)