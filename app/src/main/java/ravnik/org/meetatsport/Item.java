package ravnik.org.meetatsport;

/**
 * Created by Nejc Ravnik on 09-Jan-17.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{



    public String title;

    public String current_players;
    public String needed_players;
    public String contact_info;
    public String location;

    public boolean isExpanded;


    public Item(){}

    public Item(Parcel in){
        title = in.readString();

        current_players= in.readString();
        needed_players=in.readString();
        contact_info=in.readString();
        location=in.readString();
        isExpanded = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(current_players);
        dest.writeString(needed_players);
        dest.writeString(contact_info);
        dest.writeString(location);
        dest.writeInt(isExpanded ? 1 : 0);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>(){
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}