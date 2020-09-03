package com.grpc.test.service;

import com.test.servicet.PersonServiceGrpc;
import com.test.servicet.ServiceTest;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

// Complete asynchronous
public class GRPCPersonServerImpl extends PersonServiceGrpc.PersonServiceImplBase {

    // Unary API
    @Override
    public void search(ServiceTest.PersonRequest request, StreamObserver<ServiceTest.PersonReponse> responseObserver) {
        ServiceTest.PersonData person = request.getPerson();
        System.out.println("CLient request has reached to server");
        String result = "Hello " + person.getFirstName() + " " + person.getLastName();
        ServiceTest.PersonReponse reponse = ServiceTest.PersonReponse.newBuilder().setResult(result).build();
        // async way of sending response

        responseObserver.onNext(reponse);
        // complte the RPC call
        responseObserver.onCompleted();
        //super.search(request, responseObserver);
    }


    // Server streaming
    @Override
    public void searchManyTimes(ServiceTest.PersonRequest request, StreamObserver<ServiceTest.PersonReponse> responseObserver) {
        ServiceTest.PersonData person = request.getPerson();
        for (int i = 0; i < 10; i++) {
            String result = "Server Steam >> Hello " + person.getFirstName() + " and " + person.getLastName() + " : " + i;
            ServiceTest.PersonReponse response = ServiceTest.PersonReponse.newBuilder().setResult(result).build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

    // Client Streaming
    // client will be sending many request to server as a stream, server returns a implementation how to react to each request from client


    @Override
    public StreamObserver<ServiceTest.PersonRequest> clientStreamSearch(StreamObserver<ServiceTest.PersonReponse> responseObserver) {
        StreamObserver<ServiceTest.PersonRequest> personRequestStreamObserver = new StreamObserver<ServiceTest.PersonRequest>() {
            String result = "";

            // how to react if server receives a new request
            @Override
            public void onNext(ServiceTest.PersonRequest value) {
                result += " Hello " + value.getPerson().getFirstName() + " . ";
            }

            // client sends an error
            @Override
            public void onError(Throwable t) {

            }

            // client is done
            // we need to return response observer
            @Override
            public void onCompleted() {
                System.out.println("Client is done sending all requests");
                responseObserver.onNext(ServiceTest.PersonReponse
                        .newBuilder()
                        .setResult(result)
                        .build());
                responseObserver.onCompleted();
            }
        };

        return personRequestStreamObserver;
    }

    //It is not necessary to respond to each message

    @Override
    public StreamObserver<ServiceTest.PersonRequest> biDirectionalStreamSearch(StreamObserver<ServiceTest.PersonReponse> responseObserver) {

        StreamObserver<ServiceTest.PersonRequest> requestObserver = new StreamObserver<ServiceTest.PersonRequest>() {

            @Override
            public void onNext(ServiceTest.PersonRequest value) {
                String result = " Hello " + value.getPerson().getFirstName() + " . ";
                responseObserver.onNext(ServiceTest.PersonReponse
                        .newBuilder()
                        .setResult(result)
                        .build());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }

    @Override
    public void deadLineDemo(ServiceTest.PersonRequest request, StreamObserver<ServiceTest.PersonReponse> responseObserver) {

        Context context = Context.current();

        try {
            //if (!context.isCancelled()) {
                System.out.println("Sleep for 300 ms");
                Thread.sleep(300l);
//            } else {
//                return;
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Sending response from server");
        ServiceTest.PersonData person = request.getPerson();
        String result = "Hello " + person.getFirstName() + " " + person.getLastName();
        ServiceTest.PersonReponse response = ServiceTest.PersonReponse.newBuilder()
                .setResult(result)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void errorHandlingDemo(ServiceTest.PersonRequest request, StreamObserver<ServiceTest.PersonReponse> responseObserver) {

        if(request.getPerson().getFirstName().equalsIgnoreCase("test name")) {
            ServiceTest.PersonData person = request.getPerson();
            String result = "Hello " + person.getFirstName() + " " + person.getLastName();
            ServiceTest.PersonReponse response = ServiceTest.PersonReponse.newBuilder()
                    .setResult(result)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("first name does not exist")
                    .augmentDescription("Name sent : "+request.getPerson().getFirstName())
                    .asRuntimeException());
        }
    }
}
