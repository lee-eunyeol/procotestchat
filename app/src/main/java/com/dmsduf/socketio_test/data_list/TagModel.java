package com.dmsduf.socketio_test.data_list;

public class TagModel {
    int idx;  //태그인덱스
    String tag_name; //태그이름

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public TagModel(int idx, String tag_name) {
        this.idx = idx;
        this.tag_name = tag_name;
    }


}
