package com.fastchat.Ui.Conversation;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fastchat.Core.FastChat;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.MediaMessage;
import com.fastchat.Model.TextMessage;
import com.fastchat.R;
import com.fastchat.adapter.ConversationListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
public class FragmentConversation extends Fragment implements TextWatcher {


    private EditText searchEdit;

    private TextView tvTitle;


    private RelativeLayout rlSearchBox;

    private LinearLayout noConversationView;

    private static final String TAG = "ConversationList";

    private View view;

    private ConversationListAdapter messageAdapter2;
    RecyclerView rvConversationList;
    String userId;

    public FragmentConversation() {
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation_screen, container, false);

        rvConversationList = view.findViewById(R.id.rv_conversation_list);

        noConversationView = view.findViewById(R.id.no_conversation_view);

        searchEdit = view.findViewById(R.id.search_bar);

        tvTitle = view.findViewById(R.id.tv_title);


        rlSearchBox = view.findViewById(R.id.rl_search_box);


        makeConversationList();


        return view;
    }


    private void handleArguments() {
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
    }

    private void makeConversationList() {
        FirebaseRecyclerOptions<BaseMessage> options =
                new FirebaseRecyclerOptions.Builder<BaseMessage>()
                        .setQuery(FastChat.getFastChat().getChatInteract().GetConversation(30, userId), new SnapshotParser<BaseMessage>() {
                            @NonNull
                            @Override
                            public BaseMessage parseSnapshot(@NonNull DataSnapshot snapshot) {
                                BaseMessage university = snapshot.getValue(BaseMessage.class);
                                if (university.getType().equals("text")) {
                                    return snapshot.getValue(TextMessage.class);
                                } else {
                                    MediaMessage mediaMessage = snapshot.getValue(MediaMessage.class);
                                    if (mediaMessage != null) {
                                        return mediaMessage;
                                    }

                                }


                                return null;
                            }
                        })
                        .build();

        messageAdapter2 = new ConversationListAdapter(getActivity(), options, userId) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();

            }
        };
        rvConversationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvConversationList.setAdapter(messageAdapter2);


    }


    private void checkNoConverstaion(boolean b) {
        if (b) {
            stopHideShimmer();
            noConversationView.setVisibility(View.VISIBLE);
            rvConversationList.setVisibility(View.GONE);
        } else {
            stopHideShimmer();
            noConversationView.setVisibility(View.GONE);
            rvConversationList.setVisibility(View.VISIBLE);
        }
    }


    private void stopHideShimmer() {
//        conversationShimmer.stopShimmer();
        //  conversationShimmer.setVisibility(View.GONE);
        // tvTitle.setVisibility(View.VISIBLE);
        // rlSearchBox.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        // searchEdit.addTextChangedListener(this);
//        rvConversationList.clearList();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        // searchEdit.removeTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
             //   makeConversationList();
        } else {
         }
    }

    @Override
    public void onStop() {
        super.onStop();
        messageAdapter2.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        messageAdapter2.startListening();

    }
}
