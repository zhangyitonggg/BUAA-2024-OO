import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Tag tag;

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.tag = null;
    }
    
    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1, Tag messageTag) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 1;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.tag = messageTag;
    }
    
    @Override
    public int getType() {
        return this.type;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public int getSocialValue() {
        return socialValue;
    }
    
    @Override
    public Person getPerson1() {
        return person1;
    }
    
    @Override
    public Person getPerson2() {
        return person2;
    }
    
    @Override
    public Tag getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        }
        return this.id == ((Message) obj).getId();
    }
}
