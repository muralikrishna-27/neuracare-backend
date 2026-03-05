CREATE TABLE reports (
    report_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    file_name TEXT NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT NOT NULL
);