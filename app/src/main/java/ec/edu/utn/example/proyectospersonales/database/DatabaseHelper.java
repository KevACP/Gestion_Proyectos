package ec.edu.utn.example.proyectospersonales.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ec.edu.utn.example.proyectospersonales.model.Actividad;
import ec.edu.utn.example.proyectospersonales.model.Proyecto;
import ec.edu.utn.example.proyectospersonales.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "proyectos.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla Usuario
    private static final String TABLE_USUARIO = "Usuario";
    private static final String COLUMN_ID_USUARIO = "id_usuario";
    private static final String COLUMN_NOMBRE_USUARIO = "nombre_usuario";
    private static final String COLUMN_CONTRASENA = "contrasena";
    private static final String COLUMN_CORREO = "correo";

    // Tabla Proyecto
    private static final String TABLE_PROYECTO = "Proyecto";
    private static final String COLUMN_ID_PROYECTO = "id_proyecto";
    private static final String COLUMN_ID_USUARIO_FK = "id_usuario";
    private static final String COLUMN_NOMBRE_PROYECTO = "nombre";
    private static final String COLUMN_DESCRIPCION_PROYECTO = "descripcion";
    private static final String COLUMN_FECHA_INICIO_PROYECTO = "fecha_inicio";
    private static final String COLUMN_FECHA_FIN_PROYECTO = "fecha_fin";

    // Tabla Actividad
    private static final String TABLE_ACTIVIDAD = "Actividad";
    private static final String COLUMN_ID_ACTIVIDAD = "id_actividad";
    private static final String COLUMN_ID_PROYECTO_FK = "id_proyecto";
    private static final String COLUMN_NOMBRE_ACTIVIDAD = "nombre";
    private static final String COLUMN_DESCRIPCION_ACTIVIDAD = "descripcion";
    private static final String COLUMN_FECHA_INICIO_ACTIVIDAD = "fecha_inicio";
    private static final String COLUMN_FECHA_FIN_ACTIVIDAD = "fecha_fin";
    private static final String COLUMN_ESTADO_ACTIVIDAD = "estado";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsuario = "CREATE TABLE " + TABLE_USUARIO + " (" +
                COLUMN_ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE_USUARIO + " TEXT NOT NULL UNIQUE, " +
                COLUMN_CONTRASENA + " TEXT NOT NULL, " +
                COLUMN_CORREO + " TEXT)";
        db.execSQL(createTableUsuario);

        String createTableProyecto = "CREATE TABLE " + TABLE_PROYECTO + " (" +
                COLUMN_ID_PROYECTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_USUARIO_FK + " INTEGER NOT NULL, " +
                COLUMN_NOMBRE_PROYECTO + " TEXT NOT NULL, " +
                COLUMN_DESCRIPCION_PROYECTO + " TEXT, " +
                COLUMN_FECHA_INICIO_PROYECTO + " TEXT NOT NULL, " +
                COLUMN_FECHA_FIN_PROYECTO + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_ID_USUARIO_FK + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_ID_USUARIO + "))";
        db.execSQL(createTableProyecto);

        String createTableActividad = "CREATE TABLE " + TABLE_ACTIVIDAD + " (" +
                COLUMN_ID_ACTIVIDAD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_PROYECTO_FK + " INTEGER NOT NULL, " +
                COLUMN_NOMBRE_ACTIVIDAD + " TEXT NOT NULL, " +
                COLUMN_DESCRIPCION_ACTIVIDAD + " TEXT, " +
                COLUMN_FECHA_INICIO_ACTIVIDAD + " TEXT NOT NULL, " +
                COLUMN_FECHA_FIN_ACTIVIDAD + " TEXT, " +
                COLUMN_ESTADO_ACTIVIDAD + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_ID_PROYECTO_FK + ") REFERENCES " + TABLE_PROYECTO + "(" + COLUMN_ID_PROYECTO + "))";
        db.execSQL(createTableActividad);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROYECTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        onCreate(db);
    }

    public Usuario validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIO + " WHERE " + COLUMN_NOMBRE_USUARIO + " = ? AND " + COLUMN_CONTRASENA + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_USUARIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_USUARIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO))
            );
        }
        cursor.close();
        return usuario;
    }

    public boolean registerUser(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_USUARIO, usuario.getNombreUsuario());
        values.put(COLUMN_CONTRASENA, usuario.getContrasena());
        values.put(COLUMN_CORREO, usuario.getCorreo());

        try {
            long result = db.insertOrThrow(TABLE_USUARIO, null, values);
            return result != -1;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public String getUserEmail(String identifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CORREO + " FROM " + TABLE_USUARIO +
                " WHERE " + COLUMN_NOMBRE_USUARIO + " = ? OR " + COLUMN_CORREO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{identifier, identifier});
        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO));
        }
        cursor.close();
        return email;
    }

    // Métodos CRUD para Proyecto
    public boolean addProyecto(Proyecto proyecto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_USUARIO_FK, proyecto.getIdUsuario());
        values.put(COLUMN_NOMBRE_PROYECTO, proyecto.getNombre());
        values.put(COLUMN_DESCRIPCION_PROYECTO, proyecto.getDescripcion());
        values.put(COLUMN_FECHA_INICIO_PROYECTO, proyecto.getFechaInicio());
        values.put(COLUMN_FECHA_FIN_PROYECTO, proyecto.getFechaFin());

        try {
            long result = db.insertOrThrow(TABLE_PROYECTO, null, values);
            return result != -1;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public List<Proyecto> getProyectosByUser(int userId) {
        List<Proyecto> proyectos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROYECTO + " WHERE " + COLUMN_ID_USUARIO_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Proyecto proyecto = new Proyecto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_PROYECTO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_USUARIO_FK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_PROYECTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION_PROYECTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INICIO_PROYECTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_FIN_PROYECTO))
                );
                proyectos.add(proyecto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return proyectos;
    }

    public boolean updateProyecto(Proyecto proyecto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_PROYECTO, proyecto.getNombre());
        values.put(COLUMN_DESCRIPCION_PROYECTO, proyecto.getDescripcion());
        values.put(COLUMN_FECHA_INICIO_PROYECTO, proyecto.getFechaInicio());
        values.put(COLUMN_FECHA_FIN_PROYECTO, proyecto.getFechaFin());

        try {
            int rows = db.update(TABLE_PROYECTO, values, COLUMN_ID_PROYECTO + " = ?",
                    new String[]{String.valueOf(proyecto.getIdProyecto())});
            return rows > 0;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deleteProyecto(int proyectoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_ACTIVIDAD, COLUMN_ID_PROYECTO_FK + " = ?",
                    new String[]{String.valueOf(proyectoId)});
            int rows = db.delete(TABLE_PROYECTO, COLUMN_ID_PROYECTO + " = ?",
                    new String[]{String.valueOf(proyectoId)});
            return rows > 0;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    // Métodos CRUD para Actividad
    public boolean addActividad(Actividad actividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_PROYECTO_FK, actividad.getIdProyecto());
        values.put(COLUMN_NOMBRE_ACTIVIDAD, actividad.getNombre());
        values.put(COLUMN_DESCRIPCION_ACTIVIDAD, actividad.getDescripcion());
        values.put(COLUMN_FECHA_INICIO_ACTIVIDAD, actividad.getFechaInicio());
        values.put(COLUMN_FECHA_FIN_ACTIVIDAD, actividad.getFechaFin());
        values.put(COLUMN_ESTADO_ACTIVIDAD, actividad.getEstado());

        try {
            long result = db.insertOrThrow(TABLE_ACTIVIDAD, null, values);
            return result != -1;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public List<Actividad> getActividadesByProyecto(int proyectoId) {
        List<Actividad> actividades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ACTIVIDAD + " WHERE " + COLUMN_ID_PROYECTO_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(proyectoId)});

        if (cursor.moveToFirst()) {
            do {
                Actividad actividad = new Actividad(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_ACTIVIDAD)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_PROYECTO_FK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_ACTIVIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION_ACTIVIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INICIO_ACTIVIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_FIN_ACTIVIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO_ACTIVIDAD))
                );
                actividades.add(actividad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return actividades;
    }

    public boolean updateActividad(Actividad actividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_ACTIVIDAD, actividad.getNombre());
        values.put(COLUMN_DESCRIPCION_ACTIVIDAD, actividad.getDescripcion());
        values.put(COLUMN_FECHA_INICIO_ACTIVIDAD, actividad.getFechaInicio());
        values.put(COLUMN_FECHA_FIN_ACTIVIDAD, actividad.getFechaFin());
        values.put(COLUMN_ESTADO_ACTIVIDAD, actividad.getEstado());

        try {
            int rows = db.update(TABLE_ACTIVIDAD, values, COLUMN_ID_ACTIVIDAD + " = ?",
                    new String[]{String.valueOf(actividad.getIdActividad())});
            return rows > 0;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deleteActividad(int actividadId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_ACTIVIDAD, COLUMN_ID_ACTIVIDAD + " = ?",
                    new String[]{String.valueOf(actividadId)});
            return rows > 0;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    // Método para calcular el progreso del proyecto
    public int getProgresoProyecto(int proyectoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalActividades = 0;
        int actividadesRealizadas = 0;

        // Contar total de actividades
        String queryTotal = "SELECT COUNT(*) FROM " + TABLE_ACTIVIDAD + " WHERE " + COLUMN_ID_PROYECTO_FK + " = ?";
        Cursor cursorTotal = db.rawQuery(queryTotal, new String[]{String.valueOf(proyectoId)});
        if (cursorTotal.moveToFirst()) {
            totalActividades = cursorTotal.getInt(0);
        }
        cursorTotal.close();

        // Contar actividades en estado "Realizado"
        String queryRealizadas = "SELECT COUNT(*) FROM " + TABLE_ACTIVIDAD + " WHERE " +
                COLUMN_ID_PROYECTO_FK + " = ? AND " + COLUMN_ESTADO_ACTIVIDAD + " = ?";
        Cursor cursorRealizadas = db.rawQuery(queryRealizadas, new String[]{String.valueOf(proyectoId), "Realizado"});
        if (cursorRealizadas.moveToFirst()) {
            actividadesRealizadas = cursorRealizadas.getInt(0);
        }
        cursorRealizadas.close();

        // Calcular porcentaje
        if (totalActividades == 0) {
            return 0; // Evitar división por cero
        }
        return (actividadesRealizadas * 100) / totalActividades;
    }
}