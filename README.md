# Cache

## Описание

### Добавлена возможность записи результатов запрорсов в файл pdf (в корень проекта)

##### Проект создан с использованием java 17.

В проекте реализованы CRUD операции, синхронизированные с кешем, на основе алгоритма LRU, через аспект(aspectj).
В проекте реализовано автоматическое создание и заполнение данными таблиц, с помощью Liquibase.

### Запуск: через docker-compose.yml

Зайдите в корень проекта, где находится файл docker-compose.yml,
в ней же откройте командную строку и введите: (docker compose up -d) - для загрузки контейнера postgres,
(-d) - в фоновом режиме.

#### Примеры:

POST - запрос: ClientDto clientDto = new ClientDto("Пётр", "Петров", "Петрович", LocalDate.parse("2000-01-01"));
ответ:
{
"id": 4,
"clientName": "Пётр",
"familyName": "Петров",
"surName": "Петрович",
"birthDay": "2000-01-01"
}

GET - запрос: ClientDto clientDtoFirst = clientService.findById(1);
ответ:
{
"clientName": "Пётр",
"familyName": "Петров",
"surName": "Петровия",
"birthDay": "2001-02-02"
}

PUT - запрос: Client update = clientService.update(1, clientDtoTwo);
ответ: update = Client(id=0, clientName=Николай, familyName=Николаев, surName=Николаевич, birthDay=2000-01-01)

##### Метод для получения информации в виде XML:

SerializatorXML serializatorXML = new SerializatorXMLImpl();
String serialize = serializatorXML.serialize(clientDtoTwo);

ClientDto clientDtoTwo = new ClientDto("Николай", "Николаев", "Николаевич",
LocalDate.parse("2000-01-01"));

##### Вывод: System.out.println("serialize = " + serialize);

serialize = <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<clientDto>
<birthDay/>
<clientName>Николай</clientName>
<familyName>Николаев</familyName>
<surName>Николаевич</surName>
</clientDto>
