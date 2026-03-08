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