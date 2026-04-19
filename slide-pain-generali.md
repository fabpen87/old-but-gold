# Status Quo — I Pain di Generali nella Modernizzazione Applicativa

> Slide basata esclusivamente su fonti pubbliche relative al Gruppo Generali (piano strategico Lifetime Partner 27, comunicati stampa, bilancio integrato, interviste al management, normative IVASS/UE). Nessun dato generico di mercato.

---

## KPI BOX — I numeri di Generali

| # | KPI | Valore | Fonte |
|---|-----|--------|-------|
| 1 | Investimenti cumulati in **AI & Technology** nel piano **Lifetime Partner 27** (2025-2027) | **€ 1,2 – 1,3 miliardi** | Comunicato stampa Generali, *"Generali launches Lifetime Partner 27: Driving Excellence"*, 30 gen 2025 — generali.com/media/press-releases/all/2025/Generali-launches-Lifetime-Partner-27-Driving-Excellence |
| 2 | Target di miglioramento **Insurance Cost/Income** nel piano LP27 | **+2,5 – 3,0 p.p.** | Stesso comunicato 30 gen 2025 + pagina piano strategico generali.com/it/who-we-are/Strategic-Plan-Lifetime-Partner-27 |
| 3 | Polizze nel perimetro iniziale della piattaforma core condivisa **"Insurance in a Box" (IIAB)** su PASS di RGI | **~ 15 milioni** (live in Spagna e Svizzera, roll-out in Portogallo, Ungheria, Slovenia, Croazia) | Generali, *"Generali launches Generali Core Tech"*, 13 feb 2026 — generali.com/media/press-releases/all/2026/Generali-launches-Generali-Core-Tech ; RGI Group, 13 feb 2026 — rgigroup.com/blog/2026/02/13/generali-selects-rgis-pass-insurance-platform |
| 4 | FTE della software factory **Generali Core Tech** di nuova costituzione | **~ 150 esperti** (GenAI-powered) a servizio del roll-out IIAB | Insurance Business, *"Generali creates new tech hub for core systems overhaul"*, 16 feb 2026 — insurancebusinessmag.com/uk/news/breaking-news/generali-creates-new-tech-hub-for-core-systems-overhaul-565402.aspx |
| 5 | FTE della JV **GOSP – Generali Operations Service Platform** (cloud, infrastructure, security ops) con Accenture come minoranza | **~ 429 dipendenti** (Mogliano Veneto, +9,3% YoY) | LinkedIn company page *GOSP – Generali Operations Service Platform* — linkedin.com/company/gosp-generali-operations-service-platform ; comunicato fondativo Generali-Accenture, dic 2020 — newsroom.accenture.com/news/2020/generali-group-and-accenture-form-joint-venture-to-accelerate-the-insurers-digital-transformation-strategy |
| 6 | Investimento globale IT di Generali Spagna 2022→2024 | **€ 1,1 miliardi** (+60% vs base 2022), obiettivo dichiarato dal CIO: **decommissioning del 100% del legacy in 5 anni** | Intervista Noel Rojo (CIO Generali Spagna), *El Español / Invertia*, 30 lug 2023 — elespanol.com/invertia/disruptores/grandes-actores/corporaciones/20230730/noel-rojo-cio-generali-anos-podremos-decomisionar-legacy/779922059_0.html |

---

## COST SIDE — Il costo del legacy per Generali

- **Stack core ancora frammentato per business unit**: Generali ha dichiarato pubblicamente (feb 2026) di voler centralizzare Life, P&C e Claims su **un'unica piattaforma condivisa PASS di RGI** via programma "Insurance in a Box". Oggi è live solo in Spagna e Svizzera; Portogallo, Ungheria, Slovenia e Croazia sono in roll-out — a indicare che la maggior parte del perimetro Gruppo resta su **core system locali e disomogenei** (*comunicato Generali Core Tech, 13 feb 2026*).
- **Run-cost del legacy come leva diretta sul Cost/Income**: il target LP27 di **+2,5-3,0 p.p. di miglioramento Insurance Cost/Income** è esplicitamente agganciato a *"centralize distinctive competences and shared services at scale"* — cioè allo spegnimento di stack duplicati e alla razionalizzazione applicativa (*piano strategico Lifetime Partner 27, gen 2025*).
- **Over-spending da duplicazione di competenze IT**: la sola JV **GOSP con Accenture** (IT infrastructure, procurement, global architecture, security operations, HQ IT services) assorbe **~429 FTE** a Mogliano Veneto, a cui si aggiungono ora i **~150 FTE di Generali Core Tech** per la sola software factory — un perimetro operativo che esiste *perché* il core applicativo storico non è ancora stato decommissionato (*LinkedIn GOSP; Generali press 13 feb 2026*).
- **Investimento in cash per "uscire" dal legacy**: Generali Spagna da sola ha dichiarato **€ 1,1 miliardi investiti 2022-2024 (+60% vs. 2022)** per poter decommissionare il 100% del legacy in 5 anni — un ordine di grandezza del costo reale della transizione per una singola BU Gruppo (*intervista Noel Rojo, CIO Generali Spagna, El Español, lug 2023*).

---

## RISK SIDE — I rischi specifici per Generali

