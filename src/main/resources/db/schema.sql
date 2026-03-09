CREATE TABLE reports (
    report_id UUID PRIMARY KEY,
    user_id UUID,
    file_name TEXT NOT NULL,
    file_path TEXT NOT NULL,
    extracted_text TEXT,
    risk_level TEXT,
    observations JSONB,
    ai_explanation TEXT,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT NOT NULL
);

CREATE TABLE report_chat_messages (
    message_id UUID PRIMARY KEY,
    report_id UUID NOT NULL,
    role TEXT NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--voice call schemas
CREATE TABLE voice_tasks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    task_type VARCHAR(20) NOT NULL,
    task_message TEXT NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE voice_call_logs (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    call_sid VARCHAR(100),
    call_time TIMESTAMP NOT NULL,
    result VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);