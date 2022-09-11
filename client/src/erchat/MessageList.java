package erchat;


public class MessageList {
    int type;
    long time;
    String content;
    
    public MessageList(int type, long time, String content) {
        this.type = type;
        this.time = time;
        this.content = content;
    }
    
    public long getTime() {
        return time;
    }

    public void setTime(long ime) {
        this.time = time;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
