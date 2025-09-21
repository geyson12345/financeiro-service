ALTER TABLE conta_bancaria
    ADD COLUMN pessoa_Id_Titular BIGINT NULL;

ALTER TABLE conta_bancaria_aud
    ADD COLUMN pessoa_Id_Titular BIGINT NULL;

ALTER TABLE conta_bancaria
    ALTER COLUMN codigo_operacao TYPE VARCHAR(5),
    ALTER COLUMN codigo_operacao DROP NOT NULL;

CREATE INDEX idx_conta_bancaria_pessoa_Id_Titular
    ON conta_bancaria (pessoa_Id_Titular);
