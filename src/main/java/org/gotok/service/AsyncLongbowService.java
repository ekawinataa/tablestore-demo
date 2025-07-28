package org.gotok.service;

import com.alicloud.openservices.tablestore.model.CreateTableResponse;
import org.gotok.dto.CreateTableRequestDTO;
import org.gotok.dto.PutRowRequestDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface AsyncLongbowService {
    Future<CreateTableResponse> createTable(CreateTableRequestDTO createTableRequestDTO);
    CompletableFuture<Void> putRow(PutRowRequestDTO putRowRequestDTO);
}
