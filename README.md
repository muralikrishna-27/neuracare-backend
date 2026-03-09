# NeuraCare Backend – Medical Report Analysis API

This README explains how to use the **NeuraCare backend APIs** from a **mobile application (TypeScript / React Native)**.
It also gives a **simple overview of each backend file/module** so developers understand the flow.

---

# 1. System Overview

NeuraCare allows users to:

1. Upload a medical report (PDF/Image)
2. Extract medical data using OCR
3. Analyze health parameters using a rule engine
4. Generate AI explanations
5. Produce a final summarized report
6. Ask AI questions about the uploaded report

Pipeline:

Upload Report → OCR → Rule Engine → AI Explanation → Final Analysis → Report Chat

---

# 2. Backend Base URL

Example:

```
http://localhost:8080
```

Mobile apps should store this as a constant.

Example TypeScript config:

```ts
export const API_BASE = "http://localhost:8080";
```

---

# 3. API Endpoints

## 3.1 Upload Medical Report

Endpoint

```
POST /reports/upload
```

Description

Uploads a medical report file.

Request (multipart/form-data)

Field name must be:

```
file
```

Example using TypeScript:

```ts
const form = new FormData();
form.append("file", file);

await fetch(`${API_BASE}/reports/upload`, {
  method: "POST",
  body: form
});
```

Response Example

```
{
  "reportId": "uuid"
}
```

Save this **reportId**. It is required for all other APIs.

---

# 3.2 Process OCR

Endpoint

```
POST /reports/{reportId}/ocr
```

Description

Extracts text from the uploaded report and analyzes medical parameters.

Example

```ts
await fetch(`${API_BASE}/reports/${reportId}/ocr`, {
  method: "POST"
});
```

After this step the system will:

* extract report text
* detect medical values
* calculate risk level
* generate AI explanation

---

# 3.3 Get Final Report Analysis

Endpoint

```
GET /reports/{reportId}/analysis
```

Description

Returns the complete analysis of the medical report.

Example

```ts
const res = await fetch(`${API_BASE}/reports/${reportId}/analysis`);
const data = await res.json();
```

Response Example

```
{
  "reportId": "...",
  "summary": "...",
  "overallRisk": "HIGH",
  "explanation": "...",
  "observations": [...],
  "disclaimer": "..."
}
```

This endpoint should be used to show the **main results screen in the mobile app**.

---

# 3.4 Ask Questions About the Report

Endpoint

```
POST /reports/{reportId}/chat
```

Description

Allows users to ask questions related to their report.

Request Body

```
{
  "question": "Explain my blood pressure"
}
```

Example TypeScript

```ts
const res = await fetch(`${API_BASE}/reports/${reportId}/chat`, {
  method: "POST",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify({
    question: "Explain my blood pressure"
  })
});

const data = await res.json();
```

Response Example

```
{
  "answer": "...",
  "disclaimer": "..."
}
```

The chat system automatically:

* uses report text
* uses detected medical values
* uses previous conversation history

---

# 4. Expected Mobile App Flow

Recommended frontend flow:

1️⃣ User uploads report
2️⃣ Call OCR API
3️⃣ Show loading state
4️⃣ Fetch final analysis
5️⃣ Show results screen
6️⃣ Allow user to ask questions via chat

Flow diagram:

Upload → OCR → Analysis → Chat

---

# 5. Important Medical Safety Behavior

The AI system is designed to:

* Explain medical values
* Simplify report information
* Avoid diagnosis
* Avoid treatment advice
* Reject unrelated questions

Example allowed question:

```
Explain my cholesterol level
```

Example rejected question:

```
Which medicine should I take?
```

---

# 6. Backend File Structure

Below is a simplified explanation of important backend files.

## Controller Layer

Handles API requests.

ReportController.java
Handles report upload, OCR processing, and final analysis APIs.

ReportChatController.java
Handles chat requests related to medical reports.

---

## Service Layer

Contains core application logic.

ReportService.java
Manages report upload and report metadata storage.

OCRProcessingService.java
Runs OCR extraction and triggers rule engine analysis.

