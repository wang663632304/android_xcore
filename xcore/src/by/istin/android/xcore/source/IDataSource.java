package by.istin.android.xcore.source;

import java.io.IOException;
import java.io.InputStream;

import by.istin.android.xcore.XCoreHelper.IAppServiceKey;

public interface IDataSource extends IAppServiceKey {

	InputStream getSource(DataSourceRequest dataSourceRequest) throws IOException;
	
}
