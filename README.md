<h1 align="center"> 🚨삐용삐용🚨 </h1>
<h3 align="center">응급대처 가이드 서비스</h3>
<br />
<br />

<h3>☺️ 서비스 이름 및 소개 </h3>
 <div align="left" style="margin-bottom: 20px;">
            <img width="617" alt="Logo" src="https://github.com/user-attachments/assets/3e642332-2214-43bd-a9de-11393b1fd9b9" style="max-width: 100%; height: auto;">
        </div>
<body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;">
    <div style="max-width: 400px; margin: 50px auto; background-color: #ffffff; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; padding: 20px;">   

        삐용삐용은 AI기반 응급대처가이드를 제공합니다.
        삐용삐용은 다급한 응급상황을 뜻하며, 이 서비스를 통해 다양한 응급상황을 대처, 예방 그리고 모두가 일상적 대처법 학습을 목표로 하고 있습니다.

</body>
<br />





<br/>
<hr/>

## 👀 View 소개
### 🌱 Main View

| <img src="https://github.com/user-attachments/assets/3d5be14c-1cf0-4537-9e4f-f86c7f9a560b"/> | <img src="https://github.com/user-attachments/assets/baadde38-aece-43eb-a2a4-00dbdd7bed01"/> | <img src="https://github.com/user-attachments/assets/e29d031b-332c-477b-8e65-a3009e65ed60"/> |
| :---: | :---: | :---: |
| 1️⃣ 로그인 | 2️⃣ 회원가입 | 3️⃣ 마이페이지|


### 🌱 Hospital Map & Chat bot View

| <img src="https://github.com/user-attachments/assets/f2d62458-0f59-4086-97c9-ee618a2b910d"/> | <img src="https://github.com/user-attachments/assets/7918a868-5f9e-4811-860e-3a224d645fa5"/> | <img src="https://github.com/user-attachments/assets/e29d031b-332c-477b-8e65-a3009e65ed60"/> |
| :---: | :---: | :---: |
| 1️⃣ 응급지도 | 2️⃣ 병원 전화연결 | 3️⃣ 챗봇|

### 🌱 Manual view

| <img src="https://github.com/user-attachments/assets/93a3c1d1-1ee5-4498-abc3-cad3ed5e1b34"/>| <img src="https://github.com/user-attachments/assets/b6c68442-a318-403e-bf96-b4e3d794891e"/> |
| :---: | :---: | 
| 1️⃣ 매뉴얼 | 2️⃣ 세부 매뉴얼 | 


<br />





<br/>
<hr/>


<h2>👩🏻‍💻 Server Developers </h2>


