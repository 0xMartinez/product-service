CREATE KEYSPACE IF NOT EXISTS mykeyspace
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

USE mykeyspace;

CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     username TEXT,
                                     password TEXT,
                                     roles SET<TEXT>
);


CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY,
                                        name TEXT,
                                        description TEXT,
                                        price DECIMAL,
                                        category TEXT
);
