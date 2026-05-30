# AI Service

Flask AI service for resume parsing and job matching.

## Endpoints

### `POST /parse-resume`

Accepts `multipart/form-data` with a PDF file field named `file`.

Returns JSON:

```json
{
  "text": "Extracted resume text",
  "email": "candidate@example.com",
  "phone": "+1 555 0100",
  "keywords": ["python", "spring", "mysql"],
  "word_count": 320
}
```

### `POST /match-job`

Accepts JSON:

```json
{
  "resumeText": "Candidate resume text",
  "jobDescription": "Job description text"
}
```

Returns JSON:

```json
{
  "score": 78.42,
  "commonKeywords": ["python", "mysql"],
  "missingKeywords": ["kubernetes"]
}
```

## NLP Model

The service attempts to load `en_core_web_sm`. If the model is unavailable, it falls back to a blank English spaCy pipeline so the API can still run.
