/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizCard;

/**
 *
 * @author nomad
 */
public class QuizCard {
    
    private String question;
    private String answer;

    public QuizCard(String q, String a) {
        this.question = q;
        this.answer = a;
    }
    
    /**
     * Get the value of answer
     *
     * @return the value of answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set the value of answer
     *
     * @param answer new value of answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }


    /**
     * Get the value of question
     *
     * @return the value of question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the value of question
     *
     * @param question new value of question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

}
