// read lines, write lines
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class W6FileIO {

public static final Scanner in = new Scanner(System.in);

public static void main(String[] arg)
{
    System.out.print("Enter name of input file: ");
    String inName = in.nextLine();
    
    Scanner fin = null;
    
    try{
        fin = new Scanner( new BufferedReader( new FileReader( // FileNotFoundException
            inName)));
    }
    catch(FileNotFoundException e) 
    { 
        die("can't open " + inName); 
    } // input file is open

    System.out.print("name of output file: ");
    String outName = in.nextLine();

    PrintWriter fout = null;

    try 
    {
        fout = new PrintWriter(new BufferedWriter(new FileWriter(    // IOException, destructive
        outName)));
    } 
    catch(IOException e) 
    {
        die("can't open " + outName);
    }

    int linesRead = 0;

    while(fin.hasNextLine())
    {
        String line = fin.nextLine();
        linesRead++;
        ALString t = processLine(line);
    
        if(t.ok)
        {
            for(int i = 0; i < t.al.size(); i++)
                if(i == t.al.size()-1)
                    fout.print("  <" + t.al.get(i) +">");
                else
                    fout.print("  " + t.al.get(i));        
          
                fout.println(t.comment);
        } 
        else 
        {
            fout.println("BOGUS LINE #" + linesRead +": " + line);
        }
    }

    fout.close();
    fin.close();

    System.out.println(pl(linesRead, "line") + " read");

} // main

public static ALString processLine(String line)
{
    ALString ret = new ALString();
    int pos = line.indexOf("//");
    
    if(pos == -1)
        pos = line.length();
    
    ret.comment = line.substring(pos);
    line = line.substring(0, pos);

    ret.al = new ArrayList<Double>();

    Scanner sin = new Scanner(line);
    
    while(sin.hasNextInt()) 
    {
        double x = sin.nextInt();
        if(x >= 0)
            ret.al.add(x);
    }
    
    if(sin.hasNext())
    {
        ret.ok = false;
        return ret;
    } // now do square & average

    double sum = 0;

    for(int i = 0; i < ret.al.size(); i++)
    {
        double sq = ret.al.get(i);
        sq *= sq;
        sum += sq;
        ret.al.set(i, sq);
    }
    
    if(ret.al.size() > 0) 
        ret.al.add(sum/ret.al.size());
    
    ret.ok = true;
    return ret;
}

public static String pl(int howMany, String what) 
{
    String ret = howMany + " " + what;
    if(howMany != 1)
        ret += 's';
    return ret;
} // pl

public static void waitForEnter() 
{ 
    System.out.print("Waiting...");
    in.nextLine();
} // wait

public static void die(String msg) 
{ 
    System.out.println("Fatal error: " + msg);
    System.exit(1);
} // die} 
// class W6FileIO
// not an inner class

class ALString 
{
    public boolean ok;
    public ArrayList<Double> al;
    public String comment;
}