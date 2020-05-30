<!--
https://www.youtube.com/watch?v=U4y2R3v9tlY
http://cloudurable.com/blog/kafka-tutorial-kafka-from-command-line/index.html
-->

# Kafka Usage Example

Lo scopo di questa repository è quello di creare degli appunti usabili per comprendere, modificare e creare una coda kafka.

Userò anche docker per contenere kafka e zookeeper in modo da avere una white box degli strumenti necessari.

## Index

* [Docker](#docker)
	* docker-compose
* [Kafka](#kafka)

## Docker

Docker è un tool di sviluppo che permette di sviluppare, rilasciare ed eseguire applicazioni all'interno di container.
Questo consente di non modificare l'environment di sviluppo mantenendolo sicuro da modifiche.
Per informazioni aggiuntive leggere al [documentazione](https://docs.docker.com/get-started/).

### Create docker-compose

[Docker-compose](https://docs.docker.com/compose/) è uno strumento usato per definire e eseguire container multipli Docker.
Per poter usare questo strumento è necessario compilare un file in linguaggio YAML, per ulteriori informazioni è possibile leggere la [documentazione ufficiale](https://docs.docker.com/compose/).

Il docker compose creato per questo progetto è il seguente:

```YAML
version: '2'
services:

  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    ports:
      - "2181:2181"
    environment:
        ZOOKEEPER_CLIENT_PORT: 2181
        ZOOKEEPER_TICK_TIME: 2000
    volumes:
      - ./zk-data/zookeeper/data:/var/lib/zookeeper/data

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - 9092:9092
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 192.168.1.84
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_BROKER_ID: 1
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_DEFAULT_REPLICATION_FACTOR: 1
    volumes:
      - ./zk-data/kafka/data:/var/lib/kafka/data
    depends_on:
      - zookeeper

```

Andiamo ad analizzare più a fondo le configurazioni definite:

* **version**: serve per indicare la versione del docker-compose da usare durante il parsing del file
* **services**: all'interno di questo campo saranno presenti tutti i servizi/container che la build dovrà usare
    * ***zookeeper***: definizione del container chiamato zookeeper
        * `image: zookeeper` indica l'immagine docker che il container dovrà contenere
        * `ports: ...` è la lista di tutte le porte che verranno lasciate aperte per poter accedere ai servizi del container
        * `environment: ...` sono le variabili d'ambiente definite per il contenitore
        * `volumes: ...` ogni elemento di questa lista rappresenta il volume nella memoria locale a cui dovrà fare riferimento un dato volume interno al container, nel caso sopra avremo che la directory `/var/lib/zookeeper/data` sarà logicamente riferita alla directory `./zk-kafka/kafka/data`
    * ***kafka***: definizione del container chiamato kafka
        * stessi argomenti di zookeeper
        * `depends_on: ...` è un campo in cui vengono indicati i containers che devono essere attivi

### Run docker-compose

Per eseguire il multi-container definito sopra è ovviamente necessario installare docker e docker-compose.
Una volta installati è possibile eseguire il comando:

```shell script
docker-compose up -d
```

Il comando eseguirà il parse del file docker-compose.yml e eseguirà i due container.
A questo punto sarà possibile utilizzare kafka localmente.

## Kafka

(*Ho scritto alcuni appunti su questo strumento ed è possibile trovarli qui: [kafka notes](https://wabri.github.io/post/apache_kafka/))

Kafka è uno strumento usato per la messaggistica real-time.
Le sue caratteristiche sono varie: è tollerante nei fallimenti, è altamente scalabile e soprattutto può processare e inviare milioni di messaggi al secondo verso molti ricevitori.
Può essere usato in diverse situazioni tra cui:
* Servizi di messaggistica istantanea
* Servizio per processare in real-time stream di dati
* Aggregazione di log da più sistemi verso un centrale
* Servizi di logging in sistemi distribuiti
* Mantenimento di sequenze ordinate di eventi

Il funzionamento di questo strumento si basa sulla comunicazione tra oggetti di 3 tipi:
* **Producer**, è l'oggetto che produce il messaggio e lo invia a kafka
* **Consumer**, colui che consuma il messaggio contenuto da kafka
* **Broker**, è l'oggetto che esegue le mansioni intermedie

## Zookeeper

ZooKeeper è un servizio di coordinamento ad alte prestazioni per applicazioni
distribuite. Viene utilizzato per implementare protocolli di consenso, gestione
ed elezioni di leader e presenza. In questo caso viene sfruttato come backend
per gestire le code presenti all'interno di kafka.

### Creazione del produttore











