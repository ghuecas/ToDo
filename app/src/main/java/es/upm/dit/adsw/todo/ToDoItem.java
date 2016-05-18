package es.upm.dit.adsw.todo;

/**
 * Created by gabriel on 5/5/16.
 */
public class ToDoItem {
    private String todo;

    public ToDoItem(String todo) {
        this.todo = todo;
    }

    public void set (String todo)
    {
        this.todo= todo;
    }

    @Override
    public String toString() {
        return todo;
    }
}
