package com.ayakix.validationtextinputlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ayakix.validationtextinputlayout.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainViewInterface {
    private ActivityMainBinding mBinding;
    private ValidationTextInputLayout[] mInputLayouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setViewImpl(this);

        mInputLayouts = new ValidationTextInputLayout[] {
                mBinding.layoutName, mBinding.layoutPostCode, mBinding.layoutEmail
        };
    }

    @Override
    public void onValidationButtonClick() {
        boolean isValidated = true;
        for (final ValidationTextInputLayout inputLayout : mInputLayouts) {
            isValidated &= inputLayout.isValidated();
        }
        mBinding.textResult.setText(isValidated ? "Validated :)" : "Invalidated :(");
    }
}
