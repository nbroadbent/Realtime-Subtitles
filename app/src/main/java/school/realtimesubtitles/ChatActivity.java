package school.realtimesubtitles;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.ColorLong;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    CustomItemClickListener listener;
    private SpeechRecognizer sr;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Microphone> mics;
    private ArrayList<Message> chat;
    private RecyclerView chatList;


    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;


    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    private Switch rec;
    private Switch lang;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        chatList = findViewById(R.id.recycler1);

        layoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(layoutManager);



        final FloatingActionButton mic1 = (FloatingActionButton) findViewById(R.id.mic1);
        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mics.get(0).isActive()) {
                    Snackbar.make(view, "Mic 1 listening stopped", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    sr.cancel();
                    sr.stopListening();
                    sr.destroy();
                    mic1.setRippleColor(0);
                    mics.get(0).setActive(false);
                } else {
                    Snackbar.make(view, "Mic 1 listening", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    mic1.setRippleColor(500);
                    restartSpeech();
                    listenForSpeech();
                    mics.get(0).setActive(true);
                }
            }
        });

        final FloatingActionButton mic2 = (FloatingActionButton) findViewById(R.id.mic2);
        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Mic 2 listening", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                listenForSpeech();
            }
        });


        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        mRecordButton = new RecordButton(this);
        mPlayButton = new PlayButton(this);

        lang = findViewById(R.id.lang);

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < mics.size(); i++) {
                    mics.get(i).setEnglish(lang.isChecked());
                }

                sr.setRecognitionListener(new listener());
                listenForSpeech();
                mics.get(0).setActive(true);

                if (lang.isChecked()) {
                    Snackbar.make(view, "English", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "French", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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
            //chat.add(new Message(mics.get(0), "YOOOOOOOOO"));
            //chat.add(new Message(mics.get(1), "Let's get some pizza"));
            //chat.add(new Message(mics.get(2), "Yes"));
        }
        updateChatView();

        /*chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeName(i);
            }
        });*/
    }

    private void restartSpeech() {
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());
    }

    private void listenForSpeech(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (mics.get(0).isEnglish())
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        else
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);

        //rec.setChecked(false);

        sr.startListening(intent);
        Log.i("111111","11111111");
    }

    class listener implements RecognitionListener {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(LOG_TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(LOG_TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(LOG_TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(LOG_TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(LOG_TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(LOG_TAG,  "error " +  error);

            sr.destroy();
            restartSpeech();
            listenForSpeech();
        }
        public void onResults(Bundle results)
        {
            // Analyze speech results.
            String str = new String();
            Log.d(LOG_TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(LOG_TAG, "result " + data.get(i));
                str += data.get(i);
            }

            // Create chat message.
            Message message = new Message(mics.get(0), String.valueOf(data.get(0)));
            chat.add(message);
            updateChatView();
            listenForSpeech();
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(LOG_TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(LOG_TAG, "onEvent " + eventType);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    protected void updateChatView(){
        chatAdapter = new ChatAdapter(ChatActivity.this, chat, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                changeName(position);
            }
        });
        chatList.setAdapter(chatAdapter);
        chatList.smoothScrollToPosition(chatAdapter.getItemCount());
    }

    private void changeName(final int pos){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Name Change")
                .setMessage("Change " + chat.get(pos).getMicrophone().getName() + "'s name")
                .setView(input)
                .setNeutralButton("Cancel",null)
                .setPositiveButton("French", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set new name.
                        chat.get(pos).getMicrophone().setName(input.getText().toString());
                        chat.get(pos).getMicrophone().setEnglish(false);
                        updateChatView();
                    }
                })
                .setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set new name.
                        chat.get(pos).getMicrophone().setName(input.getText().toString());
                        chat.get(pos).getMicrophone().setEnglish(true);
                        updateChatView();
                    }
                }).create().show();
    }
}
