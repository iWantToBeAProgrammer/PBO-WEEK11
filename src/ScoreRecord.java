import java.sql.Timestamp;
import java.util.ArrayList;

public class ScoreRecord {
    String name;
    Timestamp date;
    int score;

    public ScoreRecord(String name, Timestamp date, int score) {
        this.name = name;
        this.date = date;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public static void showList(ArrayList<ScoreRecord> topScores) {
        int i = 1;
        System.out.println("=============Leaderboard==============");
        for (ScoreRecord topScore : topScores) {
            System.out.println(i++ + ". Nama: " + topScore.name + "\t Timestamp: " + topScore.date + "\t Score: "
                    + topScore.score);
        }
    }

}
