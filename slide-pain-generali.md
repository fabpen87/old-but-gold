# Status Quo — I Pain di Generali nella Modernizzazione Applicativa

> Fonti: comunicati stampa Generali (2022–2026), Investor Day 2025, Annual Integrated Report 2024, RGI press release, IVASS, InsuranceERM, Digital4, Insurzine, Computer Weekly. Nessun dato di mercato generico — solo evidenze riferite direttamente a Generali.

---

## KPI BOX — I numeri che pesano su Generali

| KPI | Valore | Fonte |
|---|---|---|
| Investimenti cumulati Gruppo in **AI & technology** nel piano Lifetime Partner 27 (2025-2027) | **€1,2 – 1,3 miliardi** | Generali, *Lifetime Partner 27: Driving Excellence* press release, 30 gen 2025 |
| Quota degli investimenti tech destinata al **mercato italiano** | **~€433 milioni** (≈ 1/3 del totale) | Insurzine, *L'AI ridisegna Generali: 433 milioni in Italia*, 24 feb 2026 (Forum Masseria – Winter Edition) |
| Perimetro iniziale della nuova core platform **"Insurance in a Box" (IIAB)** | **~15 milioni di polizze** su 6 Paesi (Spagna, Svizzera live; Portogallo, Ungheria, Slovenia, Croazia in rollout) | Generali press release *Generali launches Generali Core Tech*, 13 feb 2026 |
| FTE dedicati alla nuova software factory di Gruppo **"Generali Core Tech"** | **~150 esperti** (centro di eccellenza IIAB + Gen-AI) | Generali / RGI press releases, 13 feb 2026 |
| Contractual Service Margin (CSM) da rifatturare sui nuovi **IFRS 17** | **~€33 miliardi** — nuovo motore contabile Vita | Generali, *Investor Update 13 dic 2022* (induction IFRS 17/9 di C. Borean) |
| Perimetro geografico su cui ricadono i pain IT | **50+ Paesi**, ~75M clienti, ~70.000 dipendenti, ~163.000 consulenti | Oracle Customer Story Generali, 2020; Generali *Lifetime Partner 27* PR, 30 gen 2025 |

---

## COST SIDE — Quanto costa a Generali il legacy

- **Stack core frammentato per Paese/BU.** IIAB + PASS di RGI è stato scelto *«al termine di un articolato processo di selezione internazionale … per centralizzare e modernizzare i sistemi Vita, Danni e Sinistri»*: oggi copre solo 6 Paesi (~15M polizze); il resto dei 50+ mercati continua a girare su core systems locali separati (*RGI press release, 13 feb 2026*; *Oracle Customer Story Generali, 2020*).

- **Run-cost di modernizzazioni "a metà".** La migrazione storica del core SiPo (Danni non-auto) da **mainframe z/OS-DB2 a Unix-Oracle** in Generali France è stata esplicitamente motivata dalla *"politica di riduzione dei costi del sistema informativo"*: un re-host che ha tolto il mainframe ma ha lasciato un'applicazione non cloud-native ancora in esercizio (*Movesol case study Generali, 2020*).

- **Integrazione Cattolica tuttora in corso (IT debt acquisito).** Generali ha alzato il target di sinergie Cattolica da **€80M a €120-130M entro il 2025**: una quota rilevante deriva dal consolidamento sistemi/piattaforme IT acquisiti nel 2021 (*Generali, Investor Update press release, 13 dic 2022*).

- **150 FTE centrali NON bastano per i 50+ Paesi.** Generali Core Tech è un *"centro di eccellenza"* di ~150 persone sul solo perimetro IIAB: tutto ciò che sta fuori da quel perimetro resta presidiato dai team IT locali delle singole business unit, con duplicazione di skill, toolchain e ciclo di vita applicativo (*Generali / Insurzine, 13 feb 2026*).

---

## RISK SIDE — I rischi concreti per Generali

- **DORA (Reg. UE 2022/2554) — applicabile dal 17 gennaio 2025.** IVASS ha indirizzato a Generali e agli altri assicuratori la *"Lettera al mercato del 14 febbraio 2025"* con le procedure operative per la segnalazione obbligatoria degli incidenti ICT gravi e volontaria delle minacce cyber: ogni gap di osservabilità/resilienza sul legacy diventa direttamente un rischio di non-conformità (*IVASS, Lettera al mercato DORA, 14 feb 2025*).

