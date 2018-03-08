package school.realtimesubtitles;

/**
 * Created by Nick-JR on 3/7/2018.
 */

class Message {

    private Microphone microphone;
    private String text;
    //private DateTime dateTime;

    public Message(Microphone microphone, String text){
        this.microphone = microphone;
        this.text = text;
    }

    protected Microphone getMicrophone(){
        return microphone;
    }

    protected String getText(){
        return text;
    }
}
