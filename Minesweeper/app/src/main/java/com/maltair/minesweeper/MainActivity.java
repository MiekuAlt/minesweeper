package com.maltair.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final int NUM_MINES = 80;
    public boolean gameStarted = false;
    public GridView gv;
    public String[] items = new String[81];

    // The cell that is selected by the user, -1 means no cell
    public int selectedCell = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gv = (GridView) this.findViewById(R.id.myGrid);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO: For testing...
                String debugMsg = "";
                ArrayList buds = findBuddies(position);
                for(int i = 0; i < buds.size(); i++){
                    debugMsg += " " + buds.get(i);
                }
                debugDisp(debugMsg);

                // Change the colour of the selected cell
                LinearLayout ll = (LinearLayout) view;
                TextView tv = ll.findViewById(R.id.textview);
                tv.setBackgroundColor(Color.parseColor("#FF4081"));

                // Colour the previous cell back to the original colour
                if(selectedCell != -1 && selectedCell != position) {
                    View oldView = gv.getChildAt(selectedCell);
                    ll = (LinearLayout) oldView;
                    TextView backSelectedItem = ll.findViewById(R.id.textview);
                    backSelectedItem.setBackgroundColor(Color.parseColor("#7288FF"));
                }

                selectedCell = position;

                if(!gameStarted) { // This activates on the first press
                    gameStarted = true;
                    spawnMines(position);
                    CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
                    gv.setAdapter(gridAdapter);
                }
            }
        });
    }

    // Runs the algorithm to populate mines in the gameworld everywhere except where the user pressed
    public void spawnMines(int userSelection) {
        // Initializing the array
        for(int i = 0; i < items.length; i++) {
            items[i] = "";
        }

        // Spawning the mines
        int minesLeft = NUM_MINES;
        while(minesLeft > 0) {
            Random rand = new Random();
            int newPos = rand.nextInt(items.length);
            if(newPos != userSelection && !items[newPos].equals("*")) {
                items[newPos] = "*";
                minesLeft--;
            }
        }
    }

    // provides and array of the indexes of the grid's neighbours
    public ArrayList findBuddies(int targetIndex) {
        ArrayList buddies = new ArrayList();

        // Checking which case the target is in (ex: top left corner)
        // Corners
        if(targetIndex == 0){
            buddies.addAll(Arrays.asList(1,9,10));
        } else if(targetIndex == 8){
            buddies.addAll(Arrays.asList(7,16,17));
        } else if(targetIndex == 72){
            buddies.addAll(Arrays.asList(63,64,73));
        } else if(targetIndex == 80){
            buddies.addAll(Arrays.asList(70,71,79));
        } else if(targetIndex < 9) { // top row
            buddies.addAll(Arrays.asList(targetIndex - 1, targetIndex + 1, targetIndex + 8, targetIndex + 9, targetIndex + 10));
        } else if(targetIndex > 71) { // bottom row
            buddies.addAll(Arrays.asList(targetIndex - 1, targetIndex + 1, targetIndex - 8, targetIndex - 9, targetIndex - 10));
        } else if(targetIndex % 9 == 0) { // first column
            buddies.addAll(Arrays.asList(targetIndex + 1, targetIndex + 9, targetIndex + 10, targetIndex - 8, targetIndex - 9));
        } else if(targetIndex % 9 == 8) { // last column
            buddies.addAll(Arrays.asList(targetIndex - 1, targetIndex + 8, targetIndex + 9, targetIndex - 9, targetIndex - 10));
        } else { // all the rest
            buddies.addAll(Arrays.asList(targetIndex - 1, targetIndex + 1, targetIndex - 8, targetIndex + 8, targetIndex - 9, targetIndex + 9, targetIndex - 10, targetIndex + 10));
        }

        return buddies;
    }

    // Sets the value of the selected grid based on the value of the button pressed
    public void setGridValue(View v){
        if(selectedCell != -1) {
            Button curBut = (Button) findViewById(v.getId());
            String butVal = curBut.getText().toString(); // This is the button's value

            items[selectedCell] = butVal;
            CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
            gv.setAdapter(gridAdapter);

            selectedCell = -1;
        }
    }

    // Randomizes the values of the grid that have not been entered yet
    public void randomizeGrid(View v) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] == null) {
                String possibleLetters[] = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
                Random rand = new Random();
                int randomNum = rand.nextInt(9);
                String randLetter = possibleLetters[randomNum];
                items[i] = randLetter;
            }
        }



        // Updating the grid's view
        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);
    }


    // This is just for debugging
    // TODO: Remove this!
    public void debugDisp(String msg) {
        TextView debugView = (TextView) findViewById(R.id.debugView);
        debugView.setText(msg);
    }

} // end of MainActivity class