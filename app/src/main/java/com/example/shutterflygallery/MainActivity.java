package com.example.shutterflygallery;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.ToggleButton;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private int pageNum = 1;
    private GridView grid;
    private Toolbar toolbar;

    private PixabayPhotoRetriever pixabayPhotoRetriever;
    private Adapter adapter;
    private String query;
    private AtomicBoolean isExecuting = new AtomicBoolean(false);
    private Set<Integer> favIndexSet = new HashSet<>();
    //ImageButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        adapter = new Adapter(MainActivity.this);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnScrollListener(new AbsListView.OnScrollListener(){
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {
            //Scroll is affected while we updated the results on page , we dont want that
            if (totalItemCount == 0 || isExecuting.get()){
                return;
            }

            if(firstVisibleItem + visibleItemCount >= totalItemCount){
                executeQuery(query,++pageNum);
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState){

        }
    });

//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            // Get the GridView selected/clicked item text
//            String selectedItem = parent.getItemAtPosition(position).toString();
//
//            // Display the selected/clicked item text and position on TextView
//            Log.d("d", "GridView item clicked : " + selectedItem
//                    + "\nAt index position : " + position);
//
//            lastCellClicked = position ;
//
//            if (favIndexSet.contains(position)){
//                toggleButton.setColorFilter(Color.RED);
//            }
//            else{
//                toggleButton.setColorFilter(Color.GRAY);
//            }
//            }
//        });

        pixabayPhotoRetriever = new PixabayPhotoRetriever(20);
        pixabayPhotoRetriever.setResultListner(new PhotoResultLisetner(){

            @Override
            public void onResult(Bitmap bmp) {
                adapter.addImage(bmp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinished(boolean allRcvd) {
                isExecuting.set(false);
            }
        });

       // toggleButton = findViewById(R.id.imageBtn);
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
////            if (lastCellClicked < 0){
////                return;
////            }
////
////            if (isChecked){
////                favIndexSet.add(lastCellClicked);
////            }
////            else{
////                favIndexSet.remove(lastCellClicked);
////            }
////            }
////        });
//        toggleButton.setOnClickListener(new ImageButton.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if (true){ // TODO: if exists in favList
//                    toggleButton.setColorFilter(Color.RED);
//                }
//                else{
//                    toggleButton.setColorFilter(Color.GRAY);
//                }
//            }
//        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.isEmpty()) {
                    reset();
                    executeQuery(s, 1);
                    return true;
                }
                return false;
                //TODO: ????????????????
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    private void reset() {
        adapter.removeAll();
        adapter.notifyDataSetChanged();
//        lastCellClicked = -1;
        favIndexSet.clear();
        //toggleButton.setColorFilter(Color.GRAY);
    }

    private void executeQuery(String query,int pageNum) {
        if (query == null){
            return;
        }
        this.pageNum = pageNum ;
        this.query = query;
        isExecuting.set(true);
        pixabayPhotoRetriever.search(query, pageNum);
    }
}
