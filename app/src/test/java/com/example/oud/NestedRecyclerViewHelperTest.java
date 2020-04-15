package com.example.oud;

import android.content.Context;

import com.example.oud.user.fragments.home.nestedrecyclerview.NestedRecyclerViewHelper;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.VerticalRecyclerViewAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import static com.google.common.truth.Truth.*;

@RunWith(RobolectricTestRunner.class)
public class NestedRecyclerViewHelperTest {

    private Context context;
    /*private UserActivity userActivity;
    private HomeFragment fragment;
    private FragmentScenario scenario;*/


    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        /*userActivity = Robolectric.setupActivity(UserActivity.class);
        fragment = userActivity.getSupportFragmentManager().findFragmentById();

        fragment = new HomeFragment();*/
    }

    @Test
    public void test_addSection() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);

        NestedRecyclerViewHelper.Section section = dummySection0();

        // when
        helper.addSection(section);

        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();

        assertThat(vAdapter.getIcons().get(1))
                .isEqualTo(Constants.USER_HOME_RECENTLY_PLAYED_ICON);

        assertThat(vAdapter.getTitles().get(1))
                .isEqualTo("Untitled");

        HorizontalRecyclerViewAdapter hAdapter = vAdapter.getInnerItemAdapters().get(1);

        assertThat(hAdapter.getImages().get(0))
                .isEqualTo("+18");

        assertThat(hAdapter.getTitles().get(0))
                .isEqualTo("Confirm your age.");

        assertThat(hAdapter.getSubtitles().get(0))
                .isEqualTo("Are you sure ?");

        assertThat(section.getHelper())
                .isEqualTo(helper);
    }

    @Test
    public void test_addSection_atPosition() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        helper.addSection(dummySection0());
        NestedRecyclerViewHelper.Section section = dummySection1();

        // when
        helper.addSection(1, section);
        
        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();

        assertThat(vAdapter.getIcons().get(2))
                .isEqualTo(Constants.USER_HOME_RECENTLY_PLAYED_ICON);

        assertThat(vAdapter.getTitles().get(2))
                .isEqualTo("Untitled");

        HorizontalRecyclerViewAdapter hAdapter = vAdapter.getInnerItemAdapters().get(2);

        assertThat(hAdapter.getImages().get(0))
                .isEqualTo("+18");

        assertThat(hAdapter.getTitles().get(0))
                .isEqualTo("Confirm your age.");

        assertThat(hAdapter.getSubtitles().get(0))
                .isEqualTo("Are you sure ?");


        assertThat(vAdapter.getIcons().get(1))
                .isEqualTo(Constants.USER_HOME_RECENTLY_PLAYED_ICON);

        assertThat(vAdapter.getTitles().get(1))
                .isEqualTo("حضرتك جى تعزى ولا جى تهرج ؟");

        hAdapter = vAdapter.getInnerItemAdapters().get(1);

        assertThat(hAdapter.getImages().get(0))
                .isEqualTo("لا أنا جى أهرج.");

        assertThat(hAdapter.getTitles().get(0))
                .isEqualTo("م تمسك يبنى نفسك شوية ...");

        assertThat(hAdapter.getSubtitles().get(0))
                .isEqualTo("تعالى امسكها معايا");

        assertThat(section.getHelper())
                .isEqualTo(helper);
    }

    @Test
    public void test_removeSection() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        NestedRecyclerViewHelper.Section section = dummySection0();
        helper.addSection(section);

        // when
        helper.removeSection(0);
        helper.removeSection(0);

        // then
        assertThat(section.getHelper())
                .isNull();

        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();

        assertThat(vAdapter.getIcons())
                .isEmpty();

        assertThat(vAdapter.getTitles())
                .isEmpty();

        assertThat(vAdapter.getInnerItemAdapters())
                .isEmpty();
    }

    @Test
    public void test_section_setters_whenAttachedToHelper() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        NestedRecyclerViewHelper.Section section = dummySection0();
        helper.addSection(section);

        // when
        section.setIcon(Constants.USER_HOME_RECENTLY_CATEGORY_ICON);
        section.setTitle("New");

        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();

        assertThat(vAdapter.getIcons().get(1))
                .isEqualTo(Constants.USER_HOME_RECENTLY_CATEGORY_ICON);

        assertThat(vAdapter.getTitles().get(1))
                .isEqualTo("New");
    }

    @Test
    public void test_Section_addItem_whenSectionIsAttached() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        NestedRecyclerViewHelper.Section section = dummySection0();
        helper.addSection(section);

        // when
        section.addItem(dummyItem1());

        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();
        HorizontalRecyclerViewAdapter hAdapter = vAdapter.getInnerItemAdapters().get(1);

        assertThat(hAdapter.getImages().get(1))
                .isEqualTo("لا أنا جى أهرج.");

        assertThat(hAdapter.getTitles().get(1))
                .isEqualTo("م تمسك يبنى نفسك شوية ...");

        assertThat(hAdapter.getSubtitles().get(1))
                .isEqualTo("تعالى امسكها معايا");
    }

    @Test
    public void test_Section_removeItem_whenSectionIsAttached() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        NestedRecyclerViewHelper.Section section = dummySection0();
        helper.addSection(section);

        // when
        section.removeItem(0);

        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();
        HorizontalRecyclerViewAdapter hAdapter = vAdapter.getInnerItemAdapters().get(0);

        assertThat(hAdapter.getImages())
                .isEmpty();

        assertThat(hAdapter.getTitles())
                .isEmpty();

        assertThat(hAdapter.getSubtitles())
                .isEmpty();
    }

    @Test
    public void test_Item_setters_whenSectionIsAttached() {
        // given
        NestedRecyclerViewHelper helper = new NestedRecyclerViewHelper(new RecyclerView(context), context);
        NestedRecyclerViewHelper.Section section = new NestedRecyclerViewHelper.Section();
        section.setIcon(Constants.USER_HOME_RECENTLY_CATEGORY_ICON);
        section.setTitle("Untitled");
        NestedRecyclerViewHelper.Item item = dummyItem1();
        section.addItem(item);
        helper.addSection(section);

        // when
        item.setImage("Url", false);
        item.setTitle("Title");
        item.setSubtitle("Subtitle");

        // then
        VerticalRecyclerViewAdapter vAdapter = helper.getmVerticalAdapter();
        HorizontalRecyclerViewAdapter hAdapter = vAdapter.getInnerItemAdapters().get(1);

        assertThat(hAdapter.getImages().get(0))
                .isEqualTo("Url");

        assertThat(hAdapter.getTitles().get(0))
                .isEqualTo("Title");

        assertThat(hAdapter.getSubtitles().get(0))
                .isEqualTo("Subtitle");
    }

    private NestedRecyclerViewHelper.Section dummySection0() {
        NestedRecyclerViewHelper.Section section = new NestedRecyclerViewHelper.Section();
        section.setIcon(Constants.USER_HOME_RECENTLY_PLAYED_ICON);
        section.setTitle("Untitled");
        section.addItem(dummyItem0());

        return section;
    }

    private NestedRecyclerViewHelper.Section dummySection1() {
        NestedRecyclerViewHelper.Section section = new NestedRecyclerViewHelper.Section();
        section.setIcon(Constants.USER_HOME_RECENTLY_PLAYED_ICON);
        section.setTitle("حضرتك جى تعزى ولا جى تهرج ؟");
        section.addItem(new NestedRecyclerViewHelper.Item("لا أنا جى أهرج.", false,
                "م تمسك يبنى نفسك شوية ...", "تعالى امسكها معايا"));

        return section;
    }

    private NestedRecyclerViewHelper.Item dummyItem0() {
        NestedRecyclerViewHelper.Item item = new NestedRecyclerViewHelper.Item("+18", false,
                "Confirm your age.", "Are you sure ?");

        return item;
    }

    private NestedRecyclerViewHelper.Item dummyItem1() {
        NestedRecyclerViewHelper.Item item = new NestedRecyclerViewHelper.Item("لا أنا جى أهرج.", false,
                "م تمسك يبنى نفسك شوية ...", "تعالى امسكها معايا");

        return item;
    }
}
