package com.example.ibook;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ibook.activities.MainActivity;
import com.example.ibook.activities.PageActivity;
import com.example.ibook.activities.SignUpActivity;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

public class SignUpActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class,true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

    }
    @Test
    public void checkSuccessfulSignUp(){
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.ed_username_signup),"rob99");
        solo.enterText((EditText) solo.getView(R.id.ed_email_signup),"rob@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_phoneNumber_signup),"780123456");
        solo.enterText((EditText) solo.getView(R.id.ed_password_signup),"password");
        solo.enterText((EditText) solo.getView(R.id.ed_confirmPassword_signup),"password");

        solo.clickOnButton("Confirm");
        solo.waitForActivity("Home page",2000);
        solo.assertCurrentActivity("Should go to home page", PageActivity.class);
    }
    @Test
    public void checkImproperPassword(){
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.ed_username_signup),"sdhiman");
        solo.enterText((EditText) solo.getView(R.id.ed_email_signup),"sdhiman@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_phoneNumber_signup),"780123456");
        solo.enterText((EditText) solo.getView(R.id.ed_password_signup),"password");
        solo.enterText((EditText) solo.getView(R.id.ed_confirmPassword_signup),"hello");

        solo.clickOnButton("Confirm");
        solo.waitForActivity("Sign up activity",2000);
        solo.assertCurrentActivity("Should stay in sign up activity", SignUpActivity.class);
    }
    @Test
    public void checkEmptyPhoneNumber(){
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.ed_username_signup),"sdhiman@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_email_signup),"sdhiman@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_password_signup),"password");
        solo.enterText((EditText) solo.getView(R.id.ed_confirmPassword_signup),"password");

        solo.clickOnButton("Confirm");
        solo.waitForActivity("Sign up activity",2000);
        solo.assertCurrentActivity("Should stay in sign up activity", SignUpActivity.class);
    }
    @Test
    public void checkEmptyEmail(){
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.ed_username_signup),"sdhiman@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_phoneNumber_signup),"780123456");
        solo.enterText((EditText) solo.getView(R.id.ed_password_signup),"password");
        solo.enterText((EditText) solo.getView(R.id.ed_confirmPassword_signup),"password");

        solo.clickOnButton("Confirm");
        solo.waitForActivity("Sign up activity",2000);
        solo.assertCurrentActivity("Should stay in sign up activity", SignUpActivity.class);
    }
    @Test
    public void checkEmptyUsername(){
        solo.assertCurrentActivity("Wrong activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.ed_email_signup),"sdhiman@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.ed_phoneNumber_signup),"780123456");
        solo.enterText((EditText) solo.getView(R.id.ed_password_signup),"password");
        solo.enterText((EditText) solo.getView(R.id.ed_confirmPassword_signup),"password");

        solo.clickOnButton("Confirm");
        solo.waitForActivity("Sign up activity",2000);
        solo.assertCurrentActivity("Should stay in sign up activity", SignUpActivity.class);
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }

}
