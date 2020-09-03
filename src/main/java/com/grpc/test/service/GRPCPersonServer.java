package com.grpc.test.service;

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

        //  if we do not write this , it will start server and immediately exit

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("shutdown huok");
            server.shutdown();
        }));
        server.awaitTermination();
    }
}
