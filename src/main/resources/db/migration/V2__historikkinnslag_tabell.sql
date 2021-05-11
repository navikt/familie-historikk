DROP TABLE IF EXISTS historikkinnslag_dok_link;

DROP TABLE IF EXISTS historikkinnslag_detaljer;

DROP TABLE IF EXISTS historikkinnslag;

CREATE TABLE historikkinnslag
(
    id                UUID PRIMARY KEY,
    behandling_id     VARCHAR                             NOT NULL,
    ekstern_fagsak_id VARCHAR                             NOT NULL,
    fagsystem         VARCHAR                             NOT NULL,
    applikasjon       VARCHAR                             NOT NULL,
    type              VARCHAR                             NOT NULL,
    aktor             VARCHAR                             NOT NULL,
    tittel            VARCHAR                             NOT NULL,
    tekst             VARCHAR,
    steg              VARCHAR,
    journalpost_id    VARCHAR,
    dokument_id       VARCHAR,
    opprettet_av      VARCHAR      DEFAULT 'VL'           NOT NULL,
    opprettet_tid     TIMESTAMP(3) DEFAULT localtimestamp NOT NULL
);

COMMENT ON TABLE historikkinnslag
    IS 'Historikk over hendelser i saken';

COMMENT ON COLUMN historikkinnslag.id
    IS 'Primary key';

COMMENT ON COLUMN historikkinnslag.behandling_id
    IS 'Fagsystems/Tilbakekrevings behandling id som historikkinnslag er lagret for';

COMMENT ON COLUMN historikkinnslag.ekstern_fagsak_id
    IS 'Saksnummer (som gsak har mottatt)';

COMMENT ON COLUMN historikkinnslag.fagsystem
    IS 'Fagsystemet som historikkinnslag er lagret for';

COMMENT ON COLUMN historikkinnslag.applikasjon
    IS 'Applikasjonen som er ansvarlig for å generere historikkinnslaget';

COMMENT ON COLUMN historikkinnslag.type
    IS 'Type av historikkinnslag';

COMMENT ON COLUMN historikkinnslag.aktor
    IS 'aktor type som opprettet historikkinnslaget';

COMMENT ON COLUMN historikkinnslag.tittel
    IS 'Tittel som vises i historikkfanen';

COMMENT ON COLUMN historikkinnslag.tekst
    IS 'Tekst som beskriver hendelsen (som skal vises i historikkfanen)';

COMMENT ON COLUMN historikkinnslag.steg
    IS 'vurderte stegnavn som opprettet historikkinnslaget';

COMMENT ON COLUMN historikkinnslag.journalpost_id
    IS 'Journal post Id';

COMMENT ON COLUMN historikkinnslag.dokument_id
    IS 'Dokumnent Id';


CREATE INDEX ON historikkinnslag (behandling_id);

CREATE INDEX ON historikkinnslag (ekstern_fagsak_id);

CREATE INDEX ON historikkinnslag (fagsystem);

CREATE INDEX ON historikkinnslag (applikasjon);







