package com.shazamstore.app_shazamstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.shazamstore.app_shazamstore.models.Producto;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "ShazamStore.db";

    public DatabaseHelper(@Nullable Context context){
        super(context,DATABASE_NAME,null,21);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Producto(idProducto integer primary key, descripcion text, precio double, stock integer, idCategoria integer, idMarca integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Producto");
        onCreate(db);
    }

    public void registrarProductoDb(Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idProducto",producto.getIdProducto());
        contentValues.put("descripcion",producto.getDescripcion());
        contentValues.put("precio",producto.getPrecio());
        contentValues.put("stock",producto.getStock());
        contentValues.put("idCategoria",producto.getIdCategoria());
        contentValues.put("idMarca",producto.getIdMarca());
        long product = sqLiteDatabase.insert("Producto",null,contentValues);
        if(product != -1){
            Log.e(TAG,"registrarProducto: Producto registrado correctamente");
        } else{
            Log.e(TAG,"registrarProducto: Error al registrar producto");
        }
        sqLiteDatabase.close();
    }

    public List<Producto> obtenerProductosDb(){
        List<Producto> productoList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Producto",null,null);
        while(cursor.moveToNext()){
            Producto producto = new Producto(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),
                    cursor.getInt(3),cursor.getInt(4),cursor.getInt(5));
            productoList.add(producto);
        }
        cursor.close();
        sqLiteDatabase.close();
        return productoList;
    }

    public void eliminarProductos(){
        if(obtenerProductosDb()!=null){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from Producto");
            sqLiteDatabase.close();
        }
    }
}
