package com.harris.cinnato.outputs;




import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Async_Test {

    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // Simulate a long-running task
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, async!";
        });

        // Perform some other tasks while waiting for the future to complete

        // Example 1: Blocking call to get the result
        try {
            String result = future.get(); // This call blocks until the future completes
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Example 2: Non-blocking callback when the future completes
        future.thenAccept(result -> System.out.println(result));

        // Perform some other tasks while the future is still running

        // Example 3: Non-blocking call to check if the future is done
        boolean isDone = future.isDone();
        System.out.println("Future done? " + isDone);
    }
}



























    
}
