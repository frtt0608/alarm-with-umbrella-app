# ⏰우산 챙겨주는 알람시계



![우산 챙겨주는 알람시계graphic](C:\Users\82103\Desktop\alarm-weather-app\image\우산 챙겨주는 알람시계graphic.png)





### Overview

 알람과 날씨 기능을 접목한 **⏰우산 챙겨주는 알람시계**입니다.
**알람 생성 화면**에서 [위치]와 [2개의 알림음]을 설정할 수 있으며, 오늘의 날씨에 따라 다른 알림음과 UI를 제공하고 있습니다.

 현재 Android OS에서만 다운로드가 가능하며, 최소 API 21이상에서 사용 가능합니다.

https://play.google.com/store/apps/details?id=com.heon9u.alarm_weather_app



----

### Develop tools

- Android Studio
- Jetpack
- Java
- XML
- Open API
  - 도로명주소 API: https://www.juso.go.kr/addrlink/devAddrLinkRequestGuide.do?menu=roadApi
  - Openweather API: https://openweathermap.org/api/one-call-api





----

### Usage

 다운로드 후, 맨 처음 화면에서 2개의 권한 요청이 필요합니다. (자세한 사항은 Permissions 카테고리)

이후, 메인 화면에서 **[ + ]** 버튼을 눌러 **알람 생성 화면**으로 이동합니다.



|                          메인 화면                           |                        알람 생성 화면                        |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="C:\Users\82103\Desktop\alarm-weather-app\image\AlarmMain_empty.png" alt="AlarmMain_empty" style="zoom: 50%;" /> | <img src="C:\Users\82103\Desktop\alarm-weather-app\image\AlarmSet.png" alt="AlarmSet" style="zoom: 50%;" /> |



 알림음을 설정하는 경우, 스마트폰에 저장소에 접근하기 위한 권한이 요청됩니다. 마지막으로 [위치]를 터치하면 **저장된 위치 리스트**와 **위치 생성 화면**으로 이동하여 주소 검색 및 위치를 설정할 수 있습니다.

|                      저장된 위치 리스트                      |                        위치 생성 화면                        |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="C:\Users\82103\Desktop\alarm-weather-app\image\LocationList.png" alt="LocationList" style="zoom:50%;" /> | <img src="C:\Users\82103\Desktop\alarm-weather-app\image\LocationSearch.png" alt="LocationSearch" style="zoom:50%;" /> |



이후, 알람 시간이 되면 설정된 위치에 따라 오늘의 날씨 정보를 가져오고, 이를 기반으로 다른 알림음과 UI를 제공하게 됩니다.

|               날씨에 따라 다른 알람 해제 화면                |
| :----------------------------------------------------------: |
| <img src="C:\Users\82103\Desktop\alarm-weather-app\image\AlarmOnCloud.jpg" alt="AlarmOnCloud" style="zoom:20%;" /><img src="C:\Users\82103\Desktop\alarm-weather-app\image\AlarmOnRain.jpg" alt="AlarmOnRain" style="zoom:20%;" /> |





----

### Permissions

1. [배터리 최적화를 무시하도록 요청]
   - API 23이상부터 Doze모드가 적용되었습니다. 스마트폰을 특정 시간동안 사용하지 않는 경우, 시스템에서 wake lock을 무시하거나 네트워크 엑세스가 정지됩니다.
   - 해당 어플은 알람시간에 외부 API에서 날씨 정보를 가져오기 위해서 Doze모드에서도 네트워크 접근이 필요합니다.
   - 이를 위해 배터리 최적화에 대응할 수 있는 해당 권한이 필요합니다.
2. [다른 앱 위에 표시]
   - 알람 시간에 디스플레이가 꺼져있는 경우, **알람 해제 화면**을 띄우기위해 해당 권한이 필요합니다.
3. [외부 저장소 접근]
   - **알람 생성 화면**에서 스마트폰에 저장된 음악 파일에 접근하기 위해 해당 권한이 필요합니다.





----

### References

👍 [Icon 출처]
flaticon에서 Freepik이 만든 아이콘
flaticon에서 pixel perfect가 만든 아이콘


👍 [Image출처]
Photo by JanFillem on Unsplash
Photo by Ali Abdul Rahman on Unsplash
Photo by Tony-Sebastian on Unsplash
Photo by Agustin Gunawan on Unsplash

Photo by Filip Zrnzević on Unsplash
Photo by Eberhard Grossgasteiger on Unsplash



👍 [Font 출처]
font by Cookierun_Regular, kyobo_2019 on noonu

