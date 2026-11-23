-- V5 - Inativação (exclusão lógica) de peças do estoque
-- Regra: peça que já foi usada (orçamento, OS ou caixa) não pode ser excluída, apenas inativada.
-- Peça inativa continua no histórico, mas não pode ser adicionada a novos orçamentos/OS.
ALTER TABLE pecas_estoque
    ADD COLUMN `ativa` BOOLEAN NOT NULL DEFAULT TRUE;
