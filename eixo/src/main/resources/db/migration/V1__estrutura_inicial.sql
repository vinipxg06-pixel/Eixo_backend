-- V1 - Estrutura inicial do banco do Eixo
-- Origem: script do MySQL Workbench (EixoScriptDB.sql), adaptado para o Flyway:
--   * sem CREATE SCHEMA/USE (o Flyway roda no banco configurado em DB_URL)
--   * os_vinculada já criado como BOOLEAN (antes era ALTER TABLE avulso)

-- -----------------------------------------------------
-- Table `oficinas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oficinas` (
  `id_oficina` BIGINT NOT NULL AUTO_INCREMENT,
  `cnpj` VARCHAR(14) NOT NULL,
  `nome_oficina` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `telefone` VARCHAR(11) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_oficina`),
  UNIQUE INDEX `cnpj_UNIQUE` (`cnpj` ASC) VISIBLE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` BIGINT NOT NULL AUTO_INCREMENT,
  `nome_usuario` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` ENUM('Ativo', 'Inativo') NOT NULL DEFAULT 'Ativo',
  `oficinas_id_oficina` BIGINT NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_usuarios_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` BIGINT NOT NULL AUTO_INCREMENT,
  `status` ENUM('Ativo', 'Inativo') NOT NULL DEFAULT 'Ativo',
  `nome_cliente` VARCHAR(100) NOT NULL,
  `cpf_cnpj` VARCHAR(14) NULL,
  `telefone` VARCHAR(11) NOT NULL,
  `email` VARCHAR(100) NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `oficinas_id_oficina` BIGINT NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE INDEX `cpf_cnpj_UNIQUE` (`cpf_cnpj` ASC) VISIBLE,
  INDEX `fk_clientes_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  CONSTRAINT `fk_clientes_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `pecas_estoque`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pecas_estoque` (
  `id_estoque` BIGINT NOT NULL AUTO_INCREMENT,
  `nome_peca` VARCHAR(100) NOT NULL,
  `quantidade` DECIMAL(10,2) NOT NULL,
  `categoria` ENUM('Motor', 'Freios', 'Suspensao', 'Transmissao', 'Embreagem', 'Eletrica', 'Arrefecimento', 
  'Filtros', 'Lubrificante', 'Pneu', 'Roda', 'Correia', 'Bateria', 'Carroceria', 'Acessorios', 'Outros') NOT NULL,
  `codigo_barras` VARCHAR(100) NULL,
  `estoque_minimo` DECIMAL(10,2) NOT NULL,
  `preco_unitario` DECIMAL(10,2) NOT NULL,
  `unidade_medida` ENUM('UN', 'L', 'KG', 'M', 'CX') NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `oficinas_id_oficina` BIGINT NOT NULL,
  PRIMARY KEY (`id_estoque`),
  UNIQUE INDEX `codigo_barras_UNIQUE` (`codigo_barras` ASC) VISIBLE,
  INDEX `fk_pecas_estoque_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  CONSTRAINT `fk_pecas_estoque_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_pecas_estoque_quantidade`
    CHECK (`quantidade` >= 0),
  CONSTRAINT `chk_pecas_estoque_estoque_minimo`
    CHECK (`estoque_minimo` >= 0),
  CONSTRAINT `chk_pecas_estoque_preco_unitario`
    CHECK (`preco_unitario` >= 0))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `marcas_veiculo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `marcas_veiculo` (
  `id_marca` BIGINT NOT NULL AUTO_INCREMENT,
  `nome_marca` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_marca`),
  UNIQUE INDEX `nome_marca_UNIQUE` (`nome_marca` ASC) VISIBLE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `modelos_veiculo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `modelos_veiculo` (
  `id_modelo` BIGINT NOT NULL AUTO_INCREMENT,
  `nome_modelo` VARCHAR(100) NOT NULL,
  `marcas_veiculo_id_marca` BIGINT NOT NULL,
  PRIMARY KEY (`id_modelo`),
  INDEX `fk_modelos_veiculo_marcas_veiculo1_idx` (`marcas_veiculo_id_marca` ASC) VISIBLE,
  CONSTRAINT `fk_modelos_veiculo_marcas_veiculo1`
    FOREIGN KEY (`marcas_veiculo_id_marca`)
    REFERENCES `marcas_veiculo` (`id_marca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `veiculos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `veiculos` (
  `id_veiculo` BIGINT NOT NULL AUTO_INCREMENT,
  `placa` VARCHAR(7) NOT NULL,
  `combustivel` ENUM('Gasolina', 'Flex', 'Hibrido', 'Eletrico', 'Diesel', 'Etanol') NOT NULL,
  `cor` ENUM('Preto', 'Branco', 'Vermelho', 'Prata', 'Cinza', 'Verde', 'Azul') NULL,
  `ano` VARCHAR(4) NOT NULL,
  `quilometragem` BIGINT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `oficinas_id_oficina` BIGINT NOT NULL,
  `modelos_veiculo_id_modelo` BIGINT NOT NULL,
  `clientes_id_cliente` BIGINT NOT NULL,
  PRIMARY KEY (`id_veiculo`),
  UNIQUE INDEX `placa_UNIQUE` (`placa` ASC) VISIBLE,
  INDEX `fk_veiculos_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  INDEX `fk_veiculos_modelos_veiculo1_idx` (`modelos_veiculo_id_modelo` ASC) VISIBLE,
  INDEX `fk_veiculos_clientes1_idx` (`clientes_id_cliente` ASC) VISIBLE,
  CONSTRAINT `fk_veiculos_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_veiculos_modelos_veiculo1`
    FOREIGN KEY (`modelos_veiculo_id_modelo`)
    REFERENCES `modelos_veiculo` (`id_modelo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_veiculos_clientes1`
    FOREIGN KEY (`clientes_id_cliente`)
    REFERENCES `clientes` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `orcamentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orcamentos` (
  `id_orcamento` BIGINT NOT NULL AUTO_INCREMENT,
  `status` ENUM('Aprovado', 'Pendente', 'Recusado') NOT NULL DEFAULT 'Pendente',
  `mao_obra` DECIMAL(10,2) NULL,
  `descricao` VARCHAR(100) NOT NULL,
  `valor_total` DECIMAL(10,2) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `veiculos_id_veiculo` BIGINT NOT NULL,
  `clientes_id_cliente` BIGINT NOT NULL,
  `oficinas_id_oficina` BIGINT NOT NULL,
  PRIMARY KEY (`id_orcamento`),
  INDEX `fk_orcamentos_veiculos1_idx` (`veiculos_id_veiculo` ASC) VISIBLE,
  INDEX `fk_orcamentos_clientes1_idx` (`clientes_id_cliente` ASC) VISIBLE,
  INDEX `fk_orcamentos_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  CONSTRAINT `fk_orcamentos_veiculos1`
    FOREIGN KEY (`veiculos_id_veiculo`)
    REFERENCES `veiculos` (`id_veiculo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orcamentos_clientes1`
    FOREIGN KEY (`clientes_id_cliente`)
    REFERENCES `clientes` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orcamentos_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_orcamentos_mao_obra`
    CHECK (`mao_obra` >= 0),
  CONSTRAINT `chk_orcamentos_valor_total`
    CHECK (`valor_total` >= 0))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ordens_servicos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ordens_servicos` (
  `id_ordem_servico` BIGINT NOT NULL AUTO_INCREMENT,
  `status` ENUM('Aberta', 'Fechada') NOT NULL DEFAULT 'Aberta',
  `valor_total` DECIMAL(10,2) NOT NULL,
  `descricao` VARCHAR(255) NOT NULL,
  `data_abertura` DATE NOT NULL,
  `data_fechamento` DATE NULL,
  `mao_obra` DECIMAL(10,2) NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `oficinas_id_oficina` BIGINT NOT NULL,
  `clientes_id_cliente` BIGINT NOT NULL,
  `veiculos_id_veiculo` BIGINT NOT NULL,
  `orcamentos_id_orcamento` BIGINT NULL,
  PRIMARY KEY (`id_ordem_servico`),
  INDEX `fk_ordens_servicos_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  INDEX `fk_ordens_servicos_clientes1_idx` (`clientes_id_cliente` ASC) VISIBLE,
  INDEX `fk_ordens_servicos_veiculos1_idx` (`veiculos_id_veiculo` ASC) VISIBLE,
  INDEX `fk_ordens_servicos_orcamentos1_idx` (`orcamentos_id_orcamento` ASC) VISIBLE,
  INDEX `idx_ordens_servicos_data_abertura` (`data_abertura` ASC) VISIBLE,
  CONSTRAINT `fk_ordens_servicos_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ordens_servicos_clientes1`
    FOREIGN KEY (`clientes_id_cliente`)
    REFERENCES `clientes` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ordens_servicos_veiculos1`
    FOREIGN KEY (`veiculos_id_veiculo`)
    REFERENCES `veiculos` (`id_veiculo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ordens_servicos_orcamentos1`
    FOREIGN KEY (`orcamentos_id_orcamento`)
    REFERENCES `orcamentos` (`id_orcamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_ordens_servicos_valor_total`
    CHECK (`valor_total` >= 0),
  CONSTRAINT `chk_ordens_servicos_mao_obra`
    CHECK (`mao_obra` >= 0))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `os_pecas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `os_pecas` (
  `id_os_peca` BIGINT NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(10,2) NOT NULL,
  `quantidade` DECIMAL(10,2) NOT NULL,
  `pecas_estoque_id_estoque` BIGINT NOT NULL,
  `ordens_servicos_id_ordem_servico` BIGINT NOT NULL,
  PRIMARY KEY (`id_os_peca`),
  INDEX `fk_os_pecas_pecas_estoque1_idx` (`pecas_estoque_id_estoque` ASC) VISIBLE,
  INDEX `fk_os_pecas_ordens_servicos1_idx` (`ordens_servicos_id_ordem_servico` ASC) VISIBLE,
  CONSTRAINT `fk_os_pecas_pecas_estoque1`
    FOREIGN KEY (`pecas_estoque_id_estoque`)
    REFERENCES `pecas_estoque` (`id_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_os_pecas_ordens_servicos1`
    FOREIGN KEY (`ordens_servicos_id_ordem_servico`)
    REFERENCES `ordens_servicos` (`id_ordem_servico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_os_pecas_valor`
    CHECK (`valor` >= 0),
  CONSTRAINT `chk_os_pecas_quantidade`
    CHECK (`quantidade` >= 0))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `oc_pecas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oc_pecas` (
  `id_oc_peca` BIGINT NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(10,2) NOT NULL,
  `quantidade` DECIMAL(10,2) NOT NULL,
  `orcamentos_id_orcamento` BIGINT NOT NULL,
  `pecas_estoque_id_estoque` BIGINT NOT NULL,
  PRIMARY KEY (`id_oc_peca`),
  INDEX `fk_oc_pecas_orcamentos1_idx` (`orcamentos_id_orcamento` ASC) VISIBLE,
  INDEX `fk_oc_pecas_pecas_estoque1_idx` (`pecas_estoque_id_estoque` ASC) VISIBLE,
  CONSTRAINT `fk_oc_pecas_orcamentos1`
    FOREIGN KEY (`orcamentos_id_orcamento`)
    REFERENCES `orcamentos` (`id_orcamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_oc_pecas_pecas_estoque1`
    FOREIGN KEY (`pecas_estoque_id_estoque`)
    REFERENCES `pecas_estoque` (`id_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_oc_pecas_valor`
    CHECK (`valor` >= 0),
  CONSTRAINT `chk_oc_pecas_quantidade`
    CHECK (`quantidade` >= 0))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `movimentacao_fc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `movimentacao_fc` (
  `id_fluxo_caixa` BIGINT NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `tipo` ENUM('Entrada', 'Saida') NOT NULL,
  `valor` DECIMAL(10,2) NULL,
  `categoria` ENUM('Servico', 'Venda de Peca', 'Compra de Peca', 'Salario', 'Aluguel', 'Conta de Consumo', 
  'Manutencao de Equipamento', 'Imposto', 'Fornecedor', 'Marketing', 'Outros') NOT NULL,
  `data_movimentacao` DATE NOT NULL,
  `os_vinculada` BOOLEAN NOT NULL DEFAULT FALSE,
  `ordens_servicos_id_ordem_servico` BIGINT NULL,
  `pecas_estoque_id_estoque` BIGINT NULL,
  `oficinas_id_oficina` BIGINT NOT NULL,
  PRIMARY KEY (`id_fluxo_caixa`),
  INDEX `fk_movimentacao_fc_ordens_servicos1_idx` (`ordens_servicos_id_ordem_servico` ASC) VISIBLE,
  INDEX `fk_movimentacao_fc_pecas_estoque1_idx` (`pecas_estoque_id_estoque` ASC) VISIBLE,
  INDEX `fk_movimentacao_fc_oficinas1_idx` (`oficinas_id_oficina` ASC) VISIBLE,
  INDEX `idx_movimentacao_fc_data_movimentacao` (`data_movimentacao` ASC) VISIBLE,
  CONSTRAINT `fk_movimentacao_fc_ordens_servicos1`
    FOREIGN KEY (`ordens_servicos_id_ordem_servico`)
    REFERENCES `ordens_servicos` (`id_ordem_servico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_movimentacao_fc_pecas_estoque1`
    FOREIGN KEY (`pecas_estoque_id_estoque`)
    REFERENCES `pecas_estoque` (`id_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_movimentacao_fc_oficinas1`
    FOREIGN KEY (`oficinas_id_oficina`)
    REFERENCES `oficinas` (`id_oficina`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `chk_movimentacao_fc_valor`
    CHECK (`valor` >= 0))
ENGINE = InnoDB;
