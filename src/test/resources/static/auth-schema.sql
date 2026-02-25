CREATE SCHEMA IF NOT EXISTS auth;

CREATE SEQUENCE IF NOT EXISTS auth.refresh_token_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS auth.audit_log_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auth.organization (
                                   id UUID PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   enabled BOOLEAN NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   org_admin_id VARCHAR(255)
);

CREATE TABLE auth.permission (
                                 id UUID PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL
);

CREATE TABLE auth.role (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           org_id UUID,
                           CONSTRAINT fk_role_org
                               FOREIGN KEY (org_id)
                                   REFERENCES auth.organization (id)
);

CREATE TABLE auth.role_permissions (
                                       role_id UUID NOT NULL,
                                       permission_id UUID NOT NULL,
                                       PRIMARY KEY (role_id, permission_id),
                                       CONSTRAINT fk_role_permissions_role
                                           FOREIGN KEY (role_id)
                                               REFERENCES auth.role (id),
                                       CONSTRAINT fk_role_permissions_permission
                                           FOREIGN KEY (permission_id)
                                               REFERENCES auth.permission (id)
);

CREATE TABLE auth.user_type (
                                id UUID PRIMARY KEY,
                                name VARCHAR(50) NOT NULL
);