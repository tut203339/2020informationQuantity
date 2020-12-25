package s4.B203338; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {  // This interface provides the design for frequency counter.
    void setTarget(byte[] target);  // set the data to search.
    void setSpace(byte[] space);  // set the data to be searched target from.
    int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
                     // Otherwise, it return 0, when SPACE is not set or Space's length is zero
                     // Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e. target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/


public class Frequencer implements FrequencerInterface {/*
    // Code to Test, *warning: This code contains intentional problem*
    byte[] myTarget;
    byte[] mySpace;

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }
    @Override
    public void setSpace(byte[] space) {
        mySpace = space;
    }

    @Override
    public int frequency() {
        int targetLength = myTarget.length;
        int spaceLength = mySpace.length;
        int count = 0;
        for(int start = 0; start<spaceLength; start++) { // Is it OK?
            boolean abort = false;
            for(int i = 0; i<targetLength; i++) {
                if(myTarget[i] != mySpace[start+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
        }
        return count;
    }

    // I know that here is a potential problem in the declaration.
    @Override
    public int subByteFrequency(int start, int length) {
        // Not yet implemented, but it is not currently used by anyone.
        return -1;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
        try {
            System.out.println("checking my Frequencer");
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
            System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
            if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP");
        }
    }*/
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.
    private void printSuffixArray() {
      if(spaceReady) {
        for(int i=0; i< mySpace.length; i++) {
          int s = suffixArray[i];
          for(int j=s;j<mySpace.length;j++) {
            System.out.write(mySpace[j]);
          }
          System.out.write('\n');
        }
      }
    }
    private int suffixCompare(int i, int j) {
      String temp = new String(mySpace);
      String suffix_i = new String();
      String suffix_j = new String();
      suffix_i = temp.substring(suffixArray[i]);
      suffix_j = temp.substring(suffixArray[j]);
      //suffix_i = toString(mySpace[suffixArray[i]]);
      //suffix_j = toString(mySpace[suffixArray[j]]);
      for(int k=0;k<Math.min(suffix_j.length(),suffix_i.length());k++){
        if(suffix_i.substring(k,k+1).equals(suffix_j.substring(k,k+1))){
        }
        else{
          return suffix_i.substring(k,k+1).compareTo(suffix_j.substring(k,k+1))/Math.abs(suffix_i.substring(k,k+1).compareTo(suffix_j.substring(k,k+1)));
        }

      }
      if(suffix_i.length()<suffix_j.length())
        return -1;
      return 1;
    }

    public void setSpace(byte []space) {
      mySpace = space; if(mySpace.length>0) spaceReady = true;
      // First, create unsorted suffix array.
       suffixArray=new int[mySpace.length];
      // put all suffixes in suffixArray.
      for(int i = 0; i< mySpace.length; i++) {
        suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
      }
      for(int i = 0; i< mySpace.length-1; i++) {
        for(int j = mySpace.length-1; j>i; j--) {
          if(suffixCompare(j-1,j)==1){
            int temp = suffixArray[j-1];
            suffixArray[j-1]=suffixArray[j];
            suffixArray[j]=temp;
          }
        }
      }
    }
    public void setTarget(byte [] target) {
      myTarget = target; if(myTarget.length>0) targetReady = true;
    }
    public int frequency() {
      if(targetReady == false) return -1;
      if(spaceReady == false) return 0;
      return subByteFrequency(0, myTarget.length);
    }
    public int subByteFrequency(int start, int end) {
      /* This method be work as follows, but much more efficient
      int spaceLength = mySpace.length;
      int count = 0;
      for(int offset = 0; offset< spaceLength - (end - start); offset++) {
      boolean abort = false;
      for(int i = 0; i< (end - start); i++) {
      if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
      }
      if(abort == false) { count++; }
      }
      */
      int first = subByteStartIndex(start, end);
      int last1 = subByteEndIndex(start, end);
      return last1 - first;
    }
    private int targetCompare(int i, int j, int k) {
      String suffix_i = new String(mySpace);
      String target_j_k = new String(myTarget);
      suffix_i = suffix_i.substring(i);
      target_j_k = target_j_k.substring(j,k);
      if(suffix_i.length()<target_j_k.length()){
        return -1;
      }
      else{
        if(suffix_i.startsWith(target_j_k)){
          return 0;
        }
      }
      return 1;
    }
    private int subByteStartIndex(int start, int end) {
      for(int i = 0;i<suffixArray.length;i++){
        if(targetCompare(suffixArray[i],start,end)==0){
          return i;
        }
      }
      return -1;
    }
    private int subByteEndIndex(int start, int end) {
      for(int i = suffixArray.length-1;i>=0;i--){
        if(targetCompare(suffixArray[i],start,end)==0){
          return i+1;
        }
      }
      return -1;
    }
    public static void main(String[] args) {
      Frequencer frequencerObject;

      try {
        frequencerObject = new Frequencer();
        frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
        System.out.println("printing suffix array");
        frequencerObject.printSuffixArray(); // you may use this line for DEBUG
        /* Example from "Hi Ho Hi Ho"
        0: Hi Ho
        1: Ho
        2: Ho Hi Ho
        3:Hi Ho
        4:Hi Ho Hi Ho
        5:Ho
        6:Ho Hi Ho
        7:i Ho
        8:i Ho Hi Ho
        9:o
        A:o Hi Ho
        */
        frequencerObject.setTarget("H".getBytes());

        //
        // **** Please write code to check subByteStartIndex, and subByteEndIndex
        //
        int result = frequencerObject.frequency();
        System.out.print("Freq = "+ result+" ");
        if(4 == result) { System.out.println("OK"); } else
        {System.out.println("WRONG"); }
        frequencerObject.setTarget(" Ho".getBytes());
        result = frequencerObject.frequency();
        if(2 == result) { System.out.println("The Frequency of the starting word    Ho is 2 and it appeared "+result+" times\nOK!"); } else
        {System.out.println("The Frequency of the starting word    Ho is 2 and it appeared "+result+" times\nWRONG"); }
      }
      catch(Exception e) {
        System.out.println("STOP");
      }
    }
}
