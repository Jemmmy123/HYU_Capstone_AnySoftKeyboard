package dev.jemmmy.sensitiveinputpoc;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
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
  private static final int COLOR_TEXT_PRIMARY = 0xFF2B2B2B;
  private static final int COLOR_TEXT_SECONDARY = 0xFF555555;
  private static final int COLOR_SURFACE = 0xFFF6F7F8;
  private static final int COLOR_VULNERABLE = 0xFFB3261E;
  private static final int COLOR_OBSERVED = 0xFF0B6E4F;
  private static final int COLOR_NEUTRAL = 0xFF5F6368;

  private final List<EditText> mInputs = new ArrayList<>();
  private int mClipboardTestCounter = 123456;
  private TextView mLastClipboardValueView;
  private LinearLayout mOptionalCases;
  private Button mToggleOptionalCases;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ScrollView scrollView = new ScrollView(this);
    final LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(dp(16), dp(16), dp(16), dp(24));
    scrollView.addView(root);

    root.addView(title("ASK Sensitive Input PoC"));
    root.addView(
        body(
            "AnySoftKeyboard의 민감 입력 필드 추천 strip과 클립보드 표시 정책을 재현하는 테스트 화면입니다."));

    root.addView(sectionHeader("Verified Findings"));
    root.addView(
        summaryRow(
            "F1",
            "NO_SUGGESTIONS conflict",
            "AUTO_CORRECT/AUTO_COMPLETE에서 추천 strip 표시",
            "VULNERABLE",
            COLOR_VULNERABLE));
    root.addView(
        summaryRow(
            "F2",
            "PIN clipboard masking",
            "숫자 비밀번호에서 비보안 출처 클립보드 평문 표시",
            "VULNERABLE",
            COLOR_VULNERABLE));
    root.addView(
        summaryRow(
            "F3-F5",
            "Defensive detection gaps",
            "웹/이메일/학습금지/hint 필드에서 추천 strip 유지",
            "OBSERVED",
            COLOR_OBSERVED));
    root.addView(
        summaryRow(
            "F6",
            "TYPE_NULL edge case",
            "이번 PoC에서는 후보 추천 strip 미재현",
            "OPTIONAL",
            COLOR_NEUTRAL));

    final LinearLayout actions = new LinearLayout(this);
    actions.setOrientation(LinearLayout.HORIZONTAL);
    actions.setGravity(Gravity.CENTER_VERTICAL);
    actions.setPadding(0, dp(14), 0, dp(10));
    root.addView(actions);

    final Button copyClipboard = new Button(this);
    copyClipboard.setText("Copy F2 Clipboard");
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

    root.addView(sectionHeader("Main Test Fields"));
    root.addView(
        testInput(
            "T1 Baseline normal text",
            "leakcasealpha",
            "Control field. Suggestions should be active.",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T2 Text password",
            "leakcasepassword",
            "Expected protection: no suggestions, masked clipboard strip.",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T3 Number password/PIN",
            "123456",
            "F2 target. Clipboard strip should be masked but was observed in plaintext.",
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T4 NO_SUGGESTIONS only",
            "leakcasenosuggest",
            "Control field. Suggestions should be disabled.",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T5 NO_SUGGESTIONS + AUTO_CORRECT",
            "leakcaseautocorrect",
            "F1 target. Suggestion strip was observed despite NO_SUGGESTIONS.",
            InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T6 NO_SUGGESTIONS + AUTO_COMPLETE",
            "leakcaseautocomplete",
            "F1 extension. Suggestion strip was observed despite NO_SUGGESTIONS.",
            InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T7 WEB_EDIT_TEXT",
            "leakcasewebedit",
            "F3 target. Web text fields keep the suggestion strip active.",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T8 EMAIL_ADDRESS",
            "leakcaseemail@example.com",
            "F3 target. Email layout is used, but candidate strip remains active.",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(
        testInput(
            "T9 NO_PERSONALIZED_LEARNING",
            "leakcaseprivate",
            "F4 target. Learning is disabled, but suggestion strip remains active.",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE | EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING,
            null));
    root.addView(
        testInput(
            "T10 normal text + hint=비밀번호",
            "leakcasehintpass",
            "F5 target. Hint text alone does not trigger sensitive-field protection.",
            InputType.TYPE_CLASS_TEXT,
            EditorInfo.IME_ACTION_NONE,
            "비밀번호"));

    mToggleOptionalCases = new Button(this);
    mToggleOptionalCases.setText("Show Optional Edge Case");
    mToggleOptionalCases.setOnClickListener(view -> toggleOptionalCases());
    root.addView(mToggleOptionalCases, new LinearLayout.LayoutParams(-1, dp(48)));

    mOptionalCases = new LinearLayout(this);
    mOptionalCases.setOrientation(LinearLayout.VERTICAL);
    mOptionalCases.setVisibility(View.GONE);
    mOptionalCases.addView(sectionHeader("Optional Edge Case"));
    mOptionalCases.addView(
        testInput(
            "T11 TYPE_NULL",
            "leakcasenulltype",
            "F6 check. Keyboard appears, but candidate suggestions were not reproduced.",
            InputType.TYPE_NULL,
            EditorInfo.IME_ACTION_NONE,
            null));
    root.addView(mOptionalCases);

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
      String label,
      String sampleText,
      String observation,
      int inputType,
      int imeOptions,
      String hintOverride) {
    final LinearLayout container = new LinearLayout(this);
    container.setOrientation(LinearLayout.VERTICAL);
    container.setPadding(0, dp(10), 0, dp(12));

    final TextView title = new TextView(this);
    title.setText(label);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setTextSize(15);
    title.setTextColor(COLOR_TEXT_PRIMARY);
    container.addView(title);

    final TextView sample = new TextView(this);
    sample.setText("sample: " + sampleText);
    sample.setTextSize(12);
    sample.setTextColor(COLOR_TEXT_SECONDARY);
    container.addView(sample);

    final TextView note = new TextView(this);
    note.setText(observation);
    note.setTextSize(12);
    note.setTextColor(COLOR_TEXT_SECONDARY);
    note.setPadding(0, dp(2), 0, dp(2));
    container.addView(note);

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

  private LinearLayout summaryRow(
      String id, String title, String detail, String status, int statusColor) {
    final LinearLayout wrapper = new LinearLayout(this);
    wrapper.setOrientation(LinearLayout.VERTICAL);
    wrapper.setPadding(0, 0, 0, dp(6));

    final LinearLayout row = new LinearLayout(this);
    row.setOrientation(LinearLayout.VERTICAL);
    row.setPadding(dp(12), dp(10), dp(12), dp(10));
    row.setBackgroundColor(COLOR_SURFACE);
    wrapper.addView(row);

    final LinearLayout top = new LinearLayout(this);
    top.setOrientation(LinearLayout.HORIZONTAL);
    top.setGravity(Gravity.CENTER_VERTICAL);
    row.addView(top);

    final TextView idView = new TextView(this);
    idView.setText(id);
    idView.setTypeface(Typeface.DEFAULT_BOLD);
    idView.setTextSize(13);
    idView.setTextColor(statusColor);
    top.addView(idView);

    final TextView titleView = new TextView(this);
    titleView.setText("  " + title);
    titleView.setTypeface(Typeface.DEFAULT_BOLD);
    titleView.setTextSize(13);
    titleView.setTextColor(COLOR_TEXT_PRIMARY);
    top.addView(titleView, new LinearLayout.LayoutParams(0, -2, 1f));

    final TextView statusView = new TextView(this);
    statusView.setText(status);
    statusView.setTextSize(11);
    statusView.setTypeface(Typeface.DEFAULT_BOLD);
    statusView.setTextColor(Color.WHITE);
    statusView.setGravity(Gravity.CENTER);
    statusView.setPadding(dp(8), dp(3), dp(8), dp(3));
    statusView.setBackgroundColor(statusColor);
    top.addView(statusView);

    final TextView detailView = new TextView(this);
    detailView.setText(detail);
    detailView.setTextSize(12);
    detailView.setTextColor(COLOR_TEXT_SECONDARY);
    detailView.setPadding(0, dp(4), 0, 0);
    row.addView(detailView);

    return wrapper;
  }

  private TextView title(String text) {
    final TextView title = new TextView(this);
    title.setText(text);
    title.setTextSize(24);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setTextColor(COLOR_TEXT_PRIMARY);
    title.setPadding(0, 0, 0, dp(8));
    return title;
  }

  private TextView sectionHeader(String text) {
    final TextView header = new TextView(this);
    header.setText(text);
    header.setTextSize(17);
    header.setTypeface(Typeface.DEFAULT_BOLD);
    header.setTextColor(COLOR_TEXT_PRIMARY);
    header.setPadding(0, dp(14), 0, dp(6));
    return header;
  }

  private TextView body(String text) {
    final TextView body = new TextView(this);
    body.setText(text);
    body.setTextSize(14);
    body.setTextColor(COLOR_TEXT_SECONDARY);
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

  private void toggleOptionalCases() {
    final boolean shouldShow = mOptionalCases.getVisibility() != View.VISIBLE;
    mOptionalCases.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    mToggleOptionalCases.setText(shouldShow ? "Hide Optional Edge Case" : "Show Optional Edge Case");
  }

  private int dp(int value) {
    return Math.round(value * getResources().getDisplayMetrics().density);
  }
}
