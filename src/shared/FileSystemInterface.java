package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface FileSystemInterface extends Remote {
    
    boolean mkdir(String path) throws RemoteException;
    boolean mknod(String path) throws RemoteException;
    boolean write(String path, byte[] content) throws RemoteException;
    boolean rmdir(String path) throws RemoteException;
    boolean symlink(String target, String linkPath) throws RemoteException;
    boolean rename(String oldPath, String newPath) throws RemoteException;
    byte[] read(String path) throws RemoteException;
    List<String> readdir(String path) throws RemoteException;
    Map<String, Object> getattr(String path) throws RemoteException;
}
