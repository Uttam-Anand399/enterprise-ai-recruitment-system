# Environment Variables

These values are configured through `.env.example` files and deployment secrets.

## Frontend

- `VITE_API_BASE_URL`: Backend API base URL.
- `VITE_AI_SERVICE_URL`: AI service base URL for development-only direct calls. The production frontend should call the backend, not the AI service directly.

## Backend

- `SERVER_PORT`: Backend HTTP port.
- `DB_HOST`: MySQL host.
- `DB_PORT`: MySQL port.
- `DB_NAME`: MySQL database name.
- `DB_USERNAME`: MySQL username.
- `DB_PASSWORD`: MySQL password.
- `JWT_SECRET`: Secret used for JWT signing.
- `JWT_EXPIRATION_MS`: JWT lifetime in milliseconds.
- `AI_SERVICE_BASE_URL`: Internal AI service base URL.

## AI Service

- `FLASK_ENV`: Flask runtime environment.
- `FLASK_DEBUG`: Flask debug flag.
- `HOST`: Service bind host.
- `PORT`: Service bind port.
- `BACKEND_API_BASE_URL`: Backend API URL.
