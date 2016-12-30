/*
 * this class contain information of database 
 */
package com.netforceinfotech.inti.database;

import android.provider.BaseColumns;

import static java.sql.Types.INTEGER;

public class TableData {

	public TableData() {

	}

	// to create the expenses report for each business trip..


	public static abstract class ExpensesTableList implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "list_of_expenses";
		public static final String EXPENSES_ID = "expensesID";
		public static final String EMPLOYEE_EMAIL = "emailID";
		public static final String USER_TYPE= "usertype";
		public static final String CREATEION_DATE = "creationdate";
		public static final String STATUS = "status";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String FROM_DATE = "fromdate";
		public static final String TO_DATE = "todate";
		public static final String USER_ID = "user_id";
		public static final String CUSTOMER_ID = "customer_id";



	}



	public static abstract class ListofAnExpensesTable implements BaseColumns {
		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "list_of_an_expenses";
		public static final String EXPENSES_ID = "expensesID";
		public static final String EMPLOYEE_EMAIL = "emailID";
		public static final String USER_TYPE= "usertype";
		public static final String CREATEION_DATE = "creationdate";
		public static final String EXPENSES_IMAGE_URL="imageUrl";
		public static final String EXPENSE_DATE= "expenseDate";
		public static final String CURRENCY_CODE= "currencyCode";
		public static final String ORIGINAL_AMOUNT= "originalAmount";
		public static final String EXCHANGE_RATE= "exchangeRate";
		public static final String CONVERTED_AMOUNT= "convertedAmount";
		public static final String EXPENSES_DESCRIPTION= "expenseDescription";
		public static final String EXPENSES_CATEGORY= "expenseCategory";
		public static final String EXPENSESRUC= "expenseRUC";
		public static final String EXPNSES_PROVIDER= "expenseProvider";
		public static final String EXPENSECOST_CENTER= "expenseCostCenter";
		public static final String EXPENSES_DOCUMENT_TYPE= "expensesDocumentType";
		public static final String EXPENSES_SERIES= "expenseSeries";
		public static final String EXPENSES_NUMBER_OF_DOCS= "expenseNumberofDocs";
		public static final String EXPENSE_DRAFT= "expenseDraft";
		public static final String EXPENSE_TAX_RATE = "expenseTaxRate";
		public static final String EXPENSE_IGV = "expenseIGV";
		public static final String TITLE = "title";
		public static final String BILLABLE = "billable";
		public static final String ER_DESCRIPTION = "erDescription";
		public static final String ERFROMDATE = "erFromDate";
		public static final String ERTODATE = "erToDate";
		public static final String ERSTATUS = "erStatus";
		public static final String ERLISTID = "erListID";




	}









//
//     public static abstract class TableInfo_ListOf_Survey implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "list_of_survey";
//	    public static final String QUESTIONNAIRE_ID = "questionnaireID";
//	    public static final String VERSION = "version";
//	    public static final String CREATEION_DATE = "creationdate";
//	    public static final String LAST_UPDATED = "lastupdate";
//	    public static final String UNIQUE_CODE = "uniquecode";
//	    public static final String COMPANY_ID = "companyID";
//	    public static final String AGENT_ID = "agentID";
//	    public static final String SCREEN_TYPE = "screen_type";
//	    public static final String STATUS = "status";
//	    public static final String TITLE = "title";
//	    public static final String FONT = "font";
//	    public static final String MESSAGE_TYPE = "message_type";
//	    public static final String COMPLETION_TEXT = "completion_text";
//
//     }
//
//
//     public static abstract class TableInfo_Survey_Question implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "survey_question";
//	    public static final String QUESTION_ID = "questionID";
//	    public static final String UNIQUE_CODE = "uniquecode";
//	    public static final String CREATION_DATE = "creationdate";
//	    public static final String LAST_UPDATE = "lastupdate";
//	    public static final String QUESTIONNAIRE_ID = "questionnaireID";
//	    public static final String STATUS = "status";
//	    public static final String REQUIRED = "required";
//	    public static final String OTHER = "other";
//	    public static final String QUESTION_TYPE = "question_type";
//	    public static final String FIELD_TYPE = "field_type";
//	    public static final String QUESTION = "question";
//	    public static final String OPTION1 = "option1";
//	    public static final String OPTION2 = "option2";
//	    public static final String OPTION3 = "option3";
//	    public static final String OPTION4 = "option4";
//	    public static final String OPTION5 = "option5";
//	    public static final String OPTION6 = "option6";
//	    public static final String OPTION7 = "option7";
//	    public static final String OPTION8 = "option8";
//	    public static final String OPTION9 = "option9";
//	    public static final String DISPLAY_ORDER = "display_order";
//
//     }
//
//     public static abstract class TableInfo_Survey_Answer implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "survey_answer";
//	    public static final String UNIQUE_USER_ID = "uniqueUserId";
//	    public static final String UNIQUE_QUESTIONNAIRE_ID = "questionnaire_id";
//
//	    public static final String RESPONSE_JSONSTRING = "response";
//
//	    ;
//
//     }
//
//     public static abstract class Table_Info_Field_Table implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "field_table";
//	    public static final String FIELD_ID = "field_id";
//	    public static final String FIELD_NAME = "field_name";
//	    ;
//
//     }
//
//     public static abstract class Table_Info_Questionnaire_Field_Association implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "questionnaire_field_association";
//	    public static final String QUESTIONNAIRE_ID = "questionnaire_id";
//	    public static final String FIELD_ID = "field_id";
//	    public static final String REQUIRED = "required";
//	    public static final String FIELD_TYPE = "field_type";
//	    public static final String OPTION_VALUE = "option_value";
//	    public static final String ORDER = "display_order";
//
//	    ;
//
//     }
//     public static abstract class Table_Info_Notification implements BaseColumns {
//	    public static final String DATABASE_NAME = "survtapp";
//	    public static final String TABLE_NAME = "notification_table";
//	    public static final String KEY_ID = "_id";
//	    public static final String KEY_TITLE = "_title";
//	    public static final String KEY_MESAGE = "_message";
//	    public static final String KEY_DESCRIPTION = "_description";
//	    public static final String KEY_URL = "_url";
//
//
//     }


}