package abcd.com.spinner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static final int SocketServerPORT = 8080;

    TextView infoIp, infoPort, News;

    String Log = "";

    List<mClient> userList;
    ServerSocket serverSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoIp = (TextView) findViewById(R.id.infoip);
        infoPort = (TextView) findViewById(R.id.infoport);
        News = (TextView) findViewById(R.id.news);

        infoIp.setText(getIpAddress());

        userList = new ArrayList<mClient>();

        ServerThread mchatServerThread = new ServerThread();
        mchatServerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ServerThread extends Thread {

        @Override
        public void run() {
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        infoPort.setText(serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    mClient client = new mClient();
                    userList.add(client);
                    ConnectThread connectThread = new ConnectThread(client, socket);
                    connectThread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private class ConnectThread extends Thread {

        Socket socket;
        mClient connectClient;
        String newsToSend = "";

        ConnectThread(mClient client, Socket socket){
            connectClient = client;
            this.socket= socket;
            client.socket = socket;
            client.mThread = this;
        }

        @Override
        public void run() {
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String n = dataInputStream.readUTF();

                connectClient.name = n;
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });


                while (true) {
                    if (dataInputStream.available() > 0) {
                        String newMsg = dataInputStream.readUTF();


                        Log += n + ": " + newMsg;
                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                News.setText(Log);
                            }
                        });

                        broadcastMsg(n + ": " + newMsg);
                    }

                    if(!newsToSend.equals("")){
                        dataOutputStream.writeUTF(newsToSend);
                        dataOutputStream.flush();
                        newsToSend = "";
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }

        }

        private void sendNews(String msg){
            newsToSend = msg;
        }

    }

    private void broadcastMsg(String msg){
        for(int i=0; i<userList.size(); i++){
            userList.get(i).mThread.sendNews(msg);
            Log +=  userList.get(i).name + "\n";
            Notify();
        }

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                News.setText(Log);
            }
        });
    }

    private void Notify() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Notification notification = new Notification(R.drawable.notification_template_icon_bg,"you got new post", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,NotificationView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(MainActivity.this,"spinner","you got new post", pendingIntent);
        notificationManager.notify(9999, notification);
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    class mClient {
        String name;
        Socket socket;
        ConnectThread mThread;

    }

}
