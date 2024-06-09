package kr.undefined.chatclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import kr.undefined.chatclient.R;
import kr.undefined.chatclient.adapter.RoomListAdapter;
import kr.undefined.chatclient.item.RoomItem;
import kr.undefined.chatclient.manager.SocketManager;
import kr.undefined.chatclient.util.DialogManager;

public class LobbyActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Toolbar toolbar;
    private RecyclerView rvRoomList;
    private TextView tvConcurrentConnectors, tvUserNickname;
    private ImageButton btnUserProfileImg;
    private FrameLayout btnSearchingRoom, btnCreatingRoom;

    private Intent it;

    private RoomListAdapter roomListAdapter;
    private ArrayList<RoomItem> roomList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();

        // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
        user = auth.getCurrentUser();
        if (user == null) {
            it = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            SocketManager.getInstance().setLobbyActivity(this);
        }

        toolbar = findViewById(R.id.toolbar);
        rvRoomList = findViewById(R.id.rv_room_list);
        tvConcurrentConnectors = findViewById(R.id.tv_concurrent_connectors_value);
        tvUserNickname = findViewById(R.id.tv_user_nickname);
        btnUserProfileImg = findViewById(R.id.ib_user_profile);
        btnSearchingRoom = findViewById(R.id.btn_searching_room);
        btnCreatingRoom = findViewById(R.id.btn_creating_room);

        rvRoomList.setLayoutManager(new LinearLayoutManager(this));
        roomList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(roomList);
        rvRoomList.setAdapter(roomListAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        roomListAdapter.setOnItemClickListener(item -> {
            // FIXME: 방 입장 프로세스 다이얼로그 생성
            //  => 아래는 채팅방 작업을 위한 임시 코드임
            it = new Intent(getApplicationContext(), ChatRoomActivity.class);
            it.putExtra("roomId", item.getRoomId());
            startActivity(it);
        });

        // TODO: 사용자 닉네임 할당 (기본 값은 이메일 부분이 제외된 ID)
//        tvUserNickname.setText(currentUser.getEmail());

        btnUserProfileImg.setOnClickListener(view -> {
            it = new Intent(getApplicationContext(), MyPageActivity.class);
            startActivity(it);
        });

        btnSearchingRoom.setOnClickListener(view -> {
            // TODO: 방 찾기 다이얼로그 생성
        });

        btnCreatingRoom.setOnClickListener(view -> DialogManager.showCreateRoomDialog(this, user.getUid()));

        // 서버 연결
        SocketManager.getInstance().connectToServer();
    }

    /**
     * 동시 접속자 수 업데이트 함수
     * 사용자가 서버 소켓에 연결하거나 연결을 해제할 때마다 호출됨
     * @param userCount 동시 접속자 수
     */
    public void updateUserCount(String userCount) {
        runOnUiThread(() -> tvConcurrentConnectors.setText(userCount));
    }

    public void setRoomList(List<RoomItem> updatedRooms) {
        runOnUiThread(() -> {
            roomList.clear();
            roomList.addAll(updatedRooms);
            roomListAdapter.notifyDataSetChanged();
        });
    }

    /**
     * 채팅방 고유 ID를 기준으로 해당 방으로 이동시키는 메서드
     * @param roomId 채팅방 고유 ID
     */
    public void startChatRoomActivity(String roomId) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("roomId", roomId);
        startActivity(intent);
    }
}
