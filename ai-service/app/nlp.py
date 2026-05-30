import re
from functools import lru_cache

import spacy
from spacy.language import Language


@lru_cache(maxsize=1)
def load_nlp() -> Language:
    try:
        return spacy.load("en_core_web_sm")
    except OSError:
        return spacy.blank("en")


def normalize_text(text: str) -> str:
    cleaned = re.sub(r"\s+", " ", text or "").strip()
    return cleaned


def extract_email(text: str) -> str | None:
    match = re.search(r"[\w.+-]+@[\w-]+\.[\w.-]+", text)
    return match.group(0) if match else None


def extract_phone(text: str) -> str | None:
    match = re.search(r"(?:\+?\d[\d\s().-]{7,}\d)", text)
    return match.group(0).strip() if match else None


def extract_keywords(text: str, limit: int = 30) -> list[str]:
    doc = load_nlp()(normalize_text(text).lower())
    keywords: list[str] = []
    seen: set[str] = set()

    for token in doc:
        if token.is_stop or token.is_punct or token.like_num or len(token.text) < 3:
            continue

        value = token.lemma_.strip() if token.lemma_ else token.text.strip()
        if value and value not in seen:
            seen.add(value)
            keywords.append(value)

        if len(keywords) >= limit:
            break

    return keywords
