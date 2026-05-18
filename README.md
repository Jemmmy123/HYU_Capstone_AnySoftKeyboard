# AnySoftKeyboard&nbsp;[![Latest release](https://img.shields.io/github/release/AnySoftKeyboard/AnySoftKeyboard.svg)](https://github.com/AnySoftKeyboard/AnySoftKeyboard/releases)

Discussions can be found on&nbsp;[💬&nbsp;GitHub](https://github.com/AnySoftKeyboard/AnySoftKeyboard/discussions)<br/>
Follow us on <a rel="me" href="https://hachyderm.io/@anysoftkeyboard">Mastodon</a>
<br/>
`main` latest build&nbsp;[![checks](https://github.com/AnySoftKeyboard/AnySoftKeyboard/actions/workflows/checks.yml/badge.svg)](https://github.com/AnySoftKeyboard/AnySoftKeyboard/actions/workflows/checks.yml)<br/>
`main` coverage&nbsp;[![codecov](https://codecov.io/gh/AnySoftKeyboard/AnySoftKeyboard/branch/main/graph/badge.svg)](https://codecov.io/gh/AnySoftKeyboard/AnySoftKeyboard)<br/>
<br/>
Android (f/w 6.0+, API level 23+) on screen keyboard for multiple languages.

<a href="https://play.google.com/store/apps/details?id=com.menny.android.anysoftkeyboard&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" height="80pt"/></a>&nbsp;&nbsp;<a href="https://f-droid.org/repository/browse/?fdid=com.menny.android.anysoftkeyboard"><img alt="Get it on F-Droid" src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" height="80pt"/></a>

Sign up to beta-channel [here](https://play.google.com/apps/testing/com.menny.android.anysoftkeyboard). Beta versions are pushed via Google Play Store.

## Features

- All kinds of keyboards:
  - Supporting lots of languages via external packages. E.g., English (QWERTY, Dvorak, AZERTY, Colemak, and Workman), Hebrew, Russian, Arabic, Lao, Bulgarian, Swiss, German, Swedish, Spanish, Catalan, Belarusian, Portuguese, Ukrainian and [many more](addons/languages/PACKS.md).
  - Special keyboard for text fields which require only numbers.
  - Special keyboard for text fields which require email or URI addresses.
- Physical keyboard is supported as-well.
- Auto-capitalization.
- Word suggestions, and Next-Word suggestions.
  - Automatic correction can be customized, or turned off entirely.
  - External packages include word lists that can be freely mixed. You can use a French layout and get suggestions for German and Russian!
- Gesture typing.
- Dark mode, automatic (based on system) and manual.
- Power saving mode, disables various features to save battery.
- Per-app tint, the keyboard changes color depending on the current app.
- Special key-press effects:
  - Sound on key press (if phone is not muted).
  - Vibrate on key press.
- Voice input.
- Incognito Mode - will not learn new words, will not keep history of what was typed (including emoji history).
- Plenty of emojis - long-press the smiley key. You customize those by clicking the Settings icon in emojis window.
- More on AnySoftKeyboard can be found [here](https://anysoftkeyboard.github.io/).

## Releases

### from _main_ branch

#### IME - AnySoftKeyboard

- Every commit to _main_ branch will [deploy](.github/workflows/checks.yml) a new release to the _ALPHA_ channel in Google Play-Store. You can subscribe to this release channel by joining the [Google Groups](https://groups.google.com/d/forum/anysoftkeyboard-alpha-testers) group, and opt-in by visiting [this link](https://play.google.com/apps/testing/com.menny.android.anysoftkeyboard).
- Every Wednesday the latest _ALPHA_ will be [promoted](.github/workflows/deployment_promote.yml) to _BETA_. You can opt-in to this channel by visiting [this link](https://play.google.com/apps/testing/com.menny.android.anysoftkeyboard).
- Once all requirements for a release were finished, a _STABLE_ release branch (in the format of `release-branch-ime-vX.X-rX`) will be cut. Every commit to this branch will be automatically published to Google Play Store (_STABLE_ channel) and will roll-out users gradually.

#### AddOns

- Every commit to _main_ branch will [deploy](.github/workflows/checks.yml) all addons to closed-testing _ALPHA_ channel in Google Play-Store. If you want to subscribe to this channel, open a [ticket](https://github.com/AnySoftKeyboard/AnySoftKeyboard/discussions/3391) and asking to join.
- Every Wednesday the latest _ALPHA_ will be promoted to _BETA_. This is only for supported packs. The _BETA_ channel is an open-testing group, you can subscribe to this release channel by joining the _BETA_ channel for each [addon](addons/languages/PACKS.md).
- Once all requirements for a release were finished, a _STABLE_ release branch (in the format of `release-branch-addons-vX.X-rX`) will be cut. Every commit to this branch will be automatically published to Google Play Store (_STABLE_ channel) and will roll-out users gradually.

### from _release-branch_

#### IME - AnySoftKeyboard

- Each new commit to the release-branch will be published to 10% of the users.
- Each day - if no new commit was pushed to the release-branch - we will increase the roll-out.
- When roll-out reaches 100%, an fdroid release will be made.

#### AddOns

- Each new commit to the release-branch will be published to 10% of the users.
- Each day - if no new commit was pushed to the release-branch - we will increase the roll-out.

## Read more

- Our fancy [web-site](https://anysoftkeyboard.github.io/)
- [Language-Pack](addons/languages/PACKS.md) add-ons in this repo.
- [Theme](addons/themes/PACKS.md) add-ons in this repo.
- [Quick-Text](addons/quicktexts/PACKS.md) add-ons in this repo.
- [Crowdin](https://crowdin.com/project/anysoftkeyboard) to translate the app to your language. [![Crowdin](https://badges.crowdin.net/anysoftkeyboard/localized.svg)](https://crowdin.com/project/anysoftkeyboard)

# Development/Contributing

Want to develop a new feature, fix a bug, or add new language-pack? Read more [here](CONTRIBUTING.md).
Contributors should adhere to the [Code of Conduct](CODE_OF_CONDUCT.md) document.

## Copyright requirement

_Remember:_ the components in this repository are released under the Apache2 license. By contributing to this repository you give all copyright and distribution rights to the [AnySoftKeyboard maintainer](https://github.com/menny).

# License

    Copyright 2009 Menny Even-Danan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    # 졸업 프로젝트 계획
 
> **주제:** 키보드 앱의 자동 추천 기능 보안성 강화
> **베이스:** AnySoftKeyboard (오픈소스 Android IME, Apache 2.0)
> **팀:** 2명 (A / B)
> **기간:** 약 10주
 
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
 
### A: 분석 & 공격 담당 (Red Team)
- 현재 키보드가 막지 못하는 케이스 발굴
- 결과물: 취약점 진단 보고서 + PoC 데모 앱
### B: 방어 & 구현 담당 (Blue Team)
- 발견된 문제를 키보드 코드 수정으로 차단
- 결과물: 패치된 AnySoftKeyboard + 강화 기능
### 역할 분담의 장점
- 졸프 스토리가 자연스러움 (문제 발견 → 해결)
- 서로 다른 전문 영역 형성 → 발표 시 Q&A 분담 가능
- 한쪽이 막혀도 다른 쪽 진행 가능
- 마지막에 A의 PoC로 B의 패치를 검증 → 자연스러운 평가 파트 도출
---
 
## 3. 주차별 일정
 
### Phase 1: 공동 학습 (1~2주차)
 
둘 다 함께 진행:
- AnySoftKeyboard 빌드 및 실행 환경 세팅
- 코드베이스 전체 구조 파악
- 안드로이드 IME 동작 원리 학습 (`InputMethodService`, `EditorInfo`, `InputType`)
- 자동 추천 관련 핵심 파일 위치 공동 파악
### Phase 2: 본격 분담 (3~7주차)
 
#### A 담당 (분석/공격)
 
| 주차 | 작업 |
|---|---|
| 3주차 | InputType 플래그 종류별 케이스 정리, 각 케이스 코드 동작 추적 |
| 4주차 | 테스트용 Android 앱 제작 (다양한 InputType의 EditText 모음) |
| 5주차 | 실험: 각 필드 입력 → 학습/추천 발생 여부 측정, 표로 정리 |
| 6주차 | 추가 시나리오: 연락처 기반 추천 누출, 클립보드 추천 등 |
| 7주차 | 취약점 보고서 작성 + PoC 데모 영상 |
 
#### B 담당 (방어/구현)
 
| 주차 | 작업 |
|---|---|
| 3주차 | 추천/학습 핵심 코드 분석 (`AnySoftKeyboard.java`, 사전 관련 클래스) |
| 4주차 | InputType 검사 로직 강화 — 누락 케이스 차단 |
| 5주차 | 휴리스틱 탐지 추가 — hint/label 키워드 검사 ("password", "비밀번호" 등) |
| 6주차 | 학습된 사전에서 민감 패턴 자동 제거 (긴 영숫자 혼합, 카드번호 형식 등) |
| 7주차 | 사용자 알림 UI — "이 필드는 민감 정보로 판단되어 학습하지 않습니다" |
 
### Phase 3: 통합 & 마무리 (8~10주차)
 
**8주차 (공동 - 가장 중요)**
- A의 PoC 앱으로 B의 패치 검증
- A: 발견 케이스 중 패치 후 차단된 비율 측정
- B: 새로 발견된 케이스 추가 패치
- 졸프 발표의 핵심 데이터 도출 구간
**9주차**
- A: 최종 보고서 작성 (분석 + 평가 결과)
- B: 최종 보고서 작성 (구현 + 기여 포인트)
- 데모 영상 공동 제작
**10주차**
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
### 공유 문서
취약점 추적용 공유 표 (Notion 또는 Google Docs):
 
| 케이스 # | InputType | A가 발견한 동작 | B의 패치 상태 | 패치 후 검증 |
|---|---|---|---|---|
| | | | | |
 
→ 이 표가 최종 보고서의 핵심 데이터가 됨
 
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
 
## 7. 운영 팁
 
- A가 1~2주 정도 앞서가는 페이스가 자연스러움 (B는 A 결과를 받아야 막을 수 있음)
- 둘 중 Android 코드에 더 자신 있는 사람이 B 추천
- 상대방 영역도 한 번씩 코드 훑어보기 (발표 Q&A 대비)
- 캡처 / 영상 / 로그는 발견 즉시 저장 (보고서 작성 시 큰 도움)
- 매 주차 작업 내역을 주차별 메모로 남겨두면 최종 보고서 작성 시간 단축
---
 
## 8. 현재 진행 상황
 
- [x] AnySoftKeyboard 빌드 환경 세팅 (macOS, Android Studio)
- [x] 에뮬레이터 실행 및 IME 활성화 완료
- [x] 권한 설정 완료 (Contacts 기반 추천 허용)
- [x] Auto suggestion aggression: Maximum
- [ ] 코드베이스 구조 파악
- [ ] InputType 처리 코드 추적 시작
 
