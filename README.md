PLATFORMA

    [x] Aplikacja jest realizowana w architekturze kontener-komponent.

    [x] Wybrano jeden z kontenerów: Jakarta, Quarkus lub Spring IoC.

MODEL

    [x] Aplikacja odwzorowuje model biznesowy alokacji zasobów w czasie (np. wypożyczalnia, rezerwacje).

    [x] Model danych obejmuje co najmniej trzy klasy.

    [x] Wszystkie klasy: Posiadają atrybut klucza (identyfikator, np. int lub UUID).

    [x] Klucz: Wartość klucza jest wymagana i unikatowa w obrębie hierarchii.

    [x] Klucz: Jest podstawą relacji równości (equals() i hashCode()).

    [x] Klucz: Nie ma znaczenia w domenie biznesowej.

    [x] Użytkownicy: Istnieje hierarchia klas Użytkowników.

    [x] Użytkownicy: Uwzględniono co najmniej 3 poziomy dostępu (administrator, zarządca, klient).

    [x] Użytkownicy: Istnieje atrybut określający, czy Użytkownik jest aktywny.

    [x] Użytkownicy: Istnieje atrybut tekstowy login.

    [x] Użytkownicy: login jest wymagany i unikatowy dla całego zbioru.

    [x] Zasób: Istnieje klasa reprezentująca Zasób (nie trzeba wielu typów).

    [x] Alokacja: Istnieje klasa reprezentująca fakt alokacji.

    [x] Alokacja: Minimalne atrybuty to: Klient, Zasób, czas rozpoczęcia, czas zakończenia.

FUNKCJONALNOŚĆ

    [ ] Wyszukiwanie wszystkich rodzajów obiektów według wartości klucza (zwraca jeden obiekt).

    [x] Wyszukiwanie Użytkowników według login (dopasowanie dokładne, zwraca jeden obiekt).

    [x] Wyszukiwanie Użytkowników według login (dopasowanie częściowe/wzorzec, zwraca listę).

    [ ] Zasoby: Zaimplementowano operacje CRUD (Create, Read lista, Update, Delete).

    [ ] Zasoby: Usunięcie Zasobu jest możliwe tylko wtedy, gdy nie ma powiązanych alokacji.

    [x] Użytkownicy: Zaimplementowano operacje CRU (Create, Read lista, Update).

    [ ] Użytkownicy: Istnieje oddzielna operacja aktywowania Użytkownika.

    [ ] Użytkownicy: Istnieje oddzielna operacja deaktywowania Użytkownika.

    [ ] Alokacje: Można utworzyć alokację dla Klienta i Zasobu (wskazanych przez klucz).

    [ ] Alokacje: Warunkiem utworzenia jest aktywność Klienta.

    [ ] Alokacje: Warunkiem utworzenia jest dostępność Zasobu (brak innych nie zakończonych alokacji).

    [ ] Alokacje: Można pobrać listę alokacji minionych (oddzielnie dla Klienta lub Zasobu).

    [ ] Alokacje: Można pobrać listę alokacji bieżących (oddzielnie dla Klienta lub Zasobu).

    [ ] Alokacje: Istnieje funkcja zakończenia alokacji.

    [ ] Alokacje: Istnieje funkcja usuwania alokacji.

    [ ] Alokacje: Czas rozpoczęcia alokacji może być ustawiony jako przyszły.

    [ ] Alokacje: Zakończenie alokacji polega na ustawieniu atrybutu czasu zakończenia.

    [ ] Alokacje: Usuwanie dotyczy tylko alokacji nie zakończonych.

    [ ] Alokacje: Operacje są prowadzone z perspektywy operatora (osoby trzeciej).

UWAGI

    [ ] Funkcjonalność nie jest rozbudowana ponad wymagania.

    [ ] Model aplikacji nie powiela modelu "Wypożyczalnia pojazdów".

WARUNKI POPRAWNOŚCI

    [ ] Implementacja uwzględnia sprawdzanie poprawności danych wejściowych.

    [ ] Realizowana jest walidacja składniowa danych w żądaniu.

    [ ] Błąd walidacji składniowej powoduje uznanie żądania za niepoprawne (np. błąd 400).

    [ ] Weryfikowana jest możliwość realizacji operacji ze względu na stan danych (logika biznesowa).

SZCZEGÓŁY ARCHITEKTONICZNE

    [ ] Aplikacja jest zrealizowana w modelu warstwowym.

    [ ] Wyodrębniono warstwę "menedżerów" (logika biznesowa).

    [ ] Funkcjonalność menedżerów jest dostępna jako Rest API.

    [ ] Wyodrębniono warstwę "źródeł danych" (DAO / Repozytoria).

    [ ] Źródła danych realizują składowanie danych (unikatowość ID biznesowych).

    [ ] Składowanie danych jest realizowane w pamięci aplikacji.

    [ ] Konstrukcja pozwala na łatwą zmianę implementacji źródeł danych (bez zmiany menedżerów).

    [ ] Uruchomienie aplikacji wczytuje zestaw danych inicjujących.

    [ ] Źródło danych odpowiada za unikalność i niepustość kluczy oraz login.

    [ ] Źródło danych odpowiada za nadawanie wartości kluczy nowym obiektom.

    [ ] Poszczególne hierarchie obiektów mają oddzielne klasy menedżerów / źródeł danych (SRP).

    [ ] Operacje filtrowania i wyszukiwania są realizowane przez źródła danych (minimalizacja transferu).

OBSŁUGA WIELOWĄTKOWOŚCI

    [ ] Obiekty źródeł danych są współdzielone pomiędzy żądaniami (np. jako singletony).

    [ ] Unika się współdzielenia innych klas wykonawczych (menedżerów) między wątkami (np. poprzez odpowiedni scope w kontenerze).

CECHY SPECYFICZNE APLIKACJI REST

    [ ] Uwzględniono ryzyko hazardu (przeplot żądań modyfikujących dane).

    [ ] Uwzględniono możliwość ponownego przesłania identycznego żądania.

TESTY INTEGRACYJNE

    [ ] Zaimplementowano zestaw automatycznych testów usługi REST.

    [ ] Użyto dedykowanego frameworku (np. JUnit + RestAssured).

    [ ] Narzędzie integruje się z IDE i generuje raporty pokrycia.

    [ ] Procedura testów zakłada, że usługa jest uruchomiona i dostępna.

    [ ] Testy mogą zakładać zerowy stan danych przy starcie usługi.

    [ ] Nie ma obowiązku czyszczenia danych po testach.

    [ ] Unika się uzależniania testów od siebie.

    [ ] Każdy test sam przygotowuje dane potrzebne do jego wykonania.

    [ ] Testy pozytywne: Pokryto CRU[D] na Użytkownikach.

    [ ] Testy pozytywne: Pokryto CRU[D] na Zasobach.

    [ ] Testy pozytywne: Pokryto alokację Zasobu na rzecz Klienta.

    [ ] Testy negatywne: Pokryto naruszenie ograniczeń składniowych dla Użytkowników (1 przykład).

    [ ] Testy negatywne: Pokryto naruszenie ograniczeń składniowych dla Zasobów (1 przykład).

    [ ] Testy negatywne: Pokryto naruszenie unikalności identyfikatora (login) Użytkownika.

    [ ] Testy negatywne: Pokryto niepowodzenie alokacji (Zasób już zaalokowany).
