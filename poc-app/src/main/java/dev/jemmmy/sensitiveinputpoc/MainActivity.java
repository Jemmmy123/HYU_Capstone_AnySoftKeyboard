package dev.jemmmy.sensitiveinputpoc;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private static final String CLIPBOARD_TEST_PREFIX = "clipcasepin";

  private final List<EditText> mInputs = new ArrayList<>();
  private int mClipboardTestCounter = 123456;
  private TextView mLastClipboardValueView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ScrollView scrollView = new ScrollView(this);
    final LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(dp(16), dp(16), dp(16), dp(24));
    scrollView.addView(root);

    root.addView(title("Sensitive Input PoC"));
    root.addView(
        body(
            "AnySoftKeyboard 추천/학습/클립보드 노출을 확인하기 위한 테스트 앱입니다. "
                + "샘플 값은 반드시 키보드로 직접 입력한 뒤 일반 텍스트 필드에서 prefix를 다시 입력해 추천 여부를 확인하세요."));

    final LinearLayout actions = new LinearLayout(this);
    actions.setOrientation(LinearLayout.HORIZONTAL);
    actions.setGravity(Gravity.CENTER_VERTICAL);
    actions.setPadding(0, dp(10), 0, dp(14));
    root.addView(actions);

    final Button copyClipboard = new Button(this);
    copyClipboard.setText("Copy Clipboard Test");
    copyClipboard.setOnClickListener(view -> copyClipboardValue());
    actions.addView(copyClipboard, new LinearLayout.LayoutParams(0, dp(48), 1f));

    final Button pickKeyboard = new Button(this);
    pickKeyboard.setText("Pick IME");
    pickKeyboard.setOnClickListener(view -> showInputMethodPicker());
    final LinearLayout.LayoutParams pickParams = new LinearLayout.LayoutParams(0, dp(48), 1f);
    pickParams.setMargins(dp(8), 0, 0, 0);
    actions.addView(pickKeyboard, pickParams);

    mLastClipboardValueView = body("Last clipboard test value: none");
    root.addView(mLastClipboardValueView);

    root.addView(
        testInput(
            "T1 일반 텍스트 baseline",
            "leakcasealpha",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T2 텍스트 비밀번호",
            "leakcasepassword",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T3 숫자 비밀번호/PIN",
            "123456",
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T4 NO_SUGGESTIONS 단독",
            "leakcasenosuggest",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T5 NO_SUGGESTIONS + AUTO_CORRECT",
            "leakcaseautocorrect",
            InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T6 NO_SUGGESTIONS + AUTO_COMPLETE",
            "leakcaseautocomplete",
            InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T7 WEB_EDIT_TEXT",
            "leakcasewebedit",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T8 EMAIL_ADDRESS",
            "leakcaseemail@example.com",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T9 NO_PERSONALIZED_LEARNING",
            "leakcaseprivate",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE | EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING,
            null));
    root.addView(
        testInput(
            "T10 일반 text + hint=비밀번호",
            "leakcasehintpass",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE,
            "비밀번호"));
    root.addView(
        testInput(
            "T11 TYPE_NULL",
            "leakcasenulltype",
            InputType.TYPE_NULL,
            EditorInfo.IME_ACTION_NONE,
            null));

    final Button clearAll = new Button(this);
    clearAll.setText("Clear All Inputs");
    clearAll.setOnClickListener(
        view -> {
          for (EditText input : mInputs) {
            input.setText("");
          }
        });
    root.addView(clearAll, new LinearLayout.LayoutParams(-1, dp(48)));

    setContentView(scrollView);
  }

  private LinearLayout testInput(
      String label, String sampleText, int inputType, int imeOptions, String hintOverride) {
    final LinearLayout container = new LinearLayout(this);
    container.setOrientation(LinearLayout.VERTICAL);
    container.setPadding(0, dp(8), 0, dp(10));

    final TextView title = new TextView(this);
    title.setText(label);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setTextSize(15);
    container.addView(title);

    final TextView sample = new TextView(this);
    sample.setText("sample: " + sampleText);
    sample.setTextSize(12);
    sample.setTextColor(0xFF555555);
    container.addView(sample);

    final EditText input = new EditText(this);
    input.setSingleLine(true);
    input.setSelectAllOnFocus(false);
    input.setInputType(inputType);
    input.setImeOptions(imeOptions);
    input.setHint(hintOverride == null ? sampleText : hintOverride);
    input.setTextSize(16);
    input.setPadding(dp(10), 0, dp(10), 0);
    if (inputType == InputType.TYPE_NULL) {
      input.setRawInputType(InputType.TYPE_NULL);
      input.setShowSoftInputOnFocus(true);
    }
    container.addView(input, new LinearLayout.LayoutParams(-1, dp(52)));
    mInputs.add(input);

    return container;
  }

  private TextView title(String text) {
    final TextView title = new TextView(this);
    title.setText(text);
    title.setTextSize(24);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setPadding(0, 0, 0, dp(8));
    return title;
  }

  private TextView body(String text) {
    final TextView body = new TextView(this);
    body.setText(text);
    body.setTextSize(14);
    body.setTextColor(0xFF444444);
    body.setPadding(0, 0, 0, dp(8));
    return body;
  }

  private void copyClipboardValue() {
    final String clipboardTestValue = CLIPBOARD_TEST_PREFIX + mClipboardTestCounter++;
    final ClipboardManager clipboardManager =
        (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    clipboardManager.setPrimaryClip(ClipData.newPlainText("Sensitive Input PoC", clipboardTestValue));
    mLastClipboardValueView.setText("Last clipboard test value: " + clipboardTestValue);
    Toast.makeText(this, "Copied test value to clipboard", Toast.LENGTH_SHORT).show();
  }

  private void showInputMethodPicker() {
    final InputMethodManager inputMethodManager =
        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.showInputMethodPicker();
  }

  private int dp(int value) {
    return Math.round(value * getResources().getDisplayMetrics().density);
  }
}
