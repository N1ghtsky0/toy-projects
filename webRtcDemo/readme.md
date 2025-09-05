# 스프링부트 WebRTC 화면 공유 (feat. Kurento Media Server)

### 📌 발표용 화면 공유 샘플 프로젝트
발표자 1명과 여러 명의 청취자가 참여하는 **단방향 화면 공유**를 위한 최소 기능만 구현한 WebRTC 프로젝트입니다.  
(다대다 화상회의 방식이 아닙니다)

---

## ⚙️ 기술 스택

- Java 21
- Spring Boot 3
- [Kurento Media Server (KMS)](https://doc-kurento.readthedocs.io/en/stable/#)

---

## 🚀 실행 방법

### 1. 사전 준비
- **웹캠 또는 카메라**가 연결되어 있어야 합니다.
    - 기기에 따라 아래 코드에서 수정이 필요할 수 있습니다:
      ```javascript
      localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
      ```
      > 위치: `/src/main/resources/static/js/*.js`

- **Docker**가 설치되어 있어야 합니다.

---

### 2. 프로젝트 실행

```bash
git clone https://github.com/N1ghtsky0/toy-projects.git
cd webRtcDemo

# docker-compose.yml 내 환경 변수 수정 필요:
# - KMS_EXTERNAL_IPV4
# - KMS_URL

./mvnw clean package -DskipTests
docker-compose up --build
