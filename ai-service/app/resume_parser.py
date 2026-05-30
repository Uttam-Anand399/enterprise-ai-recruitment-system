from io import BytesIO

import pdfplumber

from app.nlp import extract_email, extract_keywords, extract_phone, normalize_text


ALLOWED_EXTENSIONS = {"pdf"}


def is_allowed_resume(filename: str) -> bool:
    return "." in filename and filename.rsplit(".", 1)[1].lower() in ALLOWED_EXTENSIONS


def extract_pdf_text(file_bytes: bytes) -> str:
    pages: list[str] = []
    with pdfplumber.open(BytesIO(file_bytes)) as pdf:
        for page in pdf.pages:
            pages.append(page.extract_text() or "")
    return normalize_text(" ".join(pages))


def parse_resume(file_bytes: bytes) -> dict:
    text = extract_pdf_text(file_bytes)
    return {
        "parser": "pdfplumber",
        "text": text,
        "email": extract_email(text),
        "phone": extract_phone(text),
        "keywords": extract_keywords(text),
        "word_count": len(text.split()) if text else 0,
    }
