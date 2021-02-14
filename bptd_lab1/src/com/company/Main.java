package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        // write your code here
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите размер кода: ");
        int codeLen = scan.nextInt();
        int codLen2 = 0;

        PrintWriter writer = new PrintWriter("output.txt");
        writer.print("");
        writer.close();

        int a[] = readTextInput();

        System.out.println();
        int q = 0;
        for (int i = 0; i < a.length/codeLen; i++) {
            int [] b = new int[codeLen];
            for (int j = 0; j < codeLen; j++){
                b[j] = a[q];
                q++;
            }
            int [] out = generateCode(b);
            codLen2 = out.length;
            String outString = "";
            for(int j = 0; j < out.length;j++){
                outString += Integer.toString(out[j]);
            }
            System.out.println(outString);
            writeText(outString);
        }


        a = readTextOutput();



        int [] out = new int[50000];

        q = 0;
        int k = 0;
        for(int i = 0; i < a.length/codLen2; i++) {
            int [] b = new int[codLen2];
            for(int j = 0; j < codLen2; j++ ){
                b[j] = a[q];
                q++;
            }

            a[codLen2/2] = (a[codLen2/2]+1)%2;

            int [] c;
            c = receive(b, codLen2-codeLen);
            for(int g = 0; g < c.length; g++){
                out[k] = c[g];
                k++;
            }
        }
        writeDecode(out);
    }

    static int[] readTextInput(){
        int [] a;
        try(FileInputStream fin=new FileInputStream("input.txt"))
        {
            System.out.printf("File size: %d bytes \n", fin.available());
            a = new int[fin.available() * 8];
            int j = 0;
            int i=-1;
            while((i=fin.read())!=-1){

                char b = (char) i;
                String buff = Integer.toBinaryString(b);

                while (buff.length()<8){
                    buff = "0" + buff;
                }
                System.out.println(buff);
                int k = 0;

                while (buff.length() > k){
                    a[j] = Integer.parseInt("" + (buff.charAt(k)));
                    k++;
                    j++;
                }
            }
            return a;
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return a = new int[0];
    }

    static int[] readTextOutput(){
        int [] a;
        try(FileInputStream fin=new FileInputStream("output.txt"))
        {
            System.out.printf("File size: %d bytes \n", fin.available());
            a = new int[fin.available()];
            int j = 0;
            int i=-1;
            while((i=fin.read())!=-1){
                a[j] = (i == 48)? 0 : 1;
                j++;
            }
            return a;
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return a = new int[0];
    }

    static void writeText(String text){

        try(FileOutputStream fos=new FileOutputStream("output.txt",true))
        {
            byte[] buffer = text.getBytes();

            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println("The file has been written");
    }

    static void writeDecode(int [] a){

        try(FileOutputStream fos=new FileOutputStream("output.txt"))
        {
            int q = 0;
            for(int i = 0; i  < a.length/8; i++) {
                String buffer = "";
                for(int j = 0; j < 8; j++){
                    buffer += a[q];
                    q++;
                }
                System.out.println(buffer);
                int ascii = Integer.parseInt(buffer,2);
                char ch = (char)ascii;
                fos.write(ch);
            }

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println("The file has been written");
    }

    static int[] generateCode(int a[]) {
        int b[];
        int i=0, parity_count=0 ,j=0, k=0;
        while(i < a.length) {

            if(Math.pow(2,parity_count) == i+parity_count + 1) {
                parity_count++;
            }
            else {
                i++;
            }
        }
        b = new int[a.length + parity_count];

        for(i=1 ; i <= b.length ; i++) {
            if(Math.pow(2, j) == i) {

                b[i-1] = 2;
                j++;
            }
            else {
                b[k+j] = a[k++];
            }
        }
        for(i=0 ; i < parity_count ; i++) {

            b[((int) Math.pow(2, i))-1] = getParity(b, i);
        }
        return b;
    }

    static int getParity(int b[], int power) {
        int parity = 0;
        for(int i=0 ; i < b.length ; i++) {
            if(b[i] != 2) {
                int k = i+1;
                String s = Integer.toBinaryString(k);

                int x = ((Integer.parseInt(s))/((int) Math.pow(10, power)))%10;
                if(x == 1) {
                    if(b[i] == 1) {
                        parity = (parity+1)%2;
                    }
                }
            }
        }
        return parity;
    }

    static int[] receive(int a[], int parity_count) {
        int power;
        int parity[] = new int[parity_count];

        String syndrome = new String();

        for(power=0 ; power < parity_count ; power++) {

            for(int i=0 ; i < a.length ; i++) {

                int k = i+1;
                String s = Integer.toBinaryString(k);
                int bit = ((Integer.parseInt(s))/((int) Math.pow(10, power)))%10;
                if(bit == 1) {
                    if(a[i] == 1) {
                        parity[power] = (parity[power]+1)%2;
                    }
                }
            }
            syndrome = parity[power] + syndrome;
        }

        int error_location = Integer.parseInt(syndrome, 2);
        if(error_location != 0) {
            System.out.println("Error is at location " + error_location + ".");
            a[error_location-1] = (a[error_location-1]+1)%2;
            System.out.println("Corrected code is:");
            for(int i=0 ; i < a.length ; i++) {
                System.out.print(a[a.length-i-1]);
            }
            System.out.println();
        }
        else {
            System.out.println("There is no error in the received data.");
        }


        int [] out = new int[a.length-power];
        int j = 0;
        System.out.println("Original data sent was:");
        power = parity_count-1;
        for(int i=a.length ; i > 0 ; i--) {
            if(Math.pow(2, power) != i) {

                out[out.length-j-1] = a[i-1];
                System.out.print(out[out.length-j-1]);
                j++;
            }
            else {
                power--;
            }
        }
        System.out.println();
        return out;
    }
}
