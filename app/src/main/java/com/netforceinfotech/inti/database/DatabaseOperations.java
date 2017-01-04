/*
 * This class contain necessary database operation e.g: update data, remove data etc... needed in application
 */

package com.netforceinfotech.inti.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperations extends SQLiteOpenHelper {

    static int currentVersion = 2;


// add email address here....  in the expenses table...

    public String CREATE_EXPENSES_REPORT_TABLE = "CREATE TABLE IF NOT EXISTS " + TableData.ExpenseReportTable.TABLE_NAME + "("
            + TableData.ExpenseReportTable.ER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableData.ExpenseReportTable.ER_NAME + " TEXT,"
            + TableData.ExpenseReportTable.ER_STATUS + " TEXT,"
            + TableData.ExpenseReportTable.ER_FROM_DATE + " TEXT,"
            + TableData.ExpenseReportTable.ER_TO_DATE + " TEXT,"
            + TableData.ExpenseReportTable.USER_EMAIL + " TEXT,"
            + TableData.ExpenseReportTable.USER_TYPE + " TEXT,"
            + TableData.ExpenseReportTable.ER_CREATION_DATE + " TEXT,"
            + TableData.ExpenseReportTable.ER_DESCRIPTION + " TEXT,"
            + TableData.ExpenseReportTable.USER_ID + " INTEGER ,"
            + TableData.ExpenseReportTable.CUSTOMER_ID + " TEXT,"
            + TableData.ExpenseReportTable.ER_TOTAL_COST + " TEXT,"
            + TableData.ExpenseReportTable.ER_IS_OFFLINE + " INTEGER DEFAULT 0,"
            + TableData.ExpenseReportTable.ER_IS_REQUESTED + " INTEGER DEFAULT 0 );";


    public String CREATE_EXPENSES_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TableData.ExpensesListTable.TABLE_NAME + "("
            + TableData.ExpensesListTable.EL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + TableData.ExpensesListTable.ER_ID + " INTEGER,"
            + TableData.ExpensesListTable.EL_CREATION_DATE + " TEXT,"
            + TableData.ExpensesListTable.USER_EMAIL + " TEXT,"
            + TableData.ExpensesListTable.EL_USER_ID + " INTEGER ,"
            + TableData.ExpensesListTable.EL_IMAGE_URL + " TEXT,"
            + TableData.ExpensesListTable.EL_CATEGORY + " TEXT,"
            + TableData.ExpensesListTable.EL_CURRENCY_CODE + " TEXT,"
            + TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT + " TEXT,"
            + TableData.ExpensesListTable.EL_EXCHANGE_RATE + " TEXT,"
            + TableData.ExpensesListTable.EL_CONVERTED_AMOUNT + " TEXT,"
            + TableData.ExpensesListTable.EL_DESCRIPTION + " TEXT,"
            + TableData.ExpensesListTable.EL_DATE + " TEXT,"
            + TableData.ExpensesListTable.EL_RUC + " TEXT,"
            + TableData.ExpensesListTable.EL_PROVIDER + " TEXT,"
            + TableData.ExpensesListTable.EL_COST_CENTER + " TEXT,"
            + TableData.ExpensesListTable.EL_DOCUMENT_TYPE + " TEXT,"
            + TableData.ExpensesListTable.EL_NUMBER_OF_DOCS + " TEXT,"
            + TableData.ExpensesListTable.EL_SERIES + " TEXT,"
            + TableData.ExpensesListTable.EL_DRAFT + " TEXT,"
            + TableData.ExpensesListTable.EL_TAX_RATE + " TEXT,"
            + TableData.ExpensesListTable.EL_IGV + " TEXT,"
            + TableData.ExpensesListTable.EL_BILLABLE + " INTEGER );";


    public DatabaseOperations(Context context) {

        super(context, TableData.ExpensesListTable.DATABASE_NAME, null, currentVersion);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(CREATE_EXPENSES_REPORT_TABLE);
        db.execSQL(CREATE_EXPENSES_LIST_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_EXPENSES_REPORT_TABLE);
        db.execSQL(CREATE_EXPENSES_LIST_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // will add later...
        db.execSQL("DROP TABLE IF EXISTS " + TableData.ExpensesListTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableData.ExpenseReportTable.TABLE_NAME);

        // Create tables again
        onCreate(db);


    }

    // insert expense_report datas...

    // insert databasse in the local db ...

    public long AddExpenseReport(DatabaseOperations dop,String ername,String erfromdate, String ertodate, String erdescription,
                                      String erstatus,String ercreationDate, String useremail, String usertype,String userid,String customerId,int isoffline,int isrequested) {


        try {

            SQLiteDatabase sq = dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableData.ExpenseReportTable.ER_NAME, ername);
            cv.put(TableData.ExpenseReportTable.ER_FROM_DATE, erfromdate);
            cv.put(TableData.ExpenseReportTable.ER_TO_DATE, ertodate);
            cv.put(TableData.ExpenseReportTable.ER_DESCRIPTION, erdescription);
            cv.put(TableData.ExpenseReportTable.ER_STATUS, erstatus);
            cv.put(TableData.ExpenseReportTable.ER_CREATION_DATE, ercreationDate);
            cv.put(TableData.ExpenseReportTable.USER_EMAIL, useremail);
            cv.put(TableData.ExpenseReportTable.USER_TYPE, usertype);
            cv.put(TableData.ExpenseReportTable.USER_ID, userid);
            cv.put(TableData.ExpenseReportTable.CUSTOMER_ID, customerId);
            cv.put(TableData.ExpenseReportTable.ER_TOTAL_COST, erstatus);
            cv.put(TableData.ExpenseReportTable.ER_IS_OFFLINE, isoffline);
            cv.put(TableData.ExpenseReportTable.ER_IS_REQUESTED, isrequested);



            //long reslt = sq.insert(TableData.ExpenseReportTable.TABLE_NAME,null,cv);

            long reslt = sq.insertWithOnConflict(TableData.ExpenseReportTable.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

            Log.d("InserData", String.valueOf(reslt));



            return reslt;

        } catch (Exception ex) {

            return -1;
        }
    }

    // ADD EXPENSE LIST DATA...

    public long AddExpensesList(DatabaseOperations dop,
                                                String erId,String elcreationdate,
                                                String eEmail, int userId,String imageUrl, String expenseDate, String currencyCode,
                                                String originalAmount, String exchangeRate, String convertedAmount,
                                                String elDescription, String elCategory, String elRUC,
                                                String elProvider, String elCostCenter,
                                                String elDocumentType, String elSeries, String elNumberofDocs,
                                                String elDraft, String elTaxRate, String elIGV,int billable) {

        try {

            SQLiteDatabase sq = dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableData.ExpensesListTable.ER_ID, erId);
            cv.put(TableData.ExpensesListTable.EL_CREATION_DATE, elcreationdate);
            cv.put(TableData.ExpensesListTable.USER_EMAIL, eEmail);
            cv.put(TableData.ExpensesListTable.EL_USER_ID,userId);
            cv.put(TableData.ExpensesListTable.EL_IMAGE_URL, imageUrl);
            cv.put(TableData.ExpensesListTable.EL_DATE, expenseDate);
            cv.put(TableData.ExpensesListTable.EL_CURRENCY_CODE, currencyCode);
            cv.put(TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT, originalAmount);
            cv.put(TableData.ExpensesListTable.EL_EXCHANGE_RATE, exchangeRate);
            cv.put(TableData.ExpensesListTable.EL_CONVERTED_AMOUNT, convertedAmount);
            cv.put(TableData.ExpensesListTable.EL_DESCRIPTION, elDescription);
            cv.put(TableData.ExpensesListTable.EL_CATEGORY, elCategory);
            cv.put(TableData.ExpensesListTable.EL_RUC, elRUC);
            cv.put(TableData.ExpensesListTable.EL_PROVIDER, elProvider);
            cv.put(TableData.ExpensesListTable.EL_COST_CENTER, elCostCenter);
            cv.put(TableData.ExpensesListTable.EL_DOCUMENT_TYPE, elDocumentType);
            cv.put(TableData.ExpensesListTable.EL_SERIES, elSeries);
            cv.put(TableData.ExpensesListTable.EL_NUMBER_OF_DOCS, elNumberofDocs);
            cv.put(TableData.ExpensesListTable.EL_DRAFT, elDraft);
            cv.put(TableData.ExpensesListTable.EL_TAX_RATE, elTaxRate);
            cv.put(TableData.ExpensesListTable.EL_IGV, elIGV);
            cv.put(TableData.ExpensesListTable.EL_BILLABLE, billable);

            long reslt = sq.insertWithOnConflict(TableData.ExpensesListTable.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

            Log.d("InserData", String.valueOf(reslt));

            return reslt;

        } catch (Exception ex) {

            return -1;
        }


    }

    public Cursor getErId(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery ="SELECT * FROM "+TableData.ExpenseReportTable.TABLE_NAME+"";
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;

        //String s ="SELECT "+TableData.ExpenseReportTable.ER_ID+" FROM "+TableData.ExpenseReportTable.TABLE_NAME+" WHERE "+TableData.ExpenseReportTable.CUSTOMER_ID+"='"+customerid+"'";

    }

    public Cursor getErIds(DatabaseOperations dop, String userid){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery ="SELECT "+TableData.ExpenseReportTable.ER_ID+" FROM "+TableData.ExpenseReportTable.TABLE_NAME+" WHERE "+TableData.ExpenseReportTable.USER_ID+"='"+userid+"'";

        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;


    }


    public Cursor getExpensesListData(DatabaseOperations dop){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

        // String selectQuery = "SELECT * FROM " + TableData.ExpenseReportTable.TABLE_NAME;
        String selectQuery ="SELECT * FROM "+TableData.ExpensesListTable.TABLE_NAME+"";

        //  String dd = "SELECT * FROM TABLENAME";
        //  String dd = "SELECT * FROM "+TableData.ExpenseReportTable.TABLE_NAME+"";


        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;


    }

    // insert expenses list of an ER....

    public long InsertInExpenseListTableData(DatabaseOperations dop,
                                                String expenseID, String userType, String expenseCreationDate,
                                                String eEmail, String imageUrl, String expenseDate, String currencyCode,
                                                String originalAmount, String exchangeRate, String convertedAmount,
                                                String expenseDescription, String expenseCategory, String expenseRUC,
                                                String expenseProvider, String expenseCostCenter,
                                                String expenseDocumentType, String expenseSeries, String expenseNumberofDocs,
                                                String expenseDraft, String expenseTaxRate, String expenseIGV, String title, String billable, String erDescription, String erFromDate, String erToDate ,String erStatus,String erListID) {

//
//        try {
//
//            SQLiteDatabase sq = dop.getWritableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_ID, expenseID);
//           // cv.put(TableData.ListofAnExpensesTable.USER_TYPE, userType);
//            cv.put(TableData.ListofAnExpensesTable.CREATEION_DATE, expenseCreationDate);
//            cv.put(TableData.ListofAnExpensesTable.EMPLOYEE_EMAIL, eEmail);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_IMAGE_URL, imageUrl);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSE_DATE, expenseDate);
//            cv.put(TableData.ListofAnExpensesTable.CURRENCY_CODE, currencyCode);
//            cv.put(TableData.ListofAnExpensesTable.ORIGINAL_AMOUNT, originalAmount);
//            cv.put(TableData.ListofAnExpensesTable.EXCHANGE_RATE, exchangeRate);
//            cv.put(TableData.ListofAnExpensesTable.CONVERTED_AMOUNT, convertedAmount);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_DESCRIPTION, expenseDescription);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_CATEGORY, expenseCategory);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSESRUC, expenseRUC);
//            cv.put(TableData.ListofAnExpensesTable.EXPNSES_PROVIDER, expenseProvider);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSECOST_CENTER, expenseCostCenter);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_DOCUMENT_TYPE, expenseDocumentType);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_SERIES, expenseSeries);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSES_NUMBER_OF_DOCS, expenseNumberofDocs);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSE_DRAFT, expenseDraft);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSE_TAX_RATE, expenseTaxRate);
//            cv.put(TableData.ListofAnExpensesTable.EXPENSE_IGV, expenseIGV);
//            cv.put(TableData.ListofAnExpensesTable.BILLABLE, billable);
//
//
//             long reslt = sq.insert(TableData.ListofAnExpensesTable.TABLE_NAME,null,cv);
//            sq.close();
//           // long reslt = sq.insertWithOnConflict(TableData.ListofAnExpensesTable.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
//
//            Log.d("InserData", String.valueOf(reslt));
//
//            return reslt;
//
//        } catch (Exception ex) {
//
//            return -1;
//        }

        return 0;
    }


//    select from the expensesTable


    public Cursor SelectFromErTable(DatabaseOperations dop) {

        SQLiteDatabase SQ = dop.getReadableDatabase();
        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

       String selectQuery = "SELECT * FROM " + TableData.ExpenseReportTable.TABLE_NAME;
       // String selectQuery ="SELECT * FROM "+TableData.ExpenseReportTable.TABLE_NAME+"";

      //  String dd = "SELECT * FROM TABLENAME";
      //  String dd = "SELECT * FROM "+TableData.ExpenseReportTable.TABLE_NAME+"";


        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;
    }
    // select all data from the table...


    public Cursor ErhasEl(DatabaseOperations dop,String erId) {

        SQLiteDatabase SQ = dop.getReadableDatabase();
        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

        //String selectQuery = "SELECT * FROM " + TableData.ExpenseReportTable.TABLE_NAME + "WHERE erid='id';
            //    selectQuery ="SELECT FROM TNAME WHERE id ='id'";


        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME + " WHERE " + TableData.ExpensesListTable.ER_ID + " ="+ erId ;



        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;
    }

    public Cursor SelectFromLISTOFANEXPENSETABLE(DatabaseOperations dop) {

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME;
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;

    }

    // select from report table...

    public Cursor SelectDatafromExpenseReportTable(DatabaseOperations dop, String erID,String eEmail) {

        SQLiteDatabase SQ = dop.getReadableDatabase();


        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

        //String selectQuery3 = "SELECT * FROM " + TableData.ExpensesTableList.TABLE_NAME + "WHERE" + TableData.ExpensesTableList.EXPENSES_ID + "=" + erID;

        String selectQuery = "SELECT * FROM " + TableData.ExpenseReportTable.TABLE_NAME + " WHERE " + TableData.ExpenseReportTable.USER_EMAIL + " ='" + eEmail + "' AND " + TableData.ExpenseReportTable.ER_ID + "='" + erID + "'";

        Cursor c = SQ.rawQuery(selectQuery, null);

        Log.d("Result", String.valueOf(c));


        return c;
    }

    public Cursor DummDatas(DatabaseOperations dop,String eEmail) {

        SQLiteDatabase SQ = dop.getReadableDatabase();


        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

        //String selectQuery3 = "SELECT * FROM " + TableData.ExpensesTableList.TABLE_NAME + "WHERE" + TableData.ExpensesTableList.EXPENSES_ID + "=" + erID;

        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME + " WHERE " + TableData.ExpensesListTable.USER_EMAIL + " ='" + eEmail + "'";

        Cursor c = SQ.rawQuery(selectQuery, null);

        Log.d("Result", String.valueOf(c));


        return c;
    }

    public Cursor getMyExpenseReports(DatabaseOperations dop, String email) {

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery = "SELECT *FROM "+TableData.ExpenseReportTable.TABLE_NAME+" WHERE "+TableData.ExpenseReportTable.USER_EMAIL+" ='"+email+"'";
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;
    }


    public Cursor SelectDatafromListExpensesTable(DatabaseOperations dop, String email) {

        SQLiteDatabase SQ = dop.getReadableDatabase();

       // String my = "SELECT *FROM TNAME WHERE email ='email'";
        String selectQuery = "SELECT *FROM "+TableData.ExpensesListTable.TABLE_NAME+" WHERE "+TableData.ExpensesListTable.USER_EMAIL+" ='"+email+"'";

        //  String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";

       // String selectQuery = "SELECT * FROM " + TableData.ListofAnExpensesTable.TABLE_NAME + "WHERE" + TableData.ListofAnExpensesTable.EMPLOYEE_EMAIL + "=" + email;
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;
    }


    // check if there is datas in the table or not.. if yes then ... else....

    public Cursor getListofExpensesCount(DatabaseOperations dop, String eEmail, String erID) {


        SQLiteDatabase db = dop.getReadableDatabase();
        //  String selectQuery = "SELECT * FROM TABLENAME WHERE email ='email' AND id='erid'";

        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME + " WHERE " + TableData.ExpensesListTable.USER_EMAIL + " ='" + eEmail + "' AND " + TableData.ExpensesListTable.EL_ID + "='" + erID + "'";
           Log.d("SELECTQUERY",selectQuery);

       // String countQueryd = "SELECT  * FROM " + TableInfo_Survey_Answer.TABLE_NAME + " WHERE " + TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID + " = '" + questionnaireId + "'";


        Cursor c = db.rawQuery(selectQuery, null);
        return c;


    }


    public Cursor getUserElDatas(DatabaseOperations dop,String erid,String email){


        SQLiteDatabase db = dop.getReadableDatabase();


        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME + " WHERE " + TableData.ExpensesListTable.USER_EMAIL + " ='" + email + "' AND " + TableData.ExpensesListTable.ER_ID + "='" + erid + "'";
        Log.d("SELECTQUERY",selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }





    public Cursor getListofExpensesData(DatabaseOperations dop, String erID){

        SQLiteDatabase dp =dop.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TableData.ExpensesListTable.TABLE_NAME + " WHERE " + TableData.ExpensesListTable.EL_ID + " ="+ erID ;

        Cursor c = dp.rawQuery(selectQuery,null);

        return  c;

      //  SELECT * FROM list_of_an_expenses WHERE emailID ='ajay@netforce.co' AND expensesID='36598559-1937-4d43-9399-7f177d1a4ff7'

    }


    // ends here....
    public long insertExpensesDatas(DatabaseOperations dop, String expenseid, String fromdate, String todate, String creaationDate,
                                    String status, String title,String description,String useremail,String usertype,String userId,String customerID) {


        try {

            SQLiteDatabase sq = dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
         //   cv.put(TableData.ExpensesTableList.EXPENSES_ID, expenseid);
//            cv.put(TableData.ExpensesTableList.TITLE, title);
//            cv.put(TableData.ExpensesTableList.FROM_DATE, fromdate);
//            cv.put(TableData.ExpensesTableList.TO_DATE, todate);
//            cv.put(TableData.ExpensesTableList.CREATEION_DATE, creaationDate);
//            cv.put(TableData.ExpensesTableList.DESCRIPTION, description);
//            cv.put(TableData.ExpensesTableList.EMPLOYEE_EMAIL, useremail);
//            cv.put(TableData.ExpensesTableList.USER_TYPE, usertype);
//            cv.put(TableData.ExpensesTableList.STATUS, status);
//            cv.put(TableData.ExpensesTableList.CUSTOMER_ID, customerID);
//            cv.put(TableData.ExpensesTableList.USER_ID, userId);

// need to check here....

            long reslt = sq.insertWithOnConflict(TableData.ExpenseReportTable.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

            Log.d("InserData", String.valueOf(reslt));



            return reslt;

        } catch (Exception ex) {

            return -1;
        }
    }




    // get the datas from the databases.....


    public Cursor SelectFromExpenseTable(DatabaseOperations dop) {

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TableData.ExpenseReportTable.TABLE_NAME;
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;

    }



    public Cursor SelectAllData(DatabaseOperations dop,String userid){

        SQLiteDatabase SQ = dop.getReadableDatabase();


        String selectQuery = "SELECT " + TableData.ExpenseReportTable.TABLE_NAME + ".* ," +
                TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_SERIES +
                " FROM " + TableData.ExpenseReportTable.TABLE_NAME + " INNER JOIN " + TableData.ExpensesListTable.TABLE_NAME + " ON " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.USER_ID + " = " + TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_USER_ID +
                " WHERE " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.USER_ID + "= '" + userid + "'";

       Log.d("Result",selectQuery);
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;



    }

    public Cursor SelectAllDataSummary(DatabaseOperations dop,String erid){

        SQLiteDatabase SQ = dop.getReadableDatabase();


        String selectQuery = "SELECT " + TableData.ExpenseReportTable.TABLE_NAME + ".* ," +
                TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_SERIES +
                " FROM " + TableData.ExpenseReportTable.TABLE_NAME + " INNER JOIN " + TableData.ExpensesListTable.TABLE_NAME + " ON " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.USER_ID + " = " + TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_USER_ID +
                " WHERE " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.ER_ID + "= '" + erid + "'";




        Log.d("Result",selectQuery);
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;



    }

    public Cursor CheckIsFirstTimeSummary(DatabaseOperations dop,String erid){

        SQLiteDatabase SQ = dop.getReadableDatabase();

//
//        String selectQuery = "SELECT " + TableData.ExpenseReportTable.TABLE_NAME + ".* ," +
//                TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_SERIES +
//                " FROM " + TableData.ExpenseReportTable.TABLE_NAME + " INNER JOIN " + TableData.ExpensesListTable.TABLE_NAME + " ON " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.USER_ID + " = " + TableData.ExpensesListTable.TABLE_NAME + "." + TableData.ExpensesListTable.EL_USER_ID +
//                " WHERE " + TableData.ExpenseReportTable.TABLE_NAME + "." + TableData.ExpenseReportTable.ER_ID + "= '" + erid + "'";
//

        String selectQuery = "SELECT * FROM "+TableData.ExpensesListTable.TABLE_NAME+" WHERE "+ TableData.ExpensesListTable.ER_ID + " = '"+ erid +"'";
        Log.d("Result",selectQuery);
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;



    }


    public Cursor getEditExpensesReportData(DatabaseOperations dop,String erid){

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+TableData.ExpenseReportTable.TABLE_NAME+" WHERE "+ TableData.ExpenseReportTable.ER_ID + " = '"+ erid +"'";
        Log.d("Result",selectQuery);
        Cursor c = SQ.rawQuery(selectQuery, null);
        return c;



    }







//    public void getVersion(DatabaseOperations dop) {
//
//
//        SQLiteDatabase SQ = dop.getReadableDatabase();
//        String selectQuery = "SELECT " + TableData.ExpensesTableList.DB_VERSION + " FROM " +
//                TableData.ExpensesTableList.TABLE_NAME;
//        Cursor c = SQ.rawQuery(selectQuery, null);
//        int version;
//        if (c.moveToFirst()) {
//          //  version = c.getString(c.getColumnIndex(String.valueOf(TableData.ExpensesTableList.DB_VERSION)));
//            version=c.getInt(c.getColumnIndex(String.valueOf(TableData.ExpensesTableList.DB_VERSION)));
//
//            if(version<currentVersion){
//
//
//            }
//
//        }
//
//
//    }


    //extends SQLiteOpenHelper {

   /*  public static final int database_version = 1;
     public String CREATE_TABLE_LIST_OF_SURVEY = "CREATE TABLE IF NOT EXISTS " + TableInfo_ListOf_Survey.TABLE_NAME + "("
	    + TableInfo_ListOf_Survey.QUESTIONNAIRE_ID + " TEXT PRIMARY KEY,"
	    + TableInfo_ListOf_Survey.CREATEION_DATE + " TEXT,"
	    + TableInfo_ListOf_Survey.VERSION + " TEXT,"
	    + TableInfo_ListOf_Survey.LAST_UPDATED + " TEXT ,"
	    + TableInfo_ListOf_Survey.UNIQUE_CODE + " TEXT ,"
	    + TableInfo_ListOf_Survey.COMPANY_ID + " TEXT ,"
	    + TableInfo_ListOf_Survey.AGENT_ID + " TEXT ,"
	    + TableInfo_ListOf_Survey.SCREEN_TYPE + " TEXT ,"
	    + TableInfo_ListOf_Survey.STATUS + " TEXT ,"
	    + TableInfo_ListOf_Survey.TITLE + " TEXT ,"
	    + TableInfo_ListOf_Survey.FONT + " TEXT ,"
	    + TableInfo_ListOf_Survey.MESSAGE_TYPE + " TEXT ,"
	    + TableInfo_ListOf_Survey.COMPLETION_TEXT + " TEXT );";

     public String CREATE_TABLE_SURVEY_QUESTION = "CREATE TABLE IF NOT EXISTS " + TableInfo_Survey_Question.TABLE_NAME + "("
	    + TableInfo_Survey_Question.QUESTION_ID + " TEXT PRIMARY KEY,"
	    + TableInfo_Survey_Question.UNIQUE_CODE + " TEXT,"
	    + TableInfo_Survey_Question.CREATION_DATE + " TEXT,"
	    + TableInfo_Survey_Question.LAST_UPDATE + " TEXT,"
	    + TableInfo_Survey_Question.QUESTIONNAIRE_ID + " TEXT,"
	    + TableInfo_Survey_Question.STATUS + " TEXT,"
	    + TableInfo_Survey_Question.REQUIRED + " TEXT,"
	    + TableInfo_Survey_Question.OTHER + " TEXT,"
	    + TableInfo_Survey_Question.QUESTION_TYPE + " TEXT,"
	    + TableInfo_Survey_Question.FIELD_TYPE + " TEXT,"
	    + TableInfo_Survey_Question.QUESTION + " TEXT,"
	    + TableInfo_Survey_Question.OPTION1 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION2 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION3 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION4 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION5 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION6 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION7 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION8 + " TEXT,"
	    + TableInfo_Survey_Question.OPTION9 + " TEXT,"
	    + TableInfo_Survey_Question.DISPLAY_ORDER + " INTEGER );";


     public String CREATE_TABLE_SURVEY_ANSWER = "CREATE TABLE IF NOT EXISTS " + TableInfo_Survey_Answer.TABLE_NAME + "("
	    + TableInfo_Survey_Answer.UNIQUE_USER_ID + " TEXT PRIMARY KEY,"
	    + TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID + " TEXT,"
	    + TableInfo_Survey_Answer.RESPONSE_JSONSTRING + " TEXT );";

     public String CREATE_TABLE_FIELD_TABLE = "CREATE TABLE IF NOT EXISTS " + Table_Info_Field_Table.TABLE_NAME + "("
	    + Table_Info_Field_Table.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + Table_Info_Field_Table.FIELD_NAME + " TEXT UNIQUE);";

     public String CREATE_TABLE_QUESTIONNAIRE_FIELD_ASSOCIATION = "CREATE TABLE IF NOT EXISTS " + Table_Info_Questionnaire_Field_Association.TABLE_NAME + "("
	    + Table_Info_Questionnaire_Field_Association.QUESTIONNAIRE_ID + " TEXT,"
	    + Table_Info_Questionnaire_Field_Association.FIELD_ID + " INTEGER,"
	    + Table_Info_Questionnaire_Field_Association.REQUIRED + " TEXT,"
	    + Table_Info_Questionnaire_Field_Association.FIELD_TYPE + " TEXT,"
	    + Table_Info_Questionnaire_Field_Association.OPTION_VALUE + " TEXT,"
	    + Table_Info_Questionnaire_Field_Association.ORDER + " INTEGER,"
	    + "FOREIGN KEY(" + Table_Info_Questionnaire_Field_Association.FIELD_ID + ") REFERENCES " + Table_Info_Field_Table.TABLE_NAME + "(" + Table_Info_Field_Table.FIELD_ID + "),"
	    + "PRIMARY KEY (" + Table_Info_Questionnaire_Field_Association.QUESTIONNAIRE_ID + "," + Table_Info_Questionnaire_Field_Association.FIELD_ID + ") );";

     public String CREATE_TABLE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " +
	    TableData.Table_Info_Notification.TABLE_NAME + "("
	    + TableData.Table_Info_Notification.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + TableData.Table_Info_Notification.KEY_TITLE + " TEXT,"
	    + TableData.Table_Info_Notification.KEY_MESAGE + " TEXT,"
	    + TableData.Table_Info_Notification.KEY_DESCRIPTION + " TEXT,"
	    + TableData.Table_Info_Notification.KEY_URL + " TEXT );";


     public DatabaseOperations(Context context) {
	    super(context, TableInfo_ListOf_Survey.DATABASE_NAME, null, database_version);
	    Log.d("database operation", "database created");

	    // TODO Auto-generated constructor stub
     }

     @Override
     public void onOpen(SQLiteDatabase db) {
	    // TODO Auto-generated method stub
	    super.onOpen(db);
	    db.execSQL(CREATE_TABLE_LIST_OF_SURVEY);
	    db.execSQL(CREATE_TABLE_SURVEY_QUESTION);
	    db.execSQL(CREATE_TABLE_SURVEY_ANSWER);
	    db.execSQL(CREATE_TABLE_FIELD_TABLE);
	    db.execSQL(CREATE_TABLE_QUESTIONNAIRE_FIELD_ASSOCIATION);
	    db.execSQL(CREATE_TABLE_NOTIFICATION);
	    //  db.execSQL(CREATE_QUERY1);
     }

     @Override
     public void onCreate(SQLiteDatabase sdb) {
	    // TODO Auto-generated method stub
	    sdb.execSQL(CREATE_TABLE_LIST_OF_SURVEY);
	    sdb.execSQL(CREATE_TABLE_SURVEY_QUESTION);
	    sdb.execSQL(CREATE_TABLE_SURVEY_ANSWER);
	    sdb.execSQL(CREATE_TABLE_FIELD_TABLE);
	    sdb.execSQL(CREATE_TABLE_QUESTIONNAIRE_FIELD_ASSOCIATION);
	    sdb.execSQL(CREATE_TABLE_NOTIFICATION);

	    //  sdb.execSQL(CREATE_QUERY1);

	    Log.d("database operation", "table created");
     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    // TODO Auto-generated method stub


	    db.delete(TableInfo_ListOf_Survey.TABLE_NAME, null, null);
	    db.delete(TableInfo_Survey_Question.TABLE_NAME, null, null);
	    db.delete(TableInfo_Survey_Answer.TABLE_NAME, null, null);
	    db.delete(Table_Info_Field_Table.TABLE_NAME, null, null);
	    db.delete(Table_Info_Questionnaire_Field_Association.TABLE_NAME, null, null);
	    db.delete(TableData.Table_Info_Notification.TABLE_NAME, null, null);
	    //    db.delete(TableInfo.TABLE_NAME_CATEGORY, null, null);
	    //  db.execSQL("delete from " + TableInfo.TABLE_NAME);
	    // db.execSQL("delete from " + TableInfo.TABLE_NAME_CATEGORY);
     }

     public long insertListOfSurveyQuestion(com.survtapp.database.DatabaseOperations dop,
							  String questionnaireID,
							  String creationdate,
							  String version,
							  String lastupdate,
							  String uniquecode,
							  String companyID,
							  String agentID,
							  String screen_type,
							  String status,
							  String title,
							  String font,
							  String message_type,
							  String completion_text) {
	    try {
		   completion_text = completion_text.replace("'", "''");
		   title = title.replace("'", "''");
		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(TableInfo_ListOf_Survey.QUESTIONNAIRE_ID, questionnaireID);
		   cv.put(TableInfo_ListOf_Survey.CREATEION_DATE, creationdate);
		   cv.put(TableInfo_ListOf_Survey.VERSION, version);
		   cv.put(TableInfo_ListOf_Survey.LAST_UPDATED, lastupdate);
		   cv.put(TableInfo_ListOf_Survey.UNIQUE_CODE, uniquecode);
		   cv.put(TableInfo_ListOf_Survey.COMPANY_ID, companyID);
		   cv.put(TableInfo_ListOf_Survey.AGENT_ID, agentID);
		   cv.put(TableInfo_ListOf_Survey.SCREEN_TYPE, screen_type);
		   cv.put(TableInfo_ListOf_Survey.STATUS, status);
		   cv.put(TableInfo_ListOf_Survey.TITLE, title);
		   cv.put(TableInfo_ListOf_Survey.FONT, font);
		   cv.put(TableInfo_ListOf_Survey.MESSAGE_TYPE, message_type);
		   cv.put(TableInfo_ListOf_Survey.COMPLETION_TEXT, completion_text);
		   long k = SQ.insertWithOnConflict(TableInfo_ListOf_Survey.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		   Log.d("database operation", "one row inserted");
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }
     }

     public String getVersion(DatabaseOperations dop, String questionnaireId) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT " + TableInfo_ListOf_Survey.VERSION + " FROM " +
		   TableInfo_ListOf_Survey.TABLE_NAME + " " +
		   "WHERE " + TableInfo_ListOf_Survey.QUESTIONNAIRE_ID + " = '" + questionnaireId + "'";
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    String str = "";
	    if (c.moveToFirst()) {
		   str = c.getString(c.getColumnIndex(TableInfo_ListOf_Survey.VERSION));
	    }

	    return str;

     }

     public Cursor getListOfSurvey(DatabaseOperations dop, String company_id) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + TableInfo_ListOf_Survey.TABLE_NAME + " WHERE " + TableInfo_ListOf_Survey.COMPANY_ID + " = '" + company_id + "' AND " + TableInfo_ListOf_Survey.STATUS + "= 'A'";
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    return c;

     }

     public long insertSurveyQuestion(com.survtapp.database.DatabaseOperations dop,
						  String questionID,
						  String uniquecode,
						  String creationdate,
						  String lastupdate,
						  String questionnaireID,
						  String status,
						  String required,
						  String other,
						  String question_type,
						  String field_type,
						  String question,
						  String option1,
						  String option2,
						  String option3,
						  String option4,
						  String option5,
						  String option6,
						  String option7,
						  String option8,
						  String option9,
						  int display_order) {
	    try {
		   question_type = question_type.replace("'", "''");
		   question = question.replace("'", "''");
		   field_type = field_type.replace("'", "''");
		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(TableInfo_Survey_Question.QUESTION_ID, questionID);
		   cv.put(TableInfo_Survey_Question.UNIQUE_CODE, uniquecode);
		   cv.put(TableInfo_Survey_Question.CREATION_DATE, creationdate);
		   cv.put(TableInfo_Survey_Question.LAST_UPDATE, lastupdate);
		   cv.put(TableInfo_Survey_Question.QUESTIONNAIRE_ID, questionnaireID);
		   cv.put(TableInfo_Survey_Question.STATUS, status);
		   cv.put(TableInfo_Survey_Question.REQUIRED, required);
		   cv.put(TableInfo_Survey_Question.OTHER, other);
		   cv.put(TableInfo_Survey_Question.QUESTION_TYPE, question_type);
		   cv.put(TableInfo_Survey_Question.FIELD_TYPE, field_type);
		   cv.put(TableInfo_Survey_Question.QUESTION, question);
		   cv.put(TableInfo_Survey_Question.OPTION1, option1);
		   cv.put(TableInfo_Survey_Question.OPTION2, option2);
		   cv.put(TableInfo_Survey_Question.OPTION3, option3);
		   cv.put(TableInfo_Survey_Question.OPTION4, option4);
		   cv.put(TableInfo_Survey_Question.OPTION5, option5);
		   cv.put(TableInfo_Survey_Question.OPTION6, option6);
		   cv.put(TableInfo_Survey_Question.OPTION7, option7);
		   cv.put(TableInfo_Survey_Question.OPTION8, option8);
		   cv.put(TableInfo_Survey_Question.OPTION9, option9);
		   cv.put(TableInfo_Survey_Question.DISPLAY_ORDER, display_order);
		   long k = SQ.insertWithOnConflict(TableInfo_Survey_Question.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		   Log.d("database operation", "one row inserted");
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }
     }

     public Cursor getSurveyQuestions(com.survtapp.database.DatabaseOperations dop, String questionnaireID) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + TableInfo_Survey_Question.TABLE_NAME + " WHERE " + TableInfo_Survey_Question.QUESTIONNAIRE_ID + " = " + questionnaireID + " ORDER BY " + TableInfo_Survey_Question.QUESTION_ID + " ASC";
	    Cursor c = SQ.rawQuery(selectQuery, null);

	    return c;

     }


     public long insertSurveyAnswer(com.survtapp.database.DatabaseOperations dop,
						String uniqueUserId,
						String questionnaire_id,
						String response_jsonString) {
	    try {

		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(TableInfo_Survey_Answer.UNIQUE_USER_ID, uniqueUserId);
		   cv.put(TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID, questionnaire_id);
		   cv.put(TableInfo_Survey_Answer.RESPONSE_JSONSTRING, response_jsonString);
		   long k = SQ.insertWithOnConflict(TableInfo_Survey_Answer.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		   Log.d("database operation", "one row inserted");
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }

     }

     public boolean deleteAnswer(String uniqueUserId) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    return db.delete(TableInfo_Survey_Answer.TABLE_NAME, TableInfo_Survey_Answer.UNIQUE_USER_ID + "=" + uniqueUserId, null) > 0;
     }


     public boolean deleteAnswerByQuestionnaireId(String questionnaireId) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    return db.delete(TableInfo_Survey_Answer.TABLE_NAME, TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID + "=" +
		   questionnaireId, null) > 0;
     }

     public boolean deleteAllAnswer() {
	    SQLiteDatabase db = this.getWritableDatabase();
	    return db.delete(TableInfo_Survey_Answer.TABLE_NAME, null, null) > 0;
     }

     public boolean deleteAllSurvey() {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TableInfo_ListOf_Survey.TABLE_NAME, null, null);
	    db.delete(Table_Info_Field_Table.TABLE_NAME, null, null);
	    db.delete(Table_Info_Questionnaire_Field_Association.TABLE_NAME, null, null);
	    db.delete(TableData.TableInfo_Survey_Answer.TABLE_NAME, null, null);
	    return true;
     }


     public int getUnsentCount(String questionnaireId) {
	    String countQuery = "SELECT  * FROM " + TableInfo_Survey_Answer.TABLE_NAME + " WHERE " + TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID + " = '" + questionnaireId + "'";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    cursor.close();
	    return cnt;

     }

     public Cursor getUnsentSurveyById(String questionnaireId) {
	    String countQuery = "SELECT  * FROM " + TableInfo_Survey_Answer.TABLE_NAME + " WHERE " + TableInfo_Survey_Answer.UNIQUE_QUESTIONNAIRE_ID + " = '" + questionnaireId + "'";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    return cursor;

     }

     public Cursor getSurveyAnswer(com.survtapp.database.DatabaseOperations dop) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + TableInfo_Survey_Answer.TABLE_NAME;
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    return c;

     }

     public long insertFieldTable(com.survtapp.database.DatabaseOperations dop,
					    String field_name) {
	    try {
		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(Table_Info_Field_Table.FIELD_NAME, field_name);
		   long k = SQ.insertWithOnConflict(Table_Info_Field_Table.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
		   int i = getFieldId(dop, field_name);
		   Log.i("field id", field_name + i);
		   Log.d("database operation", "one row inserted");
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }

     }

     public Cursor getFieldTable(com.survtapp.database.DatabaseOperations dop) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + Table_Info_Field_Table.TABLE_NAME;
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    return c;

     }

     public int getFieldId(com.survtapp.database.DatabaseOperations dop, String field_name) {

	    field_name = field_name.replace("'", "''");
	    int id;
	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT " + Table_Info_Field_Table.FIELD_ID + " FROM " + Table_Info_Field_Table.TABLE_NAME + " WHERE " + Table_Info_Field_Table.FIELD_NAME + " = '" + field_name + "'";
	    //String selectQuery = "SELECT " +  "* FROM " + Table_Info_Field_Table.TABLE_NAME ;
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    if (c.moveToFirst()) {
		   id = c.getInt(c.getColumnIndex(Table_Info_Field_Table.FIELD_ID));
	    } else {
		   id = -1;
	    }
	    return id;

     }

     public long insertQuestionnaire_Field_Association(com.survtapp.database.DatabaseOperations dop,
									 String questionnaire_id,
									 int field_id,
									 String required,
									 String field_type,
									 String option_value,
									 int order) {
	    try {
		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(Table_Info_Questionnaire_Field_Association.QUESTIONNAIRE_ID, questionnaire_id);
		   cv.put(Table_Info_Questionnaire_Field_Association.FIELD_ID, field_id);
		   cv.put(Table_Info_Questionnaire_Field_Association.REQUIRED, required);
		   cv.put(Table_Info_Questionnaire_Field_Association.FIELD_TYPE, field_type);
		   cv.put(Table_Info_Questionnaire_Field_Association.OPTION_VALUE, option_value);
		   cv.put(Table_Info_Questionnaire_Field_Association.ORDER, order);
		   long k = SQ.insertWithOnConflict(Table_Info_Questionnaire_Field_Association.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		   Log.d("database operation", "one row inserted:" + questionnaire_id);
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }

     }

     public Cursor getQuestionnaireFieldAssociation(com.survtapp.database.DatabaseOperations dop, String questionnaireId) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT " + Table_Info_Questionnaire_Field_Association.TABLE_NAME + ".* ," +
		   Table_Info_Field_Table.TABLE_NAME + "." + Table_Info_Field_Table.FIELD_NAME +
		   " FROM " + Table_Info_Questionnaire_Field_Association.TABLE_NAME + " INNER JOIN " +
		   Table_Info_Field_Table.TABLE_NAME + " ON " + Table_Info_Questionnaire_Field_Association.TABLE_NAME + "." + Table_Info_Field_Table.FIELD_ID + " = " + Table_Info_Field_Table.TABLE_NAME + "." + Table_Info_Questionnaire_Field_Association.FIELD_ID +
		   " WHERE " + Table_Info_Questionnaire_Field_Association.TABLE_NAME + "." + Table_Info_Questionnaire_Field_Association.QUESTIONNAIRE_ID + "= '" + questionnaireId

		   + "' ORDER BY " + Table_Info_Questionnaire_Field_Association.ORDER + " ASC";

	    Log.i("database join", selectQuery);
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    return c;

     }

     public long insertNotification(com.survtapp.database.DatabaseOperations dop,
						String title,
						String message,
						String description,
						String url) {
	    try {

		   SQLiteDatabase SQ = dop.getWritableDatabase();
		   ContentValues cv = new ContentValues();
		   cv.put(TableData.Table_Info_Notification.KEY_TITLE, title);
		   cv.put(TableData.Table_Info_Notification.KEY_MESAGE, message);
		   cv.put(TableData.Table_Info_Notification.KEY_DESCRIPTION, description);
		   cv.put(TableData.Table_Info_Notification.KEY_URL, url);
		   long k = SQ.insertWithOnConflict(TableData.Table_Info_Notification.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
		   Log.d("database operation", "one row inserted");
		   return k;
	    } catch (Exception ex) {
		   return -1;
	    }

     }

     public Cursor getNotification(com.survtapp.database.DatabaseOperations dop) {

	    SQLiteDatabase SQ = dop.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + TableData.Table_Info_Notification.TABLE_NAME +
		   " ORDER BY " + TableData.Table_Info_Notification.KEY_ID + " DESC";
	    Cursor c = SQ.rawQuery(selectQuery, null);
	    return c;

     }*/
}
