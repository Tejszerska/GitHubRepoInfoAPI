# GitHubRepoInfoAPI

GitHubRepoInfoAPI to narzędzie do pobierania listy repozytoriów użytkownika GitHub, które nie są forkami, oraz uzyskiwania informacji o ich gałęziach i najnowszych commitach.

## Funkcjonalności

- Pobieranie listy repozytoriów użytkownika GitHub, które nie są forkami.
- Wyświetlanie szczegółowych informacji o gałęziach każdego repozytorium.
- Wyświetlanie najnowszych commitów w każdej z gałęzi repozytorium.

## Wymagania

- Java 21 
- Maven

## Instalacja

1. Sklonuj repozytorium:
    git clone https://github.com/Tejszerska/GitHubRepoInfoAPI.git

2. Przejdź do katalogu projektu:
    cd GitHubRepoInfoAPI

3. Zbuduj projekt przy użyciu Mavena:
    mvn clean install

## Użycie

1. Uruchom aplikację:
    java -jar target/GitHubRepoInfoAPI-1.0-SNAPSHOT.jar

## Endpoint

- **URL**: `/ghinfo/{username}`
- **Metoda**: `GET`
- **Parametry**:
  - `Authorization` (string) - Token GitHub w formacie `ghp_xxx...`

**Przykład**:
GET ghinfo/tejszerska?YOUR_GITHUB_TOKEN

## Autor

- [Tejszerska](https://github.com/Tejszerska)

## Licencja

Ten projekt jest licencjonowany na warunkach licencji Unlicense.
