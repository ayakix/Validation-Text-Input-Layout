バリデーション機能付きのTextInputLayoutの実装方法

## 概要
AndroidのTextInputLayoutを拡張し、バリデーション機能付きのValidationTextInputLayoutの実装方法について説明します。
ValidationTextInputLayoutではカスタム属性を追加することで、必須、バリデーション項目、エラーメッセージをxml上から指定できます。

## 動作例
![animation](https://github.com/ayakix/Validation-Text-Input-Layout/raw/master/images/animation.gif)

## 目指すべき形
下記は、Emailアドレスの入力欄を必須にし、入力内容がEmail形式か否かをチェックします。
Email内容でない場合には、error_textで指定した文字列を表示します。

```java
<ValidationTextInputLayout
    app:required="true"
    app:validation_type="email"
    app:error_text="Email value is invalidated"
    >
    <EditText
        android:hint="Email (Required)"
        />
</ValidationTextInputLayout>
```

## 仕組み
### カスタム属性の定義
res/values/attrs.xmlにカスタム属性を定義します。
後で定義するValidationTextInputLayoutでは、属性を受け取り挙動を変更します。

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="ValidationTextInputLayout">
        <attr name="required" format="boolean" />
        <attr name="validation_type">
            <enum name="post_code" value="0" />
            <enum name="email" value="1" />
        </attr>
        <attr name="error_text" format="string" />
    </declare-styleable>
</resources>
```

### バリデーションタイプの定義
ValidationTypeを列挙型として定義します。
この処理は必須ではないですが、ソースコードの見通しが向上します。
enumの値とattrs.xmlで定義した値は一致させる必要があります。

```java
public enum ValidationType {
    PostCode(0), Email(1),
    Null(9999);
    private int value;

    ValidationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ValidationType valueOf(final int value) {
        ValidationType type = Null;
        for (ValidationType validationType : ValidationType.values()) {
            if (validationType.getValue() == value) {
                type = validationType;
                break;
            }
        }
        return type;
    }
}
```

### TextInputLayoutの拡張
initAttrs()で指定された属性値を取得します。
updateError()では、バリデーションを行い、必要に応じてエラーメッセージを出力します。
外部からは、isValidated()を呼び出すことで、バリデーションが通ったかを判断できます。

```java
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
```

## サンプル
[Validation-Text-Input-Layout@github](https://github.com/ayakix/Validation-Text-Input-Layout)に動作するプロジェクトがあります。

## iOS版
同じような処理をiOSでも実装しています。
[Text-Input-Layout@github](https://github.com/ayakix/Text-Input-Layout) を御覧ください。
