# B 담당 보안 패치 요구사항

작성일: 2026-05-20
작성자: A 담당, 분석 및 PoC

## 1. 전달 목적

본 문서는 A 담당이 원본 AnySoftKeyboard를 분석하고 PoC 앱으로 재현한 결과 중, B 담당자가 방어 구현 대상으로 삼을 항목을 정리한 것이다.

현재 가장 우선순위가 높은 구현 대상은 F2 숫자 비밀번호/PIN 필드의 클립보드 마스킹 누락이다. F1 `NO_SUGGESTIONS` 플래그 충돌은 기존 AnySoftKeyboard의 의도된 호환성 정책과 연결되어 있으므로, 2차 개선 또는 정책 옵션으로 다루는 것이 안전하다.

## 2. 구현 우선순위

| 우선순위 | ID | 항목 | 구현 판단 |
|---:|---|---|---|
| 1 | F2 | 숫자 비밀번호/PIN 필드의 클립보드 마스킹 누락 | 필수 구현 |
| 2 | F1 | `NO_SUGGESTIONS`와 `AUTO_CORRECT`/`AUTO_COMPLETE` 충돌 | 정책 결정 후 선택 구현 |
| 3 | F5 | `hintText` 기반 민감 필드 탐지 | 시간이 남을 때 검토 |
| 보류 | F3/F4/F6 | 웹/이메일/학습금지/TYPE_NULL 관찰 결과 | 보고서 관찰 항목으로 유지 |

## 3. F2: 숫자 비밀번호/PIN 클립보드 마스킹 누락

### 3.1 문제 요약

텍스트 비밀번호 필드에서는 클립보드 strip이 `**********`로 마스킹된다. 하지만 `TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD`로 설정된 숫자 비밀번호/PIN 필드에서는 비보안 컨텍스트에서 복사된 클립보드 값이 평문으로 표시될 수 있다.

이 문제는 추천 학습 문제가 아니라 키보드 상단 clipboard strip UI를 통한 직접 노출 문제이다.

### 3.2 재현 조건

PoC 앱 기준:

1. AnySoftKeyboard 원본 버전을 기본 키보드로 설정한다.
2. AnySoftKeyboard의 OS clipboard sync 기능을 활성화한다.
3. PoC 앱에서 T1 일반 텍스트 또는 비보안 컨텍스트가 포커스된 상태로 `Copy F2 Clipboard`를 누른다.
4. T2 텍스트 비밀번호 필드로 이동한다.
5. T3 숫자 비밀번호/PIN 필드로 이동한다.

관찰 결과:

| 필드 | 설정 | 원본 결과 |
|---|---|---|
| T2 | `TYPE_TEXT_VARIATION_PASSWORD` | 클립보드 strip 마스킹됨 |
| T3 | `TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD` | 클립보드 strip 평문 표시 가능 |

주의할 점:

- T2 텍스트 비밀번호 필드가 포커스된 상태에서 클립보드 값을 복사하면 AnySoftKeyboard가 해당 값을 secure-origin clipboard로 기록하여 다른 필드에서도 마스킹할 수 있다.
- F2 검증은 일반 텍스트 또는 비보안 컨텍스트에서 복사한 클립보드 값이 숫자 비밀번호 필드에서 어떻게 보이는지를 확인한다.

### 3.3 관련 코드

클립보드 값이 바뀔 때 secure-origin 여부를 저장하는 위치:

`ime/app/src/main/java/com/anysoftkeyboard/ime/AnySoftKeyboardClipboard.java`

```java
mLastSyncedClipboardEntryInSecureInput = isTextPassword(currentInputEditorInfo);
```

클립보드 strip에 값을 표시할 때 마스킹 여부를 결정하는 위치:

```java
mSuggestionClipboardEntry.setClipboardText(
    mLastSyncedClipboardLabel, mLastSyncedClipboardEntryInSecureInput || isTextPassword(info));
```

현재 문제는 두 위치 모두 텍스트 비밀번호만 확인하고 숫자 비밀번호를 확인하지 않는다는 점이다.

반면 다른 보안 로직에서는 숫자 비밀번호를 이미 민감 입력으로 취급한다.

`ime/app/src/main/java/com/anysoftkeyboard/ime/AnySoftKeyboardIncognito.java`

