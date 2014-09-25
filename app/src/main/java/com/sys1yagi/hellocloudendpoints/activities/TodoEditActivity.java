package com.sys1yagi.hellocloudendpoints.activities;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.sys1yagi.hellocloudendpoints.R;
import com.sys1yagi.hellocloudendpoints.api.UpdateTask;
import com.sys1yagi.hellocloudendpoints.api.todoApi.model.Todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class TodoEditActivity extends ActionBarActivity {

    public static final String ARGS_TODO = "todo_pretty_string";

    public static Intent createIntent(Context context, Todo todo) throws IOException {
        Intent intent = new Intent(context, TodoEditActivity.class);

        intent.putExtra(ARGS_TODO, todo.toPrettyString());

        return intent;
    }

    Todo todo;

    EditText todoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        todoEdit = (EditText) findViewById(R.id.todo_edit);
        setupUi();
    }

    private void setupUi() {
        try {
            todo = new Todo();
            AndroidJsonFactory.getDefaultInstance()
                    .createJsonParser(getIntent().getStringExtra(ARGS_TODO))
                    .parse(todo);
            todoEdit.setText(todo.getText());
        } catch (IOException e) {
            Toast.makeText(this, "error occurred : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {
            updateTodo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTodo() {
        String text = todoEdit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        todo.setText(text);
        new UpdateTask() {
            @Override
            protected void onPostExecute(Todo todo) {
                if (todo != null) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        }.execute(todo);
    }
}
