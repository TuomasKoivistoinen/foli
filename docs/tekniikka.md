## Tekniikka

Lähtökohtaisesti tämä toteutus on vain tapa harjoitella erilaisten tekniikoiden käyttöä.
Eniten harjoitusta haluaisin tällä hetkellä Reagent/re-frame kanssa.
Listaan ja punnitsen vaihtoehdot, jotta oppisin itse jotain ja herättäisin keskustelua. **Punnitseminen on todella
vaikeaa yksin biassen takia**.

### Frontend

Voi käyttää vain yhtä tai ueampaa yhdessä. Valintaan vaikuttaisi eniten tiimin preferenssit, mutta kuvitellaan että
kaikki osataan yhtä hyvin ja kaikista pidetään yhtä paljon.

#### Reagent

Reagent on minimalistinen interface React käyttöön ClojureScriptillä. Minimalistin state management.

```clojure
; komponentit, jotka dereffaa atomia rendaa uusiksi arvon muuttuessa
(defonce click-count (r/atom 0))

(defn state-ful-with-atom []
  [:div {:on-click #(swap! click-count inc)}
   "I have been clicked " @click-count " times."])
```
   
➕ Vähiten opittavaa  
➕ Minimaalinen overhead. Saa ratkaista geneeriset ongelmat tarkoituksenmukaisella tasolla   
➖ ["Falling into the pit of success"](https://blog.codinghorror.com/falling-into-the-pit-of-success/) ei ole yhtä
helppoa minimaalisella rakenteella  
➖ Full-stack story puuttuu

Mielipide:  
Paras vaihtoehto pieniin frontteihin, mutta hyvä isommissakin.

#### re-frame

Data-oriented ja event-driven frontend framework. Perustuu **data-looppiin**:

1. Lähetetään event -> Lasketaan vaikutukset -> Tehdään vaikutukset
2. Otetaan app statesta data komponenteille -> Komponentit laskee uuden domin -> React rendaa uuden domin

```clojure
(defn delete-button 
  [item-id]
  [:div.garbage-bin 
    :on-click #(re-frame.core/dispatch [:delete-item item-id])]) ;; Lähetetään event :delete-item

(defn h 
  [{:keys [db]} [_ item-id]]
  {:db  (dissoc-in db [:items item-id])}) ;; Lasketaan vaikutukset :delete-item eventille -> :db effect

(re-frame.core/reg-event-fx   
  :delete-item               
  h)                          

(re-frame.core/reg-fx       
  :db ;; Tehdään vaikutukset :db effectille                       
  (fn [val]                 
    (reset! app-db val))) 

(defn query-fn
  [db v]         
  (:items db)) ;; Otetaan app statesta data komponenteille

(re-frame.core/reg-sub  
   :query-items ;; Otetaan app statesta data komponenteile :query-items queryllä
   query-fn)        

(defn items-view
  []
  (let [items  (subscribe [:query-items])] ;; Otetaan app statesta data komponenteile :query-items queryllä 
    [:div (map item-render @items)]))

```
  
➖➕ Keskiverrosti opittavaa, mutta vastineeksi saa todella hyvän state management storyn fronttiin (jos se on
tarpeellinen)   
➕ Mielestäni frontin osalta 
["Falling into the pit of success"](https://blog.codinghorror.com/falling-into-the-pit-of-success/) onnistuu todella 
hyvin  
+ re-frame lähestymistapa mahdollistaa esim. time travelin ja clientin tilan snapshottaamisen melko triviaalisti
➖ Fullstack story puuttuu

Mielipide:  
Paras vaihtoehto raskaampiin ja todella sivistyneisiin frontteihin, joihin halutaan paljon erikoista toiminnallisuutta.

#### Fulcro (+ Pathom)

1. Komponenteissa on niiden omat datamäärittelyt ja ne vetävät lapsikomponteiltaan niiden datamäärittelyt
2. Komponentit saavat datansa clientin normalisoidusta app-db:stä
3. Dataa voidaan ladata remotesta suoraan komponenttien datamäärittelyjen avulla
4. Muutokset tapahtuvat mutaatioilla, jossa voi olla optimistisia updateja, ja se voidaan lähdettää n määrään remoteja
5. Jos remotena on Pathom EQL API, niin datan kyselyt ja mutaatiot ovat todella suoraviivaiset

Disclaimer! Kirjoitin tähän koodia päästä mitään testaamatta, eli virheitä pitäisi olla.
Ajatuksen pitäisi kuitenkin välittyä.

UI
```clojure
;; stateful component
(defsc Child [this props]
  ;; data
  {:query [:child/id :child/name]
   :ident (fn [] (:child/id (:child/id props)))}
  ;; render body
  (div
    (p (:child/name props))
    (button {:onClick #(transact! this [(delete-child props)])} "Delete"))) ;; mutate

(def ui-child (factory Child))

(defsc Parent [this {:parent/keys [id name child]}]
  {:query [:parent/id :parent/name
           {:parent/child (get-query Child)}] ;; compose from child
   :ident ::parent/id}
  (div
    (h1 name)
    (ui-child child) ;; render child, pass data
    (button {:onClick #(load! this (get-ident this) Parent)} "Update"))) ;; load

(def ui-parent (factory Parent))
```
Server resolvers
````clojure
;; Voi jakaa pieniin resolvereihin (vähemmän overfetchia, useampia kutsuja)
(defresolver parent-resolver [env input]
  {:pc/input #{:parent/id}
   :pc/output [:parent/name]})
(defresolver parent-child-resolver [env input]
  {:pc/input #{:parent/id}
   :pc/output [{:parent/child [:child/id]}]})
(defresolver child-resolver [env input]
  {:pc/input #{:child/id}
   :pc/output [:child/name]})

;; Voi olla isompia resolvereita (enemmän overfetchia, vähemmän kutsuja)
(defresolver some-page-resolver [env input]
  {:pc/input #{:parent/id}
   :pc/output [:parent/name {:parent/child [:child/id :child/name]}]})

;; Voi myös olla sekä että, jolloin pathom selvittää parhaat reitit dataan
````
Client mutation
```clojure
(defmutation delete-child [{:child/keys [id]}]
  (action [{:keys [state]}]
    (swap! state dissoc-in [:child/id id])) ;; optimistinen
  (remote true)) ;; remote?
```
Server mutation
```clojure
(defmutation delete-child [{:keys conn} {:child/keys [id]}]
  (delete-child-from-db conn id))
```

➖➖➕➕ Eniten opittavaa, mutta vastineeksi saa todella hyvän fullstack storyn (jos se on
tarpeellinen)   
➕ Mielestäni koko stackin osalta 
["Falling into the pit of success"](https://blog.codinghorror.com/falling-into-the-pit-of-success/) onnistuu hyvin    
➕ Fulcron lähestymistapa mahdollistaa esim. fullstack time travelin (jos on temporal db) ja clientin tilan
snapshottaamisen melko triviaalisti     
➖ Transaktio systeemi voi olla liian raskas nopeasti muuttuvalle datalle tai datalle, jonka historia tieto ei 
kiinnosta. Tällaista dataa varten pitäisi käyttää muita ratkaisuja, kuten React hookkeja tai re-frame          
➖ Eniten overheadiä

Mielipide:  
Paras vaihtoehto isoihin fullstack kokonaisuuksiin. Soveltuu useimpiin frontteihin, mutta ei esim. peleihin.

### Rajapinta

Fölin rajapinnasta saa datasetin tiedostoina tai yksittäset resurssit REST kautta. Jos client hakee dataa itse, niin on
normaalit N+1, overfetch ja ruuhka ongelmat. Jos clienttiin haetaan vain tarpeellinen data, niin sitä pitää hakea kysely
kerrallaan, siten että jokaista 1 kyselyä varten pitää tehä N kyselyä lisää. Jos haetaan aina kaikki data, niin dataa
haetaan mahdollisesti enemmän kuin on tarve. Jos kaikki clientit hakevat datan itse suoraan, niin Fölin rajapintaan
tulee enemmän liikennettä, kuin mitä sinne tulisi jos meillä olisi oma datavarasto, johon tieto haetaan kootusti ja 
josta clientit sitä hakisivat. Omalla tietovarastolla ja rajapinnalla voidaan eliminoida N+1, overfetch ja Fölin ruuhka.

Tarvitseeko niitä elimoida? Leikitään, että joo.

#### PostGraphile

Me voidaan ylläpitää omaa PostgreSQL kantaa, jossa ylläpidetään viimeisimmän Fölin datasetin mukainen data, ja tarjota
se PostGraphile avulla automaattisesti tehdyn GraphQL rajapinnan kautta.

➕➖ Lopputulos on GraphQL rajapinta    
➕ Rajapintaa ei tarvitse erikseen tehdä    
➖ Vaati PostgreSQL kannan ja data pitää saada tietokantaan asti   

#### Pathom

Me voidaan ylläpitää mitä tahansa muuta tietovarastoa (esim. muisti/kv ja/tai diski/NoSQL), jossa ylläpidetään
viimeisimmän Fölin datasetin mukainen data, ja tarjota se Pathomin avulla tehdyn EQL rajapinnan kautta.

➕➖ Lopputulos on EQL rajapinta    
➕ Ei vaadi tiettyä datavastoa, eli dataa ei välttämättä tarvitse saada tietokantaan asti    
➖ Rajapinta pitää tehdä itse   

#### EQL vs. GQL

Olen käyttänyt GQL eniten, mutta silti rakastunut EQL [tämän talkin](https://www.youtube.com/watch?v=yyVKf2U8YVg) ja
tuotannossa testaamisen jälkeen. Molemmat siis ratkaisee samaa ongelmaa, mutta niissä on erilaisia ominaisuuksia.

GQL:
- Yleisempi
- Tyypitetty
- Merkkijonoja

EQL:
- Harvinaisempi
- Ei tyyppejä
- Dataa

[GraphQL rajapinnassa](https://graphql.org/) on skeema, jossa on **objektityyppejä**, joilla on **fieldejä**, jotka on
joko **skalaareja** tai muita objektityyppejä. Kyselyt lähtee aina **Query** nimisestä tyypistä, jonka fieldejä
valitaan. Jos fieldi on objektityyppi, niin siitäkin objektityypistä pitää valita fieldejä.

[Pathom EQL rajapinnassa](https://blog.wsscode.com/pathom/v2/pathom/2.2.0/introduction.html) on pelkkiä
**resolvereita**, joissa on **output** fieldejä ja valinnaisesti myös **input** fieldejä metadatana.
[Näin saa rakennettua GQL rajapintaa vastaavan graafin](https://blog.wsscode.com/pathom/v2/pathom/2.2.0/graphql/edn-to-gql.html), mutta globaalisti uniikkien tyyppien ja
niiden fieldien resolvaamisen sijaan resolvaus kohdistuu suoraan globaalisti uniikkien fieldien resolvaukseen.

````gql
query {
  user(id: 42) {
    name
  }
}
````

```eql
[{(:user {:id 42})
  [:name]}]
```

Jos tyyppejä käytetään informaation määrittelyssä, niin usein datan semantiikka ei ole pelkästään datassa itsessään
(fieldi) vaan osa siitä myös datan esiintymisen kontekstista (tyyppi).

```
type Query {
    user(userId: ID!): User
    company(companyId: ID!): Company
}

type User {
    id: ID!
    name: String
}

type Company {
    id: ID!
    name: String
}

{::pc/input [:user/id]
 ::pc/output [:user/name]}

{::pc/input [:company/id]
 ::pc/output [:company/name]}
```
Monella tyypillä voi olla samannimisiä fieldejä, koska fieldin semantiikka tulee osin sen esiintymisen kontekstista.
Harvoin tällä on merkitystä, koska tyypin sisällä voi olla muita tyyppejä ja tällöin kollisioita ei synny, vaikka data
ilmeentyisikin samassa kokonaisuudessa.
```
type Query {
    myself: User
}

type User {
    id: ID!
    name: String
    company: Company
}

type Company {
    id: ID!
    name: String
    users: [User]
}

{::pc/output [:user/id]}
{::pc/input [:user/id]
 ::pc/output [:user/name {:user/company [:company/id]}]}

{::pc/input [:company/id]
 ::pc/output [:company/name]}
{::pc/input [:company/id]
 ::pc/output [{:company/users [:user/id]}]}
```
Tyypit voivat kuitenkin aiheuttaa ylimääräistä työtä, jos useassa tyypissä olisi fieldi samalla semantiikalla.
```
type Query {
    myself: User
}

type YoutubePlayLists {
    id: ID!
    stuff: JSON
}
type YoutubeUser {
    "Name identifioi"
    name: String!
    playlists: YoutubePlayLists
}
type User {
    id: ID!
    name: String
    company: Company
    youtubeUser: YoutubeUser
}
type Company {
    id: ID!
    name: String
    users: [User]
    youtubeUser: YoutubeUser
}

{::pc/output [:user/id]}
{::pc/input [:user/id]
 ::pc/output [:user/name :com.youtube/username {:user/company [:company/id]}]}

{::pc/input [:company/id]
 ::pc/output [:company/name]}
{::pc/input [:company/id]
 ::pc/output [{:company/users :com.youtube.user/name [:user/id]}]}

{::pc/input [:com.youtube.user/name]
 ::pc/output [{:com.youtube.user/playlists [:com.youtube.playlist/id :com.youtube.playlist/stuff]}]}
```
Tyyppien tapauksessa jokaisen tyypin samalla semantiikalla oleva fieldi pitää tehdä omaksi tyypiksi ja eksplisiittisesti
yhdistää tähän tyyppiin. Ilman tyyppejä semantiikka on fieldissä itsessään, mikä on mielestäni parempi etenkin isolla
skaalalla. Miksi? Jos tyyppien tapauksessa on jo tiedossa dataa, esim. :com.youtube.user/name, niin sitä ei voida 
hyödyntää, ellei Query tyyppiin laiteta uutta fieldiä. Pelkkien fieldien tapauksessa ei ole juuria. Jos missään
vaiheessa on tiedossa dataa, niin se voi toimia inputtina muiden fieldien resolvaukseen ilman. Tyyppien tapauksessa 
tämä vaatisi todella sotkuiset tyypit, joissa kaikissa tyypeissä on fieldit muihin tyyppeihin ja parametrina voi antaa
resolvaukseen tarvittavaa dataa.

Osa tyyppien hyödyistä voi saada spec avulla.

En ehkä ole paras selittämään EQL tarjoamaa potentiaalia suhteessa GQL, mutta itse ainakin olin vaikuttunut nähdessäni
[Wilkerin talkin](https://www.youtube.com/watch?v=yyVKf2U8YVg).

### Lopputulos

Yksi hyvä vaihtoehto olisi ETL PostgreSQL kantaan, josta PostGraphile avulla GQL API re-frame fronttiin.
Toinen olisi ETL prosessi muistiin (tarpeen syntyessä kunnon tietovarastoon), josta Pathom avulla EQL API Fulcro
fronttiin.

pSQL + PostGraphile GQL + re-frame      
➕ Uuden oppimista   
➕ Kanta valmiina myös omaa dataa varten     
➕ API pystyssä nopeasti     
➖ Vaivaa tietokannan kanssa ehkä?   
➖ EQL sijaan käytössä olisi GQL   
(➖ Impedance mismatch UI ja kannan datan (ehkä myös rajapinnan) välillä) <- ei välttämättä totta?   

Pathom EQL + Fulcro
➕ Vanhan harjoittelua   
➕ Data suoraan UI:n tarvitsemassa muodossa   
➕ Ei välttämättä tarvitse kantaa   
➖ Vaivaa rajapinnan kanssa ehkä?   

Jos olisi varmuus oman kannan tarpeesta muunakin kuin ETL tiedon välivarastona, niin lähtisin hakemaan vipuvoimaa
PostGraphile kautta. Tässä tapauksessa (pääosin sen takia kun sytyin Wilkerin puheista uudelelen) lähtisin treenaamaan
EQL + Fulcro.

Jälkikäteen ajateltuna EQL + re-frame olisi ollut mielenkiintoinen kokeilu tähän projektiin.