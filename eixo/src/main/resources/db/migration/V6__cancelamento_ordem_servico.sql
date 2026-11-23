-- V6 - Cancelamento de ordem de serviço
-- Regras:
-- - uma OS Aberta pode ser Fechada ou Cancelada; nenhuma das duas volta a ser Aberta;
-- - o cancelamento exige um motivo da lista padrão e devolve todas as peças ao estoque, sem lançamento no caixa;
-- - as linhas de peças continuam gravadas na OS cancelada como histórico.
ALTER TABLE ordens_servicos
    MODIFY COLUMN `status` ENUM('Aberta', 'Fechada', 'Cancelada') NOT NULL DEFAULT 'Aberta',
    ADD COLUMN `motivo_cancelamento` ENUM(
        'ClienteDesistiu',
        'ValorNaoAutorizado',
        'PecaIndisponivel',
        'DefeitoNaoSolucionado',
        'AbertaPorEngano',
        'Outro'
    ) NULL,
    ADD COLUMN `data_cancelamento` DATE NULL,
    ADD CONSTRAINT `chk_ordens_servicos_cancelamento`
        CHECK ((`status` = 'Cancelada') = (`motivo_cancelamento` IS NOT NULL));
