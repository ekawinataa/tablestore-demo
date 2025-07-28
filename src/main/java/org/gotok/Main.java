package org.gotok;

import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import com.google.protobuf.InvalidProtocolBufferException;
import org.gotok.driver.MessageGenerator;
import org.gotok.dto.CreateTableRequestDTO;
import org.gotok.dto.PutRowRequestDTO;
import org.gotok.service.AsyncLongbowService;
import org.gotok.service.BaseAsyncLongbowService;
import org.gotok.tablestore.BaseAsyncClientFactory;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InvalidProtocolBufferException, InterruptedException, ExecutionException {
        AsyncLongbowService asyncLongbowService = new BaseAsyncLongbowService(
                new BaseAsyncClientFactory()
        );
        CreateTableRequestDTO createTableRequestDTO = CreateTableRequestDTO.builder()
                .tableName("test_tt2")
                .primaryKeyName("id")
                .primaryKeyType(PrimaryKeyType.STRING)
                .build();
        asyncLongbowService.createTable(createTableRequestDTO).get();
        MessageGenerator messageGenerator = new MessageGenerator(
                1000,
                10,
                values -> {
                    asyncLongbowService.putRow(PutRowRequestDTO.builder()
                            .tableName("test_tt2")
                            .id(new String(values.get("id")))
                            .value(values.get("value"))
                            .build());
                }
        );
        messageGenerator.generate();
    }
}
