package geeks.recyclerviewmovie;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static final String TAG= "Test" ;

    Toolbar toolbar;
    RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    List<Album> albumList;

    RecyclerView.LayoutManager layoutManager;

    CollapsingToolbarLayout collapsingToolbar ;
    AppBarLayout appBarLayout;
    TextView tvHoraMovie ;

    int minMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvHoraMovie = (TextView) findViewById(R.id.tvMovieMin) ;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mi Lista de Canciones");
        setSupportActionBar(toolbar);



        //animacion
        initCollapsingToolbar();

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();


        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //minMovie
        Date date = new Date();
        String time ;
        minMovie = 120;

        formatearMinutosAHoraMinutos(minMovie);
       // System.out.println(formatearMinutosAHoraMinutos(92));
        System.out.println(formaterHoraMinutos(175));
    }

    public String formatearMinutosAHoraMinutos(int minMovie){
        String formato = "%02d:%02d";
        long horaReales = TimeUnit.MINUTES.toHours(minMovie);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minMovie) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minMovie));

        Log.d(TAG ,"Horas: "+horaReales);
        Log.d(TAG, "minutos: "+minutosReales);
        String horaMovie = ""+horaReales+" h " +minutosReales + " min.";
        Log.d(TAG, "hora Pelicula: "+horaMovie);
        tvHoraMovie.setText(horaMovie);

        return String.format(formato, horaReales, minutosReales);
    }

    public String formaterHoraMinutos(int minMovie){
        int hours= minMovie/ 60;
        int minutes = minMovie%60;

        return ((hours == 0)? minutes+"min" : ((minutes != 0) ? hours + "h" + minutes + "min" : hours + "h"));
    }

    private void initCollapsingToolbar(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("List of Musik");

        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = 1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0){
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                }else if (isShow){
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {

        int[] covers = new int[]{
                R.mipmap.album1,
                R.mipmap.album2,
                R.mipmap.album3,
                R.mipmap.album4,
                R.mipmap.album5,
                R.mipmap.album6,
                R.mipmap.album7,
                R.mipmap.album8,
                R.mipmap.album9,
                R.mipmap.album10,
                R.mipmap.album11
        };

        Album album  = new Album("Maroon5",13,covers[0]);
        albumList.add(album);

        album = new Album("Sugar Ray", 8, covers[1]);
        albumList.add(album);

        album = new Album("Bon Jovi", 11, covers[2]);
        albumList.add(album);

        album = new Album("The Corrs", 12, covers[3]);
        albumList.add(album);

        album = new Album("The Cranberries", 14, covers[4]);
        albumList.add(album);

        album = new Album("Westlife", 1, covers[5]);
        albumList.add(album);

        album = new Album("Black Eyed Peas", 11, covers[6]);
        albumList.add(album);

        album = new Album("VivaLaVida", 14, covers[7]);
        albumList.add(album);

        album = new Album("The Cardigans", 11, covers[8]);
        albumList.add(album);

        album = new Album("Pussycat Dolls", 17, covers[9]);
        albumList.add(album);

        adapter.notifyDataSetChanged();

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
