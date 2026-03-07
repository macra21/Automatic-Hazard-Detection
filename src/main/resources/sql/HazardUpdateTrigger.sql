DROP TRIGGER IF EXISTS hazard_update_trigger ON hazards;

CREATE TRIGGER hazard_update_trigger
    AFTER UPDATE ON hazards
    FOR EACH ROW
EXECUTE FUNCTION log_response_time();