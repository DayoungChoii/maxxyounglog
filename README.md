# 스터디 인증 서비스

스터디 인증 서비스를 제공하는 BACK-END API 서버이다.

## 서비스 소개
회원가입시 포인트를 제공받는다. 사용자는 포인트를 사용해서 스터디를 참여할 수 있고 만약 인증 참여하지 않는다면 포인트를 돌려받지 못하고 스터디 방에서 탈퇴 당한다. 
스터디는 매달 1일에 시작하고 말일에 끝난다. 말일에 살아남은 회원들은 남은 포인트의 1/N을 다시 돌려받는다. 이렇게 매달 반복한다.

[프로젝트 총 정리글](https://velog.io/@maxxyoung/series/maxxlog)
## 프로젝트 목표
- 그동안 잘모르고 사용한 기술의 이해 높인다.
- 학습, 실무를 통해서 적용해보고 싶었던 기술 익힌다.

## 요구 사항 분석 및 스키마 도출
[요구 사항 분석과 스키마 도출](https://velog.io/@maxxyoung/maxxlog-%EC%9A%94%EA%B5%AC-%EC%82%AC%ED%95%AD-%EB%B6%84%EC%84%9D)
## ERD
![image](https://github.com/DayoungChoii/maxxyounglog/assets/38481737/3216f455-d8a4-45b3-87fb-bf31d0b5b836)

## 사용 기술 스택
`springboot 3.2.1`
`spring security 6.2.1`
`kotlin`
`mysql`
`h2`
`redis`
`jpa`
`docker`
`kafka`
## 서버 구조도
![image](https://github.com/DayoungChoii/maxxyounglog/assets/38481737/7b0461c7-ecb2-4510-a3ca-d8c580975ec4)


## API 명세서
[포스트맨 API 명세](https://documenter.getpostman.com/view/20625101/2sA3BobruN#9fe6853e-31f5-49c2-9b3d-674eaa0c3fd6)

## 이슈 정리
- [요구 사항 분석과 스키마 도출](https://velog.io/@maxxyoung/maxxlog-요구-사항-분석])
- [멀티 모듈을 통한 도메인 관리](https://velog.io/@maxxyoung/maxxlog-%EB%A9%80%ED%8B%B0-%EB%AA%A8%EB%93%88-%EC%A0%81%EC%9A%A9])
- [스프링 시큐리티를 활용한 인증, 인가 처리](https://velog.io/@maxxyoung/maxxlog-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0)
  - Redis를 활용하여 Refresh 토큰 저장
  - Custom Spring Filter를 활용한 인증, 인가 로직과  예외 처리 
- [포인트 적립 및 사용 처리를 Kafka를 활용하여 해결](https://velog.io/@maxxyoung/maxxlog-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EC%A0%81%EB%A6%BD-%EB%B0%8F-%EC%82%AC%EC%9A%A9-%EC%B2%98%EB%A6%AC)
  - Docker 환경에서 Kafka 설정
  - Kafka Transaction 적용 
- [스터디방 목록 조회 시 캐시를 적용하여 성능 향상](https://velog.io/@maxxyoung/maxxlog-%EC%8A%A4%ED%84%B0%EB%94%94%EB%B0%A9-%EB%AA%A9%EB%A1%9D-%EC%BA%90%EC%8B%9C-Redis)
  - Redis를 활용한 캐시적용
  -  목적에 따라 TTL로 shortTerm과 longTerm으로 분리
- [스터디방 가입 시 동시성 문제를 해결하기 위해 Redisson을 이용한 분산락 활용](https://velog.io/@maxxyoung/maxxlog-%EC%8A%A4%ED%84%B0%EB%94%94%EB%B0%A9-%EC%B0%B8%EC%97%AC-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4)

