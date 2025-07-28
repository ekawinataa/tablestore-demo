package org.gotok.tablestore;

import com.alicloud.openservices.tablestore.AsyncClient;

public interface AsyncClientFactory {
    AsyncClient createAsyncClient();
}
