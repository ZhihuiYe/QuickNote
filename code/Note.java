public class Note
{
    private String title;
    private String createTime;
    private String content;
    
    public Note(String givenTitle, String givenCreateTime, String givenContent)
    {
        title      = givenTitle;
        createTime = givenCreateTime;
        content    = givenContent;
    }//Note
    
    public String getTitle()
    {
        return title;
    }//getTitle
    
    public String getCreateTime()
    {
        return createTime;
    }//getCreateTime
    
    public String getContent()
    {
        return content;
    }//getContent
}//class