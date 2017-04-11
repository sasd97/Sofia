package sasd97.github.com.translator.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Alexadner Dadukin on 2/27/2017.
 */

public class BaseFragment extends Fragment {

    @LayoutRes
    protected int getLayout() {
        return -1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() == -1) return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(getLayout(), container, false);

        ButterKnife.bind(this, v);

        onViewCreate();
        onViewCreated(savedInstanceState);

        return v;
    }

    protected void onViewCreate() {}

    protected void onViewCreated(Bundle state) {}
}
