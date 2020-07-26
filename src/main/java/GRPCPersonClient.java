import com.test.servicet.PersonServiceGrpc;
import com.test.servicet.ServiceTest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GRPCPersonClient {
    public static void main(String[] args) {
        System.out.println("GRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8888)
                .usePlaintext()
                .build();

        PersonServiceGrpc.PersonServiceBlockingStub syncClient = PersonServiceGrpc.newBlockingStub(channel);

        //PersonServiceGrpc.PersonServiceFutureStub asyncClient = PersonServiceGrpc.newFutureStub(channel);

        ServiceTest.PersonReponse response = syncClient.search(ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("test first 123")
                        .setLastName(" and 345 test last")
                        .build())
                .build());
        System.out.println("Response from server :: "+response.toString());
        System.out.println("shutting down channel");
        channel.shutdown();
    }
}
