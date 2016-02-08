package com.example.psalata.moneysaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.psalata.moneysaver.outcomes.AbstractOutcome;

/**
 * Created by Paweł on 07.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="MoneySaverDB";

    //main table names
    private static final String TABLE_AMOUNT_REMAINING = "amount_remaining";
    private static final String TABLE_OUTCOMES = "outcomes";
    private static final String TABLE_INCOMES = "incomes";

    //incomes/outcomes cateogries
    private static final String TABLE_CATEGORIES_OUTCOMES = "categories_outcomes";
    private static final String TABLE_CATEGORIES_MONTHLY_OUTCOMES = "categories_monthly_outcomes";
    private static final String TABLE_CATEGORIES_INCOMES = "categories_incomes";
    private static final String TABLE_CATEGORIES_MONTHLY_INCOMES = "categories_monthly_incomes";

    //main column names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "catregory";
    private static final String KEY_MONTHLY = "is_montlhy";

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
    private static final String CREATE_TABLE_CATEGORIES_MONTHLY_OUTCOMES =
            createCategoryTable(TABLE_CATEGORIES_MONTHLY_OUTCOMES);
    private static final String CREATE_TABLE_CATEGORIES_MONTHLY_INCOMES =
            createCategoryTable(TABLE_CATEGORIES_MONTHLY_INCOMES);


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AMOUNT_REMAINING);
        db.execSQL(CREATE_TABLE_OUTCOMES);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_OUTCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_INCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_MONTHLY_OUTCOMES);
        db.execSQL(CREATE_TABLE_CATEGORIES_MONTHLY_INCOMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_AMOUNT_REMAINING);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_INCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_CATEGORIES_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_CATEGORIES_INCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_CATEGORIES_MONTHLY_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_TABLE_CATEGORIES_MONTHLY_INCOMES);

        onCreate(db);
    }

    public long editAmountRemaining(Double amountRemaining) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT * FROM TABLE_AMOUNT_REMAINING";
        Cursor mCursor = db.rawQuery(count, null);

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amountRemaining);
        if(!mCursor.moveToFirst()) {
            return db.insert(TABLE_AMOUNT_REMAINING, null, values);
        } else {
            Log.d("DB addAmountRemaining", "amountRemaining already exists," +
                    " so the value is updating");
            return db.update(TABLE_AMOUNT_REMAINING, values, "KEY_ID"+0, null);
        }
    }

    private long substractOutcomeFromAmountRemaining(Double outcomeAmount) {
        Double amountRemaining = getAmountRemaining();
        amountRemaining -= outcomeAmount;
        return editAmountRemaining(amountRemaining);
    }

    public Double getAmountRemaining() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM TABLE_AMOUNT_REMAINING";
        Cursor mCursor = db.rawQuery(selectQuery, null);

        String amountRemainingLabel = "";
        if(mCursor != null && mCursor.moveToFirst()) {
            amountRemainingLabel = mCursor.getString(mCursor.getColumnIndex("KEY_ID"));
        }
        return Double.parseDouble(amountRemainingLabel);
    }

    public long addOutcome(AbstractOutcome outcome) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, outcome.getDate());
        values.put(KEY_AMOUNT, outcome.getAmount());
        values.put(KEY_CATEGORY, outcome.getCategory());
        values.put(KEY_MONTHLY, outcome.getIsMonthly());

        substractOutcomeFromAmountRemaining(outcome.getAmount());
        return db.insert(TABLE_OUTCOMES, null, values);
    }


    private static String createAmountRemainingTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_AMOUNT_REMAINING + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY," + KEY_AMOUNT + " REAL" + ")";
    }

    private static String createOutcomesOrIncomesTable(String TABLE_NAME) {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY," + KEY_DATE + " TEXT,"
                + KEY_AMOUNT + " REAL," + KEY_CATEGORY + " TEXT,"
                + KEY_MONTHLY + " INTEGER" + ")";
    }

    private static String createCategoryTable(String CATEGORY_TABLE_NAME) {
        return "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE_NAME + "("
                + KEY_ID + " INTEGER_PRIMARY_KEY, " + KEY_CATEGORY_NAME + " TEXT" + ")";
    }



}
