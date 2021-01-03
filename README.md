# 우산 챙겨주는 알람 시계



1. 활용 api

   <날씨>

   **OpenweatherAPI**

   

   <미세먼지>

   **한국환경공단 에어코리아 대기오염정보**



2. OpenweatherAPI

   작동 순서

   1. GPS로 위치 탐색
   2. 탐색한 위치의 위도, 경도를 x, y값으로 계산
   3. **동네예보 조회서비스**로 24시 날씨 정보 요청
   4. **중기예보 조회서비스**로 10일 날씨 정보 요청

   API call

   ```
   https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
   ```
   
   Parameter
   
   lat, lon: 위도, 경도
   
   appid: API key
   
   exclude: 요청에 제외할 항목들
   
   	* current
   	* minutely
   	* hourly
   	* daily
   	* alerts
   
   units: 측정 단위
   
   	* standard: Kelvin(default)
   	* metric: Celsius
   	* imperial: Fahrenheit
   
   lang: 응답받을 단어
   
   	* English: en (default)
   	* Korean: kr
   
   
   
   
   
   