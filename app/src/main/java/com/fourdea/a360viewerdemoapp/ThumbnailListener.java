package com.fourdea.a360viewerdemoapp;

/**
 * Created by dhrumil on 8/11/2016.
 */
public interface ThumbnailListener {

    void onThumbnailClicked(int sceneNum, String sceneName) throws Exception;

    String getThumbUrl(int sceneNum) throws Exception;
}
