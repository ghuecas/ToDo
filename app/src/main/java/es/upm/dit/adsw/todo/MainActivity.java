package es.upm.dit.adsw.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private ArrayAdapter<ToDoItem> adaptador = null;
    private ArrayList<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoItemList.add(new ToDoItem("Aprender ListActivity"));
        toDoItemList.add(new ToDoItem("Usar el adaptador"));

        // adaptador de contenidos
        // we use the default layout for a row, which present just a string
        adaptador = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1,
                toDoItemList);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adaptador);

        // handle empty list: show a message with TextView
        TextView tvEmpty= (TextView)findViewById(R.id.tvEmpty);
        listView.setEmptyView(tvEmpty);

        // cuando hacemos clic en un item en la pantalla
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // should be called? 		super.on ItemClick(l, v, position, id);

                // el resto es ayuda - debugging
                ToDoItem todoItem = toDoItemList.get(position);
                String message = todoItem.toString();
                Log.d(TAG, message);
//                Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
//                toast.show();
                // hasta aquí el debugging

                Intent i = new Intent(getApplicationContext(), ToDoEditActivity.class);

                i.putExtra("TAREA", toDoItemList.get(position).toString());
                i.putExtra("POS", position);

                startActivityForResult(i, ACTIVITY_EDIT);

            }
        });

        // cuando hacemos long click en un item en la pantalla
        // registramos el menú contextual
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_item:
                Log.e(TAG, "Creamos una nota");
                Intent i = new Intent(this, ToDoEditActivity.class);
                i.putExtra("POS", -1);
                startActivityForResult(i, ACTIVITY_CREATE);
                return true;
            case R.id.delete_all_item:
                Log.e(TAG, "Borramos todos los ítems");
                borrarTodosLosElementos();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menucontextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.delete_item:
                borrarElemento(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "requestCode= " + requestCode);
        Log.e("onActivityResult", "resultCode= " + resultCode);
        Log.e("onActivityResult", "data= " + data);

        if (resultCode == RESULT_OK)
        {
            // salvar la cadena
            String todoStr = data.getStringExtra("TAREA");
            int selectedPosition= data.getIntExtra("POS", -1);

            Log.e(TAG, "TAREA DEVUELTA: " + todoStr);
            Log.e(TAG, "itemPos=" + selectedPosition);
            switch (requestCode) {
                case MainActivity.ACTIVITY_CREATE: {
                    Log.i("onActivityResult", "CREAMOS en " + selectedPosition
                            + " el valor:" + todoStr);

                    adaptador.add(new ToDoItem(todoStr));
                    break;
                }
                case MainActivity.ACTIVITY_EDIT: {
                    ToDoItem tdi = toDoItemList.get(selectedPosition);
                    tdi.set(todoStr);

                    adaptador.notifyDataSetChanged();

                    String nStr = toDoItemList.get(selectedPosition).toString();

                    Log.i("onActivityResult", "EDITAMOS en " + selectedPosition
                           + " el valor:" + nStr);
                    break;
                }
                default:
                    Log.e("onActivityResult", "ERROR EN CÓDIGO, requestCode= " + requestCode);
                    // que mal
            }
        }
    }

    public void borrarElemento(final int pos)
    {
        Log.e("borrarElemento", "Comenzando");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteItemConfirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                toDoItemList.remove(pos);
                                adaptador.notifyDataSetChanged();

                                Log.e("borrarElemento", "setPositiveButton");
                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Log.e("borrarElemento", "setNegativeButton");
                                dialog.cancel();
                            }
                        });

        Log.e("borrarElemento", "mostramos");
//		AlertDialog alert = builder.create();
//		alert.show();
        builder.show();
    }

    public void borrarTodosLosElementos()
    {
        Log.e("borrarTodosLosElementos", "Comenzando");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteAllItemsConfirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                toDoItemList.clear();
                                adaptador.notifyDataSetChanged();

                                Log.e("borrarTodosLosElementos", "setPositiveButton");
                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Log.e("borrarTodosLosElementos", "setNegativeButton");
                                dialog.cancel();
                            }
                        });

        Log.e("borrarTodosLosElementos", "mostramos");
//		AlertDialog alert = builder.create();
//		alert.show();
        builder.show();
    }

}
