package com.example.oud.user.fragments.home.nestedrecyclerview;

import android.content.Context;
import android.view.View;

import com.example.oud.R;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.VerticalRecyclerViewAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.decorations.VerticalSpaceDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NestedRecyclerViewHelper {

    private Context context;

    private RecyclerView recyclerView;
    private ArrayList<Section> sections = new ArrayList<>();

    private VerticalRecyclerViewAdapter mVerticalAdapter;

    public NestedRecyclerViewHelper(Context context) {
        this.context = context;
    }

    public NestedRecyclerViewHelper(@NotNull RecyclerView recyclerView, Context context) {
        this.context = context;
        setRecyclerView(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        initializeVerticalRecyclerView();
    }

    public void addSection(Section section) {
        //sections.add(section);
        addSection(sections.size(), section);
    }

    public void addSection(int position, Section section) {

        if (recyclerView == null)
            throw new IllegalStateException("call setRecyclerView() first.");

        sections.add(position, section);
        section.setHelper(NestedRecyclerViewHelper.this);

        /*if (recyclerView == null)
            return;*/

        mVerticalAdapter.getIcons().add(position, section.icon);
        mVerticalAdapter.getTitles().add(position, section.title);

        ArrayList<Item> items = section.items;
        ArrayList<View.OnClickListener> clickListeners = new ArrayList<>();
        ArrayList<String> imageUls = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> subtitles = new ArrayList<>();
        for (Item item : items) {
            clickListeners.add(item.clickListener);
            imageUls.add(item.imageUrl);
            titles.add(item.title);
            subtitles.add(item.subtitle);
        }
        HorizontalRecyclerViewAdapter hAdapter = new HorizontalRecyclerViewAdapter(context, clickListeners, imageUls, titles, subtitles);
        mVerticalAdapter.getInnerItemAdapters().add(position, hAdapter);

        mVerticalAdapter.notifyDataSetChanged();

    }

    public void removeSection(int position) {
        Section removedSection = sections.remove(position);

        removedSection.setHelper(null);

        mVerticalAdapter.getIcons().remove(position);
        mVerticalAdapter.getTitles().remove(position);
        mVerticalAdapter.getInnerItemAdapters().remove(position);

        mVerticalAdapter.notifyDataSetChanged();
    }

    public int getSectionCount() {
        return sections.size();
    }

    public Section getSection(int position) {
        return sections.get(position);
    }

    public void clearRecyclerView() {
        while (!sections.isEmpty()) {
            while (!sections.get(0).items.isEmpty()) {
                sections.get(0).removeItem(0);
            }
            removeSection(0);
        }
    }

    public void refreshRecyclerView() {
        //if (mVerticalAdapter)
        mVerticalAdapter.notifyDataSetChanged();
    }

    // TODO: This method is used only for testing. When you are not running tests for this class comment it.
    @Deprecated
    public VerticalRecyclerViewAdapter getmVerticalAdapter() {
        return mVerticalAdapter;
    }

    private void initializeVerticalRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceDecoration(context.getResources(), R.dimen.item_margin));

        if (mVerticalAdapter == null)
            mVerticalAdapter = new VerticalRecyclerViewAdapter(context);
        recyclerView.setAdapter(mVerticalAdapter);
    }


    public static class Section {
        private NestedRecyclerViewHelper helper;

        @IdRes
        private int icon;
        private String title;
        private ArrayList<Item> items = new ArrayList<>();

        public NestedRecyclerViewHelper getHelper() {
            return helper;
        }

        private void setHelper(NestedRecyclerViewHelper helper) {
            this.helper = helper;
            for (Item item : items) {
                item.setSection(Section.this);
            }
        }

        public void setIcon(int icon) {
            this.icon = icon;

            if (helper == null) return;

            int index = helper.sections.indexOf(Section.this);
            helper.mVerticalAdapter.getIcons().set(index, icon);

            helper.mVerticalAdapter.notifyDataSetChanged();
        }

        public void setTitle(String title) {
            this.title = title;

            if (helper == null) return;

            int index = helper.sections.indexOf(Section.this);
            helper.mVerticalAdapter.getTitles().set(index, title);

            helper.mVerticalAdapter.notifyDataSetChanged();
        }

        public void addItem(Item item) {
            addItem(items.size(), item);
        }

        public void addItem(int position, Item item) {
            item.setSection(Section.this);
            items.add(position, item);

            if (helper == null) return;

            int index = helper.sections.indexOf(Section.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(index);
            hAdapter.getClickListeners().add(position, item.clickListener);
            hAdapter.getImages().add(position, item.imageUrl);
            hAdapter.getTitles().add(position, item.title);
            hAdapter.getSubtitles().add(position, item.subtitle);

            hAdapter.notifyDataSetChanged();
        }

        public void removeItem(int position) {
            Item removedItem = items.remove(position);
            removedItem.setSection(null);

            if (helper == null) return;

            int index = helper.sections.indexOf(Section.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(index);
            hAdapter.getImages().remove(position);
            hAdapter.getTitles().remove(position);
            hAdapter.getSubtitles().remove(position);

            hAdapter.notifyDataSetChanged();
        }

        public int getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }

        public int getItemCount() {
            return items.size();
        }

        public Item getItem(int position) {
            return items.get(position);
        }
    }

    public static class Item {
        private NestedRecyclerViewHelper helper;
        private Section section;

        private String imageUrl;
        private String title;
        private String subtitle;

        private HashMap<String, Object> relatedInfo = new HashMap<>();

        private View.OnClickListener clickListener;

        public Item() {

        }

        public Item(String imageUrl, String title, String subtitle) {


            setImageUrl(imageUrl);
            setTitle(title);
            setSubtitle(subtitle);
        }

        public Section getSection() {
            return section;
        }

        private void setSection(Section section) {
            this.section = section;
            if (section != null)
                this.helper = section.helper;
            else this.helper = null;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;

            if (helper == null) return;

            int sectionIndex = helper.sections.indexOf(section);
            int itemIndex = section.items.indexOf(Item.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(sectionIndex);
            hAdapter.getImages().set(itemIndex, imageUrl);

            hAdapter.notifyDataSetChanged();
        }

        public void setTitle(String title) {
            this.title = title;

            if (helper == null) return;

            int sectionIndex = helper.sections.indexOf(section);
            int itemIndex = section.items.indexOf(Item.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(sectionIndex);
            hAdapter.getTitles().set(itemIndex, title);

            hAdapter.notifyDataSetChanged();
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;

            if (helper == null) return;

            int sectionIndex = helper.sections.indexOf(section);
            int itemIndex = section.items.indexOf(Item.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(sectionIndex);
            hAdapter.getSubtitles().set(itemIndex, subtitle);

            hAdapter.notifyDataSetChanged();
        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;

            if (helper == null) return;

            int sectionIndex = helper.sections.indexOf(section);
            int itemIndex = section.items.indexOf(Item.this);
            HorizontalRecyclerViewAdapter hAdapter = helper.mVerticalAdapter.getInnerItemAdapters().get(sectionIndex);
            hAdapter.getClickListeners().set(itemIndex, clickListener);

            hAdapter.notifyDataSetChanged();
        }

        public HashMap<String, Object> getRelatedInfo() {
            return relatedInfo;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }
}
