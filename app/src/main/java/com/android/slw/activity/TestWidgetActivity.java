package com.android.slw.activity;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.List;

public class TestWidgetActivity extends Activity implements View.OnClickListener{

    Button select;
    List<AppWidgetProviderInfo> list;
    private static final int APPWIDGET_HOST_ID = 0x200;

    private static final int REQUEST_ADD_WIDGET = 1;
    private static final int REQUEST_CREATE_WIDGET = 2;

    AppWidgetHost mAppWidgetHost;
    AppWidgetManager manager;
    MyWidgetHost myWidgetHostView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews(){
        mAppWidgetHost = new AppWidgetHost(getApplication(), APPWIDGET_HOST_ID);
        manager = AppWidgetManager.getInstance(getBaseContext());
        myWidgetHostView = new MyWidgetHost(this);

        myWidgetHostView.setOnClickListener(this);
        setContentView(myWidgetHostView);
        mAppWidgetHost.startListening();

    }

    private void bindListener(){
        select.setOnClickListener(this);
    }


    @Override
    public void onClick(View view){
        invokeSelect();
    }

    private void invokeSelect(){
        int widgetId = mAppWidgetHost.allocateAppWidgetId();

        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        startActivityForResult(pickIntent, REQUEST_ADD_WIDGET);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_ADD_WIDGET:
                    addWidget(data);
                    break;
                case REQUEST_CREATE_WIDGET:
                    createWidget(data);
                    break;
                default:
                    break;
            }

        } else if (requestCode == REQUEST_CREATE_WIDGET
                && resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void createWidget(Intent data) {
        // 获取选择的widget的id
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        // 获取所选的Widget的AppWidgetProviderInfo信息
        AppWidgetProviderInfo appWidget = manager.getAppWidgetInfo(appWidgetId);

        // 根据AppWidgetProviderInfo信息，创建HostView

        View hostView = mAppWidgetHost.createView(this, appWidgetId, appWidget);
        // View view = hostView.findViewById(appWidget.autoAdvanceViewId);
        // ((Advanceable)view).fyiWillBeAdvancedByHostKThx();
        // 将HostView添加到桌面
        hostView.measure(0,0);
        printLog(hostView);
        System.out.println("appWidget.minWidth: "+appWidget.minWidth+"   appWidget.minHeight: "+appWidget.minHeight);
        myWidgetHostView.addInScreen(hostView, appWidget.minWidth + 100,
                appWidget.minHeight + 200);

        myWidgetHostView.requestLayout();

        System.out.println("hostView.width: "+hostView.getWidth() +"  hostView.height: "+hostView.getHeight());
    }

    // 添加选择的widget。需要判断其是否含有配置，如果有，需要首先进入配置

    private void addWidget(Intent data) {
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                -1);
        AppWidgetProviderInfo appWidget = manager.getAppWidgetInfo(appWidgetId);

        Log.d("AppWidget", "configure:" + appWidget.configure);

        if (appWidget.configure != null) {
            // 有配置，弹出配置
            Intent intent = new Intent(
                    AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidget.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            startActivityForResult(intent, REQUEST_CREATE_WIDGET);

        } else {
            // 没有配置，直接添加
            onActivityResult(REQUEST_CREATE_WIDGET, RESULT_OK, data);
        }

    }

    private void printLog(View hostView){
        int density = getResources().getDisplayMetrics().densityDpi;
        hostView.measure(0,0);
        if(hostView instanceof ViewGroup){
            ViewGroup group = (ViewGroup)hostView;
            for(int i=0;i<group.getChildCount();i++){
                View childView = group.getChildAt(i);
                if(childView instanceof ViewGroup){
                    printLog(childView);
                }else{
                    System.out.println("childWidth: "+childView.getMeasuredWidth()+"  childHeight: "+childView.getMeasuredHeight()+"  density:"+density);
                }
            }
        }
    }
}
