package com.eilifint.ravimal.youread;

/**
 * Created by Ravimal on 9/24/2016.
 * {@link Category} represents a Category object which are popular among people.
 * It contains a Category title , imageResourceId for that category.
 */
public class Category {

    //category title
    private String mTitle;
    //image resource ID
    private int mImageResourceId;

    /**
     * Create a new category object.
     *
     * @param title           is title for the category
     * @param imageResourceId is ID of image
     */
    public Category(String title, int imageResourceId) {
        this.mTitle = title;
        this.mImageResourceId = imageResourceId;
    }


    /**
     * Return title of the category.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Return imageResourceId of the category.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }
}
