import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private final String noticeString;
    
    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.noticeString = noticeString;
    }
    
    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Tag messageTag) {
        super(messageId, noticeString.length(), messagePerson1, messageTag);
        this.noticeString = noticeString;
    }
 
    @Override
    public String getString() {
        return noticeString;
    }
}
