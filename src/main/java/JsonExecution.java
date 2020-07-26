import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.util.Arrays;

public class JsonExecution {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        ComplexMessageOuterClass.ComplexMessage complexMessage = ComplexMessageOuterClass.ComplexMessage.newBuilder().setAge(12)
                .setFirstName("dummyname")
                .setIsVerified(true)
                .setHeight(12.30f)
                .addAllPhoneNumbers(Arrays.asList("12345", "12345"))  // phone no as list
                //.setPhoneNumbers(1, "1234")  // one by one  (both option is not possible at time)
                //.setPhoneNumbers(2, "123456")
                .setFavColor(ComplexMessageOuterClass.ComplexMessage.FavColor.newBuilder()
                        .setColor(ComplexMessageOuterClass.ComplexMessage.Color.GREEN)
                        .build())
                .setOuterMessage(ComplexMessageOuterClass.OuterMessage.newBuilder()
                        .setContent("content data").build()).build();

        // convert proto object to json
        String jsonString = JsonFormat.printer()
                //.includingDefaultValueFields()
                .print(complexMessage);
        System.out.println("..Json Data...\n ##### \n"+jsonString);

        // convert json to proto object
        ComplexMessageOuterClass.ComplexMessage.Builder complexMsg = ComplexMessageOuterClass.ComplexMessage.newBuilder();
        JsonFormat.parser()
            .ignoringUnknownFields()
            .merge(jsonString, complexMsg);
        System.out.println("\nJson to proto object \n .... \n"+complexMsg.toString());
    }



}
