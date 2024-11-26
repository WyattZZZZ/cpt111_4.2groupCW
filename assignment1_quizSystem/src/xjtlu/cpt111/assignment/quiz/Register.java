package xjtlu.cpt111.assignment.quiz;

import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class Register extends SubMenu {
    User login_user;
    String FILEPATH = "assignment1_quizSystem/resources/questionsBank/emptyFolder/";

    public Register() throws IOException, InterruptedException {
        String[] opts = {"log in", "register user", "back"};
        super.set_options(opts);
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        while (!flag) {
            super.printmenu();
            System.out.print("Enter your choice (numbers only): ");
            String opt = sc.nextLine();
            if (super.choose_validate(opt)) {
                switch (Integer.parseInt(opt)) {
                    case 0 -> this.login_user = log_in();
                    case 1 -> register_user();
                    case 2 -> flag = super.back(opt, "2");
                }
                super.clear();
            }
            if (this.login_user != null) {
                break;
            }
        }
    }

    public User log_in() throws InterruptedException {
        super.clear();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username_id: ");
        String user_id = sc.nextLine();
        if (user_id.equals("")) {
            System.out.println("Empty User_id\n");
            return null;
        }
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        if (validate_user(user_id, password)) {
            String name = find_name(user_id, password);
            return new User(user_id, password, name);
        } else {
            System.out.println("You have entered incorrect login information.");
            Thread.sleep(1000);
            return null;
        }
    }

    public void register_user() throws IOException {
        super.clear();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username_id: ");
        String user_id = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        if (user_id.equals("") || password.equals("") || name.equals("")) {
            System.out.println("Empty infomation\n");
            return;
        }
        boolean isVerified = false;

        while (!isVerified) {
            // 生成随机验证码
            String captcha = generateCaptcha();
            System.out.println("Captcha: " + captcha);
            System.out.print("Enter Captcha (case-sensitive): ");
            String userCaptcha = sc.nextLine();

            if (captcha.equals(userCaptcha)) {
                // 验证码正确，保存用户数据
                FileWriter fw = null;
                try {
                    File f = new File("assignment1_quizSystem/resources/users.csv");
                    fw = new FileWriter(f, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter pw = new PrintWriter(fw);
                pw.print("\r\n" + user_id + "," + name + "," + password);
                pw.flush();
                try {
                    fw.flush();
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Registration successful! You can now log in.");
                isVerified = true;
            } else {
                System.out.println("Incorrect captcha. Please try again.");
            }
        }
        File dir = new File(FILEPATH + user_id + "/");
        dir.mkdirs();
    }

    public static boolean validate_user(String user_id, String password) {
        try {
            Scanner sc = new Scanner(new FileReader("assignment1_quizSystem/resources/users.csv"));
            sc.useDelimiter("\r\n");
            while (sc.hasNext()) {
                String line = sc.next();
                String[] info = line.split(",", 3);
                if (info[0].equals(user_id) && info[2].equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String find_name(String user_id, String password) {
        try {
            Scanner sc = new Scanner(new FileReader("assignment1_quizSystem/resources/users.csv"));
            sc.useDelimiter("\r\n");
            while (sc.hasNext()) {
                String line = sc.next();
                String[] info = line.split(",", 3);
                if (info[0].equals(user_id) && info[2].equals(password)) {
                    return info[1];
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "you unset your name";
    }

    public static User change_user() throws IOException, InterruptedException {
        Register Re = new Register();
        return Re.login_user;
    }

    private String generateCaptcha() {
        Random random = new Random();
        int length = 6; // 验证码长度
        StringBuilder captcha = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }
}
