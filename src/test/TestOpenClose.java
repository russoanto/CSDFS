package test;

import shared.fs.FileSystem;

public class TestOpenClose {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: TestOpenClose <numWriters> <numReaders>");
            System.exit(1);
        }

        int numWriters = Integer.parseInt(args[0]);
        int numReaders = Integer.parseInt(args[1]);

        FileSystem fs = FileSystem.mount("../root_file_system");

        // reset file
        fs.mknod("/a.txt");

        System.out.println("=== Test con " + numWriters + " writer e " + numReaders + " reader ===");

        // writers
        Runnable writer = () -> {
            boolean ok = fs.write("/a.txt", Thread.currentThread().getName().getBytes());
            System.out.println(Thread.currentThread().getName() + " write ok? " + ok);
        };

        Thread[] writers = new Thread[numWriters];
        for (int i = 0; i < numWriters; i++) {
            writers[i] = new Thread(writer, "Writer-" + i);
        }

        // readers
        Runnable reader = () -> {
            byte[] d = fs.read("/a.txt");
            String content = (d != null) ? new String(d) : "null";
            System.out.println(Thread.currentThread().getName() + " read: " + content);
        };

        Thread[] readers = new Thread[numReaders];
        for (int i = 0; i < numReaders; i++) {
            readers[i] = new Thread(reader, "Reader-" + i);
        }

        // start all
        for (Thread t : writers) t.start();
        for (Thread t : readers) t.start();

        // join all
        for (Thread t : writers) t.join();
        for (Thread t : readers) t.join();

        // check results
        byte[] finalData = fs.read("/a.txt");
        String finalContent = (finalData != null) ? new String(finalData) : null;

        // Verifica: ci deve essere esattamente UN writer riuscito
        int successCount = 0;
        for (int i = 0; i < numWriters; i++) {
            String expected = "Writer-" + i;
            if (expected.equals(finalContent)) {
                successCount++;
            }
        }

        if (finalContent == null) {
            throw new AssertionError("❌ Nessun contenuto scritto nel file!");
        }

        if (successCount != 1) {
            throw new AssertionError("❌ Errore: expected exactly 1 writer success, but got " + successCount +
                    ". Contenuto finale: " + finalContent);
        }

        System.out.println("✅ Test passed. Contenuto finale: " + finalContent);
    }
}
