package schedulebuilder.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

public class Properties extends Hashtable<String, Object> implements Serializable {
	
	private File prefFile;
	
	public Properties(File f){
		super();
		prefFile = f;
	}
	
	public static Properties load(File f){
		try{
			FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Properties temp = (Properties) in.readObject();
			in.close();
			fileIn.close();
			return temp;
		}
		catch(IOException i){
			return new Properties(f);
		}
		catch(ClassNotFoundException c){
			return new Properties(f);
		}
	}
	
	public boolean save(){
		try{
			prefFile.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(prefFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			return true;
		}catch(IOException i){
			i.printStackTrace();
			return false;
		}
	}
}
