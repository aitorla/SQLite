package com.example.aitor.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvUsuarios;
    private EditText tvIdUsuario,  tvNombre, tvEmail;
    private Button btListadoUsuarios, btInsertar, btModificar, btBorrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacemos
        tvIdUsuario = (EditText)findViewById(R.id.etIdUsuario);
        tvNombre = (EditText)findViewById(R.id.etNombre);
        tvEmail = (EditText) findViewById(R.id.etEmail);

        btInsertar = (Button)findViewById(R.id.btInsertar);
        btModificar = (Button)findViewById(R.id.btModificar);
        btBorrar = (Button)findViewById(R.id.btBorrar);
        btListadoUsuarios =(Button) findViewById(R.id.btListado);


        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        SQLLiteUsuarios usdbh = new SQLLiteUsuarios(MainActivity.this, "DBUsuarios", null, 6);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

///        Si hemos abierto correctamente la base de datos
/* Insertamos si está la base de datos abierta
        if (db != null) {
            //Insertamos 5 usuarios de ejemplo
            for (int i = 1; i <= 5; i++) {
                //Generamos los datos
                int codigo = i;
                String nombre = "Usuario" + i;
                String email = "";

                //Insertamos los datos en la tabla Usuarios

                db.execSQL("INSERT INTO Usuarios VALUES ( null, '" + nombre + "', '" + email + "' )");

            }

            //Cerramos la base de datos
            db.close();

        }
*/

        btInsertar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Recuperamos los valores de los campos de texto

                String nombre = tvNombre.getText().toString();
                String email = tvEmail.getText().toString();

                //Opción 1: Método sqlExec()
                //String sql = "INSERT INTO Usuarios (idUsuario,Nombre,Email) VALUES (null , '" + nombre + "' , '" + email + "') ";
                //db.execSQL(sql);

                //Opción 2: Método insert()
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("email", email);
                db.insert("Usuarios", null, nuevoRegistro);
            }
        });

        btModificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Recuperamos los valores de los campos de texto
                String idUsuario = tvIdUsuario.getText().toString();
                String nombre = tvNombre.getText().toString();
                String email = tvEmail.getText().toString();

                //Opción 1: Método sqlExec()
                //String sql = "UPDATE Usuarios SET nombre='" + nombre + "', email='" + email + "' WHERE idUsuario=" + idUsuario;
                //db.execSQL(sql);

                //Opción 2: Método update()
                ContentValues valores = new ContentValues();
                valores.put("nombre", nombre);
                valores.put("email", email);
                db.update("Usuarios", valores, "idUsuario =" + idUsuario, null);
            }
        });

        btBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Recuperamos los valores de los campos de texto
                String idUsuario = tvIdUsuario.getText().toString();

                //Alternativa 1: mÈtodo sqlExec()
                //String sql = "DELETE FROM Usuarios WHERE idUsuario=" + idUsuario;
                //db.execSQL(sql);

                //Alternativa 2: mÈtodo delete()
                db.delete("Usuarios", "idUsuario=" + idUsuario, null);
            }
        });

        btListadoUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] campos = new String[]{"idusuario", "nombre", "email"};
                ///// Parámetros que no pasamos
                //String[] args = new String[]{"2"};
                TextView tvUsuarios=(TextView) findViewById(R.id.tvUsuarios);
                tvUsuarios.setText("");

                /// Opción 1
               String sql = "Select idUsuario, Nombre, Email FROM Usuarios";
               Cursor c = db.rawQuery(sql,null);

                // Opción 2
               // Cursor c = db.query("Usuarios", campos, "", null, null, null, null);

                //Nos aseguramos de que existe al menos un registro
                if(c.moveToFirst())
                {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String idusuario = c.getString(0);
                        String nombre = c.getString(1);
                        String email = c.getString(2);
                        tvUsuarios.append( idusuario + ".-" + nombre + " " + email + "\n");
                    } while (c.moveToNext());
                }

            }
        });
    }



    public void modificarUsuario() {
        SQLLiteUsuarios usdbh =
                new SQLLiteUsuarios(this, "DBUsuarios", null, 6);
        SQLiteDatabase db2 = usdbh.getWritableDatabase();
        String[] campos = new String[]{"idusuario", "nombre", "email"};
        String[] args = new String[]{"2"};
        TextView tvUsuarios=(TextView) findViewById(R.id.tvUsuarios);
        Cursor c = db2.query("Usuarios", campos, "", null, null, null, null);

        tvUsuarios.setText("");
        //Nos aseguramos de que existe al menos un registro
        if(c.moveToFirst())

        {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String idusuario = c.getString(0);
                String nombre = c.getString(1);
                String email = c.getString(2);
                tvUsuarios.append( idusuario + ".-" + nombre + " " + email + "\n");
            } while (c.moveToNext());
        }

        db2.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


