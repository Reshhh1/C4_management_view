## Userstory

Als `...` wil ik `...`, zodat `...`.

## Acceptatiecriteria

> Acceptatie criteria zijn de voorwaarden waaraan moet worden voldaan om het product te accepteren bij de sprint
> _Wat laat ik zien aan de PO om dit goed te keuren_

-   De response time is tussen de `...` en `...`ms
-   Criteria 1.

## Functionele eisen

> Functionele eisen zijn waaraan het product functioneel moet voldoen, als guideline voor de programmeur
> _Welke functionaliteiten komen erbij/zijn af na dit issue._

### Request

-   [ ] Er komt een request op het endpoint: `/path`
-   [ ] Request body bevat:
    -   **`property`**: `String`
    -   **`property2`**: `Integer`

### Response

-   [ ] Succesvolle response heeft status: `2xx`
-   [ ] Response body bevat:

    -   **`property`**: `String`
    -   **`property2`**: `Integer`

-   [ ] Error responses hebben status: `4xx`, `4xx`, `500`
-   [ ] Error response body bevat:
    -   **`message`**: `String`

### Data

-   [ ] Er komt een table `table name` met de properties:
    -   **`id`**: `Long`
    -   **`created at`**: `Date`
    -   **`user`**: `User table, 1:*`

## Happy Path

1. Er wordt een `GET` request gestuurd naar `/path`
2. _validatie_
3. Er wordt een `2xx` status teruggestuurd, samen met `entity of properties` in de body.

## Unhappy Path

-   2a. ... is niet valide
-   2b. De response bevat een `4xx` statuscode en een error message met `"... is not valid"`.

## Test

| Test nummer |   Endpoint    | Test data                                                    | Doel              | Verwachte status | Verwachte resultaat | Resultaat | Pass? |
| :---------: | :-----------: | :----------------------------------------------------------- | ----------------- | :--------------: | ------------------- | --------- | :---: |
|     1.      | `GET` `/path` | JSON body = {"property": "value", "property2": "otherValue"} | Gebruiker kan ... |       200        | ...                 | ...       |  [ ]  |

## Constraints

-   Testen moeten geïmplementeerd worden in het issue.
-   Implementatie in Spring Boot of Ktor.

## Bronnen
