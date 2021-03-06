package com.example.ibook;

import android.app.Notification;
import android.graphics.pdf.PdfDocument;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ibook.activities.AddBookActivity;
import com.example.ibook.activities.MainActivity;
import com.example.ibook.activities.PageActivity;
import com.example.ibook.activities.SearchResultsActivity;
import com.example.ibook.activities.ViewBookActivity;
import com.example.ibook.activities.ViewProfileActivity;
import com.example.ibook.fragment.BookListFragment;
import com.example.ibook.fragment.NotificationsFragment;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditBookActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class,true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "sam@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.clickOnButton("Login In");

    }

    //edit a book and check if UI is updated
    @Test
    public void testEditBook() {

        solo.assertCurrentActivity("Wrong activity", PageActivity.class);
        solo.clickOnView(solo.getView(R.id.searchButton));
        solo.sleep(2000);
        //search for a book
        solo.enterText(0,"queen");
        solo.sendKey(Solo.ENTER);
        solo.waitForActivity("Searched activity",2000);
        solo.assertCurrentActivity("Should be searched results activity", SearchResultsActivity.class);
        //get activity to access its methods
        solo.sleep(1000);
        SearchResultsActivity activity = (SearchResultsActivity) solo.getCurrentActivity();
        RecyclerView listView = activity.getListView();
        //click on first book in list
        solo.clickOnView(listView.getChildAt(0));
        //view book activity
        solo.assertCurrentActivity("Wrong activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.editButton));
        //edit the author
        solo.clearEditText((EditText) solo.getView(R.id.authorEditor));
        solo.enterText((EditText) solo.getView(R.id.authorEditor), "Laura");
        solo.clickOnView(solo.getView(R.id.completeButton));
        solo.assertCurrentActivity("Wrong activity", ViewBookActivity.class);
        // check if book is edited
        assertTrue(solo.searchText("Laura"));
    }


    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }

}
