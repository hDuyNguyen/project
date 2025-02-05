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
import com.example.powertrackingapp.model.TechnicalAdapter;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.Dialog.AddTechnicalDialog;

import java.util.ArrayList;
import java.util.List;

public class TechnicalListFragment extends Fragment {

    private List<User> userListData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.technical_list, container, false);

        ListView userList = view.findViewById(R.id.technicalList);
        // Assuming you have a UserAdapter to populate data
        userListData.add(new User("Nguyễn Quang Hùng","quanghung@gmail.com", "Hà Nội", "0974927463"));
        userListData.add(new User("Đồng Văn Tín","vantin@gmail.com", "Huế", "0194837562"));
        userList.setAdapter(new TechnicalAdapter(getActivity(), this, userListData));


        ImageView back = view.findViewById(R.id.back_technical_list);
        back.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        LinearLayout addUser = view.findViewById(R.id.add_technical);
        addUser.setOnClickListener(v -> {
            AddTechnicalDialog dialog = new AddTechnicalDialog();
            dialog.show(getChildFragmentManager(),  "Add Technical");
        });

        return view;
    }

    public void showTechnicalDetailDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_technical_detail, null);
        builder.setView(dialogView);

        // Populate the user details in the dialog
        ((EditText) dialogView.findViewById(R.id.info_technical_username)).setText(user.getFullName());
        ((EditText) dialogView.findViewById(R.id.info_technical_email)).setText(user.getEmail());
        ((EditText) dialogView.findViewById(R.id.info_technical_phone_number)).setText(user.getPhoneNumber());
        ((EditText) dialogView.findViewById(R.id.info_technical_address)).setText(user.getAddress());

        // Tạo và hiển thị AlertDialog mà không thêm nút mặc định
        AlertDialog dialog = builder.create();
        dialog.show();

        Button closeButton = dialogView.findViewById(R.id.technical_dialog_cancel_button);
        closeButton.setOnClickListener(v -> {
            // Xử lý khi nhấn nút Đóng
            dialog.dismiss();
        });

        Button changeButton = dialogView.findViewById(R.id.technical_dialog_ok_button);
        changeButton.setOnClickListener(v -> {

        });
    }

    public void showConfirmDeleteTechnicalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_delete_technical, null);
        builder.setView(dialogView);

        // Tạo và hiển thị AlertDialog mà không thêm nút mặc định
        AlertDialog dialog = builder.create();
        dialog.show();

        Button closeButton = dialogView.findViewById(R.id.dialog_cancel_confirm_technical_button);
        closeButton.setOnClickListener(v -> {
            // Xử lý khi nhấn nút Đóng
            dialog.dismiss();
        });

        Button changeButton = dialogView.findViewById(R.id.dialog_ok_confirm_technical_button);
        changeButton.setOnClickListener(v -> {

        });

    }
}
