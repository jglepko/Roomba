package edu.depaul.cdm;


import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LogServiceTest {


    /*
        TestLogServiceSetupForFileOverwrite
        Tests writing to a file using the LogService
     */
    @Test
    public void TestLogServiceSetupForFileOverwrite(){

        //Create a logger object
        LogService logService = LogService.getInstance();

        //Create and format a timestamp for the purposes of testing file overwrites
        Date date = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss.sss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String time = format.format(date.getTime());

        //Log the timestamp
        logService.logCustom("file created, time: " + time);

        try{

            //Create a reader for the file
            BufferedReader reader = new BufferedReader(new FileReader("log.txt"));

            try {

                //Read a line from the file and compare it to the line that was written
                String fileIn = reader.readLine();
                assertThat(fileIn, equalTo("file created, time: " + time));

            //Catches an error if the file cannot be read from, fail condition
            } catch (IOException ie) {
                ie.printStackTrace();
                fail();
            }

        //Catches an error if the file doesn't exist, fail condition
        } catch (FileNotFoundException fe){
            fe.printStackTrace();
            fail();
        }

    }

    @Test
    public void TestLogServiceMultipleWriteToFile(){

        //Expected values from reading
        String[] expected = {
                "CleanSweep moved down",
                "CleanSweep moved down",
                "CleanSweep moved left",
                "CleanSweep moved up",
                "The dirt bucket is full",
                "The battery has run out of power",
                "The battery is charging",
                "The battery is full",
                "The dirt bucket has been emptied",
                "CleanSweep moved right",
                "The CleanSweep has finished cleaning"
        };

        //Array to store read lines
        List<String> actual = new ArrayList<>();

        //Create LogService Object
        LogService logService = LogService.getInstance();

        //Write multiple events to file
        logService.logDownMovement();
        logService.logDownMovement();
        logService.logLeftMovement();
        logService.logUpMovement();
        logService.logFullBucket();
        logService.logDepletedBattery();
        logService.logCharging();
        logService.logFullBattery();
        logService.logEmptyBucket();
        logService.logRightMovement();
        logService.logFinishedCycle();

        try{

            //Create a reader for the file
            BufferedReader reader = new BufferedReader(new FileReader("log.txt"));

            try{

                //Read from the file until every line is read
                String fileIn;
                while((fileIn = reader.readLine()) != null){
                    actual.add(fileIn);
                }

                //Check that the correct lines were written
                String[] actualArray = actual.toArray(new String[0]);
                assertThat(actualArray, equalTo(expected));

            //Catch errors from reading, fail condition
            } catch (IOException ie){
                ie.printStackTrace();
                fail();
            }

        //Catch errors from files not existing, fail condition
        } catch (FileNotFoundException fe){
            fe.printStackTrace();
            fail();
        }
    }

}