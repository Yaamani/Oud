package com.example.oud;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment {

    private static final String TAG = OptionsFragment.class.getSimpleName();

    private ArrayList<Integer> mIcons;
    private ArrayList<String> mTitles;
    private ArrayList<Boolean> mSelectedItems;
    private ArrayList<View.OnClickListener> mClickListeners;

    private RecyclerView mRecyclerView;


    public OptionsFragment() {
        // Required empty public constructor
    }

    public static OptionsFragment newInstance(ArrayList<Integer> icons,
                                              ArrayList<String> titles,
                                              ArrayList<Boolean> selectedItems,
                                              ArrayList<View.OnClickListener> clickListeners) {
        OptionsFragment instance = new OptionsFragment();
        instance.mIcons = icons;
        instance.mTitles = titles;
        instance.mSelectedItems = selectedItems;
        instance.mClickListeners = clickListeners;

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //view.setOnClickListener(v -> Log.i(TAG, "Outer View"));


        OptionsRecyclerViewAdapter adapter = new OptionsRecyclerViewAdapter(
                getContext(),
                mIcons,
                mTitles,
                mSelectedItems,
                mClickListeners
        );

        mRecyclerView = view.findViewById(R.id.recycler_view_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        //view.findViewById(R.id.view_options_click_listener).setOnClickListener(v -> Log.i(TAG, "Outer View"));

        //mRecyclerView.setOnTouchListener((v, event) -> view.dispatchTouchEvent(event));


    }

    public static boolean doesOptionsFragmentExist(FragmentActivity activity, @IdRes int containerId) {
        return activity.getSupportFragmentManager().findFragmentById(containerId) instanceof OptionsFragment;
    }

    public static void hideOptionsFragment(FragmentActivity activity, @IdRes int fragmentContainerId) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(manager.findFragmentById(fragmentContainerId))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static Builder builder(FragmentActivity activity) {
        return new Builder(activity);
    }

    public static class Builder {

        private FragmentActivity fragmentActivity;
        public static int DEFAULT_CONTAINER_ID = R.id.container_options;
        private int containerId = DEFAULT_CONTAINER_ID;

        private ArrayList<Integer> icons;
        private ArrayList<String> titles;
        private ArrayList<Boolean> selectedItems;
        private ArrayList<View.OnClickListener> clickListeners;

        public Builder(FragmentActivity fragmentActivity) {
            this.fragmentActivity = fragmentActivity;
        }

        /**
         *
         * @param iconId Can be null if you want to test something.
         * @param title Can be null if you want to test something.
         * @param selected If true, the icon as well as the title will be tinted with the primary color of the app.
         * @param onClickListener Can be null if you want to test something.
         * @return The same builder object.
         */
        public Builder addItem(@Nullable @DrawableRes Integer iconId,
                               @Nullable String title,
                               boolean selected,
                               @Nullable View.OnClickListener onClickListener) {
            if (icons == null) icons = new ArrayList<>();
            if (titles == null) titles = new ArrayList<>();
            if (selectedItems == null) selectedItems = new ArrayList<>();
            if (clickListeners == null) clickListeners = new ArrayList<>();

            if (iconId == null) icons.add(R.drawable.ic_oud);
            else icons.add(iconId);

            if(title == null) titles.add("item" + titles.size());
            else titles.add(title);

            selectedItems.add(selected);

            if (onClickListener == null) {
                String currentTitle = titles.get(titles.size() - 1);
                clickListeners.add(v -> Log.i(TAG, "Item with title : "
                        + currentTitle + " has an empty click listener."));
            } else clickListeners.add(onClickListener);

            return this;
        }

        /**
         *
         * @param iconId Can be null if you want to test something.
         * @param title Can be null if you want to test something.
         * @param onClickListener Can be null if you want to test something.
         * @return The same builder object.
         */
        public Builder addItem(@Nullable @DrawableRes Integer iconId,
                               @Nullable String title,
                               @Nullable View.OnClickListener onClickListener) {
            return addItem(iconId, title, false, onClickListener);
        }

        /**
         *
         * @param containerId The id of the fragment container to be placed inside. By default it's {@link Builder#DEFAULT_CONTAINER_ID}
         * @return
         */
        public Builder inContainer(@IdRes int containerId) {
            this.containerId = containerId;
            return this;
        }

        /**
         * Create a new instance of {@link OptionsFragment} and show it in the specified container.
         */
        public void show() {
            FragmentManager manager = fragmentActivity.getSupportFragmentManager();
            OptionsFragment optionsFragment = newInstance(icons, titles, selectedItems, clickListeners);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(containerId, optionsFragment, Constants.OPTIONS_FRAGMENT_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }


    }
}
