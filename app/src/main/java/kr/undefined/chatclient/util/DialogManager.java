package kr.undefined.chatclient.util;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;


import com.bumptech.glide.Glide;

import kr.undefined.chatclient.R;

public class DialogManager  {
    // TODO: 모든 다이얼로그 생성 함수는 여기에 구현해주세요.
    //  public static 함수로 지정 후, 다른 클래스에서는
    //  `DialogManager.functionName(params)` 과 같은 형태로 해당 함수를 호출하여 사용하시면 됩니다.



    public static void showParticipantsDialog(Context context, String profileImageUrl, String nickname, String statusMessage) {
        // 다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_profile, null);
        builder.setView(dialogView);

        // 사용자 정보 표시
        ImageView imgProfile = dialogView.findViewById(R.id.img_profile);
        TextView tvUserNickname = dialogView.findViewById(R.id.tv_user_nickname);
        TextView tvStatusMsg = dialogView.findViewById(R.id.tv_status_msg);

        // 사용자 프로필 이미지, 닉네임, 상태 메시지 설정
        Glide.with(context)
                .load(profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .centerCrop()
                .into(imgProfile);
        tvUserNickname.setText(nickname);
        tvStatusMsg.setText(statusMessage);

        // 다이얼로그의 버튼 클릭 리스너 설정
        AppCompatButton btnBack = dialogView.findViewById(R.id.btn_back);

        // 다이얼로그 생성
        AlertDialog dialog = builder.create();
        dialog.show();

        // `뒤로가기` 버튼 클릭 시 다이얼로그 닫기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
