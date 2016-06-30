package grades;

public class Grade {
    // Variables are private so that appropriate checks can be made when another object attempts to set them.
    private String name;
    private int score;
    private int maxScore;

    public Grade(String name, int score, int max) {
        this.name = name;
        this.score = score;
        this.maxScore = max;
    }

    public double computerPercentage() {
        return (double)score / (double)maxScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setMaxScore(int maxScore) {
        if (maxScore < 0) {
            this.maxScore = 0;
        } else {
            this.maxScore = maxScore;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        if (score < 0) {
            this.score = 0;
        } else {
            this.score = score;
        }
    }

    public String toString() {
        // Formats grade to look something like this: "Homework #1 - 9 / 10"
        return name + " - " + score + " / " + maxScore;
    }
}
