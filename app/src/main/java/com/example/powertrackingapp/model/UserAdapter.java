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
import com.example.powertrackingapp.view.UserListFragment;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment; // Thêm biến fragment
    private List<User> userList;

    // Constructor để khởi tạo context, fragment và danh sách user
    public UserAdapter(Context context, Fragment fragment, List<User> userList) {
        this.context = context;
        this.fragment = fragment; // Gán biến fragment
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getUserId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        }

        TextView userIndex = convertView.findViewById(R.id.userIndex);
        TextView userName = convertView.findViewById(R.id.userName);
        ImageView deleteButton = convertView.findViewById(R.id.deleteButton);

        User user = userList.get(position);

        userIndex.setText(String.valueOf(position + 1));
        userName.setText(user.getFullName());

        // Sự kiện click cho tên người dùng
        userName.setOnClickListener(v -> {
            // Gọi phương thức showUserDetailDialog từ fragment
            if (fragment instanceof UserListFragment) {
                ((UserListFragment) fragment).showUserDetailDialog(user);
            }
        });

        // Sự kiện click cho nút xóa
        deleteButton.setOnClickListener(v -> {
            // Xử lý sự kiện click cho nút xóa
            Log.i("UserAdapter", "Delete button clicked for user: " + user.getFullName());
            // Thực hiện hành động xóa
            if (fragment instanceof UserListFragment) {
                ((UserListFragment) fragment).showConfirmDeleteUserDialog();
            }
        });

        return convertView;
    }
}