- **DORA (Regolamento UE 2022/2554) pienamente obbligatorio dal 17 gennaio 2025**: Generali come entità finanziaria UE deve garantire resilienza operativa digitale, gestione rischio ICT di terze parti e segnalazione incidenti su *tutto* il perimetro applicativo, incluso il legacy non ancora sostituito. IVASS è l'autorità di vigilanza settoriale per le imprese assicurative italiane (*IVASS – "Regolamento DORA – Informazioni per gli Operatori"*, ivass.it/operatori/dora/index.html ; *Beta80 Group*, 16 feb 2026).
- **IFRS 17 in vigore dal 1° gennaio 2023 + aggiornamento IVASS 152/2024**: il nuovo standard richiede modelli di misurazione (GMM, PAA, VFA) e disclosure (CSM, rischio liquidità) che **impattano direttamente su data model e actuarial/accounting engine** del core assicurativo. Il provvedimento IVASS 152/2024 (GU 290 del 11/12/2024) ha aggiornato gli schemi di bilancio ISVAP 7/2007 proprio per CSM e rischio liquidità, obbligando a interventi applicativi continui (*dirittobancario.it*, 12 dic 2024 — dirittobancario.it/art/bilancio-assicurativo-e-ifrs-17-modifiche-ivass-agli-schemi-di-bilancio/).
- **Time-to-market per il roll-out IIAB multi-paese**: la stessa Generali ha dichiarato che il programma "Insurance in a Box" è live **solo in 2 dei ≥6 paesi** nel perimetro annunciato (Spagna, Svizzera live; Portogallo, Ungheria, Slovenia, Croazia in estensione). Ogni anno di ritardo nel decommissioning sposta a destra i benefici di costo LP27 e prolunga l'esposizione regolamentare (*comunicato Generali Core Tech, 13 feb 2026 + copertura Insurance Business, 16 feb 2026*).
- **Competence risk e dipendenza dai partner**: la software factory Core Tech nasce con **~150 specialisti interni + GenAI tools** proprio perché il Gruppo riconosce la carenza di skill moderne inhouse; in parallelo resta la dipendenza strategica da Accenture (via GOSP, 5% equity + ~40 professionisti iniziali su cloud/AI/big data) e da RGI sulla piattaforma PASS — concentrazione di fornitori che è essa stessa un *ICT third-party risk* ai sensi DORA (*comunicato Generali-Accenture, dic 2020; Generali Core Tech, feb 2026; IVASS DORA*).

---

## Bottom line (una riga per lo speaker)

Il gap di Generali **non è teorico**: è misurato in **€1,2-1,3 mld di investimenti AI/tech dichiarati nel piano 2025-27**, in **~15 mln di polizze ancora da migrare su IIAB**, in **~580 FTE già dedicati tra GOSP e Core Tech**, con **DORA già cogente** e **IFRS 17 che continua a chiedere evolutive sul core** — lo "status quo" costa oggi, e ogni trimestre di ritardo è un trimestre di Cost/Income non migliorato.

---

### Fonti citate

1. Generali Group – *"Generali launches Lifetime Partner 27: Driving Excellence"*, 30 gennaio 2025. https://www.generali.com/media/press-releases/all/2025/Generali-launches-Lifetime-Partner-27-Driving-Excellence
2. Generali Group – Pagina piano strategico *"Lifetime Partner 27: Driving Excellence"*. https://www.generali.com/it/who-we-are/Strategic-Plan-Lifetime-Partner-27
3. Generali Group – *"Generali launches Generali Core Tech"*, 13 febbraio 2026. https://www.generali.com/media/press-releases/all/2026/Generali-launches-Generali-Core-Tech
4. RGI Group – *"Generali selects RGI's PASS Insurance platform…"*, 13 febbraio 2026. https://www.rgigroup.com/blog/2026/02/13/generali-selects-rgis-pass-insurance-platform-to-empower-multi-country-life-pc-and-claims-operations/
5. Insurance Business UK – *"Generali creates new tech hub for core systems overhaul"*, 16 febbraio 2026. https://www.insurancebusinessmag.com/uk/news/breaking-news/generali-creates-new-tech-hub-for-core-systems-overhaul-565402.aspx
6. Accenture Newsroom – *"Generali Group and Accenture Form Joint Venture…"*, dicembre 2020. https://newsroom.accenture.com/news/2020/generali-group-and-accenture-form-joint-venture-to-accelerate-the-insurers-digital-transformation-strategy
7. LinkedIn – *GOSP – Generali Operations Service Platform* (company page, dati workforce 2025). https://at.linkedin.com/company/gosp-generali-operations-service-platform
8. El Español / Invertia – Intervista a Noel Rojo, CIO Generali Spagna, 30 luglio 2023. https://www.elespanol.com/invertia/disruptores/grandes-actores/corporaciones/20230730/noel-rojo-cio-generali-anos-podremos-decomisionar-legacy/779922059_0.html
9. IVASS – *"Regolamento DORA – Informazioni per gli Operatori"*. https://www.ivass.it/operatori/dora/index.html
10. Diritto Bancario – *"Bilancio assicurativo e IFRS 17: modifiche IVASS agli schemi di bilancio"* (Provv. IVASS 152/2024), 12 dicembre 2024. https://www.dirittobancario.it/art/bilancio-assicurativo-e-ifrs-17-modifiche-ivass-agli-schemi-di-bilancio/
11. AziendaBanca – *"Generali dà il via a Insurance in a Box…"*, 13 febbraio 2026. https://www.aziendabanca.it/notizie/assicurazioni/generali-insurance-in-a-box
