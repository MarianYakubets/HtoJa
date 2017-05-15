package com.htoja.mifik.htoja.expansion;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * Created by mi on 5/15/2017.
 */
public class DownloaderServiceImpl extends DownloaderService {
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm8Y70TMFoG5okbNmccJpOU8sMn4KOS1gMkWs5eFdWYZyv7YXrGg/1NIP+Mh4bUtxYulZUa3Nxy3u/YRDrQTOtx075+GryjP54hPQFGPkGWZ1AMq4vgkpm9DWBt93UANSvsaWoWWcJOPBwdXK70GQliYtkkz6YP/kEpaq6Xir99ASKxNH22d857V7B09dNF4aWCxusJ+F+g1VANt4T12JjXKWjvLCjmgKr5YrCuktfyr+xqd2DGUnqw4Xfflm0MU2+gB1zVTqPAXMSJ8Q6uWQdlSY102nngaEFY1+VEf+voMWm7uD7cfs/9tqCHvG2MsHwRY9UaXHM6WspcqwY+R7JwIDAQAB"; // TODO Add public key
    private static final byte[] SALT = new byte[]{1, 4, -1, -1, 14, 42, -79, -21, 13, 2, -8, -11, 62, 1, -10, -101, -19, 41, -12, 18}; // TODO Replace with random numbers of your choice. (it is just to ensure that the expansion files are encrypted with a unique key from your app)

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return DownloaderServiceBroadcastReceiver.class.getName();
    }
}
