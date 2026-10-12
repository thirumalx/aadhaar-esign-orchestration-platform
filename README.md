# Aadhaar eSign Orchestration Platform

                    Client Application
                  (Voting, HR, Loan, etc.)
                           |
                           |
                   Aadhaar eSign API
                           |
            -------------------------------
            |             |              |
      Provider Router  Audit Service  Document Store
            |
    -----------------------------
    |            |              |
    ESP 1     ESP 2           ESP 3
    |            |              |
    ------------Fallback---------


## Intelligent Provider Routing Strategies

The platform dynamically routes eSign requests to various providers based on pluggable strategies configured in the `EsignOrchestratorService`.

Currently supported strategies:

*   **Explicit Routing (`EXPLICIT`)**: The consumer API explicitly requests a specific provider by sending `providerCode` in the `EsignDto` payload. 
*   **Cheapest Provider (`CHEAPEST`)**: Automatically routes the request to the configured Signature Provider with the lowest recorded `cost`.
*   **Weighted Routing (`WEIGHTED`)**: Distributes request traffic on a percentage basis by evaluating the `weight` assigned to each provider (e.g., 50% to CDAC, 30% to Protean, 20% to eMudhra).
*   **Application Preference**: A specific application configured in the orchestration layer can define its own default preferred signature provider.
*   **Highest Priority Fallback (`PRIORITY`)**: A fallback strategy that routes to the highest priority provider globally if no other strategy can determine a provider.

Consumers can dynamically override default behavior by passing the `routingStrategy` parameter in the `EsignDto` (e.g. `routingStrategy: "CHEAPEST"`).

### Future Routing Rules to Implement:
* **Failover / High Availability**: If a provider fails, retry with another provider.
* **Latency Based**: Switch provider if average latency exceeds 3 seconds.

## Technical specification

| Your eMudhra field   | CCA/C-DAC concept                 | Protean equivalent                |
| -------------------- | --------------------------------- | --------------------------------- |
| `AspSignRequest`     | `Esign`                           | `Esign`                           |
| `authMode="OTP"`     | `esignClass="1"`                  | CCA 2.1 auth mode/class           |
| `clientId`           | `aspId`                           | `aspId`                           |
| `txnId`              | `txn`                             | `txn`                             |
| `DocumentHash`       | `Input` / document hash structure | `Docs` / document hash            |
| `Consent`            | `sc` / consent mechanism          | `sc` / consent mechanism          |
| `ResponseForwardUrl` | response URL                      | `responseUrl` / provider-specific |
| `Signature`          | `Signature`                       | `Signature`                       |
| `hashAlgo="SHA256"`  | SHA-256 document hash             | SHA-256                           |
| X.509 certificate    | ASP signing certificate           | ASP signing certificate           |
