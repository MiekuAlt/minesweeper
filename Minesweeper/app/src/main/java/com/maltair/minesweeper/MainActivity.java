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

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
            }
        });
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

        sortGrid();

        // Updating the grid's view
        CustomGridAdapter gridAdapter = new CustomGridAdapter(MainActivity.this, items);
        gv.setAdapter(gridAdapter);
    }

    // Sorts the values of the grid alphabetically by row
    private void sortGrid() {
        for(int i = 0; i < 9; i++) {
            Arrays.sort(items, i * 9, (i+1) * 9);
        }
    }
}