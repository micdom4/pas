PLATFORMA

Aplikacja jest realizowana w architekturze kontener-komponent, w jednym z kontenerów (do wyboru):

    [ ] Jakarta

    [ ] Quarkus

    [x] Spring IoC

MODEL

Aplikacja ma odwzorowywać prosty model biznesowy, w którym użytkownicy końcowi aplikacji (dalej zwani Klientami) mogą dokonywać wyłącznej alokacji pewnych Zasobów definiowanych przez aplikację, w określonych przedziałach czasu. Do tej klasy modeli biznesowych należą wszelkiego rodzaju wypożyczalnie, hotele, umawianie spotkań etc.

Model danych aplikacji powinien obejmować co najmniej trzy klasy:

    [x] w każdej z klas / hierarchii powinien wystąpić atrybut, którego wartość będzie wymagana i unikatowa dla całego zbioru obiektów danej hierarchii oraz będzie podstawą relacji równości tych obiektów, a jednocześnie jako atrybut nie będzie miał znaczenia w domenie (tzw. identyfikator/klucz); może to być wartość całkowita lub UUID

    [x] hierarchia klas reprezentująca Użytkowników aplikacji, uwzględniająca podział na co najmniej trzy poziomy dostępu do aplikacji (poziom administratora użytkowników, poziom zarządcy zasobów, poziom użytkownika zasobów - Klienta)

    [x] w modelu Użytkowników musi występować atrybut, którego wartość określa, czy dany Użytkownik jest aktywny tj. może wykonywać akcje w aplikacji

    [x] w modelu Użytkowników musi występować atrybut typu tekstowego będący identyfikatorem (tzw. login), którego wartość będzie wymagana i unikatowa dla całego zbioru obiektów (niezależnie od istnienia klucza w tych obiektach)

    [x] Klasa reprezentująca Zasób aplikacji; nie jest wymagane reprezentowanie wielu typów zasobów

    [x] klasa reprezentująca fakt alokacji Zasobu przez Klienta, której minimalnym zbiorem atrybutów są: Klient, Zasób, czas rozpoczęcia alokacji, czas zakończenia alokacji;

FUNKCJONALNOŚĆ

Funkcjonalność aplikacji powinna obejmować:

    [x] wyszukiwanie wszystkich rodzajów obiektów według wartości klucza (wynikiem powinien być jeden obiekt)

    [x] wyszukiwanie Użytkowników także według wartości identyfikatora tekstowego w dwóch wariantach: wyszukiwanie dokładnie podanej wartości (wynikiem powinien być jeden obiekt) oraz dopasowanie podanej wartości (wynikiem powinna być lista obiektów)

    [x] zestaw operacji CRUD (tworzenie, odczyt (lista), modyfikacja, usuwanie) dla hierarchii Zasobów, przy czym usunięcie Zasobu jest możliwe tylko wtedy, gdy nie jest z nim związana żadna alokacja

    [x] zestaw operacji CRU (tworzenie, odczyt (lista), modyfikacja) dla hierarchii Użytkowników, dodatkowo jako odrębne operacje należy uwzględnić aktywowanie i deaktywowanie Użytkownika

    [x] utworzenie alokacji dla wskazanego Klienta oraz Zasobu (wskazanie poprzez wartość klucza), obwarowane co najmniej aktywnością Klienta oraz dostępnością (brakiem nie zakończonych alokacji) Zasobu

    [x] pobranie (odrębnie) listy alokacji minionych oraz bieżących dla wskazanego Klienta lub Zasobu

    [x] zakończenie alokacji i usuwanie alokacji

    [x] czas rozpoczęcia tworzonej alokacji może być ustawiany jako przyszły

    [x] zakończenie alokacji polega na ustawieniu atrybutu czasu zakończenia alokacji

    [x] usuwanie alokacji dotyczy tylko alokacji nie zakończonych

    [x] operacje na alokacjach są prowadzone z perspektywy osoby trzeciej (operatora aplikacji)

    UWAGA: Nie ma potrzeby i nie jest zalecane rozbudowywanie funkcjonalności ponad wyżej wymienione wymagania WSKAZÓWKA: Dobrą bazą do budowy modelu aplikacji powinien być projekt realizowany w ramach przedmiotu Programowanie Obiektowe UWAGA: Model aplikacji nie może powielać modelu "Wypożyczalnia pojazdów" używanego jako referencyjny

WARUNKI POPRAWNOŚCI

