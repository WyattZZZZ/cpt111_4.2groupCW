package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Option;

import java.io.*;
import java.util.Scanner;

public class LookUpHistory extends SubMenu{
    private User user;
    private static String USERLISTPATH = "assignment1_quizSystem/resources/users.csv";
    private static String USERRECORDPATH = "assignment1_quizSystem/resources/questionsBank/emptyFolder";
    public LookUpHistory(User user) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Current User Name: " + this.user.getName() + " ID:" + this.user.getId());
        this.user = user;
        for (int i = 0; i < Questiondatabase.topics.length; i++){
            int tmp = User.load_record(Questiondatabase.topics[i], this.user.getId());
            System.out.print(Questiondatabase.topics[i] + " score: " + tmp + "\t");
        }
        super.options = new String[]{"Last Last time", "Last time", "Latest", "back"};
        String opt = "-1";
        while (!back("3", opt)){
            printmenu();
            System.out.println("Enter option: ");
            opt = sc.next();
            if (opt.equals("0") || opt.equals("1") || opt.equals("2") || opt.equals("3")){}
            while(!back("q", opt)){
                printHistory(opt);
                System.out.println("Enter q to quit");
                opt = sc.next();
                clear();
            }
        }
    }

    private void printHistory(String opt) throws FileNotFoundException {
        String fp = USERRECORDPATH  + "/" + this.user.getId() + "/" + opt + ".txt";
        Scanner sc =  new Scanner(new FileReader(fp));
        sc.useDelimiter("\r\n");
        while(sc.hasNext()){
            String line = sc.nextLine();
            String[] info = line.split(", ");
            if (info.length <= 1 || info == null){
                System.out.println("No data");
                return;
            }
            System.out.println("==========================================================");
            System.out.println("Difficulty: " + info[0] + "\tTopic: " + info[2]);

            System.out.println("Question:\n " + info[1]);
            for (int i = 3; i < info.length - 2; i++) {
                System.out.println("\tOption " + (i-2) + ":" + info[i]);
            }
            System.out.println("\t" + info[info.length - 2]);
            System.out.println("\tYour Choice: " + info[info.length - 1]);
            System.out.println("==========================================================");
            System.out.println();
        }
    }

    public static void makeHistory(NewQ[] qs, int[] choices, User user) throws IOException {
        String filepath = USERRECORDPATH + "/" + user.getId() ;
        File folder = new File(filepath);
        File[] files = folder.listFiles();
        String[] Cs = new String[choices.length];
        for (int i = 0; i < choices.length; i++){
             Cs[i] = qs[i].options[choices[i]].getAnswer();
        }
        if (files != null && files.length == 3) {
            String fp = files[2].getAbsolutePath();
            files[0].delete();
            File temp = files[1];
            files[1].renameTo(files[0]);
            files[2].renameTo(temp);
            write(fp, qs, Cs);
        }
        else {
            if (files != null) {
                for(File f : files){
                    f.delete();
                }
            }
            files = new File[3];
            for (int i = 0; i < 3; i++) {
                files[i] = new File(filepath + "/" + i + ".txt");
                files[i].createNewFile();
            }
            String fp = files[2].getAbsolutePath();
            files[0].delete();
            File temp = files[1];
            files[1].renameTo(files[0]);
            files[2].renameTo(temp);
            write(fp, qs, Cs);
        }

    }

    public static void write(String filepath, NewQ[] qs, String[] choices) throws IOException {
        FileWriter fw = null;
        File f = new File(filepath);
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = null;
        if (fw != null) {
            pw = new PrintWriter(fw);
        }
        int count = 0;
        if (pw != null) {
            for (NewQ q : qs) {
                String difi = switch ( q.difficulty){
                    case 1 -> "EASY";
                    case 2 -> "MEDIUM";
                    case 3 -> "HARD";
                    case 4 -> "VERY_HARD";
                    default -> throw new IllegalStateException("Unexpected value: " + q.difficulty);
                };
                pw.print(difi + ", " + q.question + ", " + q.topic + ", ");
                for (Option o : q.options) {
                    pw.print(o.getAnswer() + ", ");
                }
                for (String correctOption : q.correctAnswer) {
                    if (!correctOption.isEmpty()) {
                        pw.print(correctOption + ", ");
                    }
                }
                pw.print(choices[count]);
                pw.println();
                pw.flush();
                try {
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                count++;
            }
            pw.close();
            fw.close();
        }
        else{
            System.out.println("No file found");
        }
    }
}
