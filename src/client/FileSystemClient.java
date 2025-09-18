package client;

import shared.FileSystemInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class FileSystemClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            FileSystemInterface fs = (FileSystemInterface) registry.lookup("VirtualFS");

            Scanner scanner = new Scanner(System.in);
            System.out.println("RMI File System Client");

            while (true) {
                System.out.print(">>> ");
                String input = scanner.nextLine().trim();

                if (input.equals("exit")) break;

                String[] tokens = input.split(" ");
                if (tokens.length == 0) continue;

                switch (tokens[0]) {
                    case "mkdir":
                        if (tokens.length != 2) {
                            System.out.println("Usage: mkdir /path");
                        } else {
                            boolean ok = fs.mkdir(tokens[1]);
                            System.out.println(ok ? "Directory created" : "Failed to create directory");
                        }
                        break;

                    case "mknod":
                        if (tokens.length != 2) {
                            System.out.println("Usage: mknod /file");
                        } else {
                            boolean ok = fs.mknod(tokens[1]);
                            System.out.println(ok ? "File created" : "Failed to create file");
                        }
                        break;
                    case "symlink":
                        if (tokens.length != 3) {
                            System.out.println("Usage: symlink <target> <linkPath>");
                        } else {
                            boolean ok = fs.symlink(tokens[1], tokens[2]);
                            System.out.println(ok ? "Symlink created" : "Failed to create symlink");
                        }
                        break;
                    case "write":
                        if (tokens.length < 3) {
                            System.out.println("Usage: write /file content with spaces");
                        } else {
                            String path = tokens[1];
                            String content = input.substring(input.indexOf(path) + path.length()).trim();
                            boolean ok = fs.write(path, content.getBytes());
                            System.out.println(ok ? "Written" : "Write failed");
                        }
                        break;
		    case "rmdir":
			if (tokens.length < 2) {
			    System.out.println("Usage: rmdir <path>");
			} else {
			    String path = tokens[1];
			    boolean ok = fs.rmdir(path);
			    System.out.println(ok ? "Written" : "Write failed");
			}
			break;

		case "read":
                        if (tokens.length != 2) {
                            System.out.println("Usage: read /file");
                        } else {
                            byte[] data = fs.read(tokens[1]);
                            System.out.println(data != null ? new String(data) : "File not found or empty");
                        }
                        break;

                    case "ls":
                        if (tokens.length != 2) {
                            System.out.println("Usage: ls /dir");
                        } else {
                            for (String name : fs.readdir(tokens[1])) {
                                System.out.println("- " + name);
                            }
                        }
                        break;
		    case "edit":
			if (tokens.length != 2) {
			    System.out.println("Usage: edit /path/to/file");
			} else {
			    editRemoteFile(fs, tokens[1]);
			}
			break;
		    case "rename":
			if(tokens.length != 3){
			    System.out.println("Usage: rename <oldPaht> <newPaht>");
			}else{
			    fs.rename(tokens[1], tokens[2]);
			}
			break;
	      
                    default:
                        System.out.println("[!] Command not recognized. Try: mkdir, mknod, symlink, write, read, ls, exit");
                        break;
                }
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void editRemoteFile(FileSystemInterface fs, String remotePath) {
	try {
	    // Step 1: Read remote content
	    byte[] content = fs.read(remotePath);
	    File tempFile = File.createTempFile("remote_edit_", ".tmp");

	    // Step 2: Write to local temp file
	    if (content != null) {
		Files.write(tempFile.toPath(), content);
	    }

	    // Step 3: Launch the editor
	    System.out.println("Opening editor for: " + remotePath);
	    Process editor = new ProcessBuilder("vim", tempFile.getAbsolutePath()).inheritIO().start();
	    editor.waitFor();

	    // Step 4: Read edited content
	    byte[] newContent = Files.readAllBytes(tempFile.toPath());

	    // Step 5: Write back to remote
	    boolean success = fs.write(remotePath, newContent);
	    System.out.println(success ? "File updated." : "Failed to write back.");

	    tempFile.deleteOnExit();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
