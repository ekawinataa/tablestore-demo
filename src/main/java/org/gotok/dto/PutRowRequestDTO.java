package org.gotok.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutRowRequestDTO {
    private final String tableName;
    private final String id;
    private final byte[] value;

    public String getRowKey() {
        return String.format("%s_%d", id, Long.MAX_VALUE - System.currentTimeMillis());
    }

}
