CREATE TABLE accounts
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    password              VARCHAR(255) NULL,
    email                 VARCHAR(255) NULL,
    authentication_method VARCHAR(255) NULL,
    created_at            date         NULL,
    updated_at            date         NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE TABLE accounts_roles
(
    accounts_id BIGINT       NOT NULL,
    roles_name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_accounts_roles PRIMARY KEY (accounts_id, roles_name)
);

CREATE TABLE invalidated_tokens
(
    id           VARCHAR(255) NOT NULL,
    expriry_date datetime     NULL,
    CONSTRAINT pk_invalidated_tokens PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    name          VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NULL,
    created_at    date         NULL,
    updated_at    date         NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (name)
);

CREATE TABLE roles
(
    name          VARCHAR(255) NOT NULL, 
    description  VARCHAR(255) NULL,
    created_at    date         NULL,
    updated_at    date         NULL,
    CONSTRAINT pk_roles PRIMARY KEY (name)
);

CREATE TABLE roles_permissions
(
    permissions_name VARCHAR(255) NOT NULL,
    roles_name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles_permissions PRIMARY KEY (permissions_name, roles_name)
);

ALTER TABLE accounts_roles
    ADD CONSTRAINT fk_accrol_on_account FOREIGN KEY (accounts_id) REFERENCES accounts (id);

ALTER TABLE accounts_roles
    ADD CONSTRAINT fk_accrol_on_role FOREIGN KEY (roles_name) REFERENCES roles (name);

ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_name) REFERENCES permissions (name);

ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (roles_name) REFERENCES roles (name);