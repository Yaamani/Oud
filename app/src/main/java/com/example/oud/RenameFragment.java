package com.example.oud;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class RenameFragment extends Fragment {

    private String mInitialString;

    private TextView mTextViewParent;

    private EditText mEditText;
    private Button mRenameButton;

    private OnRenamingListener onRenamingListener;

    @Deprecated
    public static RenameFragment newInstance(@NonNull String initialString, TextView parentTextView) {
        RenameFragment instance = new RenameFragment();
        instance.mInitialString = initialString;
        instance.mTextViewParent = parentTextView;

        return instance;
    }

    public static RenameFragment newInstance(@NonNull String initialString, OnRenamingListener onRenamingListener) {
        RenameFragment instance = new RenameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("INITIAL_STRING", initialString);
        instance.mInitialString = initialString;

        instance.onRenamingListener = onRenamingListener;

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rename, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String initialStrnig = bundle.getString("INITIAL_STRING");
            if (initialStrnig != null) {
                mInitialString = initialStrnig;
            }
        }


        mEditText = view.findViewById(R.id.edit_text_rename);
        mEditText.setText(mInitialString);



        mRenameButton = view.findViewById(R.id.btn_rename);
        mRenameButton.setOnClickListener(v -> {

            closeKeyboard();

            if (!mEditText.getText().equals(mInitialString)) {
                if (onRenamingListener != null) {
                    onRenamingListener.onRenamingListener(mEditText.getText().toString());
                } else {
                    mTextViewParent.setText(mEditText.getText());
                }
            }

            hideRenameFragment(getActivity(), R.id.nav_host_fragment);

        });



    }



    @Deprecated
    public static void showRenameFragment(FragmentActivity activity, @IdRes int fragmentContainerId, String initialString, TextView parentTextView) {
        FragmentManager manager = activity.getSupportFragmentManager();
        RenameFragment renameFragment = RenameFragment.newInstance(initialString, parentTextView);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragmentContainerId, renameFragment, Constants.RENAME_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static void showRenameFragment(FragmentActivity activity, @IdRes int fragmentContainerId, String initialString, OnRenamingListener listener) {
        FragmentManager manager = activity.getSupportFragmentManager();
        RenameFragment renameFragment = RenameFragment.newInstance(initialString, listener);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragmentContainerId, renameFragment, Constants.RENAME_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static boolean doesRenameFragmentExist(FragmentActivity activity, @IdRes int containerId) {
        return activity.getSupportFragmentManager().findFragmentById(containerId) instanceof RenameFragment;
    }

    public static void hideRenameFragment(FragmentActivity activity, @IdRes int fragmentContainerId) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(manager.findFragmentById(fragmentContainerId))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface OnRenamingListener {
        void onRenamingListener(String s);
    }
}
