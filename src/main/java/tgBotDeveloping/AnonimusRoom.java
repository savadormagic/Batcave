package tgBotDeveloping;

import org.telegram.telegrambots.meta.api.objects.User;

public class AnonimusRoom {
    private User first;
    private User second;

    public boolean isFull() {
        return first != null && second != null;
    }



    public boolean setUser(User user) {

        if (isFull()) {
            return false;
        }
        if (first == null && second == null) {
            System.out.println("first user is here");
            first = user;
            System.out.println(first.getId());
            return true;
        }
        if (first != null && second == null) {
            System.out.println("second user is here");
            second = user;
            System.out.println(second.getId());
            return true;
        }
        return false;
    }
    public String getNameFirst(){
        return first.getFirstName();
    }
    public String getNameSecond(){
        return second.getFirstName();
    }

    public boolean removeUsers(User user){
        if (isFull()) {
            return true;
        }
        if(first != null && second != null){
            first = null;
            return true;
        }
        if(first == null && second != null){
            second = null;
            return true;
        }
        return false;
    }


    public long getIdFirst(){
        return first.getId();
    }
    public long getIdSecond(){
        return second.getId();
    }

}