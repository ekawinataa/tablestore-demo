package org.gotok.service;

import com.alicloud.openservices.tablestore.AsyncClient;
import com.alicloud.openservices.tablestore.TableStoreCallback;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.CreateTableRequest;
import com.alicloud.openservices.tablestore.model.CreateTableResponse;
import com.alicloud.openservices.tablestore.model.PrimaryKey;
import com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder;
import com.alicloud.openservices.tablestore.model.PrimaryKeySchema;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.alicloud.openservices.tablestore.model.PutRowRequest;
import com.alicloud.openservices.tablestore.model.PutRowResponse;
import com.alicloud.openservices.tablestore.model.RowPutChange;
import com.alicloud.openservices.tablestore.model.TableMeta;
import com.alicloud.openservices.tablestore.model.TableOptions;
import lombok.extern.slf4j.Slf4j;
import org.gotok.dto.CreateTableRequestDTO;
import org.gotok.dto.PutRowRequestDTO;
import org.gotok.tablestore.AsyncClientFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
public class BaseAsyncLongbowService implements AsyncLongbowService {

    private final AsyncClient asyncClient;

    public BaseAsyncLongbowService(AsyncClient asyncClient) {
        this.asyncClient = asyncClient;
    }

    public BaseAsyncLongbowService(AsyncClientFactory asyncClientFactory) {
        this.asyncClient = asyncClientFactory.createAsyncClient();
    }

    @Override
    public Future<CreateTableResponse> createTable(CreateTableRequestDTO createTableRequestDTO) {
        TableMeta tableMeta = new TableMeta(createTableRequestDTO.getTableName());
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(
                createTableRequestDTO.getPrimaryKeyName(),
                createTableRequestDTO.getPrimaryKeyType()
        ));
        CompletableFuture<CreateTableResponse> future = new CompletableFuture<>();
        return asyncClient.createTable(new CreateTableRequest(tableMeta, getDefaultTableOptions()), new TableStoreCallback<>() {
            @Override
            public void onCompleted(CreateTableRequest req, CreateTableResponse res) {
                future.complete(res);
            }

            @Override
            public void onFailed(CreateTableRequest req, Exception ex) {
                future.completeExceptionally(ex);
            }
        });
    }

    @Override
    public CompletableFuture<Void> putRow(PutRowRequestDTO putRowRequestDTO) {
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn("id", PrimaryKeyValue.fromString(putRowRequestDTO.getRowKey()));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowPutChange rowPutChange = new RowPutChange(putRowRequestDTO.getTableName(), primaryKey);
        rowPutChange.addColumn("ts", ColumnValue.fromBinary(putRowRequestDTO.getValue()));
        CompletableFuture<Void> future = new CompletableFuture<>();
        asyncClient.putRow(
                new PutRowRequest(rowPutChange),
                new TableStoreCallback<>() {
                    @Override
                    public void onCompleted(PutRowRequest req, PutRowResponse res) {
                        // Successfully put the row, complete the future
                        log.info("Successfully put row in table: {}", putRowRequestDTO.getTableName());
                        future.complete(null);
                    }

                    @Override
                    public void onFailed(PutRowRequest req, Exception ex) {
                        // Log the exception if needed
                        log.error("Failed to put row in table: {}, error: {}", putRowRequestDTO.getTableName(), ex.getMessage());
                        future.completeExceptionally(ex);
                    }
                }
        );
        return future;
    }

    private static TableOptions getDefaultTableOptions() {
        TableOptions tableOptions = new TableOptions();
        tableOptions.setMaxVersions(1);
        tableOptions.setTimeToLive(-1);
        return tableOptions;
    }
}