```java
if (isNoPersonalizedLearning(info) || isTextPassword(info) || isNumberPassword(info)) {
  setIncognito(true, false);
}
```

`ime/app/src/main/java/com/anysoftkeyboard/ime/AnySoftKeyboardPressEffects.java`

```java
mKeyPreviewForPasswordSubject.onNext(isTextPassword(info) || isNumberPassword(info));
```

즉, 인코그니토 및 키 미리보기 차단 정책은 숫자 비밀번호를 고려하지만, 클립보드 마스킹 정책만 숫자 비밀번호를 누락하고 있다.

### 3.4 수정 제안

`AnySoftKeyboardClipboard.java`에서 클립보드 마스킹 판단을 공통 helper로 묶는 방식을 권장한다.

예시:

```java
private static boolean isPasswordField(@Nullable EditorInfo info) {
  return isTextPassword(info) || (info != null && isNumberPassword(info));
}
```

그 다음 두 위치를 다음 방향으로 수정한다.

```java
mLastSyncedClipboardEntryInSecureInput = isPasswordField(currentInputEditorInfo);
```

```java
mSuggestionClipboardEntry.setClipboardText(
    mLastSyncedClipboardLabel, mLastSyncedClipboardEntryInSecureInput || isPasswordField(info));
```

주의:

- `isTextPassword(...)`는 `null`을 처리한다.
- `isNumberPassword(...)`는 현재 `EditorInfo` non-null을 전제로 하므로 helper에서 null check를 먼저 해야 한다.
- 함수명은 기존 코드 스타일에 맞춰 `isPasswordField`, `isSensitivePasswordField` 등으로 정하면 된다.

### 3.5 기대 결과

패치 후 기대 결과:

| 필드 | 기대 결과 |
|---|---|
| T1 일반 텍스트 | 비보안 클립보드 값 평문 표시 가능 |
| T2 텍스트 비밀번호 | 클립보드 값 마스킹 |
| T3 숫자 비밀번호/PIN | 클립보드 값 마스킹 |

### 3.6 권장 테스트

우선 테스트 후보:

- `ime/app/src/test/java/com/anysoftkeyboard/ime/AnySoftKeyboardClipboardTest.java`
- 테스트 이름 예시: `testClipboardEntryIsMaskedInNumberPasswordField`

테스트 관점:

| 구분 | 케이스 | 기대 결과 |
|---|---|---|
| Happy path | 일반 텍스트 필드에서 복사 후 일반 텍스트 필드 포커스 | 평문 표시 |
| Security case | 일반 텍스트 필드에서 복사 후 텍스트 비밀번호 필드 포커스 | 마스킹 표시 |
| Security case | 일반 텍스트 필드에서 복사 후 숫자 비밀번호 필드 포커스 | 마스킹 표시 |
| Regression | 텍스트 비밀번호 필드에서 복사 후 일반 필드 포커스 | 기존처럼 마스킹 유지 |

테스트 실행 예시:

```bash
./gradlew :ime:app:testDebugUnitTest --tests "com.anysoftkeyboard.ime.AnySoftKeyboardClipboardTest"
```

## 4. F1: `NO_SUGGESTIONS` 플래그 충돌

### 4.1 문제 요약

`TYPE_TEXT_FLAG_NO_SUGGESTIONS`는 앱이 키보드에게 추천을 표시하지 말라고 요청하는 플래그이다. 그러나 AnySoftKeyboard는 이 플래그가 `TYPE_TEXT_FLAG_AUTO_CORRECT` 또는 `TYPE_TEXT_FLAG_AUTO_COMPLETE`와 함께 설정되면 `NO_SUGGESTIONS`를 무시한다.

현재 코드는 다음 정책을 명시한다.

`ime/app/src/main/java/com/anysoftkeyboard/utils/IMEUtil.java`

```java
if (hasAutoCorrect || hasAutoComplete) {
  return false;
}
```

이 동작은 Google Keep 등 일부 앱이 모순된 플래그를 보내는 상황에서 사용성을 유지하기 위한 호환성 정책으로 보인다. 따라서 F1은 단순 버그라기보다 보안 우선 관점에서의 정책 충돌이다.

### 4.2 PoC 결과

| 필드 | 설정 | 원본 결과 |
|---|---|---|
| T4 | `TYPE_TEXT_FLAG_NO_SUGGESTIONS` | 추천 strip 미표시 |
| T5 | `NO_SUGGESTIONS | AUTO_CORRECT` | 추천 strip 표시 |
| T6 | `NO_SUGGESTIONS | AUTO_COMPLETE` | 추천 strip 표시 |

