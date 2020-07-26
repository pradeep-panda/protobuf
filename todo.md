. Why protobuf
. protobuf syntax
. why tag :? The numbered tags are used to match fields when serializing and deserializing the data 
. simple pproto example
. data type and default values
. enum
. inner message declaration
. package 
. import
. serialization and deserialization
. options
    . java_package
    . java_outer_class
    
. Json conversion  : JsonExecution.java

Things to take case during upgrade to newer version of protocol buffer
  . forward compatible ( Write using New protocol -> read using old proto )
  . backward compatible ( Write using old protocol -> read using new proto 
  
  - do not change the tag
          message TestMessage{
            int32 id = 1; 
          }
      
       message TestMessage{
          int32 id=1;
          string name=2 
        }
        
        - New new message is sent to old code,  will ignore "name" as it does not know the tag number
        - Old message is sent to new code , new field will not be found and it will use default value
        
        
. Reserve tag
  - to avoid further use of same tag
  - Necessary to remove conflict in codebase (before remove and after remove)
  - Both tag and filed can be reserved

. OneOf : At most one field will be set at the same time if messge has many fields
. Any : embedded types without having their .proto definition
. Maps : key value pair

. service interface : gRPC -> grpcservices are like api endpoints 
        - Java server can talk to Go client and vice versa as they are sharing same proto files
        - gRPC servers are asynchronous
        - 10 billion gRPC request paer seconds in google (tested)
    Types:
        -Unary
        - Server Streaming  (server sends data as when its available)
        - Client Streaming  (Client sends data in steram manner not at all data one time)
        - Bi-Directional Streaming


Naming convention
------------------
. Use CamelCase for message name
. Use underscore separated names for fields
            "string first_name = 1"
. Use CamelCas of enum and capital with underscopre for values
    enum Test{
        FIRST_VALUE;
        SECONDS_VALUE;
        }  
  
  