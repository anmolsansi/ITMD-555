package com.example.moviereview.model;

public class SliderItems {

//    private int image;
    private String posterImageUrl;

//    public SliderItems(int image) {
//        this.image = image;
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }


    public SliderItems(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }
}
