# GENERALI × DEVIN | INTERNAL DRAFT

> Slide content for the internal deck. Replaces the previous generic
> market-data version (Gartner stats, industry percentages) with
> Generali-specific, publicly sourced pain points.

---

## Slide title

**Generali's application-modernization pains — in Generali's own words**

## Subtitle

Why a fleet-of-agents approach matters right now: specific, documented pressure points from the
Group's own disclosures, executive interviews and partner case studies (2020–2026).

---

## Pain points (bullet layout)

1. **A highly fragmented IT estate across 50+ countries and 40+ operating entities.**
   Generali operates in "more than 50 countries" with "more than 40 operating entities" that
   "rely on similar large-scale processes, such as claims and underwriting" but lack a common
   data and application layer.
   *Source: [AWS Generali case study](https://aws.amazon.com/solutions/case-studies/generali/).*
   Generali's own UK CIO Yanna Winter described her perimeter as "150 IT systems running in 20
   countries" — "a multi-dimensional jigsaw".
   *Source: [Computer Weekly — CIO interview, Oct 2020](https://www.computerweekly.com/news/252490641/CIO-interview-Generalis-Yanna-Winter-on-the-power-of-data-integration).*

2. **Mainframe (z/OS + DB2) still in the core insurance estate, under explicit cost pressure.**
   The SiPo non-auto P&C application was migrated from a z/OS–DB2 mainframe to Unix/Oracle as
   part of "a policy of reducing the costs of its Information System" — confirming both the
   mainframe footprint and the Group's ongoing cost-driven modernization.
   *Source: [Movesol case study — "Generali: Mainframe migration from z/OS-DB2 to Unix-Oracle"](https://movesol.com/en/generali/).*

3. **Monolithic core systems the CIO herself calls "the elephant in the room".**
   Winter's multi-year strategy is explicitly about slicing a "large, complex monolithic system"
   into "building blocks" and "safe islands of stability" via APIs — a textbook legacy-decomposition
   problem at Group scale.
   *Source: [Computer Weekly — CIO interview](https://www.computerweekly.com/news/252490641/CIO-interview-Generalis-Yanna-Winter-on-the-power-of-data-integration).*

4. **Integration debt: the previous enterprise service bus was 10 years old and end-of-life.**
   Generali replaced it with MuleSoft iPaaS/API management in July 2019 — concrete evidence of
   legacy middleware rotating under load.
   *Source: [Computer Weekly — CIO interview](https://www.computerweekly.com/news/252490641/CIO-interview-Generalis-Yanna-Winter-on-the-power-of-data-integration).*

5. **Core-platform consolidation is only partially done: "Insurance in a Box" covers ~15M of 71M+ customers.**
   In Feb 2026 the Group launched **Generali Core Tech**, a dedicated software factory of ~150
   engineers built on RGI's platform, to scale IIAB — currently live only in Spain and
   Switzerland and rolling out to Portugal, Hungary, Slovenia and Croatia. Initial perimeter:
   **~15 million policies** vs. **71+ million customers Group-wide**. Everything outside IIAB
   is still heterogeneous country-by-country.
   *Sources: [Generali press release — Generali Core Tech, 13 Feb 2026](https://www.generali.com/media/press-releases/all/2026/Generali-launches-Generali-Core-Tech);
   [AWS Generali case study (71M customers, 40+ entities)](https://aws.amazon.com/solutions/case-studies/generali/).*

6. **Largest M&A of the decade adds a multi-country integration backlog on top.**
   The acquisition of Liberty Seguros (closed Jan 2024) was "the largest M&A transaction for the
   Group in the past decade", spanning Spain, Portugal and Ireland; legal/operational
   integration only completed in Nov 2025. Cattolica's integration into Generali Business
   Solutions (including claims IT systems) started in 2022–23.
   *Sources: [Generali press release — Liberty Seguros integration completion, 18 Nov 2025](https://www.generali.com/media/press-releases/all/2025/Generali-successfully-completes-the-integration-of-the-former-Liberty-Seguros);
   [UILCA — integrazione area sinistri Generali/Cattolica, 21 Feb 2023](https://www.uilca.it/gruppo-generali-integrazione-area-sinistri-di-generali-e-gruppo-cattolica/).*

7. **Multi-cloud complexity: strategic workloads on both Google Cloud and AWS.**
   Core business processes, IFRS 17 data platform and IoT/ML workloads sit on Google Cloud;
   the central data/analytics/AI foundation (16 flagship AI use cases, 21M shared-services
   transactions/month, €200M+ saved in 3 years) runs on AWS — with the explicit goal of
   standardising 40+ operating entities on one view. Governance, portability and skills scale
   across two hyperscalers is now a first-class operational concern.
   *Sources: [Google Cloud Generali case study](https://cloud.google.com/customers/generali);
   [AWS Generali case study](https://aws.amazon.com/solutions/case-studies/generali/).*

8. **Regulatory drumbeat the IT estate must keep up with — continuously.**
   DORA (Regulation (EU) 2022/2554) applies to Generali as an EU (re)insurer and became
   enforceable on **17 January 2025**, mandating ICT risk management, incident reporting,
   resilience testing and third-party ICT oversight; this sits on top of Solvency II Pillar 2 /
   ORSA and IVASS supervision. Every legacy component still in production has to be brought up
   to this bar.
   *Sources: [Regulation (EU) 2022/2554 — DORA, in force 16 Jan 2023, applicable 17 Jan 2025](https://eur-lex.europa.eu/eli/reg/2022/2554/oj);
   [EIOPA — DORA application to insurers](https://www.eiopa.europa.eu/digital-operational-resilience-act-dora_en).*

9. **€1.2–1.3 bn to be spent on AI & technology over 2025–2027 — with a hard cost/income target attached.**
   The "Lifetime Partner 27: Driving Excellence" plan earmarks **€1.2–1.3 bn** cumulative
   Group investment in AI and technology and commits to a **2.5–3.0 p.p. improvement in
   Insurance Cost/Income**. Every euro has to produce measurable efficiency, not just
   replatforming.
   *Source: [Generali press release — Lifetime Partner 27, 30 Jan 2025](https://www.generali.com/media/press-releases/all/2025/Generali-launches-Lifetime-Partner-27-Driving-Excellence).*

---

## Suggested framing line (bottom of slide)

> None of this is a market statistic. Every pain above is a number or quote Generali or its
> partners have published — and every one of them is a concrete target for agentic
> modernization at fleet scale.

---

## Layout notes for the deck author

- Keep the existing **GENERALI × DEVIN | INTERNAL DRAFT** header strip.
- Title line: **"Generali's application-modernization pains — in Generali's own words"**.
- Body: the 9 bullets above; consider grouping visually as:
  - *Estate & legacy* → bullets 1–4
  - *Transformation in flight* → bullets 5–7
  - *Non-negotiables* → bullets 8–9
- Footer: one line with the sources list compressed (e.g. "Sources: Generali press releases
  (2025, 2026), Computer Weekly (2020), AWS & Google Cloud case studies, Movesol case study,
  Regulation (EU) 2022/2554").
- Drop every Gartner / generic "X% of IT budget spent on legacy" statistic from the previous
  version — all of the bullets above are Generali-specific and independently verifiable.
