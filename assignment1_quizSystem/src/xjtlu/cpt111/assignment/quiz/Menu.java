package xjtlu.cpt111.assignment.quiz;

import java.util.Scanner;

public abstract class Menu {
    protected static String[] options;
    protected User login_user;
    protected static String topic;
    protected static int numofQ=3;
    public static String current_menu_name = "Main Menu";
    public void printmenu(){
        System.out.println("CURRENTLY, YOU ARE IN " + current_menu_name.toUpperCase());
        for (int i = 0; i < options.length; i++) {
            System.out.println(i + ". " + options[i]);
        }
    }
    public boolean quit(String key, String character){
        return key.equals(character);
    }
    public boolean choose_validate(String option){
        if (option != null && !option.isEmpty()) {
            if (Character.isDigit(option.charAt(0))) {
                if (Integer.parseInt(option) > options.length) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }
    public static void set_options(String[] opts){
        options = opts;
    }

    public void set_user(User u){
        login_user = u;
    }

    public static int getNumofQ() {
        return numofQ;
    }

    public static void setNumofQ(int numofQ) {
        Menu.numofQ = numofQ;
    }

    public static String getCurrent_menu_name() {
        return current_menu_name;
    }

    public static String getTopic() {
        return topic;
    }
    public void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
