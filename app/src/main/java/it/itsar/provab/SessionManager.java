package it.itsar.provab;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Utente user){
        int id = user.getId();
        editor.putInt(SESSION_KEY,id).commit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("utente", json).commit();

        editor.putString("username", user.getUsername()).commit();
        editor.putString("documentId", user.getDocumentId()).commit();

        editor.putString("password", user.getPassword()).commit();



    }



    public int getSession(){
        //return user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).commit();
    }
}