package school.realtimesubtitles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nick-JR on 3/7/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final Context context;
    CustomItemClickListener listener;
    private ArrayList<Message> messages;
    protected int[] imageIds = {
            R.drawable.blue,
            R.drawable.red,
            R.drawable.green,
            //R.drawable.yellow};
    };

    public ChatAdapter(Context context, ArrayList<Message> messages, CustomItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        System.out.println(messages);
        this.messages = new ArrayList<>(messages);
    }
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        holder.bindView(position,messages.get(position), listener);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView text;
        ImageView userImage;
        public ViewHolder(final View view){
            super(view);
            name = itemView.findViewById(R.id.name);
            text = itemView.findViewById(R.id.text);
            userImage = itemView.findViewById(R.id.image);
        }

        public void bindView(final int position, final Message m, final CustomItemClickListener listener){
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view,position);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }
}