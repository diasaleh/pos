/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qcri.farasa.pos;

import static com.qcri.farasa.pos.FarasaPOSTagger.binDir;
import com.qcri.farasa.segmenter.ArabicUtils;
import com.qcri.farasa.segmenter.Farasa;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.String.format;
import java.util.ArrayList;

/**
 *
 * @author kareemdarwish
 */
public class FarasaPOS {

    /**
     * @param args the command line arguments
     */
fsdfds
fsdfds
    public static Farasa farasaSegmenter = null;
    public static FarasaPOSTagger farasaPOSTagger = null;

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException, Exception {  

 
   

        System.err.print("Initializing the system ....");

        farasaSegmenter = new Farasa();
        farasaPOSTagger = new FarasaPOSTagger(farasaSegmenter);

        System.err.print("\r");
        System.err.println("System ready!               ");
       
        processBuffer(folder, start,end, farasaPOSTagger, farasaSegmenter);
        
    }   
 
    private static void processBuffer(int folder,int start, int end, FarasaPOSTagger tagger, Farasa farasa) throws FileNotFoundException, InterruptedException, Exception, IOException {
        BufferedReader br;
        String filename;
        String line="";
        String l="";
        for(int i = start;i<=end;i++){
            filename = "/Users/diasaleh/Desktop/GP/t"+folder+"/t"+i+".txt";
            System.out.println(filename);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
            while ((l = br.readLine()) != null) {
                line += l;
            }
            br.close();
        }
        
        String outfile="/Users/diasaleh/Desktop/GP/newPOSresult_1000_21_31.txt";
        String outfile2="/Users/diasaleh/Desktop/GP/newPOSresult_1000_2_21_31.txt";
        BufferedWriter bww = openFileForWriting(outfile2);
        BufferedWriter bw = openFileForWriting(outfile);
 
        line = new String(line.getBytes(),"UTF-8");

            //Sentence s = tagger.tagLine(tokenize(line));
            Sentence s = tagger.tagLine(farasa.segmentLine(line));
            for (Clitic w : s.clitics) {
                //(((w.position.equals("I"))?"#":""))+
            
                bw.write(  w.surface+"&"+ w.guessPOS+"&"  +"\n");
                if(w.genderNumber!=""){
                    bww.write(w.surface +w.genderNumber);
                    bww.write("\n"); 
                }
              

            }
            bw.write("\n");
            bw.flush();
            bww.write("\n");
            bww.flush();
        
    }

    public static BufferedReader openFileForReading(String filename) throws FileNotFoundException {
        BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
        return sr;
    }

    public static BufferedWriter openFileForWriting(String filename) throws FileNotFoundException {
        BufferedWriter sw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename))));
        return sw;
    }   
    
    public static void fixTrainingFile(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), "UTF8"));
        BufferedWriter bw = ArabicUtils.openFileForWriting(filename + ".correct");
        
        ArrayList<String> lines = new ArrayList<String>();
        
        String line = "";
        while ((line = br.readLine()) != null)
        {
            lines.add(line);
        }
        
        for (int i = 0; i < lines.size(); i++)
        {
            line = lines.get(i).trim();
            if (line.equals("���	Y	NOT	O"))
            {
                boolean print = true;
                // check if next line has 
                if (i + 1 < lines.size() && lines.get(i + 1).trim().matches("(��|�|�|�|�|��|��)\tY\tNOT\tPRON"))
                    print = false;
                else if (i > 0 && lines.get(i - 1).trim().matches("[�����]\tY\tNOT\t(PREP|CONJ)") && !lines.get(i+1).trim().contains("PUNC") && !lines.get(i+1).trim().contains("NUM")
                        && !lines.get(i+1).trim().contains("ABBREV"))
                    print = false;
                else if (i > 0 && lines.get(i - 1).trim().matches(".*\tY\tNOT\tNSUFF") && lines.get(i + 1).trim().matches("(��|�|�|�|�|��|��|��|��|��|���|���)\tY\tNOT\tPRON") 
                        && !lines.get(i-1).trim().contains("�") && !lines.get(i-1).trim().startsWith("���\t"))
                    print = false;
                else if (i > 0 && lines.get(i-1).trim().equals("�\tY\tNOT\tNSUFF"))
                    print = false;
                
                if (print)
                    bw.write(line + "\n");
            }
            else
            {
                bw.write(line + "\n");
            }
        }
        bw.close();
    }
    
}
