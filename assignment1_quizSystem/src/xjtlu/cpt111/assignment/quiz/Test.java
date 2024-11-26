package xjtlu.cpt111.assignment.quiz;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Test extends SubMenu{

     // 简单题目集合
     private final NewQ[] Questions;
     private int numsOfQ;
     private boolean[] labelOfQ;
     private User user;
     private int[] choices;
     private NewQ[] used_questions;

     private int scoreAve;//平均得分

     // 调整系数，用于控制每次难度调整的幅度
     private double adjustmentFactor ;

     // 用于生成随机数，以便从题目集合中随机抽取题目
     private Random random;
     private int topic_index;

     // 构造器
     public Test(double adjustmentFactor, int numsOfQ, NewQ[] Questions, User user, int topic_index) throws InterruptedException, IOException {
          this.Questions = Questions;
          this.numsOfQ = numsOfQ;
          this.labelOfQ = new boolean[Questions.length];
          for (boolean l : labelOfQ) {
               l = false;
          }
          this.choices = new int[numsOfQ];
          this.topic_index = topic_index;
          this.adjustmentFactor = adjustmentFactor;
          this.random = new Random();
          this.scoreAve = 0;
          this.user = user;
          this.used_questions = new NewQ[numsOfQ];
          System.out.println("Start Testing, Currently Number of Questions: " + numsOfQ);
          Thread.sleep(1000);
     }

     public void start() throws IOException, InterruptedException {
          Scanner sc = new Scanner(System.in);
          for(int i = 0 ; i < this.numsOfQ ; i++){
               int count = 0;
               int difi = choose_difficulty();
               NewQ newQ = null;
               int[] indices;
               Random rand = new Random();
               for (int j = 0; j < 10; j++) {
                    int x = rand.nextInt(this.Questions.length);
                    if (this.Questions[x].difficulty == difi && !labelOfQ[x]){
                         newQ = this.Questions[x];
                         this.used_questions[i] = newQ;
                         labelOfQ[x] = true;
                         break;
                    }
               }
               if (newQ != null) {
                    indices = Questiondatabase.printQuestions(newQ);
               }
               else{
                    count += 1;
                    if (count >10) {
                         System.out.println("No more same difficulty question");
                         return;
                    }
                    i -= 1;
                    continue;
               }
               super.clear();
               boolean flag = false;
               int choice = 0;
               while (!flag) {
                    System.out.print("Enter Anwser (Numbers Only): ");
                    String tmpChoice = sc.next();
                    if (Character.isDigit(tmpChoice.charAt(0))) {
                         choice = Integer.parseInt(tmpChoice);
                         if (choice <= 0 || choice > newQ.options.length) {
                              System.out.println("Invalid Choice");
                         }else{
                              flag = true;
                         }
                    }else{
                         System.out.println("Invalid Choice");
                    }

               }
               choice = indices[choice];
               int Score = 0;
               if (choice == indices[0]){
                    Score = 100;
               }
               this.scoreAve  = (this.scoreAve*i + Score)/(i+1);
               choices[i] = choice;
          }
          LookUpHistory.makeHistory(this.used_questions, this.choices, this.user);
          this.user.save_record(this.topic_index, this.scoreAve);
          System.out.println("Finished Testing, Your score is " + this.scoreAve);
          Thread.sleep(1000);
          clear();
     }

     public int choose_difficulty(){
           double local = 0.6 - (1-this.adjustmentFactor)*(this.scoreAve % 25)/100;
           double other = (1-local)/3;
           int level = (int)this.scoreAve/25;
           double rand = random.nextDouble(0,1);
           if (level == 4){
                return 4;
           }
           double count = 0;
           for (int i = 0; i < 4; i++) {
                if (i == level){
                     count += local;
                }
                else {
                     count += other;
                }
                if (rand < count){
                     return i+1;
                }
           }
           return 1;
     }

}