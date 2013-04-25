package by.istin.android.xcore.test.bo;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbByte;
import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbEntity;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.provider.ContentValuesEntity;

import com.google.gson.annotations.SerializedName;

public class TestEntity extends ContentValuesEntity {

	@dbLong
	@SerializedName(value="uid")
	public static final String ID = _ID;
	
	@dbInteger
	public static final String INT_VALUE = "int_value";
	
	@dbByte
	public static final String BYTE_VALUE = "byte_value";
	
	@dbDouble
	public static final String DOUBLE_VALUE = "double_value";
	
	@dbString
	public static final String STRING_VALUE = "string_value";
	
	@dbBoolean
	public static final String BOOLEAN_VALUE = "boolean_value";
	
	@dbEntity(clazz=SubEntity.class)
	public static final String SUB_ENTITY_VALUE = "sub_entity_value";
	
}