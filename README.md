# SeoulMOA.zip

<div align="center">
<img width="329" alt="image" src="https://i.ibb.co/Lz6FTjR/DALL-E-2023-12-18-14-59-48-Modify-the-logo-design-to-have-a-white-background-featuring-a-pale-grey-o.png">
</div>

# 🎨 SeoulMOA.zip
>**2023-2 모바일응용 최종 개인 프로젝트**
>

'SeoulMOA.zip'은 서울 시립 미술관(남서울 미술관, 북서울 미술관, 서소문 본관 등)에서 진행하는 다양한 미술 전시회 정보를 탐색할 수 있는 안드로이드 애플리케이션입니다. 

<br/>

## 📁 프로젝트 기획서 
사용자가 원하는 전시회를 쉽게 찾고, 관심 있는 전시회를 저장하며, 방문한 전시회에 대한 평가를 기록할 수 있고, 미술관 주변의 카페 정보를 알려주는 기능도 있다면 편리할 거 같아서 해당 애플리케이션을 기획하였습니다. 

**기획서 바로가기** 👉🏻 https://good-vein-bae.notion.site/46977f2b5f2f43e694bdeefede8f5c5e

<br/>

## 📱 화면 구성 
| 메인  |  전시 상세   |  관심 전시   |  다녀온 전시  | 
| :-------: | :------------: | :------------: | :------------: | 
|  <img width="300" src="https://github.com/mnbvcxzyj/SeoulMOA.zip/assets/101444425/0a19a19f-2856-4bab-ac35-e41d04dd4f5d" /> | <img width="300" alt="image" src="https://github.com/mnbvcxzyj/SeoulMOA.zip/assets/101444425/27d356c9-2132-4ed9-80dc-2ec81292b81c" /> |  <img width="300" alt="image" src="https://github.com/mnbvcxzyj/SeoulMOA.zip/assets/101444425/4f846b8c-f029-4adf-80ef-b77255819d34" />    |  <img width="300" alt="image" src="https://github.com/mnbvcxzyj/SeoulMOA.zip/assets/101444425/cce27ff3-da5f-44e2-b600-2107ee1cb3a3" />  |  

<br/>

## 🔎 주요 기능 
### 메인 
- 전체 전시 목록 조회
- 전시회명으로 전시 검색
- 메뉴를 통해 관심전시, 다녀온 전시, 제작자 보기로 이동 

### 전시 상세 
- 전시 세부 정보 및 주변 카페 마커 표시
- 하단 버튼을 이용하여 관심전시 및 다녀온 전시 저장 

### 관심 전시 
- 관심 전시 목록 조회
- 관심 전시 삭제

### 다녀온 전시 
- 다녀온 전시 목록 조회
- 다녀온 전시 삭제
- 다녀온 전시 별점 저장
- 카메라를 이용하여 전시 사진 저장 

<br/>

## 🖥️ 기술 설명 
- **Retrofit**과 **GsonConverter**를 사용하여 API 통신 및 응답 처리. 
- cleanHTMLString 함수를 작성하여 API 정보 중 HTML 태그나 특수문자를 제거
- **Glide**를 사용하여 이미지 처리 
- **Room Database**를 사용하여 관심 전시, 다녀온 전시 정보 저장 및 관리 
- **Coroutines**를 사용하여 비동기 처리
- **FileProvider, Intent, MediaStore** 등을 사용하여 카메라 기능 사용 



