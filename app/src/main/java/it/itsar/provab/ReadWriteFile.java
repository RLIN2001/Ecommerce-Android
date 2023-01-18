package it.itsar.provab;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteFile {



    protected Boolean scrivi(String fileName, String text, File filesDir) throws IOException {
        File file= new File(filesDir,fileName);
        FileOutputStream stream=null;
        try{
            stream=new FileOutputStream(file,true);
            stream.write(text.getBytes());
            stream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    protected String leggi(String fileName, File filesDir) throws IOException{
        File file= new File(filesDir,fileName);
        int length=(int) file.length();
        byte[] bytes= new byte[length];
        try(FileInputStream in= new FileInputStream(file)){
            int a=in.read(bytes);
            Log.d("result","a: "+a);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return new String(bytes);
    }


    protected Boolean sovrascrivi(File file,String testo){
        File f= new File(file,"carrello");

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(testo);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }





}
