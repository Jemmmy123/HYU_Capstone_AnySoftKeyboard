# 졸업 프로젝트 계획

> **주제:** Android 키보드 앱의 민감 입력 보호 정책 분석 및 보안 강화  
> **베이스:** AnySoftKeyboard (오픈소스 Android IME, Apache 2.0)  
> **팀:** 2명 (A / B)  
> **기간:** 약 5개월

---

## 1. 프로젝트 개요

### 배경
키보드 앱의 자동 추천 기능은 입력 편의성을 높이지만, 입력 필드의 성격을 잘못 판단하면 민감 정보가 추천 후보, 클립보드 strip, 개인화 학습 경로에 노출될 수 있다.

Android IME는 앱이 전달하는 `EditorInfo`, `InputType`, `imeOptions`를 기반으로 입력 필드를 판단한다. 따라서 키보드가 비밀번호, PIN, 개인화 학습 금지, 추천 비활성화 플래그를 일관되게 처리하는지 분석할 필요가 있다.

### 목표
1. AnySoftKeyboard의 민감 입력 보호 정책을 코드와 PoC 앱으로 분석한다.
2. `InputType`/`imeOptions` 조합별 추천 표시, 클립보드 표시, 학습 차단 여부를 실험한다.
3. 보호 정책의 경계 조건을 식별하고, 핵심 문제를 보안 패치로 보완한다.
4. 원본 버전과 패치 버전을 동일 PoC로 비교 평가한다.

### 핵심 주제
**민감 입력 필드에서의 추천 strip, 클립보드 strip, 개인화 학습 정책 검증 및 보강**

- `TYPE_TEXT_VARIATION_PASSWORD`, `TYPE_NUMBER_VARIATION_PASSWORD` 등 민감 입력 타입 처리 분석
- `TYPE_TEXT_FLAG_NO_SUGGESTIONS`, `IME_FLAG_NO_PERSONALIZED_LEARNING` 등 보안 관련 플래그 처리 분석
- 숫자 비밀번호/PIN 필드의 클립보드 마스킹 정책 보강
- PoC 앱을 통한 패치 전/후 비교

---

## 2. 역할 분담

### A: 분석 & PoC 담당
- AnySoftKeyboard의 입력 필드 처리 흐름 분석
- `InputType`/`imeOptions`별 테스트 케이스 설계
- PoC Android 앱 제작
- 원본/패치 버전 비교 실험
- 결과물: 분석 보고서 + PoC 앱 + 평가 결과

### B: 방어 & 구현 담당
- A가 발견한 핵심 경계 조건을 코드 수정으로 보완
- 클립보드 마스킹 조건 강화
- 필요 시 `NO_SUGGESTIONS` 정책 개선 검토
- 관련 단위 테스트 작성
- 결과물: 패치된 AnySoftKeyboard + 테스트

### 역할 분담의 장점
- 분석 → 재현 → 패치 → 재검증 흐름이 명확함
- A의 PoC로 B의 패치 효과를 바로 검증 가능
- 발표 시 분석 파트와 구현 파트의 책임 범위가 분명함

---

## 3. 주차별 일정

### Phase 1: 공동 학습

- AnySoftKeyboard 빌드 및 실행 환경 세팅
- Android IME 구조 학습
- `InputMethodService`, `EditorInfo`, `InputType`, `imeOptions` 이해
- 추천 strip, 클립보드 strip, 인코그니토/학습 차단 관련 코드 위치 파악

### Phase 2: 분석 및 PoC

#### A 담당

| 단계 | 작업 |
|---|---|
| 1 | `onStartInput`, `onStartInputView` 흐름 분석 |
| 2 | `InputType`/`imeOptions` 조합별 테스트 케이스 정리 |
| 3 | PoC Android 앱 제작 |
| 4 | 원본 AnySoftKeyboard에서 추천 strip/클립보드 strip 동작 측정 |
| 5 | 발견 사항 보고서화 및 B에게 패치 요구사항 전달 |

#### B 담당

| 단계 | 작업 |
|---|---|
| 1 | A가 전달한 F2 클립보드 마스킹 이슈 분석 |
| 2 | 숫자 비밀번호/PIN 필드에서도 클립보드가 마스킹되도록 수정 |
| 3 | 관련 단위 테스트 작성 |
| 4 | 필요 시 F1 `NO_SUGGESTIONS` 정책 개선 검토 |
| 5 | 패치 APK 제공 |

### Phase 3: 통합 및 평가

- A의 PoC 앱으로 B 패치 버전 재검증
- 원본/패치 버전 결과표 작성
- 추천 차단률, 클립보드 마스킹률, 회귀 여부 비교
- 최종 보고서 및 발표 자료 작성
- 데모 영상 제작

---

## 4. 현재 핵심 발견 사항

| ID | 내용 | 현재 판단 |
|---|---|---|
| F1 | `NO_SUGGESTIONS`가 `AUTO_CORRECT`/`AUTO_COMPLETE`와 함께 있을 때 추천 strip 표시 | 정책 충돌, 선택 개선 |
| F2 | 숫자 비밀번호/PIN 필드에서 클립보드 strip 마스킹 조건 누락 | 핵심 패치 대상 |
| F3 | 웹/이메일 필드에서 추천 strip 유지 | 관찰 결과 |
| F4 | `IME_FLAG_NO_PERSONALIZED_LEARNING`은 학습 차단이 중심이고 추천 strip은 유지 | 관찰 결과 |
| F5 | `hint="비밀번호"`만 있는 일반 텍스트 필드는 민감 필드로 판단되지 않음 | 향후 개선점 |
| F6 | `TYPE_NULL` 필드는 이번 PoC에서 추천 strip 미재현 | 낮은 우선순위 |

---

## 5. 핵심 파일

ime/app/src/main/java/com/anysoftkeyboard/
├── ime/
│   ├── AnySoftKeyboardClipboard.java      ← F2 클립보드 마스킹
│   ├── AnySoftKeyboardIncognito.java      ← 비밀번호/PIN 학습 차단
│   ├── AnySoftKeyboardSuggestions.java    ← 추천 활성 여부
│   └── AnySoftKeyboardPressEffects.java   ← 비밀번호/PIN 키 미리보기 차단
└── utils/
    └── IMEUtil.java                       ← F1 NO_SUGGESTIONS 정책
PoC 앱:

poc-app/
분석 문서:

docs/analysis/졸프_분석_보고서.md
docs/analysis/B에게_전달할_보안_패치_요구사항.md

---

## 6. Git 운영
A: analysis 브랜치
PoC 앱
분석 보고서
실험 결과
B: feature/security-enhancement 브랜치
AnySoftKeyboard 보안 패치
테스트 코드
통합 시 A의 PoC로 B의 패치 검증

---

## 7. 발표 역할
파트	담당
도입 / 문제 정의	A
AnySoftKeyboard 보호 정책 분석	A
PoC 앱 및 원본 결과	A
보안 패치 구현	B
패치 전/후 비교 평가	A
Q&A	영역별 분담

---

## 8. 현재 진행 상황
AnySoftKeyboard 빌드 환경 세팅
에뮬레이터 실행 및 IME 활성화
onStartInput / onStartInputView 흐름 분석
PoC 앱 제작
F1~F6 테스트 필드 구성
F2 클립보드 마스킹 이슈 재현
B 담당 패치 요구사항 문서 작성
B 패치 구현
패치 버전 재검증
최종 평가표 작성
