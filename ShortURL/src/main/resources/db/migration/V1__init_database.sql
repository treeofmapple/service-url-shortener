CREATE TABLE IF NOT EXISTS "urls" (
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "original_url" VARCHAR(255) NOT NULL,
    "short_url" VARCHAR(255) NOT NULL UNIQUE,
    "date_created" TIMESTAMP NOT NULL,
    "expiration_time" TIMESTAMP,
    "last_access_time" TIMESTAMP,
    "access_count" INT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_shorturl ON "urls"("shortUrl");