-- Spring Session JDBC schema for PostgreSQL
-- This creates the tables spring_session and spring_session_attributes
-- Compatible with Spring Session JDBC

CREATE TABLE IF NOT EXISTS spring_session (
  PRIMARY_ID varchar(36) NOT NULL,
  SESSION_ID varchar(36) NOT NULL,
  CREATION_TIME bigint NOT NULL,
  LAST_ACCESS_TIME bigint NOT NULL,
  MAX_INACTIVE_INTERVAL int NOT NULL,
  EXPIRY_TIME bigint NOT NULL,
  PRINCIPAL_NAME varchar(100),
  CONSTRAINT spring_session_pk PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS spring_session_ix1 ON spring_session (SESSION_ID);
CREATE INDEX IF NOT EXISTS spring_session_ix2 ON spring_session (EXPIRY_TIME);
CREATE INDEX IF NOT EXISTS spring_session_ix3 ON spring_session (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS spring_session_attributes (
  SESSION_PRIMARY_ID varchar(36) NOT NULL,
  ATTRIBUTE_NAME varchar(200) NOT NULL,
  ATTRIBUTE_BYTES bytea NOT NULL,
  CONSTRAINT spring_session_attributes_pk PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT spring_session_attributes_fk FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES spring_session(PRIMARY_ID) ON DELETE CASCADE
);

