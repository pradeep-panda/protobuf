import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

// gRPC server
public class GRPCPersonServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("hello server");

        Server server = ServerBuilder.forPort(8888)
                .addService(new GRPCPersonServerImpl())
                .build();
        server.start();

        //  if we do not write it it will start servsser and end the program

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("shutdown huok");
            server.shutdown();
        }));
        server.awaitTermination();
    }
}
