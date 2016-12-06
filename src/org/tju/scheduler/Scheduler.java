package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class Scheduler {
	
	//get parameters
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	public static double lowPriorityTh = valueOfConfigureFile.getLowPriorityTh();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	//Scheduler of All Disks, when is time to refresh������һ������������ȼ�������������ȼ��Ŀ�
	public static void SchedulerOfDisks(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks){
		
		//Calculate blocks' priority 
		//Calculate blocks' priority in SSD
		for(int i=0; i<SSDDisks.length; i++){
			PriorityOperation.calculatePriority(SSDDisks[i]);
		}
			
		//Calculate blocks' priority in cache disks
		for(int i=0; i<cacheDisks.length; i++){
			PriorityOperation.calculatePriority(cacheDisks[i]);
		}
		
		
		//Clear Cache Disks
		CacheCleaner.clearCache(SSDDisks, lowPriorityTh);
		CacheCleaner.clearCache(cacheDisks, lowPriorityTh);				
	}
	
	
	//Scheduler of All Disks per second
	public static void SchedulerOfSecond(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo[] dataDisks){
	
		//update blocks' info in cache disks
		//update blocks' info in SSD
		SchedulerOfSecond(SSDDisks);
		
		//update blocks' info in Cache Disks
		SchedulerOfSecond(cacheDisks);
		
		//update blocks' info in data Disks
		SchedulerOfSecond(dataDisks);
		
	}
	
	
	//Scheduler of Disks per second
	public static void SchedulerOfSecond(DiskInfo[] Disks){
		
		//update blocks' info in cache disks
		for(int i=0; i<Disks.length; i++){
			//tmp blocks' List
			HashMap<Integer, BlockInfo> tmpBlocks = new HashMap<Integer, BlockInfo>();
			//������̿��ţ�������ÿ���ǿ�block��idleTime��1
			if(Disks[i].getDiskState()==1){
				HashMap<Integer, BlockInfo> blocks = Disks[i].getBlockList();
				Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
				
				while (iter.hasNext()){
					Entry<Integer, BlockInfo> entry = iter.next();
					
					BlockInfo block = entry.getValue();
					
					if(block == null){
						continue ;
					} else {
						block.setIdleTime(block.getIdleTime()+1);				
						tmpBlocks.put(block.getBlockId(), block);
					}			
				}
				
				//Add to SSD
				Disks[i].setBlockList(new HashMap<Integer, BlockInfo>());
				Disks[i].setBlockList(tmpBlocks);
//				tmpBlocks.clear();
			}	
		}
		
			
		//Calculate blocks' priority
		for(int i=0; i<Disks.length; i++){
			PriorityOperation.calculatePriority(Disks[i]);
		}
			
	}

}
