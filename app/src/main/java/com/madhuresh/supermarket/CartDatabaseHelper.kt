package com.madhuresh.supermarket

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.madhuresh.supermarket.dataModel.Product

class CartDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cart.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CART = "cart"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_STOCK = "stock"
        private const val COLUMN_UNIT = "unit"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_CART (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PRICE INTEGER, " +
                "$COLUMN_STOCK INTEGER, " +
                "$COLUMN_UNIT TEXT, " +
                "$COLUMN_IMAGE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_STOCK, product.stock)
            put(COLUMN_UNIT, product.unit)
            put(COLUMN_IMAGE, product.image)
        }
        val existingProduct = getProductByName(product.name)
        if (existingProduct == null) {
            db.insert(TABLE_CART, null, values)
        }
    }

    fun removeProduct(product: Product) {
        val db = writableDatabase
        db.delete(TABLE_CART, "$COLUMN_NAME = ?", arrayOf(product.name))
    }

    @SuppressLint("Range")
    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CART"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE))
                val stock = cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK))
                val unit = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT))
                val image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                productList.add(Product(name = name, price = price, stock = stock, unit = unit, image = image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }

    @SuppressLint("Range")
    private fun getProductByName(name: String): Product? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CART,
            null,
            "$COLUMN_NAME = ?",
            arrayOf(name),
            null,
            null,
            null
        )

        var product: Product? = null
        if (cursor.moveToFirst()) {
            val price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE))
            val stock = cursor.getInt(cursor.getColumnIndex(COLUMN_STOCK))
            val unit = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT))
            val image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
            product = Product(name = name, price = price, stock = stock, unit = unit, image = image)
        }
        cursor.close()
        return product
    }

    fun clearCart() {
        val db = writableDatabase
        db.delete(TABLE_CART, null, null) // Delete all rows
    }
}