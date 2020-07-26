import com.test.servicet.PersonServiceGrpc;
import com.test.servicet.ServiceTest;
import io.grpc.stub.StreamObserver;

public class GRPCPersonServerImpl extends PersonServiceGrpc.PersonServiceImplBase {
    @Override
    public void search(ServiceTest.PersonRequest request, StreamObserver<ServiceTest.PersonReponse> responseObserver) {
        ServiceTest.PersonData person = request.getPerson();
        String result = "Hello "+ person.getFirstName() + " "+person.getLastName();
        ServiceTest.PersonReponse reponse = ServiceTest.PersonReponse.newBuilder().setResult(result).build();

        // async way of sending response
        //for(int i=0;i<10;i++)
            responseObserver.onNext(reponse);

        // complte the RPC call
        responseObserver.onCompleted();
        //super.search(request, responseObserver);
    }
}
