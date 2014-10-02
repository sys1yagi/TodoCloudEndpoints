package com.sys1yagi.hellocloudendpoints.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Api(name = "todoApi", version = "v1",
        namespace = @ApiNamespace(ownerDomain = "api.hellocloudendpoints.sys1yagi.com",
                ownerName = "api.hellocloudendpoints.sys1yagi.com", packagePath = ""))
public class TodoEndpoint {

    static {
        ObjectifyService.register(Todo.class);
    }

    @ApiMethod(name = "list", httpMethod = "get")
    public List<Todo> list() {
        return ofy().load().type(Todo.class).list();
    }

    @ApiMethod(name = "add", httpMethod = "post")
    public Todo add(Todo todo) {
        todo.setCreated(new Date().getTime());
        return update(todo);
    }

    @ApiMethod(name = "update", httpMethod = "put")
    public Todo update(Todo todo) {
        todo.setUpdated(new Date().getTime());
        Key<Todo> key = ofy().save().entity(todo).now();
        return ofy().load().type(Todo.class).id(key.getId()).now();
    }

    @ApiMethod(name = "delete", httpMethod = "delete")
    public void delete(@Named("id") long id) throws NotFoundException {
        Todo todo = ofy().load().type(Todo.class).id(id).safe();
        if (todo == null) {
            throw new NotFoundException("id=" + id + " is not found");
        }
        ofy().delete().entity(todo).now();

    }
}
