import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Execution {
    public static void main(String[] args) {

        ComplexMessageOuterClass.ComplexMessage complexMessage = ComplexMessageOuterClass.ComplexMessage.newBuilder()
                .setAge(12)
                .setFirstName("dummyname")
                .setIsVerified(true)
                .setHeight(12.30f)
                .addAllPhoneNumbers(Arrays.asList("12345", "12345"))  // phone no as list
                //.setPhoneNumbers(0, "1234")  // one by one  (both option is not possible at time)
                //.setPhoneNumbers(1, "123456")
                .setFavColor(ComplexMessageOuterClass.ComplexMessage.FavColor.newBuilder()
                        .setColor(ComplexMessageOuterClass.ComplexMessage.Color.GREEN)
                        .build())
                .setOuterMessage(ComplexMessageOuterClass.OuterMessage.newBuilder()
                        .setContent("content data").build()).build();

        // Write to a file/network
        try {
            System.out.println("Before writing to file .... \n"+complexMessage.toString());
            FileOutputStream fileOutputStream = new FileOutputStream("complex.bin");
            //fileOutputStream.write(complexMessage.toByteArray());
            complexMessage.writeTo(fileOutputStream);
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
   // use complexMessage.toByteArray() to send the data over the network

    }
}
