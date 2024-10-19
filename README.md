# LottoSpringApplication

Aplikacja LottoSpringApplication symuluje grę w lotto, w której użytkownicy mogą podać 6 liczb, otrzymać unikalny identyfikator losowania oraz datę losowania. W każdą sobotę o 12:00 odbywa się losowanie, a użytkownicy mogą sprawdzić, ile liczb udało im się trafić.

## Spis treści

- [Funkcjonalności](#funkcjonalności)
- [Wymagania](#wymagania)
- [Punkty końcowe API](#punkty-końcowe-api)
- [Przebieg losowania](#przebieg-losowania)
- [Przebieg aplikacji](#przebieg-aplikacji)

## Funkcjonalności

- Użytkownik podaje 6 liczb (bez powtórzeń) z zakresu od 1 do 99.
- Użytkownik otrzymuje datę losowania i unikalny identyfikator swojego losu.
- Co sobotę o 12:00 odbywa się losowanie.
- Po losowaniu użytkownik może sprawdzić, ile liczb udało mu się trafić.

## Wymagania

- **Liczby podane przez użytkownika**: 6 liczb z zakresu 1-99, bez powtórzeń.
- **Losowania**: Sobota, godzina 12:00.
- **MongoDB**: Do przechowywania informacji o losowaniach i wynikach.

## Punkty końcowe API

### Rejestracja liczb do losowania

- **POST** `/inputNumbers`
  - Użytkownik podaje 6 liczb (z zakresu 1-99, bez powtórzeń).
  - Zwraca unikalny identyfikator losu oraz datę najbliższego losowania.

### Sprawdzenie wyników

- **GET** `/results/{ticketId}`
  - Użytkownik może sprawdzić wyniki swojego losu (po losowaniu).
  - Zwraca informację o liczbie trafionych liczb oraz wynikach dla danego losu.

## Przebieg losowania

Losowania odbywają się co sobotę o godzinie 12:00. System generuje losowe liczby i zapisuje wyniki w bazie danych. Po losowaniu użytkownicy mogą sprawdzić, ile liczb udało im się trafić na podstawie swojego identyfikatora losu.

## Przebieg aplikacji

```plaintext
Krok 1: Zewnętrzny serwer zwraca sześć losowych liczb (1,2,3,4,5,6).

Krok 2: System pobiera zwycięskie liczby dla daty losowania 19.11.2022 12:00.

Krok 3: Użytkownik wysłał POST /inputNumbers z 6 liczbami (1,2,3,4,5,6) dnia 16-11-2022 i system zwrócił status 200 (OK) →
        → z wiadomością: "success" oraz biletem (DrawDate: 19.11.2022 12:00 (sobota), TicketId: sampleTicketId).

Krok 4: Mijają 3 dni i 1 minuta, a jest to 1 minuta po dacie losowania (19.11.2022, 12:01).

Krok 5: System pobrał wyniki dla ticketId: sampleTicketId dla daty losowania (19.11.2022 12:00) i zapisał je w bazie danych z 6 trafieniami.

Krok 6: Mijają 3 godziny, a jest to 1 minuta po czasie ogłoszenia wyników (19.11.2022, 15:01).

Krok 7: Użytkownik wysłał GET /results/sampleTicketId i system zwrócił status 200 (OK).
