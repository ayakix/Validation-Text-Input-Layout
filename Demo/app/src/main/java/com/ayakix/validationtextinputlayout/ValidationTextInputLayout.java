package com.ayakix.validationtextinputlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by r_ayaki on 2017/03/31.
 */

public class ValidationTextInputLayout extends TextInputLayout {
    private static final String PATTERN_POST_CODE = "\\d{7}";

    private boolean isRequired = false;
    private ValidationType validationType = ValidationType.Null;
    private String errorText;

    public ValidationTextInputLayout(Context context) {
        super(context);
    }

    public ValidationTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ValidationTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(final Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValidationTextInputLayout);

        isRequired = typedArray.getBoolean(R.styleable.ValidationTextInputLayout_required, false);

        final int validationTypeValue = typedArray.getInt(
                R.styleable.ValidationTextInputLayout_validation_type, ValidationType.Null.getValue());
        validationType = ValidationType.valueOf(validationTypeValue);

        String errorText = typedArray.getString(R.styleable.ValidationTextInputLayout_error_text);
        if (TextUtils.isEmpty(errorText)) {
            errorText = getContext().getString(R.string.error_default_text);
        }
        this.errorText = errorText;
    }

    public boolean isValidated() {
        updateError();

        final boolean isValidated = TextUtils.isEmpty(getError());
        setErrorEnabled(!isValidated);
        return isValidated;
    }

    private void updateError() {
        final String text = getEditText().getText().toString();
        final boolean isEmpty = TextUtils.isEmpty(text);
        setError(null);

        switch (validationType) {
            case PostCode:
                if (!isPostCode(text)) {
                    setError(errorText);
                }
                break;
            case Email:
                if (!isEmail(text)) {
                    setError(errorText);
                }
                break;
            default:
                break;
        }

        if (isEmpty) {
            if (isRequired) {
                setError("Fill in this form");
            } else {
                setError(null);
            }
        }
    }

    private boolean isPostCode(final String str) {
        return Pattern.compile(PATTERN_POST_CODE).matcher(str).matches();
    }

    private boolean isEmail(final String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }
}
