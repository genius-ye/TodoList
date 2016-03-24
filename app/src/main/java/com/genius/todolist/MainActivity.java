package com.genius.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genius.utils.FileUtils;
import com.genius.utils.Logger;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private DragSortListView dragSortListView;
    private DragSortController dragSortController;
    /**
     * 所有的数据集合（包括已完成，未完成，标题等）
     **/
    private List<TaskBean> data;
    /**
     * 未完成的任务集合
     **/
    private List<TaskBean> data1;
    /**
     * 已完成的任务集合
     **/
    private List<TaskBean> data2;

    private ListviewAdapter listviewAdapter;

    /**
     * 按钮——添加
     **/
    private ImageView imageView_add;
    public static final int REQUEST_CODE = 100;
    public static final String REQUEST_STRING = "new_task";

    /**
     * 按钮——确定
     **/
    private Button btn_confirm;
    /**
     * 修改对话框
     **/
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        setActionBar();


        //umeng自动更新
        UmengUpdateAgent.update(this);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        init();

        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Logger.d("add");
            }
        });

        //actionbar左侧的侧滑菜单
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        initDragListview();

        initData();
    }

    private void init() {
        imageView_add = (ImageView) findViewById(R.id.app_bar_add);
        imageView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * 初始化数据，从sharedPreferences中去取数据
     */
    private void initData() {

        String string1 = MyApplication.sharedPreferences.getString("data1", "");
        String string2 = MyApplication.sharedPreferences.getString("data2", "");
        if (string1.length() > 0) {
            data1.clear();
            data1.addAll(JSONArray.parseArray(string1, TaskBean.class));
        }
        if (string2.length() > 0) {
            data2.clear();
            data2.addAll(JSONArray.parseArray(string2, TaskBean.class));
        }
        setData(data1, data2);
    }

    private void initDragListview() {
        dragSortListView = (DragSortListView) findViewById(R.id.content_main_dragsortlistview);

        data = new ArrayList<>();
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();

        listviewAdapter = new ListviewAdapter(this, data, false, new ListviewAdapter.CheckCallBack() {
            @Override
            public void isChecked(int position) {

                if (position > data1.size() + 1) {
                    TaskBean taskBean = data2.get(position - data1.size() - 2);
                    taskBean.setComplete(false);
                    data2.remove(position - data1.size() - 2);
                    data1.add(taskBean);
                } else {
                    TaskBean taskBean = data1.get(position - 1);
                    taskBean.setComplete(true);
                    data2.add(taskBean);
                    //1:表示标题的个数
                    data1.remove(position - 1);
                }

                setData(data1, data2);
            }

            @Override
            public void modify(int position) {
                modifyContent(position);
            }
        });
        setData(data1, data2);
        dragSortListView.setAdapter(listviewAdapter);
        dragSortListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int i, int i1) {
                TaskBean taskBean = data.get(i);

                //表示移动到了标题一上，这是不容许的
                if (i1 == 0) {
                    return;
                }

                if (i > data1.size()) {
                    data2.remove(i - data1.size() - 2);
                } else {
                    data1.remove(i - 1);
                }
                if (i1 > data1.size() + 1) {
                    taskBean.setComplete(true);
                    data2.add(i1 - data1.size() - 2, taskBean);
                } else {
                    taskBean.setComplete(false);
                    data1.add(i1 - 1, taskBean);
                }

                setData(data1, data2);
            }
        });

        dragSortListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int i) {
                if (i > data1.size()) {
                    data2.remove(i - data1.size() - 2);
                } else {
                    data1.remove(i - 1);
                }
                setData(data1, data2);
            }
        });

    }

    private void setActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        View view = LayoutInflater.from(this).inflate(R.layout.view_actionbar_title, null);
//        editText = (EditText) view.findViewById(R.id.content_main_editText);
//        btn_confirm = (Button) view.findViewById(R.id.content_main_button);
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);

    }

    /**
     * 设置总集合
     *
     * @param data1 （未完成的任务）
     * @param data2 （已完成的任务）
     */
    private void setData(List<TaskBean> data1, List<TaskBean> data2) {
        data.clear();
        TaskBean taskBean1 = new TaskBean("正在进行：", TaskBean.TASKBEAN_TYPE_TITLE, true);
        data.add(taskBean1);
        data.addAll(data1);
        TaskBean taskBean2 = new TaskBean("已经完成：", TaskBean.TASKBEAN_TYPE_TITLE, true);
        data.add(taskBean2);
        data.addAll(data2);
        listviewAdapter.notifyDataSetChanged();
    }

    private void modifyContent(final int position) {
        dialog = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_alertdilog, null);
        final EditText editText = (EditText) view.findViewById(R.id.view_alertdilog_editText);
        dialog.setView(view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();

                TaskBean taskBean;
                if (position > data1.size() + 1) {
                    taskBean = data2.get(position - data1.size() - 2);
                } else {
                    taskBean = data1.get(position - 1);
                }
                if (taskBean.getType() != TaskBean.TASKBEAN_TYPE_TITLE) {
                    if (str != null && str.length() > 0) {
                        taskBean.setContent(str);
                        setData(data1, data2);
                        str = null;
                    }
                }

                dialog.cancel();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //隐藏actionbar中的设置
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            //备份
            case R.id.action_backup:
                backup();
                break;
            //恢复
            case R.id.action_recovery:
                recovery();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString(REQUEST_STRING);
                if (null != str && str.length() > 0) {
                    TaskBean taskBean = new TaskBean(str, TaskBean.TASKBEAN_TYPE_TASK, false);
                    data1.add(taskBean);
                    setData(data1, data2);
                } else {
                    Toast.makeText(MainActivity.this, "please enter the content !", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            //添加这个后将不走onDestroy方法
//            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = MyApplication.sharedPreferences.edit();
        editor.putString("data1", JSON.toJSONString(data1));
        editor.putString("data2", JSON.toJSONString(data2));
        editor.commit();
        Logger.w("sharedPreferences commit success……");
        super.onDestroy();
    }

    /**
     * 备份
     */
    public void backup() {
        if(data1.size()==0&&data2.size()==0)
        {
            Toast.makeText(MainActivity.this, "没有数据需要备份", Toast.LENGTH_SHORT).show();
        }
        else
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data1", JSON.toJSONString(data1));
            jsonObject.put("data2", JSON.toJSONString(data2));
            FileUtils.writeFile(this, MyApplication.BACKUP_FILE_PATH, jsonObject.toJSONString());
        }
    }

    /**
     * 恢复
     */
    public void recovery() {
        String content = FileUtils.readFile(this, MyApplication.BACKUP_FILE_PATH);
        JSONObject jsonObject1 = JSONObject.parseObject(content);
        String string1 = jsonObject1.getString("data1");
        String string2 = jsonObject1.getString("data2");
        if (string1.length() > 0) {
            data1.clear();
            data1.addAll(JSONArray.parseArray(string1, TaskBean.class));
        }
        if (string2.length() > 0) {
            data2.clear();
            data2.addAll(JSONArray.parseArray(string2, TaskBean.class));
        }
        setData(data1, data2);
    }
}
