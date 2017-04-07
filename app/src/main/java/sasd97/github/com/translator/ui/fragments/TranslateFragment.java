package sasd97.github.com.translator.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import sasd97.github.com.translator.R;
import sasd97.github.com.translator.http.HttpError;
import sasd97.github.com.translator.http.HttpResultListener;
import sasd97.github.com.translator.models.LanguagesModel;
import sasd97.github.com.translator.ui.base.BaseFragment;

import static sasd97.github.com.translator.http.YandexAPIWrapper.getLangs;

public class TranslateFragment extends BaseFragment
        implements HttpResultListener<LanguagesModel> {

    private String TAG = TranslateFragment.class.getCanonicalName();

    @Override
    protected int getLayout() {
        return R.layout.fragment_translate;
    }

    @Override
    protected boolean isButterKnifeEnabled() {
        return true;
    }

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @OnClick(R.id.button)
    public void onClick(View view) {
        getLangs(this);
    }

    @Override
    public void onHttpSuccess(LanguagesModel result) {
        Log.d(TAG, result.toString());
    }

    @Override
    public void onHttpError(HttpError error) {

    }

    @Override
    public void onHttpCanceled() {

    }
}
