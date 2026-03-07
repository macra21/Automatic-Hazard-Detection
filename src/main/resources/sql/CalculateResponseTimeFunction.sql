CREATE OR REPLACE FUNCTION log_response_time()
    RETURNS TRIGGER AS $$
DECLARE
    curr_time TIMESTAMP;
    calc_response_time FLOAT;
BEGIN
    curr_time := NOW();

    -- Response time in seconds
    calc_response_time := EXTRACT(EPOCH FROM (curr_time - NEW.date));

    INSERT INTO response_time (
        hazard_id,
        initial_date,
        update_date,
        initial_status,
        update_status,
        response_time
    ) VALUES (
                 NEW.id,
                 NEW.date,
                 curr_time,
                 OLD.status,
                 NEW.status,
                 calc_response_time
             );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;