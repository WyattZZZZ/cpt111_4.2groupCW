package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Option;

public abstract class NewQuestion{
    protected String topic;
    protected String question;
    protected int optionnum;
    protected Option[] options;
    protected String[] correctAnswer;
    protected int difficulty;
    public NewQuestion(String question, Option[] options, String[] correctAnswer, int difficulty, String topic) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.optionnum = options.length;
        this.topic = topic;
    }
}
