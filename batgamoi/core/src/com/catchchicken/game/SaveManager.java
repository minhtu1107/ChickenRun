package com.catchchicken.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

class SaveData
{
    public int m_isFirstLaunch;
    public int m_level;
    public int m_isSoundOn;
    public int m_isMusicOn;
}
public class SaveManager {

    static SaveManager instance;
    SaveData data;
    SaveManager()
    {
        data = new SaveData();
        data.m_isFirstLaunch = 1;
        data.m_level = 3;
        data.m_isSoundOn = 2;
        data.m_isMusicOn = 2;
    }

    public static SaveManager getInstance()
    {
        if(instance==null)
        {
            instance = new SaveManager();
        }
        return instance;
    }

    public void SaveGame()
    {
        FileHandle file = Gdx.files.local("data/savefile/data.sav");
        try
        {
            Json sav = new Json();
            String str = sav.prettyPrint(data);
//            String str = file.readString();

            file.writeString(str, false);
        }
        catch (Exception ex)
        {

        }
    }

    public void LoadGame()
    {
        FileHandle file = Gdx.files.local("data/savefile/data.sav");
        try
        {
            JsonValue root;
            String str = file.readString();
            root = new JsonReader().parse(str);
            data.m_isFirstLaunch = root.get("m_isFirstLaunch").asInt();
            data.m_level = root.get("m_level").asInt();
            data.m_isSoundOn = root.get("m_isSoundOn").asInt();
            data.m_isMusicOn = root.get("m_isMusicOn").asInt();
        }
        catch (Exception ex)
        {
            data.m_isFirstLaunch = 1;
            data.m_level = 3;
            data.m_isSoundOn = 1;
            data.m_isMusicOn = 1;

            Json sav = new Json();
            String str = sav.prettyPrint(data);
            file.writeString(str, false);
        }
    }

    public boolean isMusicOn()
    {
        return data.m_isMusicOn==1;
    }

    public boolean isSoundOn()
    {
        return data.m_isSoundOn==1;
    }
}
