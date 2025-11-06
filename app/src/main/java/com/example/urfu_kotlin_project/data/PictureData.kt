package com.example.urfu_kotlin_project.data

import com.example.urfu_kotlin_project.models.Picture


fun generateSamplePictures(): List<Picture> {
    return listOf(
        Picture(1, "Vova", "https://avatar.iran.liara.run/public/"),
        Picture(2, "Karen", "https://avatar.iran.liara.run/public/"),
        Picture(3, "Katy", "https://avatar.iran.liara.run/public/"),
        Picture(4, "Mike", "https://avatar.iran.liara.run/public/"),
        Picture(5, "Oleg", "https://avatar.iran.liara.run/public/")
    )
}
