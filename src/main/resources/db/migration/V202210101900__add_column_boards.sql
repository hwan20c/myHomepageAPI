ALTER TABLE boards ADD COLUMN image_path VARCHAR(200) AFTER contents;
ALTER TABLE boards ADD COLUMN view_count INTEGER AFTER image_path;
ALTER TABLE boards ADD COLUMN type INTEGER AFTER view_count;