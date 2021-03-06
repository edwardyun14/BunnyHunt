package com.bye.hi.testgame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Random;

import static android.view.View.OnClickListener;


public class bedroom extends ActionBarActivity {
    // hello from Zeke!!
    // goodbye

    private static int totalNum = 4;
    private static int hintNum = 0;
    private boolean levelClear = false;

    public static Button hintButton = null;

    public static ImageButton poster = null;
    public static ImageButton window = null;
    public static ImageButton bed = null;
    public static ImageButton books = null;

    public static ImageButton levelUp = null;


    PopupWindow promptGuess = null;
    PopupWindow lifeWindow = null;
    TextView clueDisplay = null;
    public static TextView hintDisplay = null;
    TextView msgDisplay = null;
    Button submit = null;

    public static int curClueNum = 0;

    public static Boolean[] clueState = {false,false,false,false};

    public static String[] myObj = new String[totalNum];
    public static String[] objArray = {"art","window","bed","book"};


    public static String[] myClue = new String[totalNum];
    public static String[] clueArray = {
            "My sister likes to sing, my brother likes to fart\n" +
                    "Hanging on my wall, my kindergarten _ _ _",

            "Momma went to buy some cookie dough,\n" +
                    "And she’s back, I see her out of my _ _ _ _ _ _",

            "It’s a comfortable place, where I rest my head.\n" +
                    "Where I wake up, and where I go to _ _ _",

            "My favorite thing in my room, take a look,\n" +
                    "The best way to pass time is to read a _ _ _ _",
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);

        poster = (ImageButton) this.findViewById(R.id.poster);
        window = (ImageButton) this.findViewById(R.id.window);
        bed = (ImageButton) this.findViewById(R.id.bed);
        books = (ImageButton) this.findViewById(R.id.books);

        hintButton = (Button) this.findViewById(R.id.hintButtonBed);
        levelUp = (ImageButton) findViewById(R.id.levelUpBed);


        Activity.heart1 = (ImageView) this.findViewById(R.id.heart1bed);
        Activity.heart2 = (ImageView) this.findViewById(R.id.heart2bed);
        Activity.heart3 = (ImageView) this.findViewById(R.id.heart3bed);

        Activity.halfheart1 = (ImageView) this.findViewById(R.id.halfheart1bed);
        Activity.halfheart2 = (ImageView) this.findViewById(R.id.halfheart2bed);
        Activity.halfheart3 = (ImageView) this.findViewById(R.id.halfheart3bed);

        clueDisplay = (TextView) findViewById(R.id.hintTextBed);
        hintDisplay = (TextView) findViewById(R.id.hintDispBed);

        initializeArrays();
        initializeClueAndObject(curClueNum);
        Activity.resetHearts();

        poster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                objClick("art");
            }

        });

        window.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                objClick("window");
            }
        });

        bed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                objClick("bed");
            }
        });

        books.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                objClick("book");
            }
        });

        hintButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                    displayHint();
                }

        });

        levelUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nextLevel();
            }
        });



    }


    public void objClick(String obj) {
        int clueNum=0;

        for (int i=0;i<totalNum;i++) {
            if (myObj[i].equals(obj))
                clueNum = i;
        }

        if (clueState[clueNum]) {
            popupWindow();
        }
        else {
            Activity.loseLife();
            lifePopupWindow();
        }
    }


    private void popupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater)bedroom.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.input_prompt,(ViewGroup)findViewById(R.id.popUpEl));
            promptGuess = new PopupWindow(layout,370, 500,true);
            promptGuess.showAtLocation(layout, Gravity.CENTER,0,0);

            final EditText input = (EditText) layout.findViewById(R.id.userInput);
            submit = (Button) layout.findViewById(R.id.submitButton);
            Button close = (Button) layout.findViewById(R.id.closeButton);
            msgDisplay = (TextView) layout.findViewById(R.id.msgDisp);


            close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    promptGuess.dismiss();
                }
            });

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (verifyAnswer(input.getText().toString())) {
                        InputMethodManager imm =
                                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        promptGuess.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void lifePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) bedroom.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.lose_life, (ViewGroup) findViewById(R.id.loseLifeE));
            lifeWindow = new PopupWindow(layout, 370, 500, true);
            lifeWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


            TextView lifeDisp = (TextView) layout.findViewById(R.id.lifeDisp);
            Button closeLife = (Button) layout.findViewById(R.id.closeLife);
            if (Activity.numLives==0) {
                closeLife.setText("Reset");
                lifeDisp.setText("You're out of lives!");
            }

            closeLife.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Activity.numLives==0)
                        resetLevel();
                    lifeWindow.dismiss();
                }
            });


        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void initializeArrays() {
        int[] usedNum = {24,27,27,27,27};
        for (int i=0;i<totalNum;i++) {
            int r = Activity.randomNumber(totalNum);
            while (hasBeenUsed(r, usedNum)) {
                r = Activity.randomNumber(totalNum);
            }
            usedNum[i] = r;
            myObj[i] = objArray[r];
            myClue[i] = clueArray[r];
        }

    }

    public static boolean hasBeenUsed(int n, int[] usedNum) {
        for (int i=0;i<totalNum;i++) {
            if (usedNum[i]==n)
                return true;
        }
        return false;
    }

    public void nextLevel() {
        startActivity(new Intent(bedroom.this, bathroom.class));
    }

    public void prepareNextLevel() {
        setUnclickable();
        levelUp.setVisibility(View.VISIBLE);
        hintDisplay.setText("");
        clueDisplay.setText("");
        levelClear = true;
    }

    public static void setUnclickable() {
        poster.setClickable(false);
        window.setClickable(false);
        bed.setClickable(false);
        books.setClickable(false);
        hintButton.setVisibility(View.INVISIBLE);
    }

    public static void setClickable() {
        poster.setClickable(true);
        window.setClickable(true);
        bed.setClickable(true);
        books.setClickable(true);
        hintButton.setClickable(true);
    }


    boolean verifyAnswer (String ans) {
        String ansNeat = ans.toLowerCase().trim();

        if (ansNeat.equals(myObj[curClueNum])) {

            submit.setClickable(false);
            msgDisplay.setText("Congratulations!");
            initializeClueAndObject(++curClueNum);
            return true;
        }
        else {
            //loseLife(); // This is too troublesome
            msgDisplay.setText("Sorry, wrong answer. Try again!");
            return false;
        }
    }

    void initializeClueAndObject(int clueNum) {
        if (clueNum>totalNum-1) {
            prepareNextLevel();
        }
        else {

            displayClue(clueNum);
            setObject(clueNum);
        }
    }

    void displayClue(int clueNum){
        clueDisplay.setText(myClue[clueNum]);
        hintDisplay.setText("");

    }

    void setObject(int clueNum) {
        clueState[clueNum]=true;
        if (clueNum>0) {
            clueState[clueNum-1]=false;
        }
    }

    void resetLevel() {
        curClueNum=0;
        resetClueStates();
        initializeArrays();
        initializeClueAndObject(curClueNum);
        Activity.resetHearts();
    }


    void resetClueStates() {
        for (int i=0;i<totalNum;i++) {
            clueState[i]=false;
        }
    }

    void displayHint() {
        String myString = myObj[curClueNum];
        String hint = "";
        for (int i = 0;i<hintNum;i++) {
            hint = hint + myString.charAt(i);
        }
        hintDisplay.setText(hint);
        if (hintNum<myString.length()) {
            hintNum++;
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
