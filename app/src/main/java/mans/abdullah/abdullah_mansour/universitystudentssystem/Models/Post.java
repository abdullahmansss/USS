package mans.abdullah.abdullah_mansour.universitystudentssystem.Models;

public class Post
{
    public String userID,username,userimageurl,postcontent,postimageurl;

    public Post()
    {

    }

    public Post(String userID, String username, String userimageurl, String postcontent, String postimageurl)
    {
        this.userID = userID;
        this.username = username;
        this.userimageurl = userimageurl;
        this.postcontent = postcontent;
        this.postimageurl = postimageurl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimageurl() {
        return userimageurl;
    }

    public void setUserimageurl(String userimageurl) {
        this.userimageurl = userimageurl;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public String getPostimageurl() {
        return postimageurl;
    }

    public void setPostimageurl(String postimageurl) {
        this.postimageurl = postimageurl;
    }
}
