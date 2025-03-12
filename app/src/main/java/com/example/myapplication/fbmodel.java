package com.example.myapplication;

public class fbmodel {
    private String Title;
    private String Content;


    public fbmodel() {
        //Constructor
    }

    public fbmodel(String Title, String Content){
        this.Title = Title;
        this.Content = Content;
    }
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = Content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
}
