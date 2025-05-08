import mpi.MPI;
import java.util.Scanner;
import mpi.*;

public class ArrSum {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);                         // Initializes the MPI environment, Sets up communication and process management.
        int rank = MPI.COMM_WORLD.Rank();      // Unique ID of the current process. Used to determine which chunk of work this process handles.
        int size = MPI.COMM_WORLD.Size();       // Total number of processes in the communicator (COMM_WORLD means all processes).

        int unitsize = 5;
        int root = 0;
        int send_buffer[] = null;               // Holds all data on root (master) process.
        send_buffer = new int[unitsize * size];         
        int recieve_buffer[] = new int[unitsize];       // Holds data for each process (personal memory for each process).
        int new_recieve_buffer[] = new int[size];       // Used by root to collect partial sums from all processes.

        if (rank == root) {                             // Set data for distribution
            int total_elements = unitsize * size;
            System.out.println("Enter " + total_elements + " elements");
            for (int i = 0; i < total_elements; i++) {
                System.out.println("Element " + i + " = " + i);
                send_buffer[i] = i;
            }
        }
        MPI.COMM_WORLD.Scatter(send_buffer, 0, unitsize, MPI.INT, recieve_buffer, 0, unitsize, MPI.INT, root); // Scatter/Distributes unitsize elements of send_buffer from root to each process's recieve_buffer.
        
        for (int i = 1; i < unitsize; i++) {                // Calculate & Stores partial sum in recieve_buffer[0]
            recieve_buffer[0] += recieve_buffer[i];
        }
        System.out.println("Intermediate sum at process " + rank + " is " + recieve_buffer[0]);
        MPI.COMM_WORLD.Gather(recieve_buffer, 0, 1, MPI.INT, new_recieve_buffer, 0, 1, MPI.INT, root); // Gathers one element (recieve_buffer[0]) from each process into new_recieve_buffer on root.

        if (rank == root) {                             // root computes the total sum of all partial results.
            int total_sum = 0;
            for (int i = 0; i < size; i++) {
                total_sum += new_recieve_buffer[i];
            }
            System.out.println("Final sum : " + total_sum);
        }
        MPI.Finalize();         // Finalizes the MPI environment, Cleans up resources and communication channels
    }
}

/*
 * Output :
 * PS C:\Users\HP-PC\Documents\New folder> javac -cp "C:\mpj-v0_44\lib\mpj.jar" ArrSum.java
 * PS C:\Users\HP-PC\Documents\New folder> mpjrun.bat -np 4 -cp "C:\mpj-v0_44\lib\mpj.jar" ArrSum
 
 * MPJ Express (0.44) is started in the multicore configuration
 * Enter 20 elements
 * Element 0 = 0
 * Element 1 = 1
 * Element 2 = 2
 * Element 3 = 3
 * Element 4 = 4
 * Element 5 = 5
 * Element 6 = 6
 * Element 7 = 7
 * Element 8 = 8
 * Element 9 = 9
 * Element 10 = 10
 * Element 11 = 11
 * Element 12 = 12
 * Element 13 = 13
 * Element 14 = 14
 * Element 15 = 15
 * Element 16 = 16
 * Element 17 = 17
 * Element 18 = 18
 * Element 19 = 19
 * Intermediate sum at process 0 is 10
 * Intermediate sum at process 1 is 35
 * Intermediate sum at process 2 is 60
 * Intermediate sum at process 3 is 85
 * Final sum : 190
 * PS C:\Users\HP-PC\Documents\New folder>
 */