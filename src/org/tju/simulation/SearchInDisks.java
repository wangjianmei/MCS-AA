package org.tju.simulation;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.scheduler.BlockOperation;
import org.tju.scheduler.DiskOperation;
import org.tju.scheduler.IdleTimeManager;

public class SearchInDisks {
	
	//Search In SSD Disks��block��idleTime��Ϊ-1��requestNum +1��disk��requestNum +1
	public static boolean searchInSSD(DiskInfo[] SSDDisks, String fileName, int blockId){
		
		for(int i=0; i<SSDDisks.length; i++){
			BlockInfo block = SSDDisks[i].getBlockList().get(blockId);
			if(block!=null){
				block.setIdleTime(-1);
				block.setRequestNum(block.getRequestNum()+1);
				SSDDisks[i].getBlockList().put(blockId, block);
				SSDDisks[i].setRequestNum(SSDDisks[i].getRequestNum()+1);
				//added by xx begin
				SSDDisks[i].setIdleTime(-1);
				//added by xx end
				return true;
			}
		}
			
		return false;
	}
	
	
	//Search In Cache Disks��block��idleTime��Ϊ-1��requestNum +1��disk��requestNum +1
	public static boolean searchInCache(DiskInfo[] cacheDisks, String fileName, int blockId){
		
		for(int i=0; i<cacheDisks.length; i++){
			BlockInfo block = cacheDisks[i].getBlockList().get(blockId);
			if(block!=null){
				block.setIdleTime(-1);
				block.setRequestNum(block.getRequestNum()+1);
				cacheDisks[i].getBlockList().put(blockId, block);
				cacheDisks[i].setRequestNum(cacheDisks[i].getRequestNum()+1);
				//added by xx begin
				cacheDisks[i].setIdleTime(-1);
				//added by xx end
				return true;
			}
		}
			
		return false;
	}
	
	
	//Search In data Disks
	public static boolean searchInDD(DiskInfo[] dataDisks, String fileName, int diskId, int blockId){
		//added by xx begin
		diskId = searchBetterInDD(dataDisks, blockId);
		//added by xx end
		
		BlockInfo block = dataDisks[diskId].getBlockList().get(blockId);
		
		if(block!=null){
			//added by xx begin
			if(dataDisks[diskId].getDiskState() == 1 && dataDisks[diskId].getIdleTime()>=0){
				System.out.println("          The disk whose ID == "+diskId+" is hitted when truely opened, idleTime = "+dataDisks[diskId].getIdleTime());
				IdleTimeManager.updateIdleTime(dataDisks[diskId].getIdleTime());
				dataDisks[diskId].setIdleTime(-1);
			}
			//added by xx end
			if(dataDisks[diskId].getDiskState()!=1){
				DiskOperation.openDisk(dataDisks[diskId]);
				BlockOperation.initBlocksList(dataDisks[diskId]);
			}
			dataDisks[diskId].setRequestNum(dataDisks[diskId].getRequestNum()+1);
			return true;
		}
		
		return false;		
	}
	//�ҵ������ʵĴ���, added by xx
	public static int searchBetterInDD(DiskInfo[] dataDisks, int blockId){
		
		int diskId0 = 0, diskId1 = 0, transTime0 = 1000, transTime1 = 1000;
		for(int i=0; i<dataDisks.length; i++){
			BlockInfo block = dataDisks[i].getBlockList().get(blockId);
			if(block!=null && transTime0==1000){
				transTime0=block.getTransmissionTime();
				diskId0=i;
			}else if(block!=null && transTime1==1000){
				transTime1=block.getTransmissionTime();
				diskId1=i;
			}
		}
		
		if(transTime0 < transTime1){
			return diskId0;
		}else{
			return diskId1;
		}
	}

}
