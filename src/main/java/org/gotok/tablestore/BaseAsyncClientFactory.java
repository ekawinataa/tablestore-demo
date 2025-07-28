package org.gotok.tablestore;

import com.alicloud.openservices.tablestore.AsyncClient;
import org.gotok.constants.HBaseConstant;

public class BaseAsyncClientFactory implements AsyncClientFactory {

    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String instanceName;

    public BaseAsyncClientFactory() {
        this.endpoint = System.getenv(HBaseConstant.TABLESTORE_CLIENT_ENDPOINT);
        this.accessKeyId = System.getenv(HBaseConstant.TABLESTORE_CLIENT_ACCESS_KEY_ID);
        this.accessKeySecret = System.getenv(HBaseConstant.TABLESTORE_CLIENT_ACCESS_KEY_SECRET);
        this.instanceName = System.getenv(HBaseConstant.TABLESTORE_CLIENT_INSTANCE_NAME);
    }

    @Override
    public AsyncClient createAsyncClient() {
        return new AsyncClient(endpoint, accessKeyId, accessKeySecret, instanceName);
    }

}
