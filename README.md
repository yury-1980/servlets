# Servlets

## Описание

### Есть возможность записи результата Get запрорса в файл pdf (в корень проекта)

##### Проект создан с использованием java 17. + apache-tomcat-9.0.59

Настройки находятся в файле "application.yaml".
В проекте реализованы CRUD операции, синхронизированные с кешем, на основе алгоритма LRU, через аспект(aspectj).
В проекте реализовано автоматическое создание и заполнение данными таблиц, с помощью Liquibase.

### Запуск: через docker-compose.yml

Зайдите в корень проекта, где находится файл docker-compose.yml,
в ней же откройте командную строку и введите: (docker compose up -d) - для загрузки контейнера postgres,
(-d) - в фоновом режиме.

#### Примеры:

POST - запрос: http://localhost:8080//v1/clients
{
"clientName": "Мария",
"familyName": "Альбертович",
"surName": "Николаевич",
"birthDay": "2000-01-01"
}

ответ:
{
"id": 5,
"clientName": "Мария",
"familyName": "Альбертович",
"surName": "Николаевич",
"birthDay": "2000-01-01"
}

GET - запрос: http://localhost:8080/v1/clients?id=1

ответ:
{
"clientName": "Пётр",
"familyName": "Петров",
"surName": "Петровия",
"birthDay": "2001-02-02"
}

PUT - запрос: http://localhost:8080/v1/clients?id=78
{
"clientName": "Ирина",
"familyName": "Василькова",
"surName": "Владимировна",
"birthDay": "2000-01-01"
}

ответ:
{
"id": 78,
"clientName": "Ирина",
"familyName": "Василькова",
"surName": "Владимировна",
"birthDay": "2000-01-01"
}
