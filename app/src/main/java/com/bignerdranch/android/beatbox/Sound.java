package com.bignerdranch.android.beatbox;

/**
 * Created by hotun on 21.07.2017.
 */
//фаил для хранения имени фалов, которые видит пользователь
public class Sound {

    private String mAssetPath;
    private String mName;

    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");//разбиваем путь на части
        String filename = components[components.length - 1];//отделяем имя файла
        mName = filename.replace(".wav", "");//удаляем расширение файла
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }
}
