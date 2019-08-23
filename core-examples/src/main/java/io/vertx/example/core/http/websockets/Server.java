package io.vertx.example.core.http.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.example.util.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {
  private List<ServerWebSocket> subscribeList = new ArrayList<>();


  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }


  @Override
  public void start() throws Exception {

    HttpServer httpServer = vertx.createHttpServer();

    httpServer.websocketHandler(ws -> {
//      ws.handler(ws::writeBinaryMessage);
      if(ws.uri().equals("/subscribe"))
      {
        subscribeList.add(ws);
        ws.writeBinaryMessage(Buffer.buffer("Subscribed."));
      }
      else if(ws.uri().startsWith("/packet")){
        String id  = ws.uri().split("/")[2];
        System.out.println("Receive a packet number: " +id);
        ws.writeTextMessage("Received.");
        for(ServerWebSocket socket:subscribeList){
          socket.writeBinaryMessage(Buffer.buffer(id));
          System.out.println("Forward the packet number to "+socket.binaryHandlerID());
        }
        ws.end();

      }

    }).requestHandler(req -> {
      System.out.println(req);
    }).listen(8080);



  }
}
