# Sensitive Input PoC App

This Android app is a graduation-project PoC for testing how AnySoftKeyboard handles sensitive input fields.

## Test Flow

1. Install and enable the original AnySoftKeyboard.
2. Install this PoC app.
3. Open the app and use `Pick IME` to select AnySoftKeyboard.
4. For each test field, manually type the sample value with AnySoftKeyboard.
5. Move back to `T1 일반 텍스트 baseline` and type the sample prefix.
6. Record whether the suggestion strip shows the previously entered value.
7. For clipboard testing, tap `Copy Clipboard Test`, then focus `T2` and `T3` and compare whether the clipboard strip is masked.

Do not insert test strings with `adb`, app-side autofill, or programmatic text filling. Learning tests are valid only when the text is entered through the IME.

## Important Cases

| ID | Case |
|---|---|
| T1 | Normal text baseline |
| T2 | `TYPE_TEXT_VARIATION_PASSWORD` |
| T3 | `TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD` |
| T4 | `TYPE_TEXT_FLAG_NO_SUGGESTIONS` |
| T5 | `NO_SUGGESTIONS | AUTO_CORRECT` |
| T6 | `NO_SUGGESTIONS | AUTO_COMPLETE` |
| T7 | `TYPE_TEXT_VARIATION_WEB_EDIT_TEXT` |
| T8 | `TYPE_TEXT_VARIATION_EMAIL_ADDRESS` |
| T9 | `IME_FLAG_NO_PERSONALIZED_LEARNING` |
| T10 | Normal text field with `hint="비밀번호"` |
| T11 | `TYPE_NULL` |

## Reproduced Results

### F1: `NO_SUGGESTIONS | AUTO_CORRECT`

| Field | Result |
|---|---|
| T1 normal text | Suggestions shown |
| T4 `NO_SUGGESTIONS` | Suggestions not shown |
| T5 `NO_SUGGESTIONS | AUTO_CORRECT` | Suggestions shown |

This confirms that `NO_SUGGESTIONS` works alone but is ignored when `AUTO_CORRECT` is also set.

### F2: Number password clipboard masking

After tapping `Copy Clipboard Test` from a normal-text/non-secure context, immediately compare T1, T2, and T3. AnySoftKeyboard OS clipboard sync must be enabled. The copied value changes on each tap, such as `clipcasepin123456`, `clipcasepin123457`, and so on.

If the clipboard value is copied while T2 text password is focused, AnySoftKeyboard can mark that clipboard entry as secure-originated and mask it in other fields too. For F2, copy the test value while T1 or another non-secure field is active.

| Field | Result |
|---|---|
| T1 normal text | Keyboard clipboard strip shows the latest `clipcasepin...` value |
| T2 text password | Keyboard clipboard strip is masked as `**********` |
| T3 number password/PIN | Keyboard clipboard strip shows the latest `clipcasepin...` value |

The app Toast only confirms that the test value was copied. Do not count the Toast as a keyboard clipboard-strip result.

Build with:

```bash
./gradlew :poc-app:assembleDebug
```