W implementacji aplikacji należy uwzględnić sprawdzanie poprawności danych wejściowych, a w szczególności:

    [x] walidację składniową danych przesyłanych w żądaniu, przy czym niespełnianie warunków poprawności powinno powodować uznanie żądania za niepoprawne

    [x] weryfikację możliwości realizacji operacji ze względu na stan danych aplikacji (zgodnie z opisem funkcjonalności)

SZCZEGÓŁY ARCHITEKTONICZNE

Aplikacja powinna być zrealizowana w modelu warstwowym, a w szczególności powinny być wyodrębnione dwie warstwy:

    [x] warstwa tzw. menedżerów - obiektów wykonawczych udostępniających realizację wszystkich funkcji warstwy logiki; funkcjonalność menedżerów jest dostępna jako Rest API

    [x] warstwa tzw. źródeł danych (DAO, repozytoria) - obiektów wykonawczych realizujących funkcjonalność składowania danych (z uwzględnieniem unikatowości identyfikatorów biznesowych)

    [x] składowanie danych powinno być realizowane w pamięci aplikacji, przy czym konstrukcja aplikacji powinna uwzględniać możliwość łatwej (nie wymagającej zmiany kodu klas menedżerów) zmiany implementacji źródeł danych

    [x] uruchamianie aplikacji powinno wiązać się z wczytaniem zestawu danych inicjujących

    [x] źródło danych jest odpowiedzialne za spełnianie warunku unikalności i niepustości kluczy oraz identyfikatora tekstowego (optymalność wykonania nie podlega ocenie)

    [x] źródło danych jest odpowiedzialne za nadawanie wartości kluczy nowo tworzonym obiektom

    [x] poszczególne hierarchie obiektów aplikacji powinny być obsługiwane przez odrębne klasy menedżerów / źródeł danych (zasada pojedynczej odpowiedzialności)

    [x] operacje zawężania zbioru danych (filtrowanie, wyszukiwanie) powinny być realizowane przez źródła danych (zasada minimalizacji transferu danych)

OBSŁUGA WIELOWĄTKOWOŚCI

Należy pamiętać, że aplikacja będzie wykonywana wielowątkowo na poziomie obsługi żądań.

    [x] obiekty źródeł danych muszą być współdzielone pomiędzy poszczególne żądania aplikacji Rest

    [x] w przypadku pozostałych klas wykonawczych należy unikać współdzielenia tych obiektów pomiędzy wątkami (stosować odpowiednie mechanizmy kontenera / frameworku aplikacyjnego)

CECHY SPECYFICZNE APLIKACJI REST

Należy uwzględnić cechy specyficzne dla aplikacji Rest, wynikające z komunikacji w modelu żądanie - odpowiedź, w szczególności:

    [x] ryzyko hazardu związanego z przeplotem żądań prowadzących do modyfikacji stanu źródeł danych

    [x] możliwość ponownego przesłania identycznego żądania

TESTY INTEGRACYJNE

    [x] Zaimplementować zestaw automatycznych testów usługi REST. Wymagane jest użycie frameworku / narzędzia dedykowanego do tego celu. Narzędzie powinno integrować się z IDE i umożliwiać generowanie raportów z pokrycia kodu testami. Wymagania te spełnia na pewno użycie JUnit i użycie biblioteki klienta REST (JaxRS, RestAssured,...).

    [x] Procedura uruchamiania testów zakłada, że usługa jest wdrożona i dostępna a jej URL jest znany.

    [x] Implementując testy można założyć, że usługa jest uruchomiona z zerowym stanem danych. Nie ma obowiązku "czyszczenia" danych będących wynikiem przeprowadzania testów.

    [x] Należy unikać uzależniania testów od siebie (np. wykonanie kolejnego testu wymaga wprowadzenia odpowiednich danych przez poprzedni test). Każdy test powinien sam przygotowywać dane potrzebne do jego przeprowadzenia.

Następujące funkcjonalności powinny być pokryte testami:

Testy pozytywne - weryfikacja na podstawie oczekiwanego stanu danych usługi:

    [x] CRU[D] na obiektach Użytkowników i Zasobów

    [x] alokacja Zasobu na rzecz Klienta

Testy negatywne - weryfikacja na podstawie zwróconego kodu statusu żądania HTTP

    [x] naruszenie ograniczeń składniowych dla tworzonych lub modyfikowanych obiektów - po jednym przykładzie dla obu hierarchii (Użytkowników i Zasobów)

    [x] naruszenie ograniczenia unikalności identyfikatora Użytkownika

    [x] niepowodzenie alokacji Zasobu na rzecz Klienta ze względu na to, że Zasób został już zaalokowany
