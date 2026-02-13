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
