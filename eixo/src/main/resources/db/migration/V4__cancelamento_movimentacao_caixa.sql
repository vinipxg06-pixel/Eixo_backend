-- V4 - Cancelamento (exclusão lógica) de lançamentos do fluxo de caixa
-- Regra: lançamentos não são apagados. Um lançamento manual cancelado continua no histórico,
-- mas deixa de ser contabilizado nos totais e nas métricas.
ALTER TABLE movimentacao_fc
    ADD COLUMN `cancelada` BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN `cancelada_em` DATETIME NULL;
