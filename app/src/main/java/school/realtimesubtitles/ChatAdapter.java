package school.realtimesubtitles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nick-JR on 3/7/2018.
 */

public class ChatAdapter extends ArrayAdapter<Message> {

    private final Context context;
    private ArrayList<Message> messages;
    protected int[] imageIds = {
            R.drawable.blue,
            R.drawable.red,
            R.drawable.green,
            //R.drawable.yellow};
    };

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        super(context, R.layout.chat_layout, messages);
        this.context = context;
        System.out.println(messages);
        this.messages = new ArrayList<>(messages);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.chat_layout, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView text = (TextView) rowView.findViewById(R.id.text);
        ImageView userImage = (ImageView) rowView.findViewById(R.id.image);

        // Assign image colours.
        /*
        for (int i = 0; i < messages.size(); i++) {
            if (messages != null) {
                Message t = messages.get(i);
                //userImage.setImageResource(imageIds[i]);
            }
        }
        */

        if (messages != null) {
            userImage.setImageResource(imageIds[messages.get(position).getMicrophone().getId()]);
        }

        System.out.println("Pos: " + position);
        if (messages != null) {
            if (position < messages.size()) {
                Message message = messages.get(position);
                name.setText(message.getMicrophone().getName());
                text.setText(message.getText());
            }
        }

        return rowView;
    }
}