package io.gitlab.asyndicate.uhai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import io.gitlab.asyndicate.uhai.api.ChatService;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private DrawerArrowDrawable drawerArrowDrawable;
    private SlidingMenu slidingMenu;
    FloatingActionButton fab;
    private int currentPage = 0;
    BroadcastReceiver receiver;
    Connect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(resources.getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);
        drawerArrowDrawable.setSpinEnabled(true);

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setMenu(R.layout.menu);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setOnPageChangeListener(new CustomViewAbove.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float offset = (Math.abs(positionOffset));
                offset += (offset / 0.8175f) * (1 - 0.8175f);
                drawerArrowDrawable.setProgress(offset);
            }

            @Override
            public void onPageSelected(int position) {
//                drawerArrowDrawable.setProgress(0f);
            }
        });
        initActivity();
        initMenu(slidingMenu.getMenu());
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                slidingMenu.toggle(true);
                break;
        }
        return true;
    }

    private void initMenu(final View menu) {

        final TextView name = (TextView) menu.findViewById(R.id.name);
        name.setText(Settings.getString("name", "Guest"));

        final ImageButton img = (ImageButton) menu.findViewById(R.id.edit_name);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingMenu.toggle();

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Enter display name");
                builder.setNegativeButton("Dismiss", null);
                View view3 = getLayoutInflater().inflate(R.layout.input_text, null);
                final TextView text = (TextView) view3.findViewById(R.id.input_text);
                text.setHint("Name");
                builder.setView(view3);
                builder.setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name2 = text.getText().toString().trim();
                        Settings.getInstance().putString("name", name2);
                        name.setText(Settings.getString("name", "Guest"));
                        Intent intent = new Intent(Home.this, ChatService.class);
                        intent.setAction("nick");
                        startService(intent);
                    }
                });
                builder.create().show();
            }
        });
        MenuAdapter menuAdapter = new MenuAdapter();
        RecyclerView menuList = (RecyclerView) findViewById(R.id.menuList);
        menuList.setAdapter(menuAdapter);

        menuList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        MenuItem menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_message);
        menuItem.setPrimaryText("Connect");
        menuItem.setAction(new PayloadRunnable() {
                               @Override
                               public Object run(Object result) {
                                   getSupportFragmentManager().beginTransaction().replace(R.id.container, connect).commit();
                                   setTitle("Connect");
                                   slidingMenu.toggle(true);
                                   currentPage = Home.Pages.CONNECT;
                                   supportInvalidateOptionsMenu();
                                   return true;
                               }
                           }

        );

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_diet);
        menuItem.setPrimaryText("Dietary");
        menuItem.setAction(new PayloadRunnable() {
                               @Override
                               public Object run(Object result) {
                                   Connect connect = new Connect();
                                   connect.setHomeActivity(Home.this);
                                   getSupportFragmentManager().beginTransaction().replace(R.id.container, connect).commit();
                                   setTitle("Dietary");
                                   slidingMenu.toggle(true);
                                   currentPage = Home.Pages.CONNECT;
                                   supportInvalidateOptionsMenu();
                                   return true;
                               }
                           }
        );

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);
        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_medication);
        menuItem.setPrimaryText("Medication");
        menuItem.setAction(new PayloadRunnable() {
                               @Override
                               public Object run(Object result) {
                                   Connect connect = new Connect();
                                   connect.setHomeActivity(Home.this);
                                   getSupportFragmentManager().beginTransaction().replace(R.id.container, connect).commit();
                                   setTitle("Dietary");
                                   slidingMenu.toggle(true);
                                   currentPage = Home.Pages.CONNECT;
                                   supportInvalidateOptionsMenu();
                                   return true;
                               }
                           }

        );

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);
    }

    private void initActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        connect = new Connect();
        connect.setHomeActivity(Home.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, connect).commit();
        setTitle("Connect");
        currentPage = Home.Pages.CONNECT;
        supportInvalidateOptionsMenu();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                connect.update(intent.getStringExtra("message"));
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("message");
        registerReceiver(receiver, filter);
    }

    public interface Pages {
        int HOME = 0;
        int CONNECT = 1;
        int DIET = 2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
