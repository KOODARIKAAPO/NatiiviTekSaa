Viikko5

---

Mitä Retrofit tekee?

Retrofit on HTTP-kirjasto joka tekee REST API- kutsujen tekemisestä helppoa.
Se tekee käyttää OkHttp-kirjastoa yhteyksiin ja palauttaa vastaukset kotlin-objekteina
HTTP-pyyntöjen avulla. Softa tekee GET-pyynnön retrofitin avulla. 
Ja palauttaa luettavaa dataa.

---

Miten JSON muutetaan dataluokiksi?

API palauttaa JSON-objektin joka vastaa data class -rakennetta. 
Retrofit ja gson muuntaa objektin automaattisesti data classeiksi.

---

Miten coroutines toimii tässä

API-kutsut tehdään viewModelScocpe taustasäikeessa. 
UI ei jäädy, ja tila päivittyy automaattisesti kun data päivittyy.
ViewModel hallitsee StateFlowta stateflown avulla uitä. Compose on viritetty
kuuntelemaan tilaa ja näinn ollen päivittyy vain tilan muuttuessa.

---

Miten UI-tila toimii

ViewModel hallitsee  WeatherUiState-olion stateflown avulla. Compose on viritetty
kuuntelemaan tilaa ja näinn ollen päivittyy vain tilan muuttuessa.

---

Miten API-key on tallennettu

API-avain on tallennettu loca.properties-tiedostoon. 
Gradle lukee sen ja luo OPENWEATHER_API_KEY vakion. 
Avainta käytetään retrofit kutsuissa, näin vältytään api avaimen liikkaukselta.

---

Viikko6

---

Mitä Room tekee (Entity–DAO–Database–Repository–ViewModel–UI)

-Entity mmäärittelee tietokannan rakenteen. Room luo SQLite-taulun automaattisesti.
-DAO sisältää niinsanotut 'sql kyselyt' eli queryt. Room generoi toteutuksen automaattisesti
-Database yhdistää entityt ja daot yhteen tietokantaan. Room luo SQLite-tiedoston puhelimen muistiin
-Repository toimii välikätenä roomin ja API:n välillä. TTL välimuisti toteutettu.
-ViewModel hallinnoi UI:n tilaa. Kuten aikaisemmissakin se erottaa UI ja datan.
-UI käyttää stateflowta. Ja on täysin erotettu apista ja daosta. Se kuuntelee vain viewModel tilaa

---

Projektisi rakenne

├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   └── WeatherDao.kt
│   │
│   ├── model/
│   │   ├── WeatherResponse.kt        (API-malli)
│   │   ├── WeatherCacheEntity.kt     (Room-taulu)
│   │   └── SearchHistoryEntity.kt    (Room-taulu)
│   │
│   ├── remote/
│   │   ├── WeatherApi.kt
│   │   └── RetrofitInstance.kt
│   │
│   ├── repository/
│   │   └── WeatherRepository.kt
│   │
│   └── mapper/
│       └── WeatherMappers.kt
│
├── ui/
│   ├── components/
│   │   ├── SearchBar.kt
│   │   ├── WeatherContent.kt
│   │   └── ErrorScreen.kt
│   │
│   └── screens/
│       └── weather/
│           ├── WeatherScreen.kt
│           └── WeatherViewModel.kt
│
├── util/
│   └── Result.kt
│
└── MainActivity.kt

Sovellus on rakennettu MVVM arkkitehtuurin mukaisesti.

data/local, 
sisältää Room-tietokannan: 
-entity-luokat 
-DAO
-database 

data/remote
sisältää apikutsut:
-retrofit
-weatherapi

data/repository
yhdistää:
-room-välimuistin
-api-haut
-ttl-logiikan

ui 
Käyttöliittymä:
-composet
-näytöt
-viewmodel

Perjaatteena on, että ui on erotettu api kutsuista ja tietokannasta suoraan. ViewModel kommunikoi
repository komponentin kanssa joka hallitsee välimuistia


---

Miten datavirta kulkee

UI (Käyttäjä hakee kaupungin)
    → Käyttäjä syöttää kaupungin ja painaa Hae
    → UI kutsuu viewModel.searchWeather()

 ↓

ViewModel
    → Lukee hakusanan (searchQuery)
    → Asettaa tilan Loading
    → Kutsuu repository.refreshIfNeeded(query)
    → Ei käsittele API:a tai tietokantaa suoraan
    → Hallitsee UI:n tilaa (Loading / Success / Error)

 ↓

Repository
    → Tarkistaa ensin Room-välimuistin
    → Laskee datan iän (TTL 30 min)
    → Jos data on tuore → ei API-kutsua
    → Jos data on vanha → hakee API:sta ja tallentaa Roomiin

 ↓

Room (SQLite-tietokanta laitteella)
    → Tallentaa datan pysyvästi laitteen muistiin
    → Kun upsertCache() kutsutaan,
      Room päivittää taulun automaattisesti
    → Room ei itse päivitä UI:ta,
      vaan lähettää muutoksen Flow-virran kautta

 ↓

Flow
    → DAO palauttaa Flow<WeatherCacheEntity?>
    → Kun tietokantataulu muuttuu,
      Flow lähettää uuden arvon automaattisesti
    → Tämä tekee UI:sta reaktiivisen
    → Ei tarvita manuaalista "hae uudelleen" -kutsua

 ↓

ViewModel
    → Kuuntelee Roomin Flowta
    → Päivittää weatherState-tilan (Result.Success)
    → Toimii välissä UI:n ja datakerroksen välillä

 ↓

UI (Compose)
    → Käyttää collectAsState() kuunnellakseen weatherStatea
    → Kun tila muuttuu, Compose piirtää näkymän uudelleen
    → Näyttää aina Roomista luetun datan
    → UI ei koskaan käytä API-vastausta suoraan


---

(Sää) miten välimuistilogiikka toimii
Repository tarkistaa ensin room-tietokannasta onko haetulle kaupungille tallennettua dataa.
Jos dataa löytyy, lasketaan ikä. 
Verrataan ikää TTL-arvoon joka tässä sovelluksessa 30 min.

---

