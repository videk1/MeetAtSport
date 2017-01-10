package ravnik.org.meetatsport;

/**
 * Created by Nejc Ravnik on 09-Jan-17.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;



public class ExpandableAdapter extends BaseAdapter{


    List<Item> items;
    Context context;

    public class Row{
        AppCompatTextView mTitle;

        AppCompatTextView mCurrentPlayers;
        AppCompatTextView mNeededPlayers;
        AppCompatTextView mLocation;
        AppCompatTextView mInfo;
        FrameLayout mFrame;
        ImageView mImageArrow;
    }

    public ExpandableAdapter(Context context, List<Item> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Row theRow;

        if(convertView == null){
            theRow = new Row();
            convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
            theRow.mFrame = (FrameLayout) convertView.findViewById(R.id.fl_wrapper);
            theRow.mTitle = (AppCompatTextView) convertView.findViewById(R.id.tv_title);
            theRow.mImageArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            theRow.mCurrentPlayers=(AppCompatTextView) convertView.findViewById(R.id.tv_current_players);
            theRow.mNeededPlayers=(AppCompatTextView) convertView.findViewById(R.id.tv_needed_players);
            theRow.mInfo= (AppCompatTextView)convertView.findViewById(R.id.tv_contact_info);
            theRow.mLocation= (AppCompatTextView)convertView.findViewById(R.id.tv_location);

            convertView.setTag(theRow);
        }else{

            theRow = (Row) convertView.getTag();
        }


        final Item item = items.get(position);
        if(item.isExpanded){
            theRow.mFrame.setVisibility(View.VISIBLE);
            theRow.mImageArrow.setRotation(180f);
        }else{
            theRow.mFrame.setVisibility(View.GONE);
            theRow.mImageArrow.setRotation(0f);
        }

        theRow.mTitle.setText(item.title);

        theRow.mNeededPlayers.setText(item.needed_players);
        theRow.mCurrentPlayers.setText(item.current_players);
        theRow.mInfo.setText(item.contact_info);
        theRow.mLocation.setText(item.location);
        theRow.mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double myLatitude = 46.223106;
                Double myLongitude = 14.603687;
                String labelLocation = item.title;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")"));
                    context.startActivity(intent);
                }
                catch (Exception ex ){
                    Toast.makeText(context,"Napaka pri povezavi z google maps", Toast.LENGTH_SHORT);
                }


            }
        });


        // return the view
        return convertView;
    }
}
