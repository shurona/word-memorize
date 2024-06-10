#!/bin/bash

psql --username "$POSTGRES_USER" <<-EOSQL
    SELECT 'CREATE DATABASE word_memory'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'word_memory')\gexec
EOSQL