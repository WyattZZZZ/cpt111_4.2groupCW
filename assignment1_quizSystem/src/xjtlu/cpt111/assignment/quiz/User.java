package xjtlu.cpt111.assignment.quiz;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class User {
    // discuss for topic record order
    private static final String RECORD_FILE_PATH = "assignment1_quizSystem/resources/questionsBank/emptyFolder/record.csv";
    private final String name;
    private final String id;
    private final String password;
    private static int recordLength; ;
    private int[] record;
    public User(String id, String password, String name) {
        this.password = password;
        this.id = id;
        this.name = name;
    }

    public static void init_record(String[] topics) {
        recordLength = topics.length;
    }

    public void save_record(int topic_index, int score) throws IOException {
        record = new int[User.recordLength];
        File f = new File(RECORD_FILE_PATH);
        FileWriter fw;
        boolean user_confirmation = false;
        try{
            Scanner sc = new Scanner(new FileReader(RECORD_FILE_PATH));
            sc.useDelimiter("\r\n");
            if (sc.hasNext()) {
                StringBuffer sb = new StringBuffer();
                while (sc.hasNext()) {
                    String line = sc.next();
                    String[] info = line.split(",", 3 + recordLength);
                    if (info[0].equals(this.id)) {
                        user_confirmation = true;
                        sb.append(info[0] + "," + info[1] + "," + info[2] + ",");
                        for (int i = 0; i < record.length - 1; i++) {
                            if (i == topic_index) {
                                sb.append(score).append(",");
                            }
                            else {
                                sb.append(info[3 + i]).append(",");
                            }
                        }
                        if (record.length - 1 == topic_index) {
                            sb.append(score).append("\r\n");
                        } else {
                            sb.append(info[record.length + 2]).append("\r\n");
                        }
                    } else {
                        for (String s : info) {
                            sb.append(s + ",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append("\r\n");
                    }
                }
                f.delete();
                fw = new FileWriter(f);
                fw.write(sb.toString());
                fw.close();
            }
            if (!user_confirmation) {
                fw = new FileWriter(RECORD_FILE_PATH, true);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < (record.length - 2); i++) {
                    if (i == topic_index) {
                        sb.append(score).append(",");
                    }
                    sb.append("0").append(",");
                }
                if (record.length - 1 == topic_index) {
                    sb.append(score).append("\r\n");
                } else {
                    sb.append(0).append("\r\n");
                }
                fw.write(this.id + "," + this.name + "," + this.password + "," + sb + "\r\n");
                fw.close();
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int load_record(String topic, String userId) throws IOException {
        int topic_index = 0;
        for (int i=0;i<recordLength;i++) {
            if (topic.equals(Questiondatabase.topics[i])) {
                topic_index = i+3;
                break;
            }
            if (i == recordLength-1) {
                System.out.println("Wrong topic");
                return -1;
            }
        }
        try{
            Scanner sc = new Scanner(new FileReader(RECORD_FILE_PATH));
            sc.useDelimiter("\r\n");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] info = line.split(",");
                if (userId.equals(info[0])) {
                    return Integer.parseInt(info[topic_index]);
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static String[] scan_user() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(RECORD_FILE_PATH));
        sc.useDelimiter("\r\n");
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            String line = sc.next();
            sb.append(line.split(",")[0] + ",");
        }
        String s = sb.toString();
        return s.split(",");
    }

    public String getName(){
        return this.name;
    }
    public String getId(){
        return this.id;
    }
    public String getPassword(){
        return this.password;
    }
}

