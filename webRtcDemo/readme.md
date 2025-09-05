# 스프링부트 WebRTC 비디오 화면공유 (feat. kurento-media-server)

### 최소한의 기능들만 구현한 샘플용 프로젝트입니다.
다대다 화상회의 방식이 아닌 한명의 발표자와 다수의 청취자가 있는 발표용 화면공유 프로젝트입니다.

* Java 21
* Spring Boot 3
* Kurento Media Server

# 실행방법

* 웹캠 또는 카메라 둘 중 하나는 필수적으로 pc와 연결되어 있어야합니다.
  * 연결된 기기에 따라 /src/main/resources/static/js/*.js 속 <br> "localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true } );" <br>코드 수정 필요
* 도커가 설치되어 있어야합니다.

``` bash
git clone https://github.com/N1ghtsky0/toy-projects.git
cd webRtcDemo

# docker-compose.yml 속 "KMS_EXTERNAL_IPV4" 와 "KMS_URL" 은 실행하는 환경에 맞춰서 수정해야합니다.

./mvnw clean package -DskipTests
docker-compose up --build
```

* 실행 후 http://localhost:8080 으로 접속
* Kurento(/kurento.html) 는 kurento-media-server 와 연동된 sfu 방식
* WebRTC(/webRTC.html) 는 p2p 방식
