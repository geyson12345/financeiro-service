CREATE TABLE conta_bancaria (
                                id                 BIGINT IDENTITY(1,1) PRIMARY KEY,

                                id_integracao      VARCHAR(255)   NOT NULL UNIQUE,
                                nome               VARCHAR(255)   NOT NULL UNIQUE,
                                banco              VARCHAR(50)    NOT NULL,
                                agencia            VARCHAR(50)    NOT NULL,
                                numero_conta       VARCHAR(50)    NOT NULL,
                                digito_conta       VARCHAR(10)    NOT NULL,
                                codigo_operacao    VARCHAR(50),
                                titular_conta      VARCHAR(255)   NOT NULL,
                                situacao           VARCHAR(50)    NOT NULL,
                                saldo              DECIMAL(19, 2) NOT NULL DEFAULT 0,

                                created_by         VARCHAR(255)   NOT NULL,
                                created_date       DATETIME2      NOT NULL,
                                last_modified_by   VARCHAR(255)   NOT NULL,
                                last_modified_date DATETIME2      NOT NULL
);
