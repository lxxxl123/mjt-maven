package chen;

import org.easymock.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.*;

//用注解必备
@RunWith(EasyMockRunner.class)
public class TestMain  extends EasyMockSupport {

    /**
     * 指定需要注入mock的对象
     */
    @TestSubject
    private ClassUnderTest classUnderTest;

    /**
     * 指定被注入的mock
     */
    @Mock(fieldName = "mock")
    private Collaborator collaborator;


    @Test
    public void testRemoveNonExistingDocument() {
        // This call should not lead to any notification
        // of the Mock Object:
//        collaborator.documentRemoved("11111");
        expect(collaborator.voteForRemoval("1111")).andReturn((byte) 20);
        collaborator.documentRemoved("1111");
        replayAll();
        classUnderTest.removeDocument("1111");
        System.out.println(classUnderTest.addDocument("1111"));

//        EasyMock.verify(collaborator);
        verifyAll();
    }

    public static void main(String[] args) {
        System.out.println(1234);

    }
}
