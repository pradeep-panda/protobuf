import com.test.oneoft.OneOfTestOuterClass;

public class OneOfExecution {
    public static void main(String[] args) {
        OneOfTestOuterClass.OneOfTest oneOfTest = OneOfTestOuterClass.OneOfTest.newBuilder()
                .setId(32)
                //.setProtobf("protobuf")
                .setJson("json")
                //.setProtobf("protobuf")
                .build();
        System.out.println(oneOfTest.toString());
    }
}
