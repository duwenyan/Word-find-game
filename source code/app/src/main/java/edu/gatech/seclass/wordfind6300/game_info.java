package edu.gatech.seclass.wordfind6300;

public class game_info { // use the database to generate the listview
    String gamescore;
    String rerolltime;
    String wordnumber;

    public game_info(String string1, String string2, String string3) {
        // TODO Auto-generated constructor stub
        gamescore = string1;
        rerolltime = string2;
        wordnumber = string3;
    }


        public void setScore(String gamescore) {
            this.gamescore = gamescore;
        }

        public void setReroll(String rerolltime) {
            this.rerolltime = rerolltime;
        }

        public void setNumber(String wordnumber) {
            this.wordnumber = wordnumber;
        }

        public String getScore() {
            return this.gamescore;
        }

        public String getReroll() {
            return this.rerolltime;
        }

        public String getNumber() {
            return this.wordnumber;
        }


        @Override
        public String toString() {
            return "food_info [id=" + String.valueOf(this.gamescore) + ", foodsName=" + this.rerolltime + ", EXP="
                    + this.wordnumber + "]";
        }

}
