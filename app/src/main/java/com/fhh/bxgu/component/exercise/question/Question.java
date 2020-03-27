package com.fhh.bxgu.component.exercise.question;

public class Question {
    private String title;// 每道习题的题干
    private String a;// 每道题的A选项
    private String b;// 每道题的B选项
    private String c;// 每道题的C选项
    private String d;// 每s道题的D选项
    private int answer;// 每道题的正确答案
    private int select;// 用户选中的那项（0表示所选项对了，1表示A选项错，2表示B选项错，3表示C选项错，4表示D选项错）

    String getTitle() {
        return title;
    }

    void setTitle(String subject) {
        this.title = subject;
    }

    String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public int getAnswer() {
        return answer;
    }

    void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
