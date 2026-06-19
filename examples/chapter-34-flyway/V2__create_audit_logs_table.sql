CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    actor_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    old_value VARCHAR(100),
    new_value VARCHAR(100),
    created_at TIMESTAMP NOT NULL
);

