package com.genius.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 112 on 2016/3/10.
 */
public class ListviewAdapter extends BaseAdapter {

    private Context context;
    private List<TaskBean> data;
    private CheckCallBack checkCallBack;
    /**
     * 用来判断是在进行的还是已完成的
     **/
    private boolean isChecked;

    public ListviewAdapter(Context context, List<TaskBean> data, boolean isChecked, CheckCallBack checkCallBack) {
        this.context = context;
        this.data = data;
        this.checkCallBack = checkCallBack;
        this.isChecked = isChecked;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.view_list_checkBox);
            viewHolder.drag = (TextView) convertView.findViewById(R.id.drag_handle);
            viewHolder.remove = (TextView) convertView.findViewById(R.id.remove_handle);
            viewHolder.content = (TextView) convertView.findViewById(R.id.view_list_textview_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskBean taskBean = data.get(position);
        viewHolder.content.setText(taskBean.getContent());

        viewHolder.remove.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams((int)MyApplication.SCREEN_WIDTH/7, ViewGroup.LayoutParams.MATCH_PARENT);
        if(taskBean.getComplete())
        {
            viewHolder.drag.setVisibility(View.GONE);
//            viewHolder.remove.setVisibility(View.GONE);
            marginLayoutParams.setMargins(0,0,(int)MyApplication.SCREEN_WIDTH/16,0);
        }
        else
        {
            marginLayoutParams.setMargins(0,0,(int)MyApplication.SCREEN_WIDTH/7,0);
            viewHolder.drag.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginLayoutParams);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        viewHolder.remove.setLayoutParams(layoutParams);
        if(taskBean.getType()==TaskBean.TASKBEAN_TYPE_TITLE)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.drag.setVisibility(View.GONE);
            viewHolder.remove.setVisibility(View.GONE);
            viewHolder.content.setTextSize(18);
            viewHolder.content.setTextColor(Color.BLACK);
        }
        else
        {
            viewHolder.content.setTextColor(Color.GRAY);
            viewHolder.content.setTextSize(14);
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }
        viewHolder.checkBox.setChecked(taskBean.getComplete());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCallBack.isChecked(position);
            }
        });
        viewHolder.content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkCallBack.modify(position);
                return false;
            }
        });
        return convertView;
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView content;
        TextView remove;
        TextView drag;
    }

    interface CheckCallBack {
        void isChecked(int position);
        void modify(int positon);
    }
}
