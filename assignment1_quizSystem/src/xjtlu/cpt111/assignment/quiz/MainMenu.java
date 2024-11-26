package xjtlu.cpt111.assignment.quiz;

import java.io.IOException;
import java.util.Scanner;

public class MainMenu extends Menu {
    Questiondatabase qd;

    public static void main(String[] args) throws IOException, InterruptedException {
        MainMenu mainMenu = new MainMenu();
        mainMenu.init();
        User.init_record(Questiondatabase.topics);
        mainMenu.clear();
        Scanner sc = new Scanner(System.in);
        String choice = "-1";

        while (!mainMenu.quit(choice, "6")) {
            mainMenu.printmenu();
            choice = sc.nextLine();
            if (mainMenu.choose_validate(choice)) {
                switch (choice) {
                    case "0": // Start Test
                        mainMenu.clear();
                        int topic_index = 0;
                        for (int i = 0; i <= Questiondatabase.topics.length - 1; i++) {
                            if (topic.equals(Questiondatabase.topics[i])) {
                                topic_index = i;
                            }
                        }
                        Test test = new Test(0, MainMenu.numofQ,
                                mainMenu.qd.get_valid_questions(), mainMenu.login_user, topic_index);
                        test.start();
                        mainMenu.clear();
                        break;
                    case "1": // Change Topic
                        mainMenu.clear();
                        Questiondatabase.print_topics();
                        boolean flag = false;
                        while (!flag) {
                            System.out.println("Enter your topic name:");
                            String tmpStr = sc.next();
                            if (tmpStr.equals("") || tmpStr == null) {
                                System.out.println("You must enter a topic name.");
                            } else {
                                if (!Character.isDigit(tmpStr.charAt(0))) {
                                    for (String t : Questiondatabase.topics) {
                                        if (t.equals(tmpStr)) {
                                            System.out.println("You choosed " + tmpStr + ".");
                                            flag = true;
                                            Menu.topic = tmpStr;
                                            break;
                                        }
                                    }
                                } else {
                                    System.out.println("You must enter a topic name.");
                                }
                            }
                        }
                        mainMenu.qd = Questiondatabase.change_topic(Menu.topic);
                        mainMenu.clear();
                        break;
                    case "2": // View Ranking
                        mainMenu.clear();
                        Ranking r = new Ranking();
                        mainMenu.clear();
                        break;
                    case "3": // User Dashboard
                        mainMenu.clear();
                        LookUpHistory luh = new LookUpHistory(mainMenu.login_user);
                        mainMenu.clear();
                        break;
                    case "4": // Admin Mode
                        mainMenu.clear();
                        Questiondatabase.admin();
                        mainMenu.clear();
                        break;
                    case "5": // Change User
                        mainMenu.clear();
                        User u = Register.change_user();
                        if (u == null) {
                            break;
                        }
                        User.init_record(Questiondatabase.topics);
                        mainMenu.login_user = u;
                        mainMenu.clear();
                        break;
                    case "6": // Quit
                        break;
                    case "":
                        mainMenu.clear();
                        System.out.println("Invalid choice");
                    default:
                        mainMenu.clear();
                        System.out.println("Invalid choice");
                }
            } else {
                mainMenu.clear();
                System.out.println("Invalid choice, try again");
            }
        }
    }

    public void init() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        String[] options = {
                "Test",
                "Change Topic",
                "Ranking",
                "User Dashboard",
                "Use Admin Mode",
                "Change User",
                "Quit"
        };
        setNumofQ(3);
        set_options(options);

        // Register and Login Process
        Register reg = new Register();
        if (reg.login_user == null) {
            System.out.println("You are not logged in.");
            while (reg.login_user == null) {
                System.out.println("Please login first.");
                Thread.sleep(100);
                reg = new Register(); // Recreate the Register object for login/registration
            }
        }
        set_user(reg.login_user);

        // Topic Selection
        System.out.println("Welcome to Quiz System, let's choose a topic first. (Enter the topic name)");
        Questiondatabase.print_topics();
        boolean flag = false;
        while (!flag) {
            System.out.println("Enter your topic name:");
            String tmpStr = sc.next();
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
                        }
                    }
                } else {
                    System.out.println("You must enter a topic name.");
                }
            }
        }
        this.qd = new Questiondatabase(topic);
    }
}
