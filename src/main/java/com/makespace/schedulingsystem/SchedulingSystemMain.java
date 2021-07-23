package com.makespace.schedulingsystem;

import com.makespace.schedulingsystem.service.SchedulingSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SchedulingSystemMain {
    public static void main(String[] args) throws FileNotFoundException {
        // first check to see if the program was run with the command line argument
        if(args.length < 1) {
            System.out.println("Error, usage: java ClassName inputfile");
            System.exit(1);
        }
        try(Scanner reader = new Scanner(new File(args[0]))){
            SchedulingSystem system = new SchedulingSystem();
            while (reader.hasNext()) {
                String inputLIne = reader.nextLine();
                String[] arr = inputLIne.split("\\s");
                String action = arr[0];
                String startTime = arr[1];
                String endTime = arr[2];
                if (action.equals("BOOK")) {
                    int capacity = Integer.parseInt(arr[3]);
                    String output = system.bookMeetingRoom(startTime, endTime, capacity);
                    System.out.println(output);
                } else if (action.equals("VACANCY")) {
                    String output = system.vacancy(startTime, endTime);
                    System.out.println(output);
                }
            }
        }catch (FileNotFoundException e){
              throw e;
            }
        }
    }

