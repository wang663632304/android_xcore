package by.istin.android.xcore.test;

import java.io.BufferedReader;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.test.ApplicationTestCase;
import by.istin.android.xcore.CoreApplication;
import by.istin.android.xcore.db.DBHelper;
import by.istin.android.xcore.processor.impl.AbstractGsonProcessor;
import by.istin.android.xcore.processor.impl.GsonArrayContentValuesProcessor;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.source.impl.http.HttpAndroidDataSource;
import by.istin.android.xcore.test.bo.DialogsResponse;
import by.istin.android.xcore.test.bo.FriendsResponse;
import by.istin.android.xcore.test.vk.Attachment;
import by.istin.android.xcore.test.vk.Dialog;
import by.istin.android.xcore.test.vk.FwdMessage;
import by.istin.android.xcore.test.vk.User;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TestDatasourceService extends ApplicationTestCase<CoreApplication> {

	public TestDatasourceService() {
		super(CoreApplication.class);
	}

	@Override
	protected void setUp() throws Exception {
		createApplication();
		super.setUp();
	}
	
	
	public void testGetFriends() throws Exception {
		HttpAndroidDataSource httpAndroidDataSource = new HttpAndroidDataSource();
		DataSourceRequest dataSourceRequest = new DataSourceRequest("https://dl.dropboxusercontent.com/u/16403954/xcore/getFriends.json");
		InputStream inputStream = httpAndroidDataSource.getSource(dataSourceRequest);
		GsonArrayContentValuesProcessor gsonArrayContentValuesProcessor = new GsonArrayContentValuesProcessor(User.class){

			@Override
			protected ContentValues[] process(Gson gson,
					BufferedReader bufferedReader) {
				return gson.fromJson(bufferedReader, FriendsResponse.class).getResults();
			}
			
		};
		ContentValues[] contentValues = gsonArrayContentValuesProcessor.execute(dataSourceRequest, httpAndroidDataSource, inputStream);
		DBHelper dbHelper = new DBHelper(getApplication());
		dbHelper.createTablesForModels(User.class);
		dbHelper.updateOrInsert(User.class, contentValues);
	}
	
	public void testGetDialogs() throws Exception {
		HttpAndroidDataSource httpAndroidDataSource = new HttpAndroidDataSource();
		DataSourceRequest dataSourceRequest = new DataSourceRequest("https://dl.dropboxusercontent.com/u/16403954/xcore/getDialogs.json");
		InputStream inputStream = httpAndroidDataSource.getSource(dataSourceRequest);
		AbstractGsonProcessor<DialogsResponse> gsonArrayContentValuesProcessor = new AbstractGsonProcessor<DialogsResponse>(Dialog.class, DialogsResponse.class) {

			@Override
			public void cache(Context context, DataSourceRequest dataSourceRequest, DialogsResponse result) {
				
			}
			
			@Override
			protected DialogsResponse process(Gson gson,
					BufferedReader bufferedReader) {
				JsonObject jsonObject = getGson().fromJson(bufferedReader, JsonObject.class);
				JsonArray dialogsJsonArray = jsonObject.get("response").getAsJsonObject().getAsJsonArray("dialogs");
				JsonArray usersJsonArray = jsonObject.get("response").getAsJsonObject().getAsJsonArray("users");
				Gson dialogGson = createGsonWithContentValuesAdapter(Dialog.class);
				ContentValues[] dialogs = dialogGson.fromJson(dialogsJsonArray, ContentValues[].class);
				Gson usersGson = createGsonWithContentValuesAdapter(User.class);
				ContentValues[] users = usersGson.fromJson(usersJsonArray, ContentValues[].class);
				DialogsResponse dialogsResponse = new DialogsResponse();
				dialogsResponse.setDialogs(dialogs);
				dialogsResponse.setUsers(users);
				return dialogsResponse;
			}

			@Override
			public String getAppServiceKey() {
				// not needs there
				return null;
			}
		};
		DialogsResponse dialogResponse = gsonArrayContentValuesProcessor.execute(dataSourceRequest, httpAndroidDataSource, inputStream);
		ContentValues[] dialogs = dialogResponse.getDialogs();
		ContentValues[] users = dialogResponse.getUsers();
		DBHelper dbHelper = new DBHelper(getApplication());
		dbHelper.createTablesForModels(Dialog.class);
		dbHelper.createTablesForModels(Attachment.class);
		dbHelper.createTablesForModels(FwdMessage.class);
		dbHelper.createTablesForModels(User.class);
		dbHelper.updateOrInsert(Dialog.class, dialogs);
		dbHelper.updateOrInsert(User.class, users);
	}
	

}
