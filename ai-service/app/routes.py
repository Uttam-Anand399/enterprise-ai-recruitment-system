from flask import Blueprint, jsonify, request

from app.matcher import match_job
from app.resume_parser import is_allowed_resume, parse_resume

api = Blueprint("api", __name__)


@api.post("/parse-resume")
def parse_resume_endpoint():
    if "file" not in request.files:
        return jsonify({"error": "file is required"}), 400

    file = request.files["file"]
    if not file.filename:
        return jsonify({"error": "file name is required"}), 400

    if not is_allowed_resume(file.filename):
        return jsonify({"error": "only PDF resumes are supported"}), 400

    try:
        result = parse_resume(file.read())
    except Exception:
        return jsonify({"error": "unable to parse resume"}), 422

    return jsonify({"fileName": file.filename, **result}), 200


@api.post("/match-job")
def match_job_endpoint():
    payload = request.get_json(silent=True) or {}
    if not isinstance(payload, dict):
        return jsonify({"error": "JSON body must be an object"}), 400

    resume_text = payload.get("resumeText", "")
    job_description = payload.get("jobDescription", "")

    try:
        result = match_job(resume_text, job_description)
    except ValueError as error:
        return jsonify({"error": str(error)}), 400

    return jsonify(result), 200
