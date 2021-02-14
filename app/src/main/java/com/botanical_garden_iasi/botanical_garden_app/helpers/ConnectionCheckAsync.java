package com.botanical_garden_iasi.botanical_garden_app.helpers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionCheckAsync extends AsyncTask<Void, Void, Boolean> {

    private Consumer mConsumer;

    public interface Consumer {
        void accept(Boolean serverOn);
    }

    public ConnectionCheckAsync(Consumer consumer) {
        mConsumer = consumer;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(Constants.IASI_BOTANICAL_GARDEN_SERVER_ADDR,
                    Constants.IASI_BOTANICAL_GARDEN_SERVER_PORT), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    @Override
    protected void onPostExecute(Boolean serverOn) {
        mConsumer.accept(serverOn);
    }
}

