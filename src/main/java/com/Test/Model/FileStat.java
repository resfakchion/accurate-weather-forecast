package com.Test.Model;

public class FileStat {

    private String name;
    private int countLine;
    private double avgWordInLines;
    private int maxWord;
    private int minWord;

    public FileStat() {
    }

    public FileStat(String name, int countLine, double avgWordInLines, int maxWord, int minWord) {
        this.name = name;
        this.countLine = countLine;
        this.avgWordInLines = avgWordInLines;
        this.maxWord = maxWord;
        this.minWord = minWord;
    }

    public String getName() {
        return name;
    }

    public int getCountLine() {
        return countLine;
    }

    public double getAvgWordInLines() {
        return avgWordInLines;
    }

    public int getMaxWord() {
        return maxWord;
    }

    public int getMinWord() {
        return minWord;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountLine(int countLine) {
        this.countLine = countLine;
    }

    public void setAvgWordInLines(double avgWordInLines) {
        this.avgWordInLines = avgWordInLines;
    }

    public void setMaxWord(int maxWord) {
        this.maxWord = maxWord;
    }

    public void setMinWord(int minWord) {
        this.minWord = minWord;
    }
}
