package com.dmsduf.socketio_test.chat;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dmsduf.socketio_test.R;


//채팅방에서 네비게이션 드로어 애니메이션 효과 넣기위한 토글
public class DrawerToggle implements DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    private DrawerArrowDrawable arrowDrawable;
    private AppCompatImageButton toggleButton;
    private String openDrawerContentDesc;
    private String closeDrawerContentDesc;
    private ImageView imageView;

    public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
                        int openDrawerContentDescRes, int closeDrawerContentDescRes) {

        this.drawerLayout = drawerLayout;
        this.openDrawerContentDesc = activity.getString(openDrawerContentDescRes);
        this.closeDrawerContentDesc = activity.getString(closeDrawerContentDescRes);
        //툴바에 메뉴버튼추가
        arrowDrawable = new DrawerArrowDrawable(toolbar.getContext());
        arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END);
        toggleButton = new AppCompatImageButton(toolbar.getContext(), null,
                R.attr.toolbarNavigationButtonStyle);
        toolbar.addView(toggleButton, new Toolbar.LayoutParams(GravityCompat.END));

        imageView = new ImageView(toolbar.getContext(),null);
        imageView.setImageResource(R.drawable.xbuttonchat);
        //툴바에 나가기버튼 세팅
        toolbar.addView(imageView,new Toolbar.LayoutParams(70,70));
        //채팅방 나가기버튼 클릭시
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        //채팅방 메뉴버튼 클릭시
        toggleButton.setImageDrawable(arrowDrawable);
        toggleButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                toggle();
                                            }
                                        }
        );
    }

    public void syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            setPosition(1f);
        }
        else {
            setPosition(0f);
        }
    }

    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            toggleButton.setContentDescription(closeDrawerContentDesc);
        }
        else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            toggleButton.setContentDescription(openDrawerContentDesc);
        }
        arrowDrawable.setProgress(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0, slideOffset)));
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}