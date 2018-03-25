package school.realtimesubtitles;

/**
 * Created by Nick-JR on 3/7/2018.
 */

class Microphone {
    private String name;
    private int id;
    private boolean english;
    private boolean active;

    public Microphone(String name, int id){
        this.name = name;
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    protected int getId() {
        return id;
    }

    protected void setEnglish(boolean english) {
        this.english = english;
    }

    protected boolean isEnglish() {
        return english;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected  boolean isActive() {
        return active;
    }
}
