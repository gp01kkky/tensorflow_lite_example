package org.tensorflow.lite.examples.detection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.util.Properties;

public class SFTPClient extends AsyncTask<Void, Void, Void> {
    private boolean isuploaded;
    private String TAG ="SFTPClient";
    private String username = "sftpuser", hostname = "192.168.21.194", passwd="q1w2e3r4";
    private int portnumber = 22;
    private ChannelSftp sftp;
    private String source = "";
    private String destination = "";

    public SFTPClient (String username, String hostname, int portNumber, String passwd, String source, String destination){
        Log.i(TAG, "Setting information... ");
        this.username = username;
        this.hostname = hostname;
        this.portnumber = portNumber;
        this.passwd = passwd;
        this.source = source;
        this.destination = destination;
    }
    public SFTPClient(){

    }

    private boolean conStatus = false;

    @Override
    protected Void doInBackground(Void... voids) {

        // TODO Auto-generated method stub
        Session session = null;
        Channel channel = null;
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        Log.i("Session", "is " + conStatus);
        try {
            // this is the example code with jsch version 0.1.54
            JSch ssh = new JSch();
            session = ssh.getSession(this.username, this.hostname, this.portnumber);
            session.setPassword(this.passwd);
            session.setConfig(config);
            session.connect();
            conStatus = session.isConnected();
            Log.i("Session", "is " + conStatus);
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.put(source, destination);
            isuploaded = true;

            //trying to make it simpler
            //makeConnection(session, config, channel);
            //upload("/sdcard/movie.mp4", "/data/sftpuser/upload");
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Session", "is" + conStatus);
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Session", "is" + conStatus);
        } finally {
            if (isuploaded) {
                Log.i(TAG, "Upload successful!");
                channel.disconnect();
                session.disconnect();
            }
        }
        return null;
    }

    public void makeConnection(Session session, Properties config, Channel channel) throws JSchException{
        JSch ssh = new JSch();
        session = ssh.getSession(this.username, this.hostname, this.portnumber);
        session.setPassword(this.passwd);
        session.setConfig(config);
        session.connect();
        conStatus = session.isConnected();
        Log.i("Session", "is " + conStatus);
        channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
    }

    public void upload (String src, String dest){
        try {
            sftp.put(src, dest);
            isuploaded = true;
        }catch (SftpException e){
            e.printStackTrace();
        }
    }
}
