package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.util.IOUtilities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Questiondatabase {
    private NewQ[] questions;
    protected NewQ[] validatedQuestions;
    protected String topic;
    protected static String pw = "password";
    protected static String[] name_topics = {"Computer Science", "Electronic Engineering", "English", "Mathematics"};
    protected static String[] topics = {"cs", "ee", "English", "Mathematics"};
    private static final String QUESTIONS_BANK_PATH = "assignment1_quizSystem/resources/questionsBank/";
    protected Questiondatabase(String topic) {
        this.topic = topic;
        String filename = QUESTIONS_BANK_PATH + "questions_" + topic + ".xml";
        try {
            System.out.println("=== read questions - started ===");
            Question[] questions = IOUtilities.readQuestions(filename);
            if (null == questions || questions.length == 0) {
                System.out.println("Questions is empty!");
            } else {
                int numQuestions = questions.length;
                NewQ[] newQs = new NewQ[numQuestions];
                int counter = 0;
                for (Question question : questions) {
                    NewQ Q;
                    int difficulty;
                            String diffi = String.valueOf(question.getDifficulty());
                    difficulty = switch (diffi) {
                        case "EASY" -> 1;
                        case "MEDIUM" -> 2;
                        case "HARD" -> 3;
                        case "VERY_HARD" -> 4;
                        default -> 0;
                    };

                    String ques = String.valueOf(question.getQuestionStatement());
                    Option[] options = question.getOptions();
                    String[] correctAns = new String[options.length];
                    for (int i=0; i<options.length; i++) {
                        if (options[i].isCorrectAnswer()){
                            correctAns[i] = options[i].toString();
                        }
                        else {
                            correctAns[i] = "";
                        }
                    }
                    Q = new NewQ(ques, options, correctAns, difficulty, topic);
                    newQs[counter] = Q;
                    counter++;
                }
                this.questions = newQs;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("=== read questions - complete ===");
        }
        this.validatedQuestions = get_valid_questions();
        System.out.println("\n === " + this.validatedQuestions.length + " valid questions read ===");
    }

    protected static boolean validation(NewQ q){
        boolean valid = false;
        for (String t : topics){
            if (t.equals(q.topic)) {
                valid = true;
                break;
            }
        }
        if (q.question.isEmpty()){
            valid = false;
        }
        if (q.options.length <= 1){
            valid = false;
        }
        int count = 0;
        for (String t : q.correctAnswer){
            if (!t.isEmpty()){
               count++;
            }
        }
        if (count>=2){
            valid = false;
        }
        return valid;
    }

    protected static int[] printQuestions(NewQ q){
        ArrayList<Option> opts = new ArrayList<Option>();
        opts.addAll(Arrays.asList(q.options));
        Random rand = new Random();
        int[] indices = new int[q.correctAnswer.length+1];
        int count = 1;
        for (int i = 0; i < q.correctAnswer.length; i++){
            if (!q.correctAnswer[i].isEmpty()){
                indices[0] = i;
            }
        }
        System.out.println("The topic you choose: " + q.topic);
        String difi = switch (q.difficulty){
            case 1 -> "Easy";
            case 2 -> "Medium";
            case 3 -> "Hard";
            case 4 -> "Very Hard";
            default -> throw new IllegalStateException("Unexpected value: " + q.difficulty);
        };
        System.out.println("The difficulty of current question: " + difi);
        System.out.println("\t" + q.question);
        while (count < q.correctAnswer.length+1){
            int random = rand.nextInt(0, q.correctAnswer.length-count+1);
            for (int i = 0; i < q.options.length; i++){
                if (q.options[i].getAnswer().equals(opts.get(random).getAnswer())){
                    indices[count] = i;
                    count++;
                    break;
                }
            }
            System.out.println("\t\t" + (count-1) + ". " + opts.get(random).getAnswer());
            opts.remove(random);
        }
        return indices;
    }
    protected static void admin() throws InterruptedException, IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("\b\b\b".repeat(50));
        System.out.println("Enter your admin password:");
        Scanner sc = new Scanner(System.in);
        String password = sc.nextLine();
        boolean flag = password.equals(pw);
        boolean menu_flag = false;
        int count = 0;
        while (!flag){
            if (!flag){
                System.out.println("Your admin password is wrong, try again!");
                password = sc.nextLine();
                flag = password.equals(pw);
                if (flag){
                    break;
                }
                count++;
            }
            if (count == 5){
                System.out.println("Your tried too many times!");
                Thread.sleep(5);
                System.out.println(sb);
                return;
            }
        }
        System.out.println("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        System.out.print("admin mode activating completed, now you can add questions");
        while (!menu_flag) {
            System.out.print("Choose option to continue\n1. reset your password\n2. add questions\n3. add topic\n4. back");
            char option = sc.next().charAt(0);
            boolean choose_flag = choose_validate(option);
            if (choose_flag){
                int opt = Integer.parseInt(String.valueOf(option));
                switch (opt){
                    case 1:
                        System.out.print(sb);
                        System.out.println("Your new password:");
                        pw = sc.next();
                        System.out.println("Your new password has been reset");
                        System.out.print(sb);
                        break;
                    case 2:
                        System.out.print(sb);
                        add_questions();
                        System.out.print(sb);
                        break;
                    case 3:
                        System.out.print(sb);
                        add_topics();
                        System.out.print(sb);
                        break;
                    case 4:
                        System.out.print(sb);
                        menu_flag = true;
                }
            }
        }
        return;
    }
    protected static void add_topics() throws IOException {
        System.out.print("Enter topic name: ");
        Scanner sc = new Scanner(System.in);
        String topic = sc.nextLine();
        int length = topics.length;
        String[] new_topics = new String[length+1];
        System.arraycopy(topics, 0, new_topics, 0, length);
        new_topics[length] = topic;
        topics = new_topics;
    }

    protected static void add_questions() {
        System.out.println("Choose one of the topics below to add your new question (enter one of the topics below):");
        print_topics();
        Scanner sc = new Scanner(System.in);
        String topic = sc.nextLine();

        // 1. 验证输入的主题是否有效
        boolean validTopic = Arrays.asList(topics).contains(topic);
        if (!validTopic) {
            System.out.println("Invalid topic. Please try again.");
            return;
        }

        // 2. 输入并验证难度
        Difficulty difi = null;
        while (difi == null) {
            System.out.println("Enter your difficulty (EASY/MEDIUM/HARD/VERY_HARD):");
            try {
                difi = Difficulty.valueOf(sc.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid difficulty level. Please choose one from EASY, MEDIUM, HARD, VERY_HARD.");
            }
        }

        // 3. 输入问题描述
        System.out.println("Enter your Question statement:");
        String q = sc.nextLine().trim();
        if (q.isEmpty()) {
            System.out.println("The question statement cannot be empty. Please try again.");
            return;
        }

        // 4. 输入并验证选项
        System.out.println("Enter the number of options:");
        int optionCount = sc.nextInt();
        sc.nextLine(); // 清除换行符
        Option[] options = new Option[optionCount];

        // 收集选项
        boolean hasCorrectAnswer = false;  // 标志位，确保至少一个正确答案
        for (int i = 0; i < optionCount; i++) {
            System.out.println("Enter option " + (i + 1) + ":");
            String optionText = sc.nextLine().trim();
            if (optionText.isEmpty()) {
                System.out.println("Option cannot be empty. Please enter a valid option.");
                i--;  // 如果选项为空，重新输入当前选项
                continue;
            }

            System.out.println("Is this option the correct answer? (Enter Y/n):");
            String correctAnswer = sc.nextLine().trim();

            Option option = new Option(optionText);
            if ("Y".equalsIgnoreCase(correctAnswer)) {
                option.setCorrectAnswer(true);
                hasCorrectAnswer = true;  // 标记有正确答案
            } else if ("n".equalsIgnoreCase(correctAnswer)) {
                option.setCorrectAnswer(false);
            } else {
                System.out.println("Invalid input. Please enter Y for correct answer or n for incorrect answer.");
                i--;  // 如果输入无效，重新输入当前选项
                continue;
            }
            options[i] = option;
        }

        // 5. 验证是否有正确答案
        if (!hasCorrectAnswer) {
            System.out.println("At least one option must be the correct answer. Please try again.");
            return;
        }

        // 6. 创建问题对象
        Question question = new Question(topic, difi, q, options);
        // 等待保存
        System.out.println("New question has been added successfully.");
    }


    public static boolean choose_validate(char option){
        if (Character.isDigit(option)) {
            if(Integer.parseInt(String.valueOf(option))>4){
                System.out.println("Wrong option");
                return false;
            }
            return true;
        }
        return false;
    }

    public NewQ[] get_valid_questions() {
        int count = 0;
        int[] label = new int[questions.length];
        for (int i=0; i<this.questions.length; i++){
            if (validation(this.questions[i])){
                count++;
                label[i] = 1;
            }
            else{
                label[i] = 0;
            }
        }
        NewQ[] newQs = new NewQ[count];
        count = 0;
        int qcount = 0;
        while (count<newQs.length){
            if (label[qcount] == 1){
                newQs[count] = this.questions[qcount];
                qcount++;
                count++;
            }
            else {
                qcount++;
            }
        }
        return newQs;
    }

    public static Questiondatabase change_topic(String topic){
        return new Questiondatabase(topic);
    }

    public static void print_topics(){
        System.out.println("Now, we have these topics, please enter name of the topic to choose");
        System.out.println("For example, if you choose \"0. cs\", please enter \"cs\".");
        for (int i=0; i<topics.length; i++){
            System.out.println(i + ". " + topics[i]);
        }
    }
}
