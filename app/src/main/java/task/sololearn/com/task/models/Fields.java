package task.sololearn.com.task.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Fields extends RealmObject {

    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("standfirst")
    @Expose
    private String standfirst;
    @SerializedName("trailText")
    @Expose
    private String trailText;
    @SerializedName("firstPublicationDate")
    @Expose
    private String firstPublicationDate;
    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("bodyText")
    @Expose
    private String bodyText;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getStandfirst() {
        return standfirst;
    }

    public void setStandfirst(String standfirst) {
        this.standfirst = standfirst;
    }

    public String getTrailText() {
        return trailText;
    }

    public void setTrailText(String trailText) {
        this.trailText = trailText;
    }

    public String getFirstPublicationDate() {
        return firstPublicationDate;
    }

    public void setFirstPublicationDate(String firstPublicationDate) {
        this.firstPublicationDate = firstPublicationDate;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

}