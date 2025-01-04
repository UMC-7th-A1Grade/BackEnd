# A1Grade

![프로젝트 로고](https://avatars.githubusercontent.com/u/189431915?s=200&v=4)


## 🥔 A1Grade

> 프로젝트 요약</br>
> 프로젝트 요약</br>
> 프로젝트 요약

## 🎯 프로젝트의 목적(Purpose)

> <B>주관: </B>UMC 7th Pyxis 지부</br>
> <B>주제: </B></br>
> <B>아이디어: </B></br>
>
> <B>프로젝트 기간: </B> 2025/01/03 ~ 2025/02/21

## 🤩 팀원들(Team Members)

|         Lead(윤희준)          |                                       PM                                       |                                     Design                                      |                                  BE Developer                                   |                                  BE Developer                                   |                                  FE Developer                                   |
|:--------------------------:|:------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|
|  [](https://github.com/)   |                      [](https://github.com/)                      |                     [](https://github.com/)                      |                     [](https://github.com/)                      |                      [](https://github.com/)                       |                       [](https://github.com/)                       |
| <img src="" width="400" /> | <img src="" width="300" /> | <img src="" width="300" /> | <img src="" width="300" /> | <img src="" width="300" /> | <img src="" width="300" /> |

## 🛠️ 기술 스택(Tech)

### FE
![React](https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Bootstrap](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![Recoil](https://img.shields.io/badge/recoil-3578E5?style=for-the-badge&logo=recoil&logoColor=white)

### BE
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

### Infra
![Amazon AWS](https://img.shields.io/badge/AmazonAWS-f7f7f7?style=for-the-badge&logo=AmazonAWS&logoColor=f89400)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

### 협업 툴 (Tools)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

### 디자인 
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
![Adobe Illustrator](https://img.shields.io/badge/adobe%20illustrator-%23FF9A00.svg?style=for-the-badge&logo=adobe%20illustrator&logoColor=white)

## ERD 설계
![erd]()

## API 설계 문서
> 작성중

## GitHub Flow
![github-flow](https://i.ibb.co/p3Gfnvs/Kakao-Talk-20241115-230442579-01.png)

# 🎯 Branch Convention & Git Convention

## 🎯 Git Convention

- 🎉 **Start:** Start New Project [:tada:]
- ✨ **Feat:** 새로운 기능을 추가 [:sparkles:]
- 🐛 **Fix:** 버그 수정 [:bug:]
- 🎨 **Design:** CSS 등 사용자 UI 디자인 변경 [:art:]
- ♻️ **Refactor:** 코드 리팩토링 [:recycle:]
- 🔧 **Settings:** Changing configuration files [:wrench:]
- 🗃️ **Comment:** 필요한 주석 추가 및 변경 [:card_file_box:]
- ➕ **Dependency/Plugin:** Add a dependency/plugin [:heavy_plus_sign:]
- 📝 **Docs:** 문서 수정 [:memo:]
- 🔀 **Merge:** Merge branches [:twisted_rightwards_arrows:]
- 🚀 **Deploy:** Deploying stuff [:rocket:]
- 🚚 **Rename:** 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 [:truck:]
- 🔥 **Remove:** 파일을 삭제하는 작업만 수행한 경우 [:fire:]
- ⏪️ **Revert:** 전 버전으로 롤백 [:rewind:]

## 🪴 Branch Convention (GitHub Flow)

- `main`: 배포 가능한 브랜치, 항상 배포 가능한 상태를 유지
- `feature/{description}`: 새로운 기능을 개발하는 브랜치
    - 예: `feature/social-login`

### Flow

1. `main` 브랜치에서 새로운 브랜치를 생성.
2. 작업을 완료하고 커밋 메시지에 맞게 커밋.
3. Pull Request를 생성 / 팀원들의 리뷰.
4. 리뷰가 완료되면 `main` 브랜치로 병합.
5. 병합 후, 필요시 배포.

**예시**:

```bash
# 새로운 기능 개발
git checkout -b feature/social-login

# 작업 완료 후, main 브랜치로 병합
git checkout main
git pull origin main
git merge feature/social-login
git push origin main
```

## 🐋 로컬에서 Docker 로 빌드하기 🐋

1. Docker Desktop 실행
2. Build 하기 전 spotless 적용 ```./gradlew spotlessApply```
3. 정해진 컨벤션을 지키고 있는지 검사 (spotelesscheck) ```./gradlew spotlessCheck```
4. spring boot build ```./gradlew build```
5. Docker Desktop 실행중 인지 확인 ```docker info```
6. Docker로 빌드하기 ```docker build -t "본인 dockerhub repo 이름"/helfoome . ```
7. Docker Hub login(IDE terminal에서 실행) ```docker login```
8. Docker Image Tag ```docker tag "본인 dockerhub repo 이름"/helfoome "본인 dockerhub repo 이름"/helfoome:latest```
9. Docker Image Push ```docker push "본인 dockerhub repo 이름"/helfoome:latest```

### 🐋 Docker 실행중인 컨테이너 중지하기 🐋
1. 실행 중인 컨테이너 목록 확인 ```docker ps```
2. 컨테이너 중지 실행 중인 컨테이너의 CONTAINER ID를 찾아서 중지. ```docker stop <CONTAINER ID>```
3. 컨테이너의 삭제가 필요할 경우 ```docker rm <CONTAINER ID>```
4. 이미지 삭제가 필요할 경우 ```docker rmi <IMAGE ID or IMAGE NAME>```
