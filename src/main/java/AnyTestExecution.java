import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.test.anyt.AnyTestOuterClass;

public class AnyTestExecution {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        TestMessageOuterClass.TestMessage testMessage = TestMessageOuterClass.TestMessage.newBuilder()
                .setAge(10)
                .setFirstName("dummy name")
                .setLastName("summy lastname")
                .build();
        ComplexMessageOuterClass.ComplexMessage complexMessage = ComplexMessageOuterClass.ComplexMessage.newBuilder()
                .setAge(20)
                .setFirstName("complex name")
                .setFavColor(ComplexMessageOuterClass.ComplexMessage.FavColor.newBuilder()
                        .setColor(ComplexMessageOuterClass.ComplexMessage.Color.RED)
                        .build())
                .build();
        AnyTestOuterClass.AnyTest anyTest =
                AnyTestOuterClass.AnyTest
                        .newBuilder()
                        .setMsg("hello")
                        .setContent(Any.pack(testMessage))
                        .build();



        AnyTestOuterClass.AnyTest anyTes2 =
                AnyTestOuterClass.AnyTest
                        .newBuilder()
                        .setMsg("hello seconds")
                        .setContent(Any.pack(complexMessage))
                        .build();

        System.out.println("Any without unpack - test message \n ....\n"+anyTest.toString());
        TestMessageOuterClass.TestMessage testMessageUnpack = anyTest.getContent().unpack(TestMessageOuterClass.TestMessage.class);
        System.out.println("\n Any with test message unpack \n ....\n"+testMessageUnpack.toString());

        ComplexMessageOuterClass.ComplexMessage complexMessageUnpack = anyTes2.getContent().unpack(ComplexMessageOuterClass.ComplexMessage.class);
        System.out.println("\n Any with complex message unpack \n ....\n"+complexMessageUnpack.toString());
    }
}
