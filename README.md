# familie-historikk
Applikasjon for historikkinnslag. Denne applikasjonen kan brukes av alle familieapplikasjoner for å hente,lagre og vedlikeholde historikkinnslag. 
Foreløpig brukes den for Tilbakekrevingsapplikasjon.

## Bygging
Bygging gjøres med `mvn clean compile`.

## Kjøring lokalt
For å kjøre opp appen lokalt kan en kjøre `LauncherLokal`. 
Dette krever at du har logget deg på gcloud `gcloud auth login` og at du er på Naisdevice.  
Appen tilgjengeliggjøres da på `localhost:8050`.

### Database
Dersom man vil kjøre med postgres, kan man kjøre `LauncherLokalPostgres`.
Dette krever at du har logget deg på gcloud `gcloud auth login` og at du er på Naisdevice. 
I tillegg må man sette opp postgres-databasen, dette gjøres slik:
```
# Den første kommandoen er kun hvis du bare kjører familie-historikk applikasjon
# Ellers kan du unngå den og opprette bare database.
docker run --name familie-historikk-postgres -e POSTGRES_PASSWORD=test -d -p 5432:5432 postgres
docker ps (finn container id)
(for mac) docker exec -it <container_id> bash
(for wondows) winpty docker exec -it <container_id> bash(fra git-bash windows)
psql -U postgres
CREATE DATABASE "familie-historikk";
\l (til å verifisere om databasen er opprettet)
```
### Secrets
Secrets hentes automatisk. Dette krever at du har logget deg på gcloud `gcloud auth login` og at du er på Naisdevice.  

## Produksjonssetting
Main-branchen blir automatisk bygget ved merge og deployet til prod.

## Kontaktinformasjon
For NAV-interne kan henvendelser om applikasjonen rettes til #team-familie-tilbakekreving på slack.
Ellers kan man opprette et issue her på github.
