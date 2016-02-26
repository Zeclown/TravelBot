package com.chartrand.pierreolivier.travelbot;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

public class WelcomeActivity extends Activity {


    CustomWelcomeAnimationListener animationListener;
    CustomWelcomeEditListener editListener;
    Vector<View> welcomePages;
    FrameLayout mainLayout;
    int pageCount=0;
    boolean finished;
    View currentView;
    MainDBHelper dbHelper;
    public static final String MY_PREFS_NAME = "TBOT_Prefs";
    SharedPreferences.Editor editor;
    String newName;
    //used to add listeners on specific views
    EditText newAccountNameEntry1;
    TextView functionsText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        animationListener = new CustomWelcomeAnimationListener();
        editListener=new CustomWelcomeEditListener();
        dbHelper=new MainDBHelper(this);
        functionsText1=(TextView)findViewById(R.id.welcome_functionsText1);
        //adding specific listeners
        newAccountNameEntry1=(EditText)findViewById(R.id.welcome_bubble1Edit);
        newAccountNameEntry1.setOnKeyListener(editListener);
        mainLayout=(FrameLayout)findViewById(R.id.welcome_mainLayout);
        int childcount = mainLayout.getChildCount();
        welcomePages=new Vector<View>();
        for (int i=0; i < childcount; i++){
            welcomePages.add(mainLayout.getChildAt(i));
        }
        currentView=welcomePages.get(0);
        Animation talkAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeintranslate);
        currentView.startAnimation(talkAnimation);
    }
    protected void switchView(View newView)
    {
        if(pageCount==welcomePages.size()-1)
            finished=true;
        Animation talkOutAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeouttranslate);
        talkOutAnimation.setAnimationListener(animationListener);

        View oldView=currentView;
        animationListener.setToFadeOut(oldView);//keep track of the fading out view so we can hide it after the animation ends
        //here we make sure to change the current view first so there is no problem when the endAnimation triggers off and try to access currentView
        currentView=newView;
        oldView.startAnimation(talkOutAnimation);
    }
    public void screenTapped(View view)
    {
        if(finished==true)
            finish();
    }
    private class CustomWelcomeAnimationListener implements Animation.AnimationListener
    {
        private View toFadeOut;
        public void setToFadeOut(View _toFadeOut)
        {
            toFadeOut=_toFadeOut;
        }
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            toFadeOut.setVisibility(View.INVISIBLE);
            currentView.setVisibility(View.VISIBLE);
            Animation talkAnimation= AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fadeintranslate);
            currentView.startAnimation(talkAnimation);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    private class CustomWelcomeEditListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if(v==findViewById(R.id.welcome_bubble1Edit)) {
                    newName=((EditText)v).getText().toString();
                    editor.putString("name", newName);///add the new name to prefs
                    editor.commit();
                    //change all the texts using the name here
                    functionsText1.setText(functionsText1.getText().toString().replaceAll("@name",newName));
                }
                pageCount++;
                switchView(welcomePages.get(pageCount));
                return true;
            }
            return false;
        }

    }
}
