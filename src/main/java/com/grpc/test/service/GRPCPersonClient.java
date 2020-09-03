package com.grpc.test.service;

import com.test.servicet.PersonServiceGrpc;
import com.test.servicet.ServiceTest;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GRPCPersonClient {
    public static void main(String[] args) {
        System.out.println("GRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888)
                .usePlaintext()
                .build();


        // PersonServiceGrpc.PersonServiceFutureStub asyncClient = PersonServiceGrpc.newFutureStub(channel);

         // unaryApi(channel);

        // single request from client , many response from client

         // serverStreaming(channel);

        // many request from client , single response from client

        // clientStreaming(channel);

        // biDirectionalStreaming(channel);

         errorHandlingDemoTest(channel);

         // deadLineDemoTest(channel);


        System.out.println("shutting down channel");
        channel.shutdown();
    }


    private static void unaryApi(ManagedChannel channel) {
        PersonServiceGrpc.PersonServiceBlockingStub syncClient = PersonServiceGrpc.newBlockingStub(channel);

        ServiceTest.PersonReponse response = syncClient.search(
                ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("test first 123")
                        .setLastName(" and 345 test last")
                        .build())
                .build());
        System.out.println("Response from server :: " + response.toString());
    }

    private static void serverStreaming(ManagedChannel channel) {
        PersonServiceGrpc.PersonServiceBlockingStub syncClient = PersonServiceGrpc.newBlockingStub(channel);
        Iterator<ServiceTest.PersonReponse> personReponseIterator = syncClient.searchManyTimes(ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("test first 123")
                        .setLastName(" and 345 test last")
                        .build())
                .build());
        personReponseIterator.forEachRemaining(new Consumer<ServiceTest.PersonReponse>() {
            @Override
            public void accept(ServiceTest.PersonReponse personReponse) {
                System.out.println("Response from server :: " + personReponse.toString());
            }
        });
    }

    // Async client is required as its client streaming
    private static void clientStreaming(ManagedChannel channel) {
        CountDownLatch latch = new CountDownLatch(1);
        PersonServiceGrpc.PersonServiceStub asyncClient = PersonServiceGrpc.newStub(channel);
        StreamObserver<ServiceTest.PersonRequest> requestObserver = asyncClient.clientStreamSearch(new StreamObserver<ServiceTest.PersonReponse>() {

            // react on response from server
            @Override
            public void onNext(ServiceTest.PersonReponse value) {
                System.out.println("Received a response from server = " + value.getResult());
            }

            // react on error from server
            @Override
            public void onError(Throwable t) {

            }

            // server is done sending the data
            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending the data");
                // when server is complete , countdown the latch
            }
        });

        System.out.println("Sending first request");
        requestObserver.onNext(ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("first 111")
                        .setLastName("last 111")
                        .build())
                .build());

        System.out.println("Sending second request");
        requestObserver.onNext(ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("first 222")
                        .setLastName("last 222")
                        .build())
                .build());

        System.out.println("Sending third request");
        requestObserver.onNext(ServiceTest.PersonRequest.newBuilder()
                .setPerson(ServiceTest.PersonData.newBuilder()
                        .setFirstName("first 333")
                        .setLastName("last 333")
                        .build())
                .build());

        // Client is done sending requests
        requestObserver.onCompleted();

        // after this we need to wait for server response using latch else client will just finish executing and come out of method
        // latch is required when we deal with asyn programming
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void biDirectionalStreaming(ManagedChannel channel) {
        CountDownLatch latch = new CountDownLatch(1);
        PersonServiceGrpc.PersonServiceStub asyncClient = PersonServiceGrpc.newStub(channel);
        StreamObserver<ServiceTest.PersonRequest> requestObserver = asyncClient.biDirectionalStreamSearch(new StreamObserver<ServiceTest.PersonReponse>() {
            @Override
            public void onNext(ServiceTest.PersonReponse value) {
                System.out.println("Response from server : " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("first 111", "first 222", "first 333", "first 444", "first 555", "first 666", "first 777", "first 888", "first 999")
                .forEach(name -> {
                    System.out.println("Client Sending request " + name);
                    requestObserver.onNext(ServiceTest.PersonRequest.newBuilder()
                            .setPerson(ServiceTest.PersonData.newBuilder()
                                    .setFirstName(name)
                                    .build())
                            .build());
                    try {

                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        requestObserver.onCompleted();
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void deadLineDemoTest(ManagedChannel channel) {
        PersonServiceGrpc.PersonServiceBlockingStub syncClient = PersonServiceGrpc.newBlockingStub(channel);
        try {
            System.out.println("Sending request to server with  1000 ml deadline");
            ServiceTest.PersonReponse response = syncClient.withDeadline(Deadline.after(1000, TimeUnit.MILLISECONDS))
                    .deadLineDemo(ServiceTest.PersonRequest.newBuilder()
                            .setPerson(ServiceTest.PersonData.newBuilder()
                                    .setFirstName("test first 123")
                                    .setLastName(" and 345 test last")
                                    .build())
                            .build());
            System.out.println("deadline 1000 ml response = " + response.getResult());
        } catch (StatusRuntimeException e) {
            if (e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline has been exceed");
            } else {
                e.printStackTrace();
            }
        }


        // 2nsd call
        try {
            System.out.println("Sending request to server with  100 ml deadline");
            ServiceTest.PersonReponse response = syncClient.withDeadline(Deadline.after(100, TimeUnit.MILLISECONDS))
                    .deadLineDemo(ServiceTest.PersonRequest.newBuilder()
                            .setPerson(ServiceTest.PersonData.newBuilder()
                                    .setFirstName("test first 123")
                                    .setLastName(" and 345 test last")
                                    .build())
                            .build());
            System.out.println("deadline 100 ml response = " + response.getResult());
        } catch (StatusRuntimeException e) {
            if (e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline has been exceed");
            } else {
                e.printStackTrace();
            }
        }
    }


    private static void errorHandlingDemoTest(ManagedChannel channel) {
        PersonServiceGrpc.PersonServiceBlockingStub syncClient = PersonServiceGrpc.newBlockingStub(channel);
        try {
            System.out.println("Sending request to server");
            ServiceTest.PersonReponse response = syncClient.errorHandlingDemo(ServiceTest.PersonRequest.newBuilder()
                    .setPerson(ServiceTest.PersonData.newBuilder()
                            .setFirstName("test first")
                            //.setFirstName("test name")
                            .build())
                    .build());
            System.out.println("Response from server :: " + response.toString());
        } catch (StatusRuntimeException e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
        }
    }
}
