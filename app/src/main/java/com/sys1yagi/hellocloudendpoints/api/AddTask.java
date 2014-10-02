package com.sys1yagi.hellocloudendpoints.api;

import com.sys1yagi.hellocloudendpoints.api.todoApi.model.Todo;

import android.os.AsyncTask;

import java.io.IOException;

public class AddTask extends AsyncTask<Todo, Void, Todo> {

    @Override
    protected Todo doInBackground(Todo... params) {
        Todo todo = params[0];
        try {
            return ApiClient.getInstance().add(todo).execute();
        } catch (IOException e) {
            return null;
        }
    }
}
