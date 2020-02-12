package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2018/8/22
 */
public class RecommendBookResponse {

    public List<RecDataBean> recdata;

    public static class RecDataBean implements Parcelable {
        public int bookid;
        public String bookname;
        public String author;
        public String bookcover;
        public String synopsis;
        public int chapterid;
        public int status;
        public String classname;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.bookid);
            dest.writeString(this.bookname);
            dest.writeString(this.author);
            dest.writeString(this.bookcover);
            dest.writeString(this.synopsis);
            dest.writeInt(this.chapterid);
            dest.writeInt(this.status);
            dest.writeString(this.classname);
        }

        public RecDataBean() {
        }

        protected RecDataBean(Parcel in) {
            this.bookid = in.readInt();
            this.bookname = in.readString();
            this.author = in.readString();
            this.bookcover = in.readString();
            this.synopsis = in.readString();
            this.chapterid = in.readInt();
            this.status = in.readInt();
            this.classname = in.readString();
        }

        public static final Parcelable.Creator<RecDataBean> CREATOR = new Parcelable.Creator<RecDataBean>() {
            @Override
            public RecDataBean createFromParcel(Parcel source) {
                return new RecDataBean(source);
            }

            @Override
            public RecDataBean[] newArray(int size) {
                return new RecDataBean[size];
            }
        };
    }
}
