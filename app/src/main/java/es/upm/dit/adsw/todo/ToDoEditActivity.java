package es.upm.dit.adsw.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ToDoEditActivity extends AppCompatActivity {

    private EditText todoText;

    private int pos; // posición a editar, -1 si es nueva tarea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_edit);
        setTitle(R.string.edit_todo);

        todoText = (EditText) findViewById(R.id.todoText);

        String todoStr= getIntent().getStringExtra("TAREA");
        pos= getIntent().getIntExtra("POS", -1);

        if (todoStr != null && todoStr.length() > 0)
        {
            Log.e("ToDoEditActivity", "TAREA: " + todoStr);
            todoText.setText(todoStr);
        }
        else
        {
            Log.e("ToDoEditActivity", "TAREA: NO HAY");
        }

        Button confirmButton= (Button) findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (todoText.length() <= 0) {
                    Log.e("ADSW", "ToDoEditActivity: tarea vacia");

                    Toast.makeText(getApplicationContext(),
                            R.string.errorToDoVacio, Toast.LENGTH_SHORT).show();

                    return;
                }

                String todoStr= todoText.getText().toString();

                Intent i = new Intent();
                i.putExtra("TAREA", todoStr);
                i.putExtra("POS", pos);

                setResult(RESULT_OK, i);

                finish();
            }

        });

        Button cancelButton= (Button) findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }

        });

    }
}