현재 PoC는 추천 strip 표시 여부를 확인한 것이다. 개인 사전 학습 여부는 별도 검증이 필요하다.

### 4.3 구현 옵션

F1은 바로 수정하기보다 팀 내 정책 결정이 필요하다.

옵션 A: 원본 정책 유지

- 장점: 기존 테스트와 앱 호환성 유지
- 단점: 보안 플래그가 사용성 플래그보다 약하게 처리됨

옵션 B: `NO_SUGGESTIONS`를 항상 존중

- 장점: 보안 의도 명확
- 단점: 기존 테스트 변경 필요, 일부 앱에서 자동수정 UX 저하 가능

옵션 C: 보안 우선 설정 옵션 추가

- 장점: 사용자가 보안 우선 동작을 선택 가능
- 단점: 설정 UI/문구/테스트 범위 증가

졸업 프로젝트 범위에서는 옵션 A를 유지하되, 보고서와 발표에서는 “정책 충돌 및 개선 제안”으로 설명하는 것이 안전하다. 시간이 충분하면 옵션 C를 2차 구현 대상으로 검토한다.

### 4.4 관련 테스트 주의사항

현재 다음 테스트들은 `NO_SUGGESTIONS`와 `AUTO_CORRECT`/`AUTO_COMPLETE`가 함께 있을 때 `NO_SUGGESTIONS`를 무시하는 동작을 기대한다.

- `ime/app/src/test/java/com/anysoftkeyboard/utils/IMEUtilTest.java`
- `ime/app/src/test/java/com/anysoftkeyboard/ime/AnySoftKeyboardSuggestionsTest.java`

따라서 F1을 실제로 변경하면 구현뿐 아니라 테스트 기대값과 정책 설명도 함께 수정해야 한다.

## 5. F5: hint 기반 민감 필드 탐지

T10 PoC는 일반 텍스트 필드에 `hint="비밀번호"`만 설정한 경우 추천 strip이 표시됨을 확인했다. 하지만 이는 앱이 표준 password inputType을 주지 않은 경우에 대한 방어적 보강 문제이다.

권장 판단:

- 이번 1차 패치 범위에서는 제외한다.
- 시간이 남으면 `hintText`에 `password`, `비밀번호`, `PIN`, `인증번호` 같은 키워드가 있을 때 개인화 학습만 차단하는 보수적 방식부터 검토한다.
- 추천 표시까지 즉시 차단하면 오탐으로 사용성이 떨어질 수 있다.

## 6. B 담당 완료 기준

F2 필수 완료 기준:

1. 숫자 비밀번호/PIN 필드에서 클립보드 strip이 마스킹된다.
2. 텍스트 비밀번호 필드의 기존 마스킹 동작이 유지된다.
3. 일반 텍스트 필드의 클립보드 표시 동작이 불필요하게 깨지지 않는다.
4. 관련 단위 테스트가 추가 또는 수정된다.
5. A 담당 PoC 앱으로 원본/패치 결과 비교가 가능하다.

권장 브랜치:

```bash
git checkout -b feature/security-enhancement
```

권장 검증 명령:

```bash
./gradlew :ime:app:testDebugUnitTest --tests "com.anysoftkeyboard.ime.AnySoftKeyboardClipboardTest"
./gradlew :ime:app:assembleDebug
```

Android 코드 변경 후 포맷:

```bash
./gradlew spotlessApply
```

## 7. A 담당 재검증 방법

B 패치 APK를 설치한 뒤 동일 PoC 앱으로 다음을 확인한다.

| ID | 검증 절차 | 패치 성공 기준 |
|---|---|---|
| F2 | T1에서 `Copy F2 Clipboard` 실행 후 T3 이동 | `clipcasepin...` 값이 평문 대신 마스킹됨 |
| F2 회귀 | T2 텍스트 비밀번호 이동 | 기존처럼 마스킹됨 |
| 일반 회귀 | T1 일반 텍스트 이동 | 일반 필드에서는 기존 clipboard strip 동작 유지 |

F1을 구현하지 않는 경우, T5/T6에서 추천 strip이 표시되는 동작은 원본 정책 유지로 기록한다.