MedicalRuleEngineService.java
Interface for medical rule engine logic.

MedicalRuleEngineServiceImpl.java
Detects medical parameters and calculates risk levels.

AIExplanationService.java
Generates AI explanations for detected observations.

FinalReportServiceImpl.java
Combines OCR data, rule results, and AI explanation into a final report.

ReportChatService.java
Interface for AI report chat.

ReportChatServiceImpl.java
Handles report-based chat and conversation memory.

---

## Repository Layer

Handles database access.

ReportRepository.java
Performs database operations for reports.

ReportChatRepository.java
Stores and retrieves report chat messages.

---

## Model Layer

Represents database entities.

ReportMetadata.java
Stores report details, OCR text, observations, and AI results.

MedicalObservation.java
Represents a detected medical parameter (e.g., glucose, BP).

ReportChatMessage.java
Stores chat messages for report conversations.

---

## DTO Layer

Used for API request/response data.

ReportResponse.java
Response object returned when fetching reports.

RuleEngineResponse.java
Contains detected observations and risk level.

ReportChatRequest.java
Incoming request for chat questions.

ReportChatResponse.java
Response returned from AI chat.

---

## Utility Layer

Reusable helper utilities.

IdGenerator.java
Generates UUID identifiers for reports and messages.

---

# 7. Database Tables

### reports

Stores medical report data.

Fields include:

```
report_id
file_name
file_path
extracted_text
observations
risk_level
ai_explanation
status
upload_time
```

---

### report_chat_messages

Stores chat conversation history.

Fields include:

```
message_id
report_id
role
message
created_at
```

---

# 8. AI Model

The backend uses the **Groq API** with OpenAI-compatible endpoints.

Configured in:

```
application-local.properties
```

Example configuration:

```
groq.api.key=YOUR_KEY
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.model=openai/gpt-oss-120b
groq.temperature=0.2
```

---

# 9. Development Notes

Backend runs on:

```
Java + Spring Boot
PostgreSQL
Groq AI API
OCR Engine
```

Recommended frontend:

```
React Native + TypeScript
```

---

# 10. Example Full Flow (Quick Test)

1️⃣ Upload report
2️⃣ Run OCR
3️⃣ Get analysis
4️⃣ Ask chat question

This sequence enables the full AI-powered report explanation system.

---

# 11. Disclaimer

This system only explains values found in medical reports.
It does **not provide medical diagnosis or treatment advice**.

Users should consult qualified healthcare professionals for medical guidance.

---


# NeuraCare – Automated Voice Call (IVR) Module

This module enables automated phone calls for reminders (medicine, exercise, health check-ins) using **Twilio IVR**.

It allows the backend to:

* Schedule calls
* Trigger automated voice reminders
* Capture Yes/No responses
* Retry failed tasks
* Store call logs in the database

This module is designed so the **mobile application (React Native / TypeScript)** can easily trigger calls through simple REST APIs.

---

# Architecture Overview

```
Mobile App (React Native)
        │
        ▼
POST /api/voice/schedule
        │
        ▼
Spring Boot Backend
        │
        ▼
Scheduler checks pending tasks
        │
        ▼
Twilio API triggers phone call
        │
        ▼
User presses keypad (1 or 2)
        │
        ▼
Webhook → /api/voice/response
        │
        ▼
Database stores result
```

---

# API Endpoints (For Mobile App)

## 1️⃣ Schedule a Voice Call

**Endpoint**

```
POST /api/voice/schedule
```

**Purpose**

Schedules a reminder call for the user.

**Request Body**

```json
{
  "userId": 1,
  "phoneNumber": "+919876543210",
  "taskType": "MEDICINE",
  "taskMessage": "Did you take your blood pressure medicine?",
  "scheduledTime": "2026-03-10T09:00:00",
  "maxRetries": 3
}
```

**Response**

```json
{
  "id": 5,
  "phoneNumber": "+919876543210",
  "taskType": "MEDICINE",
  "scheduledTime": "2026-03-10T09:00:00",
  "retryCount": 0,
  "active": true
}
```

---

## 2️⃣ Fetch Scheduled Tasks

