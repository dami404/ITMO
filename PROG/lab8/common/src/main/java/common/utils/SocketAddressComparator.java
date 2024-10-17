package common.utils;

import java.net.InetSocketAddress;

public class SocketAddressComparator {
    public static boolean equals(InetSocketAddress a, InetSocketAddress b){
        if(a.getAddress().equals(b.getAddress()) && a.getPort()==b.getPort()) return true;
        return false;
    }
}
