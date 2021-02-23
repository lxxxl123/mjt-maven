package chen;

public class ClassUnderTest {


    Collaborator mock;

    public void setListener(Collaborator mock) {
        this.mock = mock;
    }

    public void removeDocument(String does_not_exist) {
        mock.documentRemoved(does_not_exist);
    }

    public byte addDocument(String does_not_exist) {
        return mock.voteForRemoval(does_not_exist);
    }
}
