package school.realtimesubtitles;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;

    private ArrayList<Microphone> mics;
    private ArrayList<Message> chat;

    private ListView chatList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatList = findViewById(R.id.chatView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Testing //

        // Microphones //

        mics = new ArrayList<>(3);
        mics.add(new Microphone("Nick", 0));
        mics.add(new Microphone("Nick1", 1));
        mics.add(new Microphone("Nick2", 2));

        chat = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            chat.add(new Message(mics.get(0), "YOOOOOOOOO"));
            chat.add(new Message(mics.get(1), "Let's get some pizza"));
            chat.add(new Message(mics.get(2), "Yes"));
        }
        updateChatView();

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeName(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void updateChatView(){
        chatAdapter = new ChatAdapter(ChatActivity.this, chat);
        chatList.setAdapter(chatAdapter);
    }

    private void changeName(final int pos){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Name Change")
                .setMessage("Change " + chat.get(pos).getMicrophone().getName() + "'s name")
                .setView(input)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set new name.
                        chat.get(pos).getMicrophone().setName(input.getText().toString());
                        updateChatView();
                    }
                }).create().show();
    }
}
