package com.example.powertrackingapp.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.TechnicalListFragment;
import com.example.powertrackingapp.view.UserListFragment;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment; // Thêm biến fragment
    private List<String> historyList;

    // Constructor để khởi tạo context, fragment và danh sách user
    public HistoryAdapter(Context context, Fragment fragment, List<String> historyList) {
        this.context = context;
        this.fragment = fragment; // Gán biến fragment
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.histoy_item, parent, false);
        }

        TextView history = convertView.findViewById(R.id.historyItem);

        history.setText(historyList.get(position));
        return convertView;
    }
}
