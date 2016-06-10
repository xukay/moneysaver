package com.example.psalata.moneysaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.psalata.moneysaver.MainActivity;
import com.example.psalata.moneysaver.outcomes.Outcome;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł on 07.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static DBHelper mInstance = null;

    public static DBHelper getInstance(Context activityContext) {
        if(mInstance == null) {
            mInstance = new DBHelper(activityContext.getApplicationContext());
        }
        return mInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String LOG = "DBHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="MoneySaverDB";

    //main table names
    private static final String TABLE_AMOUNT_REMAINING = "amount_remaining";
    private static final String TABLE_OUTCOMES = "outcomes";
    private static final String TABLE_INCOMES = "incomes";

    //incomes/outcomes cateogries
    private static final String TABLE_CATEGORIES_OUTCOMES = "categories_outcomes";
    private static final String TABLE_CATEGORIES_INCOMES = "categories_incomes";

    //main column names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "category";

    //categories column names
    private static final String KEY_CATEGORY_NAME = "category_name";

    //tables create
    private static final String CREATE_TABLE_AMOUNT_REMAINING = createAmountRemainingTable();
    private static final String CREATE_TABLE_OUTCOMES =
            createOutcomesOrIncomesTable(TABLE_OUTCOMES);
    private static final String CREATE_TABLE_INCOMES =
            createOutcomesOrIncomesTable(TABLE_INCOMES);
    private static final String CREATE_TABLE_CATEGORIES_OUTCOMES =
            createCategoryTable(TABLE_CATEGORIES_OUTCOMES);
    private static final String CREATE_TABLE_CATEGORIES_INCOMES =
            createCategoryTable(TABLE_CATEGORIES_INCOMES);


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AMOUNT_REMAINING);
        db.execSQL(CREATE_TABLE_OUTCOMES);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_OUTCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_INCOMES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_AMOUNT_REMAINING);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_INCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CATEGORIES_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CATEGORIES_INCOMES);

        onCreate(db);
    }

    public long addAmountRemaining(BigDecimal amountRemaining) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amountRemaining.toString());
        return db.insert(TABLE_AMOUNT_REMAINING, null, values);
    }

    private long editAmountRemaining(BigDecimal amountRemaining) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amountRemaining.toString());
        return db.update(TABLE_AMOUNT_REMAINING, values, null, null);
    }

    private long subtractOutcomeFromAmountRemaining(BigDecimal outcomeAmount) {
        BigDecimal amountRemaining = getAmountRemaining();
        amountRemaining = amountRemaining.subtract(outcomeAmount);
        return editAmountRemaining(amountRemaining);
    }

    public String getAmountRemainingAsString() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_AMOUNT_REMAINING;
        Log.e(LOG, selectQuery);
        Cursor mCursor = db.rawQuery(selectQuery, null);

        if(mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return mCursor.getString(mCursor.getColumnIndex(KEY_AMOUNT));

        }
        mCursor.close();
        return null;
    }

    public BigDecimal getAmountRemaining() {
        String amountRemainingString = getAmountRemainingAsString();
        if(amountRemainingString != null) {
            return new BigDecimal(amountRemainingString);
        }
        return null;
    }

    public long addOutcome(Outcome outcome) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, outcome.getDate());
        values.put(KEY_AMOUNT, outcome.getAmount().toString());
        values.put(KEY_CATEGORY, outcome.getCategory());

        subtractOutcomeFromAmountRemaining(outcome.getAmount());
        return db.insert(TABLE_OUTCOMES, null, values);
    }

    public List<Outcome> getOutcomes() {
        List<Outcome> outcomes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OUTCOMES + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                BigDecimal amount =
                        new BigDecimal(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                outcomes.add(new Outcome(amount, date, category));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return outcomes;
    }

    public List<Outcome> getOutcomesWithDateRange(String startDate, String endDate) {
        List<Outcome> outcomes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OUTCOMES + " WHERE " + KEY_DATE
                + "  >= '" + startDate + "' AND " + KEY_DATE + " <= '" + endDate
                + "' ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                BigDecimal amount =
                        new BigDecimal(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                outcomes.add(new Outcome(amount, date, category));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return outcomes;
    }

    public long addOutcomeCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category);
        return db.insert(TABLE_CATEGORIES_OUTCOMES, null, values);
    }

    public List<String> getOutcomeCategories() {
        List<String> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES_OUTCOMES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(selectQuery, null);

        if(mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            do {
                categories.add(mCursor.getString(mCursor.getColumnIndex(KEY_CATEGORY_NAME)));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        return categories;
    }



    private static String createAmountRemainingTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_AMOUNT_REMAINING + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY," + KEY_AMOUNT + " REAL" + ")";
    }

    private static String createOutcomesOrIncomesTable(String TABLE_NAME) {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY," + KEY_DATE + " TEXT,"
                + KEY_AMOUNT + " REAL," + KEY_CATEGORY + " TEXT" + ")";
    }

    private static String createCategoryTable(String CATEGORY_TABLE_NAME) {
        return "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE_NAME + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY, " + KEY_CATEGORY_NAME + " TEXT" + ")";
    }



}
