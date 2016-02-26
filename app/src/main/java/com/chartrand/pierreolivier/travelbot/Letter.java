package com.chartrand.pierreolivier.travelbot;

/**
 * Created by Pierre-Olivier on 2016-02-07.
 */
public class Letter {
    private String dateSent;
    private boolean checked;
    private String rawText;

    public Letter(String dateSent, String rawText) {
        this.dateSent = dateSent;
        this.rawText = rawText;
        checked=false;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }



}
