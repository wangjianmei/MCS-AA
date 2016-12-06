package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class IdleTimeManager {
	private static int[] idleTimeList;
	private static int currentIndex; 
	private static int idleTimeTh;
	
	private static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	
	static {
		//System.out.println("idleTimeManager initial");
		idleTimeList = new int[100];
		for(int i=0; i<100;i++){
			idleTimeList[i]=-1;
		}
		currentIndex = 0;
		idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
		//System.out.println("idleTimeManager initial end");
	}
	
	
	public static int getIdleTimeTh(){
		
		return idleTimeTh;
	}
	

	public static void updateIdleTime(int idleTime){
		System.out.println("          A new idleTime is added to idleTimeList: "+idleTime + " at location "+currentIndex);
		if(idleTime <= 0)
			return;
		idleTimeList[currentIndex] =  idleTime;
		
		if(currentIndex != 99){
			currentIndex = (currentIndex + 1) % 100;
		}else{
			currentIndex = (currentIndex + 1) % 100;
			sortIdleTime();
			setIdleTime();
		}
	}
	

	
	private static void sortIdleTime() {
		// TODO Auto-generated method stub
		 for(int i =0 ; i< 99; ++i) {  
		        for(int j = 0; j < 99-i; ++j) {  
		            if(idleTimeList[j] > idleTimeList[j+1])  
		            {  
		                int tmp = idleTimeList[j] ; idleTimeList[j] = idleTimeList[j+1] ;  idleTimeList[j+1] = tmp;  
		            }  
		        }  
		    }  
		
	}
	
	
	public static int setIdleTime(){
		
		//  
		
		System.out.println(idleTimeTh +" to ");  //the old idletime
		//Tthreshold = Tfixed; 
		idleTimeTh = 30;    
		int s = 0,p = 0;
		for(int i = 0;i < 30; i++){
			idleTimeTh = i;
			
			for(int j = 0; j < 100; j++){  
				if(idleTimeList[j] <= idleTimeTh)
					s = s+1;
				if(idleTimeList[j] <= idleTimeTh + 30)
					p = p + 1;
			}
			if( (p-s) / 100 <= 0.05)   
				break;
		}
		System.out.println(idleTimeTh );  //new idletime
		return idleTimeTh;
	}
	

	public static void showIdleTimeManager(){
		System.out.println("\nidleTimeManager.idleTimeList:");
		for(int i=0; i<100;i++){
			System.out.println("idleTimeList ["+i+"] = "+idleTimeList[i]);
		}
		System.out.println("idleTimeManager.currentIndex: "+currentIndex);
		System.out.println("idleTimeManager.idleTimeTh: "+idleTimeTh+"\n");
	}
	
}