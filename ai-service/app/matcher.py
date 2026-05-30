from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

from app.nlp import extract_keywords, normalize_text


def match_job(resume_text: str, job_description: str) -> dict:
    resume = normalize_text(resume_text)
    job = normalize_text(job_description)

    if not resume or not job:
        raise ValueError("resumeText and jobDescription are required")

    vectorizer = TfidfVectorizer(stop_words="english", ngram_range=(1, 2))
    matrix = vectorizer.fit_transform([resume, job])
    score = float(cosine_similarity(matrix[0:1], matrix[1:2])[0][0])

    resume_keywords = set(extract_keywords(resume, limit=50))
    job_keywords = set(extract_keywords(job, limit=50))
    common_keywords = sorted(resume_keywords.intersection(job_keywords))
    missing_keywords = sorted(job_keywords.difference(resume_keywords))[:20]

    return {
        "score": round(score * 100, 2),
        "commonKeywords": common_keywords[:20],
        "missingKeywords": missing_keywords,
    }
