import websocket, json

from websocket import create_connection

ws = create_connection("ws://localhost:8080/subscribe")

ws.send("sss")

while True:
    d = ws.recv()
    print(d)
