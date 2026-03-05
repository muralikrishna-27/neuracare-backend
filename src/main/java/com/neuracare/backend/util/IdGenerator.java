package com.neuracare.backend.util;

import java.util.UUID;

public class IdGenerator {

    public static UUID generateReportId() {
        return UUID.randomUUID();
    }
}