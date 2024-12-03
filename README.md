## 0. 개요
[가상 면접 사례로 배우는 대규모 시스템 설계 기초 1편](https://www.yes24.com/product/goods/102819435), 8장 URL 단축기 설계에서 나온 내용을 실제 코드로 구현해보았습니다.
&nbsp;

&nbsp;


## 1. 요구사항
- URL 단축 : 주어진 긴 URL을 짧게 줄이자.
- 축약된 URL로 요청이 오면 원래 URL을 응답으로 내려주자.
- 7자리까지 shorten 하자.

&nbsp;

&nbsp;

## 2. 개략적 설계안

1) URL 단축용 엔드포인트 : 단축할 URL을 인자로 실어서 POST 요청을 보내는 엔드포인트

2) URl 리디렉션용 엔드포인트 : 단축 URL에 대해서 HTTP 요청이 오면 원래 URL로 보내주기 위한 엔드포인트

&nbsp;

&nbsp;
## 3. URL 단축기 상세 설계
<img width="700" alt="image" src="https://github.com/user-attachments/assets/7c71da7d-19fe-44a9-af38-b6ee58709b41">
&nbsp;

&nbsp;

## 4. 이론 정리
URL 단축기를 만들기 위해서는 해시 함수가 필요한데 이를 구현하는 방법에는 크게 두 가지가 있습니다.
&nbsp;

&nbsp;
### 1. 해시 후 충돌 해소

잘 알려진 해시 함수(CRC32, MD5, SHA-1)를 이용해보기.
- 근데 가장 짧은 해시값도 7보다는 길다. 어떻게 줄이지?
- 첫번째 방법으로는 계산된 해시 값에서 첫 7글자만 사용하는 것입니다.
  - 하지만 이렇게 하면 서로 충돌할 확률이 높아집니다. 
  - 충돌이 실제로 발생했을 때는 다음과 같은 방법을 써볼 수 있습니다.

![image](https://github.com/user-attachments/assets/e4ee7841-3a9a-4563-a5c0-4e7b969e1e0f)

- 이 방법을 사용하면 충돌은 해소할 수 있지만 단축 URL을 생성할 때 한 번 이상의 DB 쿼리가 필요하므로 오버헤드가 큽니다 .
- 이를 개선하기 위해서 DB 대신 블룸 필터를 사용해볼 수도 있습니다.

&nbsp;

&nbsp;
### 2. base-62 변환

- 진법변환은 URL 단축키를 구현할 때 흔히 사용되는 접근법 중 하나입니다.
- 여기서 62 진법을 이용하는 이유는 hashValue로 쓰이는 문자 개수가 62개이기 때문입니다.
- 0 → 0, … 9 → 9, a → 10, … z → 35, A →36, … Z → 61로 대응시키면 
  - 11157 (10진법) → 2TX가 된다.
&nbsp;

&nbsp;
### 두 접근법 비교

![image](https://github.com/user-attachments/assets/3a900f16-7eaa-47ea-bc71-12b39c35171e)






**이번 구현에서는 base-62 변환방법을 사용했습니다.**
















