CREATE TABLE conta_bancaria
(
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid_distribuited_system VARCHAR(36)    NOT NULL,
    nome                     VARCHAR(255)   NOT NULL UNIQUE,
    banco                    INT            NOT NULL,
    agencia                  VARCHAR(6)     NOT NULL,
    numero_conta             VARCHAR(15)    NOT NULL,
    digito_conta             VARCHAR(5)     NOT NULL,
    codigo_operacao          VARCHAR(5)     NOT NULL,
    titular_conta            VARCHAR(255)   NOT NULL,
    situacao                 INT            NOT NULL,
    motivoInativacao         VARCHAR(255),
    saldoInicial             NUMERIC(19, 2) NOT NULL,
    saldo                    NUMERIC(19, 2) NOT NULL,
    responsavel_created      VARCHAR(255)   NOT NULL,
    data_criacao             TIMESTAMP      NOT NULL,
    responsavel_updated      VARCHAR(255),
    data_ultima_atualizacao  TIMESTAMP      NOT NULL
);

CREATE INDEX idx_conta_bancaria_nome ON conta_bancaria (nome);
CREATE INDEX idx_conta_bancaria_data_criacao ON conta_bancaria (data_criacao);
CREATE INDEX idx_conta_bancaria_responsavel_created ON conta_bancaria (responsavel_created);
CREATE INDEX idx_conta_bancaria_uuid_distribuited_system ON conta_bancaria (uuid_distribuited_system);

CREATE TABLE conta_bancaria_aud
(
    id                       BIGINT         NOT NULL,
    id_aud                   BIGINT         NOT NULL,
    tipo_aud                 INT,
    uuid_distribuited_system VARCHAR(36),
    nome                     VARCHAR(255),
    banco                    INT,
    agencia                  VARCHAR(6),
    numero_conta             VARCHAR(15),
    digito_conta             VARCHAR(5),
    codigo_operacao          VARCHAR(5),
    titular_conta            VARCHAR(255),
    situacao                 INT,
    motivoInativacao         VARCHAR(255),
    saldoInicial             NUMERIC(19, 2) NOT NULL,
    saldo                    NUMERIC(19, 2) NOT NULL,
    responsavel_created      VARCHAR(255),
    data_criacao             TIMESTAMP,
    responsavel_updated      VARCHAR(255),
    data_ultima_atualizacao  TIMESTAMP,

    CONSTRAINT conta_bancaria_aud_pk PRIMARY KEY (id, id_aud)
);

ALTER TABLE conta_bancaria_aud
    ADD CONSTRAINT conta_bancaria_aud_fk FOREIGN KEY (id_aud) REFERENCES log_auditoria (id);

CREATE TABLE conta_bancaria_movimentacao
(
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid_distribuited_system VARCHAR(36)    NOT NULL,
    valor                    NUMERIC(19, 2) NOT NULL DEFAULT 0,
    responsavel_movimentacao VARCHAR(255)   NOT NULL,
    descricao                VARCHAR(255)   NOT NULL,
    motivo_estorno           VARCHAR(255),
    estornada                BOOLEAN                 DEFAULT FALSE,
    movimentacao_estornada   BOOLEAN                 DEFAULT FALSE,
    responsavel_estorno      VARCHAR(255),
    data_estorno             TIMESTAMP,
    conta_bancaria_id        BIGINT         NOT NULL,
    operacao                 INT            NOT NULL,
    data_operacao            TIMESTAMP      NOT NULL,
    lancamento_futuro        BOOLEAN                 DEFAULT FALSE,
    identificador_externo    VARCHAR(255)   NOT NULL,
    CONSTRAINT fk_conta_bancaria FOREIGN KEY (conta_bancaria_id) REFERENCES conta_bancaria (id)
);

CREATE INDEX idx_cbm_conta_bancaria_id ON conta_bancaria_movimentacao (conta_bancaria_id);
CREATE INDEX idx_cbm_data_operacao ON conta_bancaria_movimentacao (data_operacao);
CREATE INDEX idx_cbm_responsavel ON conta_bancaria_movimentacao (responsavel_movimentacao);
CREATE INDEX idx_cbm_uuid_distribuited_system ON conta_bancaria_movimentacao (uuid_distribuited_system);
CREATE INDEX idx_cbm_identificador_externo ON conta_bancaria_movimentacao (identificador_externo);
CREATE INDEX idx_cbm_movimentacao_estornada ON conta_bancaria_movimentacao (movimentacao_estornada);

CREATE TABLE conta_bancaria_movimentacao_aud
(
    id                       BIGINT NOT NULL,
    id_aud                   BIGINT NOT NULL,
    tipo_aud                 INT,
    uuid_distribuited_system VARCHAR(36),
    valor                    NUMERIC(19, 2) DEFAULT 0,
    responsavel_movimentacao VARCHAR(255),
    descricao                VARCHAR(255),
    motivo_estorno           VARCHAR(255),
    estornada                BOOLEAN        DEFAULT FALSE,
    movimentacao_estornada   BOOLEAN        DEFAULT FALSE,
    responsavel_estorno      VARCHAR(255),
    data_estorno             TIMESTAMP,
    conta_bancaria_id        BIGINT,
    operacao                 INT,
    data_operacao            TIMESTAMP,
    lancamento_futuro        BOOLEAN,
    identificador_externo    VARCHAR(255),

    CONSTRAINT cbm_aud_pk PRIMARY KEY (id, id_aud)
);

ALTER TABLE conta_bancaria_movimentacao_aud
    ADD CONSTRAINT cbm_aud_fk FOREIGN KEY (id_aud) REFERENCES log_auditoria (id);
