package com.eilifint.ravimal.youread;

/**
 * Created by Ravimal on 9/24/2016.
 * {@link News} represents a news object .
 * It contains a Category mWebTitle , mWebUrl,mTrailTex,mThumbnailUrl,mAuthor,mWebPublicationDate for that category.
 */
public class News {
    //new title
    private String mWebTitle;
    //url of the news
    private String mWebUrl;
    //trail text
    private String mTrailTex;
    //thumbnail url
    private String mThumbnailUrl;
    //author of the news
    private String mAuthor;
    //date
    private String mWebPublicationDate;

    /**
     * Create a new category object.
     *
     * @param webTitle           is title for the news
     * @param webUrl             is url of the news
     * @param trailTex           is trail text of the news
     * @param thumbnailUrl       is thumbnail url of the news
     * @param author             is the author
     * @param webPublicationDate is the published date
     */
    public News(String webTitle, String webUrl, String trailTex, String thumbnailUrl, String author, String webPublicationDate) {
        this.mWebTitle = webTitle;
        this.mWebUrl = webUrl;
        this.mTrailTex = trailTex;
        this.mThumbnailUrl = thumbnailUrl;
        this.mAuthor = author;
        this.mWebPublicationDate = webPublicationDate;
    }

    /**
     * Return mWebTitle of the news.
     */
    public String getWebTitle() {
        return mWebTitle;
    }

    /**
     * Return mWebUrl of the news.
     */
    public String getWebUrl() {
        return mWebUrl;
    }

    /**
     * Return mTrailTex of the news.
     */
    public String getTrailTex() {
        return mTrailTex;
    }

    /**
     * Return mThumbnailUrl of the news.
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    /**
     * Return mAuthor of the news.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Return mWebPublicationDate of the news.
     */
    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }
}
