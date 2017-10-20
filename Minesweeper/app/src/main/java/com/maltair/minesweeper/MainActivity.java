package com.maltair.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.maltair.minesweeper.R.layout.activity_main;
import static com.maltair.minesweeper.R.layout.cell;

public class MainActivity extends AppCompatActivity {

    public int NUM_MINES = 8;
    public boolean gameStarted = false;
    public GridView gv;
    public String[] items = new String[81];
    public String[] key = new String[81];
    private SeekBar seekBar;
    private int difficulty; // 0 = easy, 1 = medium, 2 = hard
    private TextView difficultyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        difficultyText = (TextView) findViewById(R.id.difficultyView);
        difficulty = 0;
        gv = (GridView) this.findViewById(R.id.myGrid);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!gameStarted) { // This activates on the first press
                    gameStarted = true;
                    spawnMines(position);
                    CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
                    gv.setAdapter(gridAdapter);
                }

                // Reveals the clicked cell
                checkLocation(position);

            }
        });

        // Detecting changes to the seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i == 0) { // Easy
                    difficultyText.setText("Difficulty: Easy");
                    difficulty = 0;
                } else if(i == 1) { // Medium
                    difficultyText.setText("Difficulty: Medium");
                    difficulty = 1;
                } else { // Hard
                    difficultyText.setText("Difficulty: Hard");
                    difficulty = 2;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    // Checks the location selected, and reveals it
    public void checkLocation(int pos) {
        if(key[pos].equals("*")) { // Stepping on a mine
            items[pos] = key[pos];
            // TODO: Losing condition
        } else if(key[pos].equals("0")) { // No danger
            uncoverZeros(pos);
        } else {
            items[pos] = key[pos];
        }

        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);
    }

    // Uncovers the values around the zero recursively
    public void uncoverZeros(int pos) {
        ArrayList neighbours = findBuddies(pos);
        if(key[pos].equals("0")) {
            items[pos] = " ";
        } else {
            items[pos] = key[pos];
        }
        for(int i = 0; i < neighbours.size(); i++) {
            int neighbourPos = Integer.parseInt(neighbours.get(i).toString());
            if(key[neighbourPos].equals("0") && items[neighbourPos] == null) {
                items[neighbourPos] = " ";
                uncoverZeros(neighbourPos);
            } else if(items[neighbourPos] == null) {
                items[neighbourPos] = key[neighbourPos];
            }
        }
    }

    // Runs the algorithm to populate mines in the gameworld everywhere except where the user pressed
    public void spawnMines(int userSelection) {
        // Initializing the array
        for(int i = 0; i < key.length; i++) {
            key[i] = "";
        }

        // Spawning the mines
        int minesLeft = NUM_MINES;
        while(minesLeft > 0) {
            Random rand = new Random();
            int newPos = rand.nextInt(key.length);
            if(newPos != userSelection && !key[newPos].equals("*")) {
                key[newPos] = "*";
                minesLeft--;
            }
        }

        // Filling in the numbers
        detectAllProximities();
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

    // Calculates the number of mines that surround a spot without a mine
    public void detectCloseMines(int position) {
        int numMines = 0;
        ArrayList neighbours = findBuddies(position);
        for(int i = 0; i < neighbours.size(); i++) {
            if(key[Integer.parseInt(neighbours.get(i).toString())].equals("*")){
                numMines++;
            }
        }
        key[position] = "" + numMines;
    }

    // Calculates all of the mine neighbours
    public void detectAllProximities() {
        for(int i = 0; i < key.length; i++) {
            if(!key[i].equals("*")){
                detectCloseMines(i);
            }
        }
    }

    // Restarts the game
    public void restartGame(View v) {

        // Determines the number of mines
        if(difficulty == 0) { // Easy
            NUM_MINES = 8;
        } else if(difficulty == 1) { // Medium
            NUM_MINES = 24;
        } else { // Hard
            NUM_MINES = 40;
        }

        items = new String[81];
        gameStarted = false;
        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);
    }


} // end of MainActivity class