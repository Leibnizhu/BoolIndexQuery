package com.turingdi.rtb.boolindex;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.turingdi.rtb.boolindex.entity.Activity;
import com.turingdi.rtb.boolindex.entity.Assignment;
import com.turingdi.rtb.boolindex.entity.Conjunction;
import com.turingdi.rtb.boolindex.entity.PostList;
import com.turingdi.rtb.boolindex.entity.Query;

public class BoolIndexTest {
	@Test
	public void getSampleTest(){
		Map<Conjunction, List<Activity>> primaryIndex = new HashMap<Conjunction, List<Activity>>();
		Map<Integer, LinkedHashMap<Assignment, PostList>> secondaryIndex = new HashMap<Integer, LinkedHashMap<Assignment, PostList>>();
		try {
			for(int i = 1; i >= 0; i--){
				long start = System.nanoTime();
				Activity act = new GetSampleData().getOneRedisSample("Activity::" + (i+1));
				System.out.println("活动：\n" + act);
				//System.out.println("分conjunction结果：\n" + new MakeIndex().analysisConjunction(act));
				new MakeIndex().appendIndex(primaryIndex, secondaryIndex, act);
				System.out.println("Indexing for Activity::" + (i+1) + " Cost: " + (System.nanoTime()-start)/1000/1000 + " ms...");
			}
			
			/*for(Assignment assg : secondaryIndex.get(8).keySet()){
				System.out.println(assg + "---" + secondaryIndex.get(8).get(assg));
			}*/
			FileWriter fw = new FileWriter(new File("log.log"),false);
			fw.write("\n建立索引结果：\n一级索引：\n" + primaryIndex + "\n二级索引：\n" + secondaryIndex);
			fw.close();
			//System.out.println("\n建立索引结果：\n一级索引：\n" + primaryIndex + "\n二级索引：\n" + secondaryIndex);
			
			//查询测试
			//配置query，date/week/hour在new的时候已经根据当前日期时间初始化了
			Query query = new Query();
			query.setAdsense("Adsense::3");
			query.setAdx("Tanx");
			query.setArea("顺德");
			query.setTerm("PC");
			//执行查询
			List<Activity> result = new BoolQuery().boolQuery(primaryIndex, secondaryIndex, query);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
