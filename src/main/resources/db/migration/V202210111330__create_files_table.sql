CREATE TABLE files (
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  bucket_name VARCHAR(50),
  path VARCHAR(200),
  board_id INTEGER,

  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (board_id) REFERENCES boards (id)
);