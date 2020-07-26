import com.test.mapt.MapTestOuterClass;
import java.util.HashMap;
import java.util.Map;

public class MapTestExecution {
    public static void main(String[] args) {
        Map<String, String> m = new HashMap<>();
        m.put("Rahul","Bangalore");
        m.put("Zahir","Mumbai");
        MapTestOuterClass.MapTest mapTest = MapTestOuterClass.MapTest.newBuilder().putNameVsAddress("Sachin","Mumbai")
                .putNameVsAddress("ganguly","Culcutta")
                .putAllNameVsAddress(m)
                .build();
        System.out.println(".... Map data ... \n"+mapTest.toString());
    }
}
