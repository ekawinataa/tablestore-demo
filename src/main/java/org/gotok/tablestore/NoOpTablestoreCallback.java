package org.gotok.tablestore;

import com.alicloud.openservices.tablestore.TableStoreCallback;
import com.alicloud.openservices.tablestore.model.Request;
import com.alicloud.openservices.tablestore.model.Response;

public class NoOpTablestoreCallback<T extends Request,V extends Response> implements TableStoreCallback<T, V> {
    @Override
    public void onCompleted(T req, V res) {

    }

    @Override
    public void onFailed(T req, Exception ex) {

    }

}
