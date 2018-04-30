package com.helospark.FakeSsh;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import com.helospark.lightdi.annotation.Autowired;
import com.helospark.lightdi.annotation.Component;
import com.helospark.lightdi.annotation.Value;

/**
 * Service to check whether it is allowed for a SSH connection to be established.
 * @author helospark
 */
@Component
public class ConnectionPermissionCheckingService {
    // LightDi 0.0.3 will support long as well.
    // I could write a custom converter, but 2 billion connection per IP should be enough
    private int numberOfConnectionAllowedFromSameIp;

    @Autowired
    public ConnectionPermissionCheckingService(@Value("${MAX_NUMBER_OF_CONNECTION_FROM_SAME_IP}") int numberOfConnectionAllowedFromSameIp) {
        this.numberOfConnectionAllowedFromSameIp = numberOfConnectionAllowedFromSameIp;
    }

    /**
     * Check whether to allowed the given socket to connect.
     * If there are too many connections from the same IP, the connection will be rejected
     * @param connections already established connections
     * @param socket to check whether allowed to connect
     * @return true if allowed, false otherwise
     */
    public boolean isConnectionAllowed(List<SshConnection> connections, Socket socket) {
        long numberOfConnectionsFromThisIp = connections.stream()
                .filter(connection -> isTwoConnectionReferToSameIp(socket, connection))
                .count();
        return numberOfConnectionsFromThisIp < numberOfConnectionAllowedFromSameIp;
    }

    private boolean isTwoConnectionReferToSameIp(Socket socket, SshConnection connection) {
        return Arrays.equals(getInetAddress(connection).getAddress(), socket.getInetAddress().getAddress());
    }

    private InetAddress getInetAddress(SshConnection connection) {
        return connection.getConnection().getSocket().getInetAddress();
    }
}
