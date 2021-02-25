package com.udacity.jwdnd.course1.cloudstorage.model;

public class NoteData {

    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private int userid;

    public NoteData(Integer noteId, String noteTitle, String noteDescription, int userId) {
        this.noteid = noteId;
        this.notetitle = noteTitle;
        this.notedescription = noteDescription;
        this.userid = userId;
    }

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
