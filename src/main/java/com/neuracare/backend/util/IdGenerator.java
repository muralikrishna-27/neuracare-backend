package com.neuracare.backend.util;

import java.util.UUID;

public final class IdGenerator {

    private IdGenerator() {
        // prevent instantiation
    }

    public static UUID generateReportId() {
        return UUID.randomUUID();
    }
}