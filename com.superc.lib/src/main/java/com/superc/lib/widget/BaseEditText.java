package com.superc.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

import com.superc.lib.R;
import com.superc.lib.util.StringUtils;
import com.superc.lib.util.ToastUtil;

import java.math.BigDecimal;
import java.util.regex.Pattern;


public class BaseEditText extends AppCompatEditText implements
        OnFocusChangeListener {

    private Drawable xD;
    /**
     * 是否重置了EditText的内容
     */
    protected boolean resetText;
    /**
     * 是否显示清除图标
     */
    protected boolean showResetTextEnable = true;
    /**
     * 是否显示  密文密码和明文密码的图标
     */
    protected boolean showPasswordVisibleEnable = false;
    /**
     * 当输入的字符为金钱类的数字时，是否开启每三位加入逗号
     */
    protected boolean addCommaEveryThreeNumber = false;
    /**
     * 是否禁止第一位输入小数点
     */
    protected boolean forbidPointFirstPosition = false;
    /**
     * 是否禁止第一位输入负号
     */
    protected boolean forbidMinusFirstPosition = false;
    /**
     * 是否禁止第一个不为零的数字前面不允许有0的存在
     */
    protected boolean forbidZeroBeforeNotZero = false;
    /**
     * 是否允许输入Emojo表情
     */
    protected boolean allowedInputEmoji = true;
    /**
     * 输入表情前EditText中的文本
     */
    protected String inputBeforeText;
    /**
     * 输入表情后EditText中的文本
     */
    protected String inputAfterText;
    /**
     * 提示信息
     */
    protected String hint;
    /**
     * 显示信息
     */
    protected String text;
    /**
     * 聚焦监听
     */
    protected OnFocusChangeListener f;
    /**
     * 输入监听
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!resetText) {
                // 这里用s.toString()而不直接用s是因为如果用s，
                // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                // inputAfterText也就改变了，那么表情过滤就失败了
                inputBeforeText = s.toString();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
            String t = BaseEditText.this.getText().toString();
            setClearIconVisible(s.length() > 0);
            if (!resetText) {
                allowedInputEmoji(s, start, count, after);
                forbidPointFirstPosition(s, start, count, after);
                forbidMinusFirstPosition(s, start, count, after);
                forbidZeroBeforeNotZero(s, start, count, after);
                addCommaEveryThreeNumber(s, start, count, after);
            } else {
                resetText = false;
            }
        }


        @Override
        public void afterTextChanged(Editable s) {
            if (!resetText) {
                // 这里用s.toString()而不直接用s是因为如果用s，
                // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                // inputAfterText也就改变了，那么表情过滤就失败了
                inputAfterText = s.toString();

            }
        }
    };


    public BaseEditText(Context context) {
        super(context);
        init(context, null);
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseEditText);
        showResetTextEnable = a.getBoolean(R.styleable.BaseEditText_showResetTextEnable, true);
        showPasswordVisibleEnable = a.getBoolean(R.styleable.BaseEditText_showExchangePasswordVisibleEnable, true);
        addCommaEveryThreeNumber = a.getBoolean(R.styleable.BaseEditText_addCommaEveryThreeNumber, false);
        forbidPointFirstPosition = a.getBoolean(R.styleable.BaseEditText_forbidPointFirstPosition, false);
        forbidMinusFirstPosition = a.getBoolean(R.styleable.BaseEditText_forbidMinusFirstPosition, false);
        forbidZeroBeforeNotZero = a.getBoolean(R.styleable.BaseEditText_forbidZeroBeforeNotZero, false);
        allowedInputEmoji = a.getBoolean(R.styleable.BaseEditText_allowedInputEmoji, true);
        a.recycle();
        addResetButton();
        super.setOnFocusChangeListener(this);
        addTextChangedListener(watcher);
    }

    private void addResetButton() {
        if (!showResetTextEnable) return;
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = ContextCompat.getDrawable(getContext(), R.mipmap.ic_edit_clear);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth() - 20, xD.getIntrinsicHeight() - 20);
        setClearIconVisible(false);
    }

    private void addVisiblePasswordButton() {
        if (!showPasswordVisibleEnable) return;
    }

    public void setShowResetTextEnable(boolean showResetTextEnable) {
        this.showResetTextEnable = showResetTextEnable;
        invalidate();
    }

    public void setShowPasswordVisibleEnable(boolean enable) {
        this.showPasswordVisibleEnable = enable;
        invalidate();
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null && showResetTextEnable) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean tappedX = event.getX() > (getWidth()
                        - getPaddingRight() - xD.getIntrinsicWidth());
                if (tappedX) {
                    setText("");
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * 设置清除图标是否显示或隐藏
     *
     * @param visible ture : 显示    false : 隐藏
     */
    protected void setClearIconVisible(boolean visible) {
        if (showResetTextEnable) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }

    /**
     * 是否允许输入的第一个数字为0
     */
    private void forbidPointFirstPosition(CharSequence s, int start, int count, int after) {
        if (!forbidPointFirstPosition) return;
        if (s.toString().equals(".") && after == 1) {
            setText("0.");
            moveCursorLastPosition();
        }
    }

    /**
     * 是否允许输入的第一个数字负号
     */
    private void forbidMinusFirstPosition(CharSequence s, int start, int count, int after) {
        if (!forbidMinusFirstPosition) return;
        if (s.toString().equals("-") && after == 1) {
            setText("");
            ToastUtil.show(getContext(), "不可为负数！");
        }
    }

    /**
     * 是否禁止第一个不为零的数字前面不允许有0的存在
     */
    private void forbidZeroBeforeNotZero(CharSequence s, int start, int count, int after) {
        if (!forbidZeroBeforeNotZero) return;
        if (s.toString().equals("0") && after == 1) {
            setText("");
            ToastUtil.show(getContext(), "不可输入小数！");
        }
    }

    /**
     * 当输入的字符为金钱类的数字时，每三位加入逗号
     */
    private void addCommaEveryThreeNumber(CharSequence s, int start, int count, int after) {
        if (!addCommaEveryThreeNumber) return;
        String text = getText().toString();
        text = text.replaceAll(",", "");
        if (!isNumber(text)) return;
        if (after != count) {
            setText(addComma(text));
            moveCursorLastPosition();
        }
    }

    /**
     * 判断是否允许输入Emojo表情
     */
    private void allowedInputEmoji(CharSequence s, int start, int count, int after) {
        if (!allowedInputEmoji) return;
        if (after >= 2) {//表情符号的字符长度最小为2
            CharSequence input = s.subSequence(start, start + after);
            if (containsEmoji(input.toString())) {
                resetText = true;
                Toast.makeText(getContext(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                //是表情符号就将文本还原为输入表情符号之前的内容
                setText(inputBeforeText);
                CharSequence text = getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        }
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source 原字符串
     * @return 检测结果  true：含有    false：不含有
     */
    public boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return true：是     false：不是
     */
    private boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 移动光标到最后
     */
    private void moveCursorLastPosition() {
        Editable etext = getText();
        Selection.setSelection(etext, etext.length());
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */
    public String addComma(String str) {
        // 将传进数字反转
        if (StringUtils.isEmpty(str)) return null;
        str = str.replaceAll(",", "");
        str = getStringOutE(str);
        String[] a = str.split("\\.");
        String after = "";
        if (a.length > 1) {
            int afterInt = Integer.parseInt(a[1] + "");
            if (afterInt != 0) {
                after = "." + a[1];
            }
            str = a[0];
        }
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将 【789,456,】 中最后一个【,】去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        resultStr += after;
        return resultStr;
    }

    /**
     * 去除科学计数法
     *
     * @param str 需要去除的字符串
     * @return 去除后的字符串
     */
    public String getStringOutE(String str) {
        if (str == null) return "";
        BigDecimal bd = new BigDecimal(str);
        return bd.toPlainString();
    }

    /**
     * 判断字符串是否是数字
     */
    public boolean isNumber(String value) {
        return Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(value).matches();
    }

}

