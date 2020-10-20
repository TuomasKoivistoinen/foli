## Tuote

Ekasta versiosta tulee pienin mahdollinen, mutta listataan kaikki mikä tulee mieleen.

### A. Pysäkin aikataulujen haku

1. Käyttäjänä voin pysäkkinumerolla hakea seuraavat lähtevät vuorot, jotta voin valita niistä itselleni parhaan.
    - Mitä tietoja vuoroista tulisi näyttää?
        - Päätöksenteon tueksi **oletettavista** riittää linja, kohde ja lähtöaika
    - Missä järjestyksessä vuorojen pitäisi näkyä?
        - Järjestys lähtöajan mukaan. Seuraava ensimmäisenä. Linjan ja kohteen mukaan ryhmittely tai filtteröinti voivat
         olla testaamisen arvoisia asioita myöhemmässä vaiheessa.
    - Voiko haun tehdä myös pysäkin nimen perusteella?
        - Testataan myös nimellä hakua.
    - Mitä tietoja on saatavilla rajapinnasta?
        - Tarkemmat tiedot [Fölin](https://data.foli.fi/doc/index) omasta dokumentaatiosta ja esim. GTFS tapauksessa
        [Googlen](https://developers.google.com/transit/gtfs/reference) dokumentaatiosta
    ```
   GTFS sisältää staattisen harvemmin muuttuvan tietosisällön pysäkeistä, linjoista sekä näiden suunnitelluista aikatauluista.
   SIRI sisältää reaaliaikaista tietoa ajoneuvojen liikkeistä, sekä laskennallista ennakoitua tietoa toteutuvista aikatauluista.
   ALERTS sisältää reaaliaikaisesti voimassaolevat viestit, tiedotteet ja mahdolliset linjojen peruutukset.
   GEONJSON sisältää paikkatietoa Fölin palveluista.
   CITYBIKE sisältää Turun Fölläri-kaupunkipyörien noutoasemien tilan.
   Tilastotietoja pyörien käytöstä on saatavilla linkistä : https://www.avoindata.fi/data/fi/dataset/turun-seudun-kaupunkipyorat
   ```

    - Acceptance criteria
        - Esimerkiksi käyttäjä valitsee pysäkkinumeron 59 (Kotimäenkatu), jonka jälkeen hän näkee linjan, kohteen ja
        lähtöajan seuraaville vuoroille.
        
## MVP

Ekaan versioon vain US A1.