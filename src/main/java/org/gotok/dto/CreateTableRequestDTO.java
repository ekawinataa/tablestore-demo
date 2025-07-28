package org.gotok.dto;

import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTableRequestDTO {
    private final String tableName;
    private final String primaryKeyName;
    private final PrimaryKeyType primaryKeyType;
}
