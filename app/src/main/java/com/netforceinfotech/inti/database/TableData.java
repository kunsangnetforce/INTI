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


	public static abstract class ExpenseReportTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_Database";
		public static final String TABLE_NAME = "ExpenseReport";
		public static final String ER_ID = "erID";
		public static final String USER_EMAIL = "userEmail";
		public static final String USER_TYPE= "userType";
		public static final String ER_CREATION_DATE = "erCreationDate";
		public static final String ER_STATUS = "erStatus";
		public static final String ER_NAME = "erName";
		public static final String ER_DESCRIPTION = "erDescription";
		public static final String ER_FROM_DATE = "erFromDate";
		public static final String ER_TO_DATE = "erToDate";
		public static final String USER_ID = "userId";
		public static final String CUSTOMER_ID = "customerId";
		public static final String ER_IS_OFFLINE = "erIsOffline";
		public static final String ER_IS_REQUESTED = "erIsRequested";
		public static final String ER_TOTAL_COST = "erTotalCost";

	}



	public static abstract class ExpensesListTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "ExpensesList";
		public static final String EL_ID = "elId";
		public static final String ER_ID = "erId";
		public static final String USER_EMAIL = "userEmail";
		public static final String EL_CREATION_DATE = "erCreationDate";
		public static final String EL_IMAGE_URL="elImageUrl";
		public static final String EL_DATE= "elDate";
		public static final String EL_CURRENCY_CODE= "elCurrencyCode";
		public static final String EL_ORIGINAL_AMOUNT= "elOriginalAmount";
		public static final String EL_EXCHANGE_RATE= "elExchangeRate";
		public static final String EL_CONVERTED_AMOUNT= "elConvertedAmount";
		public static final String EL_DESCRIPTION= "elDescription";
		public static final String EL_CATEGORY= "elCategory";
		public static final String EL_RUC= "elRuc";
		public static final String EL_PROVIDER= "elProvider";
		public static final String EL_COST_CENTER= "elCostCenter";
		public static final String EL_DOCUMENT_TYPE= "elDocumentType";
		public static final String EL_SERIES= "elSeries";
		public static final String EL_NUMBER_OF_DOCS= "elNumberofDocs";
		public static final String EL_DRAFT= "elDraft";
		public static final String EL_TAX_RATE = "elTaxRate";
		public static final String EL_IGV = "elIGV";
		public static final String EL_BILLABLE = "elBillable";
		public static final String EL_USER_ID ="UserId";

	}


	public static abstract class ExpenseStatusTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "ExpensesStatus";
		public static final String ES_ID = "esId";
		public static final String ES_ER_ID = "erId";
		public static final String ES_SV1= "elExchangeRate";
		public static final String ES_SV2= "elConvertedAmount";
		public static final String ES_SV3= "elDescription";
		public static final String ES_SV1_ID= "elCategory";
		public static final String ES_SV2_ID= "elRuc";
		public static final String ES_SV3_ID= "elProvider";
		public static final String ER_STATUS= "elCostCenter";
		public static final String EL_USER_ID ="UserId";
		public static final String EL_CUSTOMER_ID ="CustomerID";

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