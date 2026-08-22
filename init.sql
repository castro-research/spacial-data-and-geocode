-- Create for development
CREATE EXTENSION IF NOT EXISTS postgis;

--- Create for test
CREATE USER test WITH PASSWORD 'test';
CREATE DATABASE test OWNER test;
\connect test
CREATE EXTENSION IF NOT EXISTS postgis;
