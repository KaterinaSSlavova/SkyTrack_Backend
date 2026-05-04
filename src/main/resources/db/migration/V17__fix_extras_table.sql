DROP TABLE IF EXISTS extras;

CREATE TABLE extras (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        price DECIMAL(10,2) NOT NULL
);

INSERT INTO extras (name, price) VALUES
                                     ('extra_legroom', 25.00),
                                     ('window', 10.00);