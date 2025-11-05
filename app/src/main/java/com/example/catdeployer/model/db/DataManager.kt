package com.example.catdeployer.model.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Debug
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.catdeployer.model.CatUiModel
import com.example.catdeployer.model.Gender


class DataManager(context: Context) {
    private val db: SQLiteDatabase
    init {
        val helper=CustomSQLiteOpenHelper(context)
        db=helper.writableDatabase
    }
    companion object{
        const val ID="_id"
        const val NAME="name"
        const val GENDER="gender"
        const val BIOGRAPHY="biography"
        const val IMAGE_URL= "image_url"
        const val DB_NAME="cats"
        const val DB_VERSION=4
        const val TABLE_CATS="cats"
    }

    fun insert(name: String, gender: Gender, biography: String, url: String){
       val values= ContentValues()
        values.put(NAME, name)
        values.put(GENDER, gender.toString())
        values.put(BIOGRAPHY, biography)
        values.put(IMAGE_URL, url)
        db.insert(TABLE_CATS, null, values)
    }

    fun delete(id: Int){
        val query="delete from " + TABLE_CATS+ " WHERE " + ID +" = "+ id.toString() +";"
        Log.d("id",""+id)
        db.execSQL(query)

    }

    fun getCats(): SnapshotStateList<CatUiModel> {
        val SELECT = arrayOf<String?>(
            ID, NAME, GENDER, BIOGRAPHY,
            IMAGE_URL
        )

        val cursor =
            db.query(TABLE_CATS, SELECT, null, null, null, null, NAME)
        val cats = SnapshotStateList<CatUiModel>()
        var cat: CatUiModel
        while (cursor.moveToNext()) {
            cat = CatUiModel(cursor.getInt(0),enumValueOf<Gender>(cursor.getString(2)), cursor.getString(1), cursor.getString(3),cursor.getString(4))


            cats.add(cat)
        }
        cursor.close()

        return cats
    }

    private inner class CustomSQLiteOpenHelper(
        context: Context
    ): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
        override fun onCreate(db: SQLiteDatabase) {
            val newTableQueryString=(
                "create table " + TABLE_CATS+  " ("
                + ID + " integer primary key autoincrement,"
                + NAME + " text,"
                + GENDER + " text,"
                + BIOGRAPHY + " text,"
                + IMAGE_URL + " text);")

            db.execSQL(newTableQueryString)
            }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATS);
            onCreate(db)
        }
    }
    }


