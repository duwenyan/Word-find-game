package edu.gatech.seclass.wordfind6300;

public class word_info { // use that to generate the listview
    String playedword;
    String playedwordnumber;

    public word_info(String string1, String string2) {
        // TODO Auto-generated constructor stub
        playedword = string1;
        playedwordnumber = string2;
    }


    public void setWord(String playedword) {
        this.playedword = playedword;
    }

    public void setNumber(String playedwordnumber) {
        this.playedwordnumber = playedwordnumber;
    }

    public String getWord() {
        return this.playedword;
    }

    public String getNumber() {
        return this.playedwordnumber;
    }



    @Override
    public String toString() {
        return "word_info [id=" + this.playedword + ", foodsName=" + this.playedwordnumber + "]";
    }
}
