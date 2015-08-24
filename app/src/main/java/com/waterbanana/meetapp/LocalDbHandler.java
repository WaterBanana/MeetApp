package com.waterbanana.meetapp;

/**
 * Created by gerar_000 on 8/22/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gerar_000 on 7/29/2015.
 */


public class LocalDbHandler extends SQLiteOpenHelper {

    public LocalDbHandler(Context context) {
        super(context, LocalDbContract.DB_NAME, null, LocalDbContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RIBBON_TABLE = String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT)",
            LocalDbContract.TABLE,
                LocalDbContract.Columns.COLUMN_ID,
            LocalDbContract.Columns.COLUMN_DAY,
            LocalDbContract.Columns.COLUMN_JAN,
                    LocalDbContract.Columns.COLUMN_JAN_ID,
                    LocalDbContract.Columns.COLUMN_JAN_DATE,
                    LocalDbContract.Columns.COLUMN_JAN_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_JAN_RIBBONEND,
            LocalDbContract.Columns.COLUMN_FEB,
                    LocalDbContract.Columns.COLUMN_FEB_ID,
                    LocalDbContract.Columns.COLUMN_FEB_DATE,
                    LocalDbContract.Columns.COLUMN_FEB_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_FEB_RIBBONEND,
            LocalDbContract.Columns.COLUMN_MAR,
                    LocalDbContract.Columns.COLUMN_MAR_ID,
                    LocalDbContract.Columns.COLUMN_MAR_DATE,
                    LocalDbContract.Columns.COLUMN_MAR_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_MAR_RIBBONEND,
            LocalDbContract.Columns.COLUMN_APR,
                    LocalDbContract.Columns.COLUMN_APR_ID,
                    LocalDbContract.Columns.COLUMN_APR_DATE,
                    LocalDbContract.Columns.COLUMN_APR_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_APR_RIBBONEND,
            LocalDbContract.Columns.COLUMN_MAY,
                    LocalDbContract.Columns.COLUMN_MAY_ID,
                    LocalDbContract.Columns.COLUMN_MAY_DATE,
                    LocalDbContract.Columns.COLUMN_MAY_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_MAY_RIBBONEND,
            LocalDbContract.Columns.COLUMN_JUN,
                    LocalDbContract.Columns.COLUMN_JUN_ID,
                    LocalDbContract.Columns.COLUMN_JUN_DATE,
                    LocalDbContract.Columns.COLUMN_JUN_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_JUN_RIBBONEND,
            LocalDbContract.Columns.COLUMN_JUL,
                    LocalDbContract.Columns.COLUMN_JUL_ID,
                    LocalDbContract.Columns.COLUMN_JUL_DATE,
                    LocalDbContract.Columns.COLUMN_JUL_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_JUL_RIBBONEND,
            LocalDbContract.Columns.COLUMN_AUG,
                    LocalDbContract.Columns.COLUMN_AUG_ID,
                    LocalDbContract.Columns.COLUMN_AUG_DATE,
                    LocalDbContract.Columns.COLUMN_AUG_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_AUG_RIBBONEND,
            LocalDbContract.Columns.COLUMN_SEP,
                    LocalDbContract.Columns.COLUMN_SEP_ID,
                    LocalDbContract.Columns.COLUMN_SEP_DATE,
                    LocalDbContract.Columns.COLUMN_SEP_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_SEP_RIBBONEND,
            LocalDbContract.Columns.COLUMN_OCT,
                    LocalDbContract.Columns.COLUMN_OCT_ID,
                    LocalDbContract.Columns.COLUMN_OCT_DATE,
                    LocalDbContract.Columns.COLUMN_OCT_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_OCT_RIBBONEND,
            LocalDbContract.Columns.COLUMN_NOV,
                    LocalDbContract.Columns.COLUMN_NOV_ID,
                    LocalDbContract.Columns.COLUMN_NOV_DATE,
                    LocalDbContract.Columns.COLUMN_NOV_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_NOV_RIBBONEND,
            LocalDbContract.Columns.COLUMN_DEC,
                    LocalDbContract.Columns.COLUMN_DEC_ID,
                    LocalDbContract.Columns.COLUMN_DEC_DATE,
                    LocalDbContract.Columns.COLUMN_DEC_RIBBONSTART,
                    LocalDbContract.Columns.COLUMN_DEC_RIBBONEND

        );
        db.execSQL(CREATE_RIBBON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+LocalDbContract.TABLE);
        onCreate(sqlDB);
    }
}
