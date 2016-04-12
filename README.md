I have approached problem by creating multi-client server connection using java socket object to listen on a specific port.
The app starts with action bar activity where user needs to type username and ipaddress of the network they are connected.
If the server successfully binds to its port, then the ServerSocket object is successfully created and the server continues to the next step—accepting a connection from a client.
In a separate thread, server is made to wait for client requests to avoid disturbing the progression of main thread code.
I.P address is obtained through getipaddress method and once clients with same i.p address request is recieved,
socket = serverSocket.accept();
socket server starts to accept the clients and respective clients are appended to userlistview.
Multiple clients connection is achieved by following logic-
while (true) {
    accept a connection;
}
Once,client and server is connected connectthread method is called,where output and input stream writers are used to post news and read it.
Whenever,a news is post by the user,Notification method(with notification manager) is called which gives notification for post in status bar.
The posts are appended to listview which is open to all who have installed the apps and are interconnected.
override onStop() method is written to close the serverSocket,if not,upon stopping the Activity it will actually continue to listen for incoming clients .
Thus news updates posted by any user is viewed  by all others, displayed in listview whenever the app is opened and notification is received for each post.
# spinner10
