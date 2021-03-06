package com.sys1yagi.hellocloudendpoints.api;

import com.sys1yagi.hellocloudendpoints.api.todoApi.model.Todo;

import android.os.AsyncTask;

import java.io.IOException;

public class DeleteTask extends AsyncTask<Todo, Void, Void> {

    @Override
    protected Void doInBackground(Todo... params) {
        for (Todo todo : params) {
            try {
                ApiClient.getInstance().delete(todo.getId()).execute();
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }
        }
        return null;
    }
}