- **IFRS 17 (in vigore dal 1° gennaio 2023) — sistemi attuariali e contabili riscritti.** Generali ha dedicato un intero Investor Update all'implementazione IFRS 17/9 (CSM ~€33 mld, ridisegno della visibilità degli utili Vita): impatti diretti su data warehouse, chiusure, sub-ledger e riconciliazioni Solvency II (*Generali, Investor Update 13 dic 2022*). *Segnale di mercato sul costo dell'implementazione in Generali: WTW stima per le grandi multinazionali costi cumulati medi di programma ~$240M (WTW, 13 nov 2023) — riferimento di contesto, non dato ufficiale Generali.*

- **Time-to-market del rollout multi-country.** IIAB è *"già live in Spagna e Svizzera ed è attualmente in estensione a Portogallo, Ungheria, Slovenia e Croazia"*: 4 Paesi in rollout + 2 già live, su un Gruppo presente in oltre 50 Paesi → il residuo legacy continuerà a rallentare il lancio di prodotti e feature AI-first (*Generali / RGI press releases, 13 feb 2026*).

- **Competenze & GenAI gap.** Giulio Terzariol (Group Deputy CEO & DG) definisce l'adozione AI *"un cambiamento senza precedenti per l'intera catena del valore"* — dai tempi di sottoscrizione al "Super Agente"; David Cis (Group COO) chiarisce che Core Tech serve a *"rafforzare le competenze chiave nello sviluppo dei nuovi sistemi … l'uso di AI/GenAI nel radicale cambiamento dei processi di sviluppo software"*: riconoscimento esplicito che i sistemi esistenti e le skill attuali non sono sufficienti a sostenere il piano (*Insurzine, 24 feb 2026*; *Generali press release, 13 feb 2026*).

---

## Fonti (link diretti)

1. Generali — *Lifetime Partner 27: Driving Excellence* (comunicato 30 gen 2025): <https://www.generali.com/media/press-releases/all/2025/Generali-launches-Lifetime-Partner-27-Driving-Excellence>
2. Generali — *Lancio di Generali Core Tech* (comunicato 13 feb 2026): <https://www.generali.com/it/media/press-releases/all/2026/Generali-launches-Generali-Core-Tech>
3. RGI — *Generali selects PASS Insurance* (13 feb 2026): <https://www.rgigroup.com/blog/2026/02/13/generali-selects-rgis-pass-insurance-platform-to-empower-multi-country-life-pc-and-claims-operations/>
4. Generali — *Investor Update IFRS 17/9 & integrazione Cattolica* (13 dic 2022): <https://www.generali.com/it/media/press-releases/all/2022/Investor-Update-Press-Release>
5. Generali — *Annual Integrated Report 2024*: <https://www.generali.com/doc/jcr:259c5d6e-46f7-4a43-9512-58e5dcbd2a56/Annual%20Integrated%20Report%20and%20Consolidated%20Financial%20Statements%202024_Generali%20Group_final_interactive.pdf/lang:en/Annual_Integrated_Report_and_Consolidated_Financial_Statements_2024_Generali_Group_final_interactive.pdf>
6. Generali — *Investor Day 2025*: <https://www.generali.com/investors/financial-results-and-reports/investor-day/Generali-Investor-Day-2025>
7. InsuranceERM — *Generali plans €1.3bn tech and AI spend* (30 gen 2025): <https://www.insuranceerm.com/news-comment/generali-plans-13bn-tech-and-ai-spend-in-new-three-year-strategy.html>
8. Insurzine — *L'AI ridisegna Generali: 433 milioni in Italia* (24 feb 2026): <https://www.insurzine.com/2026/02/24/lai-ridisegna-generali-433-milioni-in-italia-per-trasformare-la-catena-del-valore/>
9. IVASS — *Lettera al mercato — Reporting DORA* (14 feb 2025): <https://www.ivass.it/media/avviso/lm-ivass-reporting-dora/>
10. Oracle — *Generali accelerates HR data-driven culture with Oracle Cloud* (50+ Paesi, 70k dipendenti): <https://www.oracle.com/in/customers/generali/>
11. Movesol — *Generali: mainframe migration from z/OS-DB2 to Unix-Oracle* (SiPo): <https://movesol.com/en/generali/>
12. Computer Weekly — *CIO interview: Generali's Yanna Winter on the power of data integration* (16 ott 2020): <https://www.computerweekly.com/news/252490641/CIO-interview-Generalis-Yanna-Winter-on-the-power-of-data-integration>
13. Digital4 — *Generali Italia, i nuovi traguardi del percorso di trasformazione digitale* (4 ago 2021, intervista David Cis): <https://www.digital4.biz/executive/digital-transformation/generali-italia-i-traguardi-del-percorso-di-trasformazione-digitale/>
14. WTW — *IFRS 17 disrupting business as usual for insurers* (riferimento di contesto costi programma, 13 nov 2023): <https://www.wtwco.com/it-it/news/2023/09/ifrs-17-disrupting-business-as-usual-for-insurers>
