package br.com.finchsolucoes.financeiro.service.core.utils;

import com.github.f4b6a3.uuid.*;
import java.time.*;
import java.util.*;

public class UUIDv7Utils {

    private UUIDv7Utils() {
    }

    public static UUID generateTimeBasedUUID() {
        return UuidCreator.getTimeOrdered(); // Gera UUID tipo 7 (time-ordered)
    }

    public static OffsetDateTime extractOffsetDateTime(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        long timestamp = (uuid.timestamp() - 0x01b21dd213814000L) / 10000L;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
