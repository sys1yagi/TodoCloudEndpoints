package com.sys1yagi.hellocloudendpoints.api;

import com.sys1yagi.hellocloudendpoints.api.todoApi.model.TodoCollection;

import android.os.AsyncTask;

import java.io.IOException;

public class ListTask extends AsyncTask<Void, Void, TodoCollection> {

    @Override
    protected TodoCollection doInBackground(Void... params) {
        try {
            return Facade.getInstance().list().execute();
        } catch (IOException e) {
            return null;
        }
    }
}
