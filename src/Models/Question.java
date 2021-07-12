/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author midor
 */
public class Question {

    // mấy cái này là gì e?thuôc tính của class question nhưng chỉ đc sử dụng tring class question, 
    private String title;
    private List<String> answers = new ArrayList<String>();
    private int level;

    public String getTitle() {
        return title;
    }

    public String getAnswers(int index) {
        return answers.get(index);
    }

    public int getLevel() {
        return level;
    }

    public Question(String title, String answerCorrect, String otherAnswer1, String otherAnswer2, String otherAnswer3, int level) {
        this.title = title;
        this.answers.add(answerCorrect);
        this.answers.add(otherAnswer1);
        this.answers.add(otherAnswer2);
        this.answers.add(otherAnswer3);
        this.level = level;

    }

    public boolean checkAnswer(String answer) {
        String correctAnswer = getAnswers(0);
        return correctAnswer.compareTo(answer) == 0;
    }
    
    public String getCorrectAnswer() {
        return answers.get(0);
    }

    @Override
    public String toString() {
        return "Cau hoi: " + title + "\n\n\t A." + answers.get(0) + "\n\n\t B." + answers.get(1) + "\n\n\t C."
                + answers.get(2) + "\n\n\t D." + answers.get(3);

    }

}
