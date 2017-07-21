package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotun on 21.07.2017.
 */
//класс для досту к активом
public class BeatBox {
    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;//определяет, сколько звуков может воспроизводиться в любой момент времени

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    //Класс SoundPool позволяет загрузить в память большой набор звуков и
    // управлять максимальным количеством звуков, воспроизводимых одновременно.
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        // Этот конструктор считается устаревшим,
        // но он нужен для обеспечения совместимости.
        //Второй параметр определяет тип аудиопотока, который может воспроизводиться объектом SoundPool,
        // последний параметр задает качество дискретизации. В документации сказано, что этот параметр игнорируется, поэтому в нем можно передать 0.
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            //list(SOUNDS_FOLDER) - используется для получения списка доступных активов в заданной папке
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

        }
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if(soundId == null) {
            return;
        }
        //Параметры содержат соответственно: идентификатор звука, громкость слева, громкость справа,
        // приоритет (игнорируется), признак циклического воспроизведения и скорость воспроизведения.
        //Для полной громкости и нормальной скорости воспроизведения передайте 1.0. Передача 0 в признаке
        // циклического воспроизведения означает «без зацикливания». (Передайте –1, если хотите, чтобы воспроизведение длилось
        // бесконечно долго. Мы считаем, что это только раздражает.)
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    //метод нужен для освобождения ресурсов SoundPool
    public void release() {
        mSoundPool.release();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);//загружает файл в SoundPool для последующего воспроизведения
        sound.setSoundId(soundId);
    }
}
