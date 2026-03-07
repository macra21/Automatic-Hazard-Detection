CREATE TABLE IF NOT EXISTS response_time (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hazard_id INT,
    initial_date TIMESTAMP,
    update_date TIMESTAMP,
    initial_status VARCHAR(255),
    update_status VARCHAR(255),
    response_time FLOAT
);