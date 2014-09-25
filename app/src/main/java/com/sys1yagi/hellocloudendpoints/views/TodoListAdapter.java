package com.sys1yagi.hellocloudendpoints.views;

import com.sys1yagi.hellocloudendpoints.R;
import com.sys1yagi.hellocloudendpoints.api.UpdateTask;
import com.sys1yagi.hellocloudendpoints.api.todoApi.model.Todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListAdapter extends ArrayAdapter<Todo> {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private final static Date DATE = new Date();

    public TodoListAdapter(Context context) {
        super(context, -1);
    }

    public List<Todo> getCheckedTodoList() {
        List<Todo> checkedTotoList = new ArrayList<Todo>();
        for (int i = 0; i < getCount(); i++) {
            Todo todo = getItem(i);
            if (todo.getChecked()) {
                checkedTotoList.add(todo);
            }
        }
        return checkedTotoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Todo todo = getItem(position);
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.listitem_todo, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        holder = (ViewHolder) convertView.getTag();

        holder.checkBox.setChecked(todo.getChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todo.setChecked(isChecked);
                updateTodo(todo);
            }
        });
        DATE.setTime(todo.getCreated());
        holder.createdAt.setText(FORMAT.format(DATE));
        holder.todoText.setText(todo.getText());

        return convertView;
    }

    private void updateTodo(Todo todo) {
        new UpdateTask().execute(todo);
    }

    static class ViewHolder {

        View root;

        CheckBox checkBox;

        TextView createdAt;

        TextView todoText;

        ViewHolder(View root) {
            this.root = root;
            checkBox = (CheckBox) root.findViewById(R.id.check_box);
            createdAt = (TextView) root.findViewById(R.id.created_at);
            todoText = (TextView) root.findViewById(R.id.todo_text);
        }
    }
}
