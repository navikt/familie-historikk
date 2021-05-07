CREATE TABLE historikkinnslag
(
    id                UUID PRIMARY KEY,
    versjon           BIGINT                              NOT NULL,
    behandling_id     UUID                                NOT NULL,
    ekstern_fagsak_id VARCHAR                             NOT NULL,
    type              VARCHAR                             NOT NULL,
    opprettet_av      VARCHAR      DEFAULT 'VL'           NOT NULL,
    opprettet_tid     TIMESTAMP(3) DEFAULT localtimestamp NOT NULL,
    endret_av         VARCHAR,
    endret_tid        TIMESTAMP(3)
);

COMMENT ON TABLE historikkinnslag
    IS 'Historikk over hendelser i saken';

COMMENT ON COLUMN historikkinnslag.id
    IS 'Primary key';

COMMENT ON COLUMN historikkinnslag.behandling_id
    IS 'Fagsystems/Tilbakekrevings behandling id som historikkinnslag er lagret for';

COMMENT ON COLUMN historikkinnslag.ekstern_fagsak_id
    IS 'Saksnummer (som gsak har mottatt)';

COMMENT ON COLUMN historikkinnslag.type
    IS 'Type av historikkinnslag';

CREATE INDEX ON historikkinnslag (behandling_id);

CREATE INDEX ON historikkinnslag (ekstern_fagsak_id);

CREATE INDEX ON historikkinnslag (type);


CREATE TABLE historikkinnslag_detaljer
(
    id                         UUID PRIMARY KEY,
    versjon                    BIGINT                              NOT NULL,
    historikkinnslag_id        UUID                                NOT NULL REFERENCES historikkinnslag,
    tekst                      VARCHAR,
    historikkinnslag_felt_type VARCHAR                             NOT NULL,
    opprettet_av               VARCHAR      DEFAULT 'VL'           NOT NULL,
    opprettet_tid              TIMESTAMP(3) DEFAULT localtimestamp NOT NULL,
    endret_av                  VARCHAR,
    endret_tid                 TIMESTAMP(3)
);

COMMENT ON TABLE historikkinnslag_detaljer
    IS 'Tabell for historikkinnslags detaljer';

COMMENT ON COLUMN historikkinnslag_detaljer.id
    IS 'Primary key';

COMMENT ON COLUMN historikkinnslag_detaljer.historikkinnslag_id
    IS 'FK: historikkinnslag';

COMMENT ON COLUMN historikkinnslag_detaljer.tekst
    IS 'Tekst som beskriver hendelsen (som skal vises i historikkfanen)';

COMMENT ON COLUMN historikkinnslag_detaljer.historikkinnslag_felt_type
    IS 'Hva slags type informasjon som er representert';

CREATE INDEX ON historikkinnslag_detaljer (historikkinnslag_id);

CREATE INDEX ON historikkinnslag_detaljer (historikkinnslag_felt_type);

CREATE TABLE historikkinnslag_dok_link
(
    id                         UUID PRIMARY KEY,
    versjon                    BIGINT                              NOT NULL,
    historikkinnslag_id        UUID                                NOT NULL REFERENCES historikkinnslag,
    lenke                      VARCHAR                             NOT NULL,
    journalpost_id             VARCHAR,
    dokument_id                VARCHAR                             NOT NULL,
    opprettet_av               VARCHAR      DEFAULT 'VL'           NOT NULL,
    opprettet_tid              TIMESTAMP(3) DEFAULT localtimestamp NOT NULL,
    endret_av                  VARCHAR,
    endret_tid                 TIMESTAMP(3)
);

COMMENT ON TABLE historikkinnslag_dok_link
    IS 'Kobling fra historikkinnslag til aktuell dokumentasjon';

COMMENT ON COLUMN historikkinnslag_dok_link.id
    IS 'Primary key';

COMMENT ON COLUMN historikkinnslag_dok_link.historikkinnslag_id
    IS 'FK: historikkinnslag';

COMMENT ON COLUMN historikkinnslag_dok_link.lenke
    IS 'Tekst som vises for link til dokumentet)';

COMMENT ON COLUMN historikkinnslag_dok_link.journalpost_id
    IS 'Journal post Id';

COMMENT ON COLUMN historikkinnslag_dok_link.dokument_id
    IS 'Dokumnent Id';

CREATE INDEX ON historikkinnslag_dok_link (historikkinnslag_id);
