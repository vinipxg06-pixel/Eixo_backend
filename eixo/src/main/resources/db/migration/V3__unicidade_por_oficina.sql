-- V3 - Regras de unicidade passam a valer por oficina (e não mais no sistema inteiro)
-- Cada oficina tem seus próprios clientes, veículos e estoque: a mesma placa, o mesmo CPF/CNPJ
-- ou o mesmo código de barras podem existir em oficinas diferentes, mas nunca repetidos na mesma oficina.
-- Observação: no MySQL, índices UNIQUE aceitam vários NULL — CPF/CNPJ e código de barras continuam opcionais.

ALTER TABLE veiculos
    DROP INDEX `placa_UNIQUE`,
    ADD UNIQUE INDEX `uk_veiculos_oficina_placa` (`oficinas_id_oficina`, `placa`);

ALTER TABLE clientes
    DROP INDEX `cpf_cnpj_UNIQUE`,
    ADD UNIQUE INDEX `uk_clientes_oficina_cpf_cnpj` (`oficinas_id_oficina`, `cpf_cnpj`);

ALTER TABLE pecas_estoque
    DROP INDEX `codigo_barras_UNIQUE`,
    ADD UNIQUE INDEX `uk_pecas_estoque_oficina_codigo_barras` (`oficinas_id_oficina`, `codigo_barras`);
