package com.bsuir.chekh.lab2.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AlarmModel
        implements  Parcelable {
    private String name;
    private String uri;
    private DateTime date;

    public AlarmModel(String uri, DateTime date) {
        if(date == null) {
            date = new DateTime();
        }

        if(uri == null) {
            uri = "";
        }

        this.uri = uri;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmModel that = (AlarmModel) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public AlarmModel(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);

        this.date = (new DateTime(Long.parseLong(data[0])));
        this.uri = (data[1]);
        this.name = (data[2]);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String dateToFormattedString(boolean separateTime) {
        String separator = separateTime ? "\n" : "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy "+ separator +"H:mm");

        if(date!=null) {
            return date.toString(fmt);
        }

        return "0/0/0";
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                Long.toString(this.date.getMillis()),
                this.uri,
                this.name
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlarmModel createFromParcel(Parcel in) {
            return new AlarmModel(in);
        }

        public AlarmModel[] newArray(int size) {
            return new AlarmModel[size];
        }
    };

    private void writeObject(ObjectOutputStream o)
            throws IOException {
        o.writeLong(date.getMillis());
        o.writeObject(uri);
        o.writeObject(name);
    }

    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {

        date = new DateTime(o.readLong());
        uri = (String) o.readObject();
        name = (String) o.readObject();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
