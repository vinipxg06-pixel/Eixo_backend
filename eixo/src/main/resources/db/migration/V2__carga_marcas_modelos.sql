-- V2 - Carga inicial de marcas e modelos de veículos
-- Idempotente: pode rodar em bancos que já possuem esses dados (não duplica registros).

-- Marcas
INSERT IGNORE INTO marcas_veiculo (nome_marca) VALUES
('Volkswagen'),
('Honda'),
('Chevrolet'),
('Fiat'),
('Toyota'),
('Hyundai'),
('Jeep'),
('Ford'),
('Mitsubishi'),
('BYD'),
('Audi'),
('BMW'),
('Mercedes'),
('Peugeot'),
('Citroen'),
('Renault'),
('GWM'),
('Kia'),
('Land Rover'),
('Lexus'),
('Nissan'),
('Subaru'),
('Suzuki'),
('Volvo');

-- Modelos (cada bloco só insere o que ainda não existe para a marca)

-- Volkswagen
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Gol' AS nome_modelo
    UNION ALL SELECT 'Polo'
    UNION ALL SELECT 'Fusca'
    UNION ALL SELECT 'Fox'
    UNION ALL SELECT 'Voyage'
    UNION ALL SELECT 'Saveiro'
    UNION ALL SELECT 'Golf'
    UNION ALL SELECT 'Jetta'
    UNION ALL SELECT 'T-Cross'
    UNION ALL SELECT 'Taos'
    UNION ALL SELECT 'Tera'
    UNION ALL SELECT 'Up'
    UNION ALL SELECT 'Tiguan'
    UNION ALL SELECT 'Parati'
    UNION ALL SELECT 'Brasilia'
    UNION ALL SELECT 'Variant'
    UNION ALL SELECT 'Santana'
    UNION ALL SELECT 'Nivus'
    UNION ALL SELECT 'Virtus'
    UNION ALL SELECT 'Amarok'
    UNION ALL SELECT 'Passat'
    UNION ALL SELECT 'Kombi'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Volkswagen'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Honda
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Civic' AS nome_modelo
    UNION ALL SELECT 'City'
    UNION ALL SELECT 'Fit'
    UNION ALL SELECT 'HR-V'
    UNION ALL SELECT 'WR-V'
    UNION ALL SELECT 'CR-V'
    UNION ALL SELECT 'Accord'
    UNION ALL SELECT 'ZR-V'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Honda'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Chevrolet
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Onix' AS nome_modelo
    UNION ALL SELECT 'Onix Plus'
    UNION ALL SELECT 'Omega'
    UNION ALL SELECT 'Opala'
    UNION ALL SELECT 'Prisma'
    UNION ALL SELECT 'Celta'
    UNION ALL SELECT 'Corsa'
    UNION ALL SELECT 'Classic'
    UNION ALL SELECT 'Cruze'
    UNION ALL SELECT 'Tracker'
    UNION ALL SELECT 'Veraneio'
    UNION ALL SELECT 'Spin'
    UNION ALL SELECT 'Sonic'
    UNION ALL SELECT 'S10'
    UNION ALL SELECT 'Montana'
    UNION ALL SELECT 'Malibu'
    UNION ALL SELECT 'Astra'
    UNION ALL SELECT 'Agile'
    UNION ALL SELECT 'Captiva'
    UNION ALL SELECT 'Trailblazer'
    UNION ALL SELECT 'Equinox'
    UNION ALL SELECT 'Silverado'
    UNION ALL SELECT 'Cobalt'
    UNION ALL SELECT 'Vectra'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Chevrolet'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Fiat
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Uno' AS nome_modelo
    UNION ALL SELECT 'Palio'
    UNION ALL SELECT 'Siena'
    UNION ALL SELECT 'Strada'
    UNION ALL SELECT 'Scudo'
    UNION ALL SELECT 'Punto'
    UNION ALL SELECT '500'
    UNION ALL SELECT 'Idea'
    UNION ALL SELECT 'Mobi'
    UNION ALL SELECT 'Argo'
    UNION ALL SELECT 'Cronos'
    UNION ALL SELECT 'Toro'
    UNION ALL SELECT 'Titano'
    UNION ALL SELECT 'Pulse'
    UNION ALL SELECT 'Fastback'
    UNION ALL SELECT 'Doblo'
    UNION ALL SELECT 'Ducato'
    UNION ALL SELECT 'Fiorino'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Fiat'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Toyota
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Corolla' AS nome_modelo
    UNION ALL SELECT 'Corolla Cross'
    UNION ALL SELECT 'Hilux'
    UNION ALL SELECT 'SW4'
    UNION ALL SELECT 'Etios'
    UNION ALL SELECT 'Yaris'
    UNION ALL SELECT 'RAV4'
    UNION ALL SELECT 'Bandeirante'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Toyota'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Hyundai
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'HB20' AS nome_modelo
    UNION ALL SELECT 'HB20S'
    UNION ALL SELECT 'Creta'
    UNION ALL SELECT 'Tucson'
    UNION ALL SELECT 'Elantra'
    UNION ALL SELECT 'Veloster'
    UNION ALL SELECT 'ix35'
    UNION ALL SELECT 'i30'
    UNION ALL SELECT 'Santa Fe'
    UNION ALL SELECT 'Azera'
    UNION ALL SELECT 'Veracruz'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Hyundai'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Jeep
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Avenger' AS nome_modelo
    UNION ALL SELECT 'Renegade'
    UNION ALL SELECT 'Compass'
    UNION ALL SELECT 'Commander'
    UNION ALL SELECT 'Wrangler'
    UNION ALL SELECT 'Cherokee'
    UNION ALL SELECT 'Grand Cherokee'
    UNION ALL SELECT 'Gladiator'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Jeep'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Ford
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Ka' AS nome_modelo
    UNION ALL SELECT 'Fiesta'
    UNION ALL SELECT 'EcoSport'
    UNION ALL SELECT 'Focus'
    UNION ALL SELECT 'Fusion'
    UNION ALL SELECT 'Ranger'
    UNION ALL SELECT 'Escort'
    UNION ALL SELECT 'Corcel'
    UNION ALL SELECT 'Maverick'
    UNION ALL SELECT 'Territory'
    UNION ALL SELECT 'Pampa'
    UNION ALL SELECT 'Bronco Sport'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Ford'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Mitsubishi
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'L200 Triton' AS nome_modelo
    UNION ALL SELECT 'Pajero Full'
    UNION ALL SELECT 'Pajero Dakar'
    UNION ALL SELECT 'Pajero TR4'
    UNION ALL SELECT 'ASX'
    UNION ALL SELECT 'Outlander'
    UNION ALL SELECT 'Eclipse Cross'
    UNION ALL SELECT 'Lancer'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Mitsubishi'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- BYD
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Dolphin' AS nome_modelo
    UNION ALL SELECT 'Dolphin Mini'
    UNION ALL SELECT 'Song Plus'
    UNION ALL SELECT 'Song Pro'
    UNION ALL SELECT 'Yuan Plus'
    UNION ALL SELECT 'Yuan Pro'
    UNION ALL SELECT 'Han'
    UNION ALL SELECT 'Seal'
    UNION ALL SELECT 'King'
    UNION ALL SELECT 'Shark'
    UNION ALL SELECT 'Sealion'
    UNION ALL SELECT 'Tan'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'BYD'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Audi
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'A3' AS nome_modelo
    UNION ALL SELECT 'A4'
    UNION ALL SELECT 'A5'
    UNION ALL SELECT 'A6'
    UNION ALL SELECT 'Q3'
    UNION ALL SELECT 'Q5'
    UNION ALL SELECT 'Q7'
    UNION ALL SELECT 'TT'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Audi'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- BMW
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Serie 3 (320i)' AS nome_modelo
    UNION ALL SELECT 'Serie 1'
    UNION ALL SELECT 'Serie 5'
    UNION ALL SELECT 'X1'
    UNION ALL SELECT 'X2'
    UNION ALL SELECT 'X3'
    UNION ALL SELECT 'X5'
    UNION ALL SELECT 'X6'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'BMW'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Mercedes
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Classe A' AS nome_modelo
    UNION ALL SELECT 'Classe C (C180/C200)'
    UNION ALL SELECT 'CLA'
    UNION ALL SELECT 'GLA'
    UNION ALL SELECT 'GLC'
    UNION ALL SELECT 'Classe E'
    UNION ALL SELECT 'GLE'
    UNION ALL SELECT 'Sprinter'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Mercedes'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Peugeot
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT '206' AS nome_modelo
    UNION ALL SELECT '207'
    UNION ALL SELECT '208'
    UNION ALL SELECT '2008'
    UNION ALL SELECT '307'
    UNION ALL SELECT '308'
    UNION ALL SELECT '3008'
    UNION ALL SELECT '408'
    UNION ALL SELECT 'Partner'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Peugeot'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Citroen
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'C3' AS nome_modelo
    UNION ALL SELECT 'Aircross'
    UNION ALL SELECT 'C3 Aircross'
    UNION ALL SELECT 'C4 Cactus'
    UNION ALL SELECT 'C4 Lounge'
    UNION ALL SELECT 'Xsara Picasso'
    UNION ALL SELECT 'C4 Picasso'
    UNION ALL SELECT 'C5 Aircross'
    UNION ALL SELECT 'Basalt'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Citroen'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Renault
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Kwid' AS nome_modelo
    UNION ALL SELECT 'Boreal'
    UNION ALL SELECT 'Clio'
    UNION ALL SELECT 'Sandero'
    UNION ALL SELECT 'Logan'
    UNION ALL SELECT 'Duster'
    UNION ALL SELECT 'Captur'
    UNION ALL SELECT 'Oroch'
    UNION ALL SELECT 'Megane'
    UNION ALL SELECT 'Scenic'
    UNION ALL SELECT 'Kardian'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Renault'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- GWM
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Haval H6' AS nome_modelo
    UNION ALL SELECT 'Haval H6 GT'
    UNION ALL SELECT 'Ora 03'
    UNION ALL SELECT 'Tank 300'
    UNION ALL SELECT 'Haval H9'
    UNION ALL SELECT 'Poer P30'
    UNION ALL SELECT 'Wey 07'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'GWM'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Kia
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Sportage' AS nome_modelo
    UNION ALL SELECT 'Cerato'
    UNION ALL SELECT 'Picanto'
    UNION ALL SELECT 'Sorento'
    UNION ALL SELECT 'Soul'
    UNION ALL SELECT 'Carnival'
    UNION ALL SELECT 'Bongo'
    UNION ALL SELECT 'Stonic'
    UNION ALL SELECT 'Optima'
    UNION ALL SELECT 'Mohave'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Kia'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Land Rover
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Range Rover Evoque' AS nome_modelo
    UNION ALL SELECT 'Discovery Sport'
    UNION ALL SELECT 'Defender'
    UNION ALL SELECT 'Range Rover Sport'
    UNION ALL SELECT 'Discovery'
    UNION ALL SELECT 'Freelander 2'
    UNION ALL SELECT 'Range Rover Velar'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Land Rover'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Lexus
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'UX' AS nome_modelo
    UNION ALL SELECT 'NX'
    UNION ALL SELECT 'RX'
    UNION ALL SELECT 'ES'
    UNION ALL SELECT 'IS'
    UNION ALL SELECT 'CT 200h'
    UNION ALL SELECT 'LS'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Lexus'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Nissan
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Kicks' AS nome_modelo
    UNION ALL SELECT 'Versa'
    UNION ALL SELECT 'March'
    UNION ALL SELECT 'Sentra'
    UNION ALL SELECT 'Frontier'
    UNION ALL SELECT 'Altima'
    UNION ALL SELECT 'Armada'
    UNION ALL SELECT 'Kait'
    UNION ALL SELECT 'Livina'
    UNION ALL SELECT 'Murano'
    UNION ALL SELECT 'Tiida'
    UNION ALL SELECT 'X-Trail'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Nissan'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Subaru
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Impreza' AS nome_modelo
    UNION ALL SELECT 'Forester'
    UNION ALL SELECT 'XV'
    UNION ALL SELECT 'WRX'
    UNION ALL SELECT 'Legacy'
    UNION ALL SELECT 'Outback'
    UNION ALL SELECT 'Tribeca'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Subaru'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Suzuki
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'Jimny' AS nome_modelo
    UNION ALL SELECT 'Grand Vitara'
    UNION ALL SELECT 'Vitara'
    UNION ALL SELECT 'SX4'
    UNION ALL SELECT 'S-Cross'
    UNION ALL SELECT 'Swift'
    UNION ALL SELECT 'Samurai'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Suzuki'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);

-- Volvo
INSERT INTO modelos_veiculo (nome_modelo, marcas_veiculo_id_marca)
SELECT novos.nome_modelo, marca.id_marca
FROM (
    SELECT 'XC60' AS nome_modelo
    UNION ALL SELECT 'XC40'
    UNION ALL SELECT 'XC90'
    UNION ALL SELECT 'EX30'
    UNION ALL SELECT 'EX40'
    UNION ALL SELECT 'EC40'
    UNION ALL SELECT 'V40'
    UNION ALL SELECT 'S60'
) AS novos
JOIN marcas_veiculo marca ON marca.nome_marca = 'Volvo'
WHERE NOT EXISTS (
    SELECT 1 FROM modelos_veiculo existente
    WHERE existente.nome_modelo = novos.nome_modelo
      AND existente.marcas_veiculo_id_marca = marca.id_marca
);