**Endpoint**

```
GET /api/voice/tasks
```

Used for displaying scheduled reminders in the mobile app.

---

# Phone Call Flow

When the scheduled time is reached:

1. Backend scheduler triggers Twilio.
2. User receives a phone call.
3. Voice message plays:

```
Hello this is NeuraCare.
Did you take your blood pressure medicine?
Press 1 for Yes.
Press 2 for No.
```

4. User presses keypad input.
5. Backend logs response.

---

# IVR Response Logic

| Key Pressed | Result          |
| ----------- | --------------- |
| 1           | Task Completed  |
| 2           | Retry Scheduled |
| No Input    | Retry Scheduled |

Retry delay is **10 minutes** until `maxRetries` is reached.

---

# Project Structure

```
voice
 ├ controller
 ├ dto
 ├ model
 ├ repository
 ├ service
 ├ scheduler
```

---

# File Explanations

### VoiceCallController.java

Handles REST APIs used by the mobile application and Twilio webhooks.

### VoiceCallService.java

Defines business logic for scheduling calls and processing IVR responses.

### VoiceCallServiceImpl.java

Implements Twilio call triggering, retry logic, and response handling.

### VoiceCallScheduler.java

Runs every minute to check pending tasks and trigger calls.

### VoiceTask.java

Database entity representing a scheduled call task.

### VoiceCallLog.java

Stores the result of each call (YES / NO / NO_RESPONSE).

### VoiceTaskRepository.java

Handles database operations for scheduled tasks.

### VoiceCallLogRepository.java

Handles database operations for call response logs.

### VoiceScheduleRequest.java

DTO used by the mobile app to schedule a call.

### VoiceTaskResponse.java

DTO returned to the mobile app when tasks are fetched.

### IVRResponseRequest.java

DTO representing keypad responses from Twilio.

---

# Database Tables

## voice_tasks

Stores scheduled calls.

| Column         | Description                   |
| -------------- | ----------------------------- |
| id             | task id                       |
| user_id        | user identifier               |
| phone_number   | phone number to call          |
| task_type      | MEDICINE / EXERCISE / CHECKIN |
| task_message   | message spoken in IVR         |
| scheduled_time | call trigger time             |
| retry_count    | number of retries             |
| max_retries    | retry limit                   |
| active         | task status                   |

---

## voice_call_logs

Stores user responses.

| Column    | Description            |
| --------- | ---------------------- |
| id        | log id                 |
| task_id   | related task           |
| call_sid  | Twilio call identifier |
| call_time | timestamp              |
| result    | YES / NO / NO_RESPONSE |

---

# Environment Variables

Required configuration:

```
TWILIO_SID=
TWILIO_TOKEN=
TWILIO_FROM=
NGROK_URL=
```

Example in `application.properties`:

```
twilio.account.sid=${TWILIO_SID}
twilio.auth.token=${TWILIO_TOKEN}
twilio.from.number=${TWILIO_FROM}
app.ngrok.url=${NGROK_URL}
```

---

# Testing

Use Postman to schedule a call:

```
POST /api/voice/schedule
```

Then wait for the scheduler to trigger the call.

---

# Notes for Mobile Developers

The mobile application only needs to interact with:

```
POST /api/voice/schedule
GET /api/voice/tasks
```

All IVR handling and Twilio interactions are managed entirely by the backend.


External Configuration File (Important)

Sensitive configuration values are stored in a separate file:

application-local.properties

This file is linked inside application.properties and contains environment-specific configuration such as:

Database credentials

Twilio SMS configuration

Ngrok URL

AI API keys

# Example values stored in application-local.properties:

spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

twilio.account.sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth.token=YOUR_TWILIO_AUTH_TOKEN
twilio.from.number=YOUR_TWILIO_PHONE_NUMBER

app.ngrok.url=YOUR_NGROK_URL

groq.api.key=YOUR_GROQ_API_KEY
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.model=openai/gpt-oss-120b
groq.temperature=0.2

This approach keeps sensitive information outside the main application configuration and allows different environments (local/dev/production) to use different settings.