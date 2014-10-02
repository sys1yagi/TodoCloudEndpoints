package com.sys1yagi.hellocloudendpoints.activities;

import com.sys1yagi.hellocloudendpoints.R;
import com.sys1yagi.hellocloudendpoints.api.AddTask;
import com.sys1yagi.hellocloudendpoints.api.DeleteTask;
import com.sys1yagi.hellocloudendpoints.api.ListTask;
import com.sys1yagi.hellocloudendpoints.api.todoApi.model.Todo;
import com.sys1yagi.hellocloudendpoints.api.todoApi.model.TodoCollection;
import com.sys1yagi.hellocloudendpoints.views.TodoListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    public static final int EDIT_REQUEST_CODE = 0x111;

    EditText todoAddEdit;

    Button todoAddButton;

    ListView todoListView;

    TodoListAdapter todoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoAddEdit = (EditText) findViewById(R.id.todo_add_edit);
        todoAddButton = (Button) findViewById(R.id.todo_add_button);
        todoListView = (ListView) findViewById(R.id.list_view);

        loadTodoList();
    }

    private void loadTodoList() {
        new ListTask() {
            @Override
            protected void onPostExecute(TodoCollection todoCollection) {
                super.onPostExecute(todoCollection);
                setupUi(todoCollection);
            }
        }.execute();
    }

    private void reloadTodoList() {
        if (todoListAdapter != null) {
            todoListAdapter.clear();
            loadTodoList();
        }
    }

    private void setupUi(TodoCollection todoCollection) {

        todoListAdapter = new TodoListAdapter(this);
        if (todoCollection != null) {
            List<Todo> todoList = todoCollection.getItems();
            if (todoList != null) {
                for (Todo todo : todoList) {
                    todoListAdapter.add(todo);
                }
            }
        }
        todoListView.setAdapter(todoListAdapter);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = todoListAdapter.getItem(position);
                openTodoEdit(todo);
            }
        });
        todoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = todoAddEdit.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    todoAddEdit.setText("");
                    addTodo(text);
                }
            }
        });
    }

    private void openTodoEdit(Todo todo) {
        try {
            startActivityForResult(TodoEditActivity.createIntent(this, todo), EDIT_REQUEST_CODE);
        } catch (IOException e) {
            Toast.makeText(this, "error occurred : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                reloadTodoList();
            }
        }
    }

    private void addAdapter(Todo todo) {
        if (todo != null) {
            todoListAdapter.add(todo);
            todoListAdapter.notifyDataSetChanged();
        }
    }

    private void addTodo(String todoText) {
        Todo todo = new Todo();
        todo.setText(todoText);
        new AddTask() {
            @Override
            protected void onPostExecute(Todo todo) {
                addAdapter(todo);
            }
        }.execute(todo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteCheckedTodo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCheckedTodo() {
        final List<Todo> checkedTodoList = todoListAdapter.getCheckedTodoList();
        new DeleteTask() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                for (Todo todo : checkedTodoList) {
                    todoListAdapter.remove(todo);
                }
            }
        }.execute(checkedTodoList.toArray(new Todo[0]));
    }
}
