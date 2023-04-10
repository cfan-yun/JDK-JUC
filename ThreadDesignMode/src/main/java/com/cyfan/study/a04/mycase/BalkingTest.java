package com.cyfan.study.a04.mycase;

public class BalkingTest {

    public static void main(String[] args) {
        DoctorFlashThread doctorFlashThread = new DoctorFlashThread("d:\\tmp","screen.txt");
        doctorFlashThread.setName("doctorFlashThread");
        doctorFlashThread.start();
    }
}
