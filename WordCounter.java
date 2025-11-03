import java.util.*;
import java.io.*;
import java.util.regex.*;


public class WordCounter {


public static int processText (StringBuffer text, String stopWord) throws InvalidStopwordException, TooSmallText {

String str= text.toString(); 

Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
Matcher matcher = regex.matcher(str);

ArrayList<String> words = new ArrayList<>();

boolean found = false;
while (matcher.find()) {
    words.add(matcher.group()); 
}
if (words.size() < 5) {
        throw new TooSmallText("Only found " + words.size() + " words.");
}

    // 3) no stopword return total
    if (stopWord == null) {
        return words.size();
    }

    // find stopword position & get count
    for (int i = 0; i < words.size(); i++) {
        if (words.get(i).equals(stopWord)) {
            return i + 1; // count is index+1
        }
    }

    // 5) not found
    throw new InvalidStopwordException("Couldn't find stopword: " + stopWord);


}

// a method called processFile that expects a String path as an argument, 
//and converts the contents of the file to a StringBuffer, which it returns.
// If the file cannot be opened, it should prompt the user to re-enter the filename 
// until they enter a file that can be opened. If the file is empty, this method
// should raise an EmptyFileException that contains the file’s path in its message.

public static StringBuffer processFile (String path) throws EmptyFileException {

//convert file to stringbuffer
// StringBuffer s = new StringBuffer(path);
// return s; 


Scanner s = new Scanner(System.in);//creates scanner to read input 
File file = new File(path); //creates file object from path string

while (!file.exists()) {  // check if file exists
    System.out.println("File not found. Re-enter file path:");
    path = s.nextLine(); 
    file = new File(path);
}

StringBuffer buffer = new StringBuffer();//creates an empty StringBuffer to store the text from the file
try {
    Scanner fileScanner = new Scanner(file); //creates scanner for actual file to read contents

while (fileScanner.hasNextLine()) { 
    buffer.append(fileScanner.nextLine()); //add file lines to the buffer
    if (fileScanner.hasNextLine()) buffer.append("\n");
}
fileScanner.close(); 
} catch (FileNotFoundException e){
    return new StringBuffer();
}

if (buffer.length() == 0) {//check if file is tmpty 
    throw new EmptyFileException(path + " was empty");
}

return buffer;


}

public static void main(String[] args) {  

StringBuffer textBuffer = new StringBuffer();
String stopWord = null;
// a main method that asks the user to choose to process a file with option 1,
// or text with option 2. If the user enters an invalid option, 
//allow them to choose again until they have a correct option. Both of these
// items will be available as the first command line argument. It then checks to
// see if there is a second command line argument specifying a stopword. 

Scanner keyboard = new Scanner(System.in);
System.out.println("Process a file with either option 1, or text with option 2");
int option = keyboard.nextInt();

while(option != 1 && option != 2){
    System.out.println("Please enter valid option");
    option = keyboard.nextInt();
    keyboard.nextLine();
}

  if (option == 1) {
     String path;
    if (args.length > 0) {
            path = args[0];
    } else {
        System.out.println("Enter file path: ");
         path = keyboard.nextLine();
    }

    try {
    textBuffer = processFile(path);
    } catch (EmptyFileException e) {
    System.out.println(e.getMessage());
    }

} else {
    System.out.println("Enter text: ");
    String input = keyboard.nextLine();
    textBuffer.append(input);
}

//Note that the path of the empty file may not be the same path that was 
//specified in the command line by the time this exception is raised.
// If the stopword wasn’t found in the text, allow the user one chance to re-specify the stopword 
//and try to process the text again. If they enter another stopword that can’t be found, 
//report that to the user.

 if(args.length > 1){
    stopWord = args[1];
}


// The method then calls the methods above to process the text, and outputs the
 //number of words it counted. If the file was empty, this method will display the
// message of the exception raised (which includes the path of the file), and then 
//continue processing with an empty string in place of the contents of the file 
 // (note that this will raise a TooSmallText exception later).


try{
    int count = processText(textBuffer, stopWord);
    System.out.println("Found " + count + " words.");
} catch(InvalidStopwordException e) {
    // allow one retry
    System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    System.out.println("Enter stopword again:");
    stopWord = keyboard.nextLine();
    
    try {
    int count = processText(textBuffer, stopWord);
    System.out.println("Found " + count + " words.");
    } catch (TooSmallText e1){
        System.out.println(e1.getClass().getSimpleName() + ": " + e1.getMessage());
    } catch (InvalidStopwordException e2){
        System.out.println(e2.getClass().getSimpleName() + ": " + e2.getMessage());
    }
} catch (TooSmallText e3) {
    // if the text was too small on the first try
        System.out.println(e3.getClass().getSimpleName() + ": " + e3.getMessage());

}



}

}

