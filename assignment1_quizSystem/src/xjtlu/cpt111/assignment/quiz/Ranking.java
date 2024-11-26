package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Question;

import java.io.*;
import java.util.*;

public class Ranking extends SubMenu {

    private static final String RECORD_FILE_PATH = "resources\\questionsBank\\emptyFolder\\record.csv";
    Score[] scores;
    String[] userIds;
    String topic;


    // Generate and print the ranking list
    public Ranking() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        // Read user data using the scan_user method
        this.userIds = User.scan_user();
        this.scores = new Score[this.userIds.length];
        boolean outer_flag = true;
        while (outer_flag) {
            Questiondatabase.print_topics();
            System.out.println("You need to enter topic name to choose, and enter \"q\" to exit");
            boolean flag = false;
            String tmpStr = sc.next();
            if (tmpStr.equals("q")) {
                break;
            }
            while (!flag) {
                System.out.println("Enter your topic name:");
                if (tmpStr.equals("") || tmpStr == null) {
                    System.out.println("You must enter a topic name.");
                } else {
                    if (!Character.isDigit(tmpStr.charAt(0))) {
                        for (String t : Questiondatabase.topics) {
                            if (t.equals(tmpStr)) {
                                System.out.println("You choosed " + tmpStr + ".");
                                flag = true;
                                this.topic = tmpStr;
                                break;
                            } else {
                                System.out.println("You must enter a topic name.");
                            }
                        }
                    }
                }
            }
            if (super.back("q", topic)){
                super.clear();
                break;
            }
            for (int i = 0; i < userIds.length; i++) {
                int score = User.load_record(topic, userIds[i]);
                Score temp = new Score(userIds[i], score);
                this.scores[i] = temp;
            }
            quickSort(scores, 0, this.scores.length - 1);
            // Output the ranking
            print_ranking();
            // Allow user to press any key to return to main menu
            super.clear();
            System.out.println();
            if (!super.back("q", sc.next())){
                outer_flag = false;
            }

        }
    }

    private void print_ranking() throws InterruptedException {
        super.clear();
        System.out.println(this.topic + " Ranking:");
        System.out.println("\tUserID\t\tscore");
        // Print the top 3 rankings using displayRank method
        for (int i = 0; i < this.userIds.length; i++) {
            if (this.scores[i].score != 0) {
                System.out.println(i + ".\t" + this.userIds[i] + "\t\t\t" + this.scores[i].score);
            }
        }
        System.out.println("\nThose who scored 0 will not be listed");
    }

    private static void quickSort(Score[] scores, int low, int high) {
        int i, j;
        Score temp;
        i = low;
        j = high;
        temp = scores[i];
        while (i < j) {
            while (i < j && temp.score <= scores[j].score) {
                j--;
            }
            scores[j] = scores[i];
            while (i < j && temp.score >= scores[i].score) {
                i++;
            }
            scores[i] = scores[j];
            quickSort(scores, low, i - 1);
            quickSort(scores, i, high);
        }
        scores[low] = scores[i];
        scores[i] = temp;

    }
}
