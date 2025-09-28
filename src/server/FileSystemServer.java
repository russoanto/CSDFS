package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import shared.fs.FileSystem;
import shared.FileSystemInterface;

public class FileSystemServer {
    public static void main(String[] args) {
        try {
            String rootPath = "../root_file_system"; 
            FileSystem fs = FileSystem.mount(rootPath);

            FileSystemInterface stub = new RemoteFileSystem(fs);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("VirtualFS", stub);

            System.out.println("FileSystem RMI Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
