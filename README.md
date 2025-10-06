# Distributed Virtual File System (DVFS)

Un file system distribuito basato su architettura client-server con comunicazione tramite **Java RMI**.  
Il progetto permette a più client di interagire con un file system remoto come se fosse locale, con supporto a directory, file e symlink.

---

## Requisiti
- **openjdk** 25.0.1 2025-09-16
- **bash** (per gli script di build e run)

---

## Struttura

- `shared/` → interfacce e logica comune  
- `server/` → implementazione lato server  
- `client/` → implementazione lato client (CLI)  
- `test/` → test di concorrenza e funzionalità  
- `build/` → cartella generata dopo la compilazione

---

## Compilazione

Per compilare l’intero progetto:

```bash
./compile.bash
```

I file `.class` verranno salvati in `build/`.

Per pulire la build:

```bash
./clean.bash
```

---

## Esecuzione

### 1. Avvio del server
Il server monta una directory reale come root del VFS e resta in ascolto delle richieste RMI:

```bash
./run-server.bash
```

Output atteso:
```
Avvio del server RMI...
FileSystem RMI Server is ready.
```

---

### 2. Avvio del client
Il client è una CLI interattiva:

```bash
./run-client.sh
```

Esempio di sessione:
```
RMI File System Client
>>> mkdir /docs
Directory created
>>> mknod /docs/file.txt
File created
>>> write /docs/file.txt Hello World
Written
>>> read /docs/file.txt
Hello World
>>> exit
```

---

## Test

Per eseguire i test di concorrenza e consistenza:

```bash
./run-tests.bash
```

---

## Comandi disponibili nel client

- `mkdir /path` → crea directory  
- `mknod /file` → crea file vuoto  
- `symlink <target> <linkPath>` → crea symlink  
- `write /file contenuto` → scrive dati in un file  
- `read /file` → legge contenuto del file  
- `ls /dir` → lista contenuti directory  
- `rename oldPath newPath` → rinomina/sposta file o cartella  
- `rmdir /dir` → rimuove directory vuota  
- `edit /file` → apre il file in `vim` e risincronizza le modifiche  
- `exit` → chiude il client  