| <img src="https://github.com/user-attachments/assets/c0401bdd-ba90-47f2-856e-43a6685d5e14" width="200"/> | <img src="https://github.com/user-attachments/assets/c0401bdd-ba90-47f2-856e-43a6685d5e14" width="200"/> | <img src="https://github.com/user-attachments/assets/c0401bdd-ba90-47f2-856e-43a6685d5e14" width="200"/> |
| :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: | 
|                                           [전유연](https://github.com/youyeon11)                                            |                                          [이효린](https://github.com/temari-cpu)                                           |                                          [최민준](https://github.com/202010927choiminjune)                                           |                                       
|                                                   `Backend`                                                   |                                                         `Backend`                                                          |                                                         `Backend`                                                          |                                                        



<br />



### **🚎 Architecture**

![다이어그램 백엔드 drawio (2)](https://github.com/user-attachments/assets/d61f3451-5881-46b1-97e4-9325f6f63bc1)

<br />



### **📈 데이터베이스**



![image (1)](https://github.com/user-attachments/assets/e92f5b95-5ff0-43f4-a793-35d96e2adb4e)

<br />


## Teck Stack ✨

| IDE | IntelliJ |
|:---|:---|
| Language | Java 17 |
| Framework | Spring Boot, Gradle |
| Authentication | Spring Security, JSON Web Tokens |
| Orm | Spring Data JPA |
| Database | MYSQL, Redis |
| External | AWS EC2, AWS RDS, AWS S3, Nginx|
| CI | Github Action |
| External API | KAKAO |
| API Docs | Notion, Swagger |
| Other Tool | Discord, Postman, Figma |

<br />







## 📁 폴더 구조

```
├── 📁 ppiyong-api
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 api
│                       ├── 📁 emergencymap
│                       │   ├── controller
│                       │   ├── domain
│                       │   ├── dto
│                       │   ├── mapper
│                       │   └── repository
|                       |   └── Service
│                       ├── 📁 manual
│                       │   ├── controller
│                       │   ├── domain
│                       │   ├── dto
│                       │   ├── mapper
│                       │   └── repository
|                       |   └── Service
|                       |   📁 Mypage
│                       │   ├── controller
│                       │   ├── domain
│                       │   ├── dto
│                       │   ├── mapper
│                       │   └── repository
|                       |__ └── Service
├── ppiyong-api
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 external
│                       ├── 📁 AwsConfig
│                       ├── 📁 S3service
│                       
├── ppiyong-api
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 global
│                       ├── 📁 auth
│                       ├── 📁 config
│                       │   └── jwt
|                       |__ └── authentication
|                       |__ |__ tokenprovider
│                       ├── 📁 config
│                       │   ├── appconfig
│                       │   └── securityconfig
│                       │   ├── casheconfig
│                       │   └── swaggerconfig
│                       ├── 📁 exception
│                       ├── 📁 kakao
│                       │   ├── controller
│                       │   └── dto
│                       │   └── service
│                       ├── 📁 security
│                       │   ├── controller
│                       │   └── domain
│                       │   └── dto
│                       │   └── service
└── gradle
    └── wrapper
```

<hr></hr>


<br/>






<br />

## 📌 협업 공간
### [📏 컨벤션 노션 링크](https://www.notion.so/13920e6d515c8027a202ff4ceead9f22)
<br/>
<br />

## 🖊️ 협업 공간

### [📏 협업 공간 피그마 링크](https://www.figma.com/design/H2T6Ut8GnVuMpoH03ccma8/UI?node-id=2-653&t=rUl6RYQ0POvzHHHI-0)

<br/>
<br/>



## 🖍️ 스웨거

### [📏스웨거 링크](http://52.79.245.244/swagger-ui/index.html)

<br/>
<br/>

### ✨ Git 컨벤션

<details>
<summary>  1️⃣ Commit 컨벤션  </summary>

<br />
<strong>Commit Type</strong>

<br />

```
type: subject 
type과 콜론 후 한칸 띄고 subject 
```

- **Commit 메시지 종류 설명**

| 제목     | 내용                                        |
| -------- | ------------------------------------------- |
| feat     | 새로운 기능에 대한 커밋                     |
| fix      | 버그 수정에 대한 커밋                       |
| build    | 빌드 관련 파일 수정에 대한 커밋             |
| chore    | 그 외 자잘한 수정에 대한 커밋               |
| docs     | 문서 수정에 대한 커밋                       |
| style    | 코드 스타일 혹은 포맷 등에 관한 커밋 |
| refactor | 코드 리팩토링에 대한 커밋                   |
| etc      | 위에 해당하지 않는 모든 변경(가능한 x) |
| test     | 테스트 코드 추가, 삭제, 변경 |

<br/>

</details>

<details>
<summary> 2️⃣ Branch 전략 </summary>

- `Git-Flow` 전략
- 브랜치 운영
    - `main` : 완전히 안전하다고 판단되었을 때, 즉 배포가 가능한 최종 merge하는 곳
    - `develop` : 배포하기 전 개발 중일 때 각자의 브랜치에서 merge하는 브랜치
    - `기능명`: 새로운 기능 개발. 개발이 완료되면 main 브랜치로 병합
   
<br/>


