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

	public static abstract class UsersTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "user";
		public static final String USER_ID = "userId";
		public static final String USER_EMAIL= "userEmail";
		public static final String BASE_CURRENCY= "baseCurrency";
		public static final String USER_NAME= "userName";
		public static final String CUSTOMER_ID= "customerId";
		public static final String USER_CONTACT ="userContact";
		public static final String USER_PROFILE ="userProfile";
		public static final String USER_CREATION_DATE="createdDate";


	}

	public static abstract class SuperVisorTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "supervisors";
		public static final String ID = "id";
		public static final String USER_ID = "userId";
		public static final String SUPERVISOR_NAME ="supervisorname";
		public static final String SUPERVISOR_ID ="supervisorid";


	}



	public static abstract class CategoryTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "category";
		public static final String ID = "id";
		public static final String USER_ID = "userId";
		public static final String CUSTOMER_ID= "customerid";
		public static final String CATEGORY_NAME= "categoryname";
		public static final String CATEGORY_ID= "categoryid";



	}

	// User Currency Table...

	public static abstract class CurrencyTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "Currency";
		public static final String ID = "id";
		public static final String USER_ID = "userId";
		public static final String CUSTOMER_ID ="customerId";
		public static final String CURRENCY_NAME= "currencyName";


	}

	// Supplier table....

	public static abstract class SupplierTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "Supplier";
		public static final String ID = "id";
		public static final String USER_ID = "userId";
		public static final String CUSTOMER_ID = "customerId";
		public static final String SUPPLIER_ID ="supplierId";
		public static final String SUPPLIER_NAME= "SupplierName";

	}

	//
	public static abstract class SupplierDetailTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "SupplierDetail";
		public static final String ID = "id";
		public static final String CUSTOMER_ID = "customerId";
		public static final String SUPPLIER_ID ="supplierid";
		public static final String SUPPLIER_NAME= "SupplierName";
		public static final String SUPPLIER_IDENTIFIER ="supplierIdentifier";


	}


	public static abstract class CostCenterTable implements BaseColumns {

		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "CostCenter";
		public static final String ID = "id";
		public static final String CUSTOMER_ID ="customerId";
		public static final String COST_CENTER_NAME= "CostCenter";

	}


	public static abstract class DocTypeTable implements BaseColumns {


		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "Doctype";
		public static final String ID = "id";
		public static final String CUSTOMER_ID ="customerId";
		public static final String DOC_NAME= "docName";

	}

	public static abstract class ProjectTable implements BaseColumns {


		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "Project";
		public static final String ID = "id";
		public static final String CUSTOMER_ID ="customerId";
		public static final String PROJECT_NAME= "projectName";

	}


	public static abstract class TaxTable implements BaseColumns {


		public static final String DATABASE_NAME = "inti_database";
		public static final String TABLE_NAME = "Tax";
		public static final String ID = "id";
		public static final String CUSTOMER_ID ="customerId";
		public static final String TAX_RATE = "taxrate";
		public static final String TAX_NAME= "taxname";

	}
// 13 tables are here.. it may suck...













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