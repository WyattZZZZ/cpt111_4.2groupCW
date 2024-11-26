package xjtlu.cpt111.assignment.quiz;

public abstract class SubMenu {
    public String[] options;
    public static String current_menu_name;
    public void printmenu(){
        for (int i = 0; i < this.options.length; i++) {
            System.out.println(i + ". " + this.options[i]);
        }
    }
    public boolean back(String key, String character){
        return key.equals(character);
    }
    public boolean choose_validate(String option){
        if (option != null && option.length() > 0) {
            if (Character.isDigit(option.charAt(0))) {
                if (Integer.parseInt(option) > options.length) {
                    System.out.println("\nWrong option\n");
                    return false;
                }
                return true;
            }
            System.out.println("\nWrong option\n");
            return false;
        }
        System.out.println("\nWrong option\n");
        return false;
    }
    public void set_options(String[] opts){
        this.options = opts;
    }
    public void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
