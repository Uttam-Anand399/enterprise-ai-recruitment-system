from flask import Flask, jsonify
from werkzeug.exceptions import HTTPException

from app.routes import api


def create_app() -> Flask:
    app = Flask(__name__)
    app.config["MAX_CONTENT_LENGTH"] = 10 * 1024 * 1024
    app.register_blueprint(api)

    @app.get("/health")
    def health():
        return jsonify({"status": "ok", "service": "ai-service"}), 200

    @app.errorhandler(HTTPException)
    def handle_http_error(error: HTTPException):
        return jsonify({"error": error.name, "message": error.description}), error.code

    @app.errorhandler(Exception)
    def handle_unexpected_error(_error: Exception):
        return jsonify({"error": "Internal Server Error", "message": "Unexpected service error"}), 500

    return app
