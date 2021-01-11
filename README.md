# 우산 챙겨주는 알람 시계



## 1. 활용 api

<날씨>

**OpenweatherAPI**



<미세먼지>

**한국환경공단 에어코리아 대기오염정보**



## 2. OpenweatherAPI

* API call

```
https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
```



* Parameter

lat, lon: 위도, 경도

appid: API key

exclude: 요청에 제외할 항목들

```
* current
* minutely
* hourly
* daily
* alerts
```

units: 측정 단위

```
* standard: Kelvin(default)
* metric: Celsius
* imperial: Fahrenheit
```

lang: 응답받을 단어

```
* English: en (default)
* Korean: kr
```





## 3. Datebase table

Alarm

| name           | type     | example       |
| -------------- | -------- | ------------- |
| id             | INETEGER | 1             |
| title          | TEXT     | "test"        |
| hour           | INTEGER  | 3             |
| minute         | INTEGER  | 24            |
| totalFlag      | boolean  | true/false    |
| allDayFlag     | boolean  | true/false    |
| day            | TEXT     | "2,4,6"       |
| basicSound     | TEXT     | "Asteroid"    |
| basicSoundFlag | boolean  | true/false    |
| umbSound       | TEXT     | "Atomic Bell" |
| umbSoundFlag   | boolean  | true/false    |
| vibFlag        | boolean  | true/false    |



Day

| id   | day  |
| ---- | ---- |
| 1    | sun  |
| 2    | mon  |
| 3    | tue  |
| 4    | wen  |
| 5    | thu  |
| 6    | fri  |
| 7    | sat  |

