package nandubrajan.ceknpy.iguard;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sreelekshmi on 2/21/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewholder> {
    List<MessageModel> mMessageList = new ArrayList<>();
    String senderid;

    public MessageAdapter(List<MessageModel> mMessageList,String senderid) {
        this.mMessageList = mMessageList;
        this.senderid = senderid;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatinflator, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
       MessageModel m =  mMessageList.get(position);
       if (constant.name.equals(m.getSendername()))
       {
           holder.chatSent.setVisibility(View.VISIBLE);
           holder.sendTime.setVisibility(View.VISIBLE);
           holder.chatSent.setText(m.getMessage());
           holder.sendTime.setText(m.getTime());
           holder.chatRes.setVisibility(View.INVISIBLE);
           holder.receiverName.setVisibility(View.INVISIBLE);
           holder.receivertime.setVisibility(View.INVISIBLE);
           Log.e("disp1"," ");
       }else {

           holder.chatSent.setVisibility(View.INVISIBLE);
           holder.sendTime.setVisibility(View.INVISIBLE);
           holder.chatRes.setVisibility(View.VISIBLE);
           holder.chatRes.setText(m.getMessage());
           holder.receiverName.setVisibility(View.VISIBLE);
           holder.receiverName.setText(m.sendername);
           holder.receivertime.setVisibility(View.VISIBLE);
           holder.receivertime.setText(m.getTime());
           Log.e("disp2"," ");
       }
    }

    @Override
    public int getItemCount() {

        return mMessageList.size();

    }

    class viewholder extends RecyclerView.ViewHolder {
        private TextView receiverName;
        private TextView chatRes;
        private TextView chatSent;
        private TextView sendTime;
        private TextView receivertime;



        public viewholder(View itemView) {
            super(itemView);
            receiverName = (TextView)itemView.findViewById( R.id.receiver_name );
            chatRes = (TextView)itemView.findViewById( R.id.chat_res );
            chatSent = (TextView)itemView.findViewById( R.id.chat_sent );
            sendTime = (TextView)itemView.findViewById( R.id.sendTime );
            receivertime = (TextView)itemView.findViewById( R.id.receivertime );
        }
    }
}
