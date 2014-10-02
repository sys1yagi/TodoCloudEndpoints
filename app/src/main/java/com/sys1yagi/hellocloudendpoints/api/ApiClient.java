package com.sys1yagi.hellocloudendpoints.api;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.sys1yagi.hellocloudendpoints.api.todoApi.TodoApi;

public class ApiClient {

    private static final TodoApi INSTANCE = getApiClient();

    public static TodoApi getInstance() {
        return INSTANCE;
    }

    private static TodoApi getApiClient() {
        return new TodoApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                //for genymotion.
                //if you use android emulator, you should replace to "10.0.2.2".
                //.setRootUrl("http://10.0.3.2:8080/_ah/api/")
                .build();
    }

}
