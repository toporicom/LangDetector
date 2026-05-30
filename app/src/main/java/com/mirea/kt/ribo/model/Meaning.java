package com.mirea.kt.ribo.model;

public class Meaning {
    private int id;
    private String word;
    private String partOfSpeech;
    private String definition;
    private String example;
    private int favorite;

    public Meaning(String word, String partOfSpeech, String definition, String example, int favorite) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
        this.favorite = favorite;
    }

    public Meaning(int id, String word, String partOfSpeech, String definition, String example, int favorite) {
        this.id = id;
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}