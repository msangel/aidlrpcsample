package ua.co.k.playground;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private final String message;

    protected Event(Parcel in) {
        this.message = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(String result) {
        message = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
    }

    @Override
    public String toString() {
        return "Event{"+message+"}";
    }
}
