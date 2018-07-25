package com.ossel.petrobot.data;

import java.util.ArrayList;

public class Poll {

    private final int MAX = 10;
    private String question;
    private ArrayList<String> options = new ArrayList<>();
    private int[] votes = new int[MAX];

    public Poll(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int[] getVotes() {
        return votes;
    }

    public void addOption(String option) {
        options.add(option);
    }


    /**
     * called by the user -> option -1
     * 
     * @param option numbers 1 to 9
     */
    public void vote(int option) {
        if (option - 1 < MAX)
            this.votes[option - 1] = this.votes[option - 1] + 1;
    }

    /**
     * 
     * @param index from 0 to 9
     * @param votes the number of votes
     */
    public void setVotes(int index, int votes) {
        if (index < MAX)
            this.votes[index] = votes;
    }

    public String toDisplayString() {
        StringBuilder b = new StringBuilder(question);
        int i = 1;
        for (String option : options) {
            b.append("\n/");
            b.append(i);
            b.append(". ");
            b.append(option);
            b.append(": ");
            b.append(votes[i - 1]);
            i++;
        }
        return b.toString();
    }


    public String toCsvString() {
        StringBuilder b = new StringBuilder(question);
        int i = 0;
        for (String option : options) {
            b.append("\n");
            b.append(option);
            b.append(",");
            b.append(votes[i]);
            i++;
        }
        return b.toString();
    }


}
