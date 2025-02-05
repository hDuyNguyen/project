package com.example.powertrackingapp.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.model.UserAdapter;
import com.example.powertrackingapp.view.Dialog.AddUserDialog;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private List<User> userListData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_list, container, false);

        ListView userList = view.findViewById(R.id.userList);
        // Assuming you have a UserAdapter to populate data
        userListData.add(new User("Nguyễn Mạnh Hùng","hung@gmail.com", "Hà Nam", "0123453463"));
        userListData.add(new User("Nguyễn Thị A","thia@gmail.com", "Nghệ An", "0123523463"));
        userListData.add(new User("Nguyễn Văn B","vanb@gmail.com", "Hồ Chí Minh", "0123963857"));
        userListData.add(new User("Nguyễn Mạnh Duy","duy@gmail.com", "Hà Nội", "0123456789"));
        userList.setAdapter(new UserAdapter(getActivity(), this, userListData));


        ImageView back = view.findViewById(R.id.back_user_list);
        back.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        LinearLayout addUser = view.findViewById(R.id.add_user);
        addUser.setOnClickListener(v -> {
            AddUserDialog dialog = new AddUserDialog();
            dialog.show(getChildFragmentManager(),  "Add User");
        });

        return view;
    }

    public void showUserDetailDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_user_detail, null);
        builder.setView(dialogView);

        // Populate the user details in the dialog
        ((EditText) dialogView.findViewById(R.id.info_username)).setText(user.getFullName());
        ((EditText) dialogView.findViewById(R.id.info_email)).setText(user.getEmail());
        ((EditText) dialogView.findViewById(R.id.info_phone_number)).setText(user.getPhoneNumber());
        ((EditText) dialogView.findViewById(R.id.info_address)).setText(user.getAddress());

        // Tạo và hiển thị AlertDialog mà không thêm nút mặc định
        AlertDialog dialog = builder.create();
        dialog.show();

        Button closeButton = dialogView.findViewById(R.id.dialog_cancel_button);
        closeButton.setOnClickListener(v -> {
            // Xử lý khi nhấn nút Đóng
            dialog.dismiss();
        });

        Button changeButton = dialogView.findViewById(R.id.dialog_ok_button);
        changeButton.setOnClickListener(v -> {

        });
    }

    public void showConfirmDeleteUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(dialogView);

        // Tạo và hiển thị AlertDialog mà không thêm nút mặc định
        AlertDialog dialog = builder.create();
        dialog.show();

        Button closeButton = dialogView.findViewById(R.id.dialog_cancel_confirm_button);
        closeButton.setOnClickListener(v -> {
            // Xử lý khi nhấn nút Đóng
            dialog.dismiss();
        });

        Button changeButton = dialogView.findViewById(R.id.dialog_ok_confirm_button);
        changeButton.setOnClickListener(v -> {

        });

    }
}
