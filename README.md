    # 졸업 프로젝트 계획
 
> **주제:** 키보드 앱의 자동 추천 기능 보안성 강화
> **베이스:** AnySoftKeyboard (오픈소스 Android IME, Apache 2.0)
> **팀:** 2명 (A / B)
> **기간:** 약 5개월
 
---
 
## 1. 프로젝트 개요
 
### 배경
키보드 앱의 자동 추천 기능은 사용 편의성을 위해 제공되지만, ID·패스워드 등 민감 정보가 학습되거나 추천 후보로 노출되어 기밀 유출 경로가 될 수 있다.
 
### 목표
1. AnySoftKeyboard의 자동 추천 동작을 분석하여 민감 정보 유출 가능 경로를 식별한다.
2. 식별된 취약점을 보완하는 보안 강화 기능을 구현한다.
3. 강화 전/후를 정량적으로 비교 평가한다.
### 핵심 주제
**민감 입력 필드에서의 추천/학습 차단 강화**
 
- 안드로이드의 `InputType` 플래그(`TYPE_TEXT_VARIATION_PASSWORD` 등)가 키보드에서 충분히 처리되는지 분석
- 누락된 케이스 및 우회 가능 시나리오 도출
- 패치 및 휴리스틱 보강을 통해 차단 범위 확장
---
 
## 2. 역할 분담
 
### A: 분석 & 공격 담당
- 현재 키보드가 막지 못하는 케이스 발굴
- 결과물: 취약점 진단 보고서 + PoC 데모 앱
### B: 방어 & 구현 담당
- 발견된 문제를 키보드 코드 수정으로 차단
- 결과물: 패치된 AnySoftKeyboard + 강화 기능
### 역할 분담의 장점
- 졸프 스토리가 자연스러움 (문제 발견 → 해결)
- 서로 다른 전문 영역 형성 → 발표 시 Q&A 분담 가능
- 한쪽이 막혀도 다른 쪽 진행 가능
- 마지막에 A의 PoC로 B의 패치를 검증 → 자연스러운 평가 파트 도출
---
 
## 3. 주차별 일정
 
### Phase 1: 공동 학습
 
둘 다 함께 진행:
- AnySoftKeyboard 빌드 및 실행 환경 세팅
- 코드베이스 전체 구조 파악
- 안드로이드 IME 동작 원리 학습 (`InputMethodService`, `EditorInfo`, `InputType`)
- 자동 추천 관련 핵심 파일 위치 공동 파악
### Phase 2: 본격 분담
 
#### A 담당 (분석/공격)
 
| 단계 | 작업 |
|---|---|
| 1 | InputType 플래그 종류별 케이스 정리, 각 케이스 코드 동작 추적 |
| 2 | 테스트용 Android 앱 제작 (다양한 InputType의 EditText 모음) |
| 3 | 실험: 각 필드 입력 → 학습/추천 발생 여부 측정, 표로 정리 |
| 4 | 추가 시나리오: 연락처 기반 추천 누출, 클립보드 추천 등 |
| 5 | 취약점 보고서 작성 + PoC 데모 영상 |
 
#### B 담당 (방어/구현)
 
| 단계 | 작업 |
|---|---|
| 1 | 추천/학습 핵심 코드 분석 (`AnySoftKeyboard.java`, 사전 관련 클래스) |
| 2 | InputType 검사 로직 강화 — 누락 케이스 차단 |
| 3 | 휴리스틱 탐지 추가 — hint/label 키워드 검사 ("password", "비밀번호" 등) |
| 4 | 학습된 사전에서 민감 패턴 자동 제거 (긴 영숫자 혼합, 카드번호 형식 등) |
| 5 | 사용자 알림 UI — "이 필드는 민감 정보로 판단되어 학습하지 않습니다" |
 
### Phase 3: 통합 & 마무리
 
1) **(공동 - 가장 중요)**
- A의 PoC 앱으로 B의 패치 검증
- A: 발견 케이스 중 패치 후 차단된 비율 측정
- B: 새로 발견된 케이스 추가 패치
- 졸프 발표의 핵심 데이터 도출 구간
2) 
- A: 최종 보고서 작성 (분석 + 평가 결과)
- B: 최종 보고서 작성 (구현 + 기여 포인트)
- 데모 영상 공동 제작
3) 
- 발표 자료 제작, 리허설, 예상 질문 정리
---
 
## 4. 분석 시작 포인트 (참고)
 
### 핵심 파일
```
ime/app/src/main/java/com/anysoftkeyboard/
├── AnySoftKeyboard.java           ← onStartInput / onStartInputView
├── dictionaries/                  ← 사전 및 학습 로직
└── keyboards/views/CandidateView* ← 추천 후보 UI
```
 
### 주요 InputType 플래그
- `TYPE_TEXT_VARIATION_PASSWORD`
- `TYPE_TEXT_VARIATION_VISIBLE_PASSWORD`
- `TYPE_TEXT_VARIATION_WEB_PASSWORD`
- `TYPE_NUMBER_VARIATION_PASSWORD`
- `TYPE_TEXT_FLAG_NO_SUGGESTIONS`
- `TYPE_CLASS_NUMBER` + 신용카드 패턴 등
---
 
## 5. 협업 운영
 
### Git 운영
- 각자 fork에서 작업
- A: `analysis` 브랜치 (PoC 앱 + 문서)
- B: `feature/security-enhancement` 브랜치 (키보드 패치)
- 8주차 통합 시점에 merge
### 주간 동기화
- 매주 1회 30분 진행 상황 공유 미팅
- A가 발견한 케이스 → B에게 즉시 공유 (B가 막아야 하므로)

 
---
 
## 6. 발표 시 역할 분담
 
| 파트 | 담당 |
|---|---|
| 도입 / 주제 동기 | A |
| 분석 방법론 / 결과 | A |
| 구현 내용 | B |
| 평가 결과 | A (자기 PoC로 검증) |
| Q&A | 영역별로 분담 |
 
---

 
## 7. 현재 진행 상황
 
- [x] AnySoftKeyboard 빌드 환경 세팅 (macOS, Android Studio)
- [x] 에뮬레이터 실행 및 IME 활성화 완료
- [x] 권한 설정 완료 (Contacts 기반 추천 허용)
- [x] Auto suggestion aggression: Maximum
- [ ] 코드베이스 구조 파악
- [ ] InputType 처리 코드 추적 시작
 
