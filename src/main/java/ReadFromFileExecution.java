import java.io.FileInputStream;
import java.io.IOException;

public class ReadFromFileExecution {
    // reading from the file
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("complex.bin");
            ComplexMessageOuterClass.ComplexMessage complexMessageRead = ComplexMessageOuterClass.ComplexMessage.parseFrom(fileInputStream);

            System.out.println("\n After reading from the file ... \n"+complexMessageRead.toString());
        }catch (
                IOException e){
            e.printStackTrace();
        }

    }

}
