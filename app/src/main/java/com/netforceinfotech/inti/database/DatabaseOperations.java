/*
 * This class contain necessary database operation e.g: update data, remove data etc... needed in application
 */

package com.netforceinfotech.inti.database;

public class DatabaseOperations {//extends SQLiteOpenHelper {

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
