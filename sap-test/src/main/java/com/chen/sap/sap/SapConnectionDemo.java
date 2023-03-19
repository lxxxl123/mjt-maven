package com.chen.sap.sap;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;

import java.util.*;
import java.util.Map.Entry;

public class SapConnectionDemo {

	private static String POOL_NAME = "SAP_POOL";

	/**
	 * 构造函数
	 */
	public static void init(String interfaceId) {
		Properties logonProperties = new Properties();
		if ("0".equals(interfaceId)) {
			logonProperties.put("jco.client.ashost", "192.168.0.51");// 服务器IP  /H/61.142.172.204/H/192.168.0.50
			logonProperties.put("jco.client.client", "200");// 客户端
			logonProperties.put("jco.client.sysnr", "00");// 系统编号
			logonProperties.put("jco.client.user", "HTTEST"); // 用户名HTTEST CHENL
			logonProperties.put("jco.client.passwd", "87654321"); // 密码87654321 12345678
		} else if ("1".equals(interfaceId)) {

//		logonProperties.put("jco.client.ashost", "/H/61.142.172.204/H/192.168.0.50");// 服务器IP  /H/61.142.172.204/H/192.168.0.50  192.168.0.50

//      logonProperties.put("jco.client.ashost", "/H/61.142.172.204/H/192.168.0.50");// 服务器IP  /H/61.142.172.204/H/192.168.0.50  192.168.0.50
			logonProperties.put("jco.client.ashost", "192.168.0.50");// 服务器IP  /H/61.142.172.204/H/192.168.0.50  192.168.0.50
//      logonProperties.put("jco.client.ashost", "192.168.0.50");// 服务器IP  /H/61.142.172.204/H/192.168.0.50  192.168.0.50


			logonProperties.put("jco.client.client", "120");// 客户端
			logonProperties.put("jco.client.sysnr", "00");// 系统编号
			logonProperties.put("jco.client.user", "CHENL"); // 用户名
			logonProperties.put("jco.client.passwd", "yqju3pe9"); // 密码
		}
		createConnectionPool(POOL_NAME, 200, logonProperties);

	}



	/**
	 * @param interfaceId 要调用接口id(0:200，1:120)
	 * @param functionName 要调用的SAP函数名称
	 * @param param 参数import
	 * @param selectTable  查询的表
	 * @param selectParam  查询的表的字段
	 * @param resultTable  返回接口的表
	 * @param resultSelectParam  返回表的字段
	 * @param resultParam  返回字段
	 * @return SapReturnObject 返回值，包含export和tables
	 */
	public static SapReturnObject getResultNew(String interfaceId, String functionName, HashMap<String, String> param, String[] selectTable, HashMap<String ,String>[][] selectParam, String[] resultTable, String[][] resultSelectParam, String[] resultParam){
		SapReturnObject obj=new SapReturnObject();
		HashMap<String,List<HashMap<String ,String>>> tables =new HashMap<String,List<HashMap<String ,String>>>();
		HashMap<String, Object> exports=new HashMap<String, Object>();
		try {
			if("0".equals(interfaceId)){
				POOL_NAME="SAP_POOL0";
			}else if("1".equals(interfaceId)){
				POOL_NAME="SAP_POOL1";
			}
			if(functionName!=null && !"".equals(functionName)){
				JCO.Client connection = getConnectionInPool(POOL_NAME);
				if(connection==null){
					init(interfaceId);
					connection = getConnectionInPool(POOL_NAME);
				}
				connection.connect();
				if (connection != null && connection.isAlive()) {
					//System.out.println("Connection is alive!");
					IRepository repository = JCO.createRepository("MYRepository", POOL_NAME);
					// 获得一个指定函数名的函数模板
					IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
					JCO.Function function = ft.getFunction();
					JCO.ParameterList input=function.getImportParameterList();
					if(param!=null && param.size()>0){
						Iterator<?> it = param.entrySet().iterator();
						while (it.hasNext()) {
							Entry<?, ?> entry = (Entry<?, ?>) it.next();
							String key=(String) entry.getKey();
							String value=(String) entry.getValue();
							input.setValue(value, key);
						}
					}
					if(selectTable!=null && selectTable.length>0){
						for(int i=0;i<selectTable.length;i++){
							if(selectParam!=null && selectParam.length>0){
								JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
								for(int j=0;j<selectParam[i].length;j++){
									// 设定该行对应变量
									if(selectParam[i]!=null && selectParam[i].length>0 && selectParam[i][j]!=null && selectParam[i][j].size()>0){
										// 新增一条空行
										tDateRange.appendRow();
										Iterator<?> it = selectParam[i][j].entrySet().iterator();
										while (it.hasNext()) {
											Entry<?, ?> entry = (Entry<?, ?>) it.next();
											String key=(String) entry.getKey();
											String value=(String) entry.getValue();
											if(value!=null && !"".equals(value)){
												// 定位到第j行
												tDateRange.setRow(j);
												tDateRange.setValue(value,key);
												//System.out.println(tDateRange.getValue(key));
											}
										}
									}
								}

							}
						}
					}
					Client client = JCO.getClient(POOL_NAME);
					// 执行函数
					client.execute(function);
					// 输出参数1
					if(resultParam!=null && resultParam.length>0){
						JCO.ParameterList output = function.getExportParameterList();
						for(String tempParam:resultParam){
							exports.put(tempParam, output.getValue(tempParam));
							//exports.put(tempParam, output.getString(tempParam));
						}
					}
					// 输出参数2
					if(resultTable!=null && resultTable.length>0){
						for(int s=0;s<resultTable.length;s++){
							JCO.Table flights = function.getTableParameterList().getTable(resultTable[s]);
							List<HashMap<String ,String>> list=new ArrayList<HashMap<String ,String>>();
							for (int i = 0; i < flights.getNumRows(); i++) {
								//System.out.println(flights.getNumRows());
								flights.setRow(i);
								HashMap<String,String> map =new HashMap<String,String>();
								for(int x=0;x<resultSelectParam[s].length;x++){
									String key=resultSelectParam[s][x];
									String value=flights.getString(resultSelectParam[s][x]);
									if(key!=null){
										key=key.trim();
									}
									if(value!=null){
										value=value.trim();
									}
									map.put(key,value);
								}
								list.add(map);
							}
							tables.put(resultTable[s], list);
							//System.out.println(resultTable[s]);
							//System.out.println(list.size());
						}
					}

					releaseConnection(connection);
				}else{
					System.out.println("======error:sap连接失败");
				}
			}else{
				System.out.println("======error:参数为空");
			}
			if(tables.size()==0){
				tables=null;
			}else{
				obj.setTables(tables);
			}
			if(exports.size()==0){
				exports=null;
			}else{
				obj.setExports(exports);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	/**
	 * @param interfaceId 要调用接口id(0:查询余额，1:参考DN单)
	 * @param functionName 要调用的SAP函数名称
	 * @param selectTable  查询的表
	 * @param selectParam  查询参数
	 * @param resultTable  返回接口的表
	 * @param resultParam  返回字段
	 * @return List<HashMap<String ,String>>
	 */
	public static List<HashMap<String ,String>> getResult(String interfaceId,String functionName,String[] selectTable,HashMap<String ,String[]>[] selectParam,String resultTable,String[] resultParam){
		List<HashMap<String ,String>> list=new ArrayList<HashMap<String ,String>>();;
		try {
			if("0".equals(interfaceId)){
				POOL_NAME="SAP_POOL0";
			}else if("1".equals(interfaceId)){
				POOL_NAME="SAP_POOL1";
			}
			if(functionName!=null && !"".equals(functionName) && selectTable!=null && resultTable!=null && !"".equals(resultTable) && resultParam!=null && resultParam.length>0){
				JCO.Client connection = getConnectionInPool(POOL_NAME);
				if(connection==null){
					init(interfaceId);
					connection = getConnectionInPool(POOL_NAME);
				}
				connection.connect();
				if (connection != null && connection.isAlive()) {
					//System.out.println("Connection is alive!");
					IRepository repository = JCO.createRepository("MYRepository", POOL_NAME);
					// 获得一个指定函数名的函数模板
					IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
					JCO.Function function = ft.getFunction();
					if(selectTable!=null && selectTable.length>0){
						for(int i=0;i<selectTable.length;i++){
							if(selectParam!=null && selectParam.length>0){
								JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
								for(int j=0;j<selectParam.length;j++){
									// 新增一条空行
									tDateRange.appendRow();
									// 设定该行对应变量
									if(selectParam[j]!=null && selectParam[j].size()>0){
										Iterator<?> it = selectParam[j].entrySet().iterator();
										while (it.hasNext()) {
											Entry<?, ?> entry = (Entry<?, ?>) it.next();
											String key=(String) entry.getKey();
											String[] value=(String[]) entry.getValue();
											if(value!=null && value.length>0){
												for(int k=0;k<value.length;k++){

													// 定位到第k行
													tDateRange.setRow(j);
													tDateRange.setValue(value[k],key);
													//System.out.println(tDateRange.getValue(key));
												}
											}
										}
									}
								}

							}
						}
					}
					Client client = JCO.getClient(POOL_NAME);
					// 执行函数
					client.execute(function);
					// 输出参数
					JCO.Table flights = function.getTableParameterList().getTable(resultTable);
					for (int i = 0; i < flights.getNumRows(); i++) {
						//System.out.println(flights.getNumRows());
						flights.setRow(i);
						HashMap<String,String> map =new HashMap<String,String>();
						for(int x=0;x<resultParam.length;x++){
							String key=resultParam[x];
							String value=flights.getString(resultParam[x]);
							if(key!=null){
								key=key.trim();
							}
							if(value!=null){
								value=value.trim();
							}
							map.put(key,value);
						}
						list.add(map);
					}
					releaseConnection(connection);
				}else{
					System.out.println("======error:sap连接失败");
				}
			}else{
				System.out.println("======error:参数为空");
			}
			if(list.size()==0){
				list=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param interfaceId 要调用接口id(0:查询余额，1:参考DN单)
	 * @param functionName 要调用的SAP函数名称
	 * @param selectParam  查询参数
	 * @param resultParam  返回字段
	 * @return HashMap<String ,String>
	 */
	public static HashMap<String ,String> getParamResult(String interfaceId,String functionName,HashMap<String ,String> selectParam,String[] resultParam){
		HashMap<String ,String> map=new HashMap<String ,String>();;
		try {
			if("0".equals(interfaceId)){
				POOL_NAME="SAP_POOL0";
			}else if("1".equals(interfaceId)){
				POOL_NAME="SAP_POOL1";
			}
			if(functionName!=null && !"".equals(functionName) && resultParam!=null && resultParam.length>0){
				JCO.Client connection = getConnectionInPool(POOL_NAME);
				if(connection==null){
					init(interfaceId);
					connection = getConnectionInPool(POOL_NAME);
				}
				connection.connect();
				if (connection != null && connection.isAlive()) {
					System.out.println("Connection is alive!");
					IRepository repository = JCO.createRepository("MYRepository", POOL_NAME);
					// 获得一个指定函数名的函数模板
					IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
					JCO.Function function = ft.getFunction();
					JCO.ParameterList input=function.getImportParameterList();
					if(selectParam!=null && selectParam.size()>0){
						Iterator<?> it = selectParam.entrySet().iterator();
						while (it.hasNext()) {
							Entry<?, ?> entry = (Entry<?, ?>) it.next();
							String key=(String) entry.getKey();
							String value=(String) entry.getValue();
							input.setValue(value, key);
						}
					}
					Client client = JCO.getClient(POOL_NAME);
					// 执行函数
					client.execute(function);
					// 输出参数
					JCO.ParameterList output = function.getExportParameterList();
					for(String param:resultParam){
						map.put(param, output.getString(param));
					}
					releaseConnection(connection);
				}else{
					System.out.println("======error:sap连接失败");
				}
			}else{
				System.out.println("======error:参数为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param interfaceId 要调用接口id(0:查询余额，1:参考DN单)
	 * @param functionName 要调用的SAP函数名称
	 * @param param  查询参数
	 * @param selectTable  查询的表
	 * @param selectParam  查询参数
	 * @param resultTable  返回接口的表
	 * @param resultParam  返回字段
	 * @return List<HashMap<String ,String>>
	 */
	public static List<HashMap<String ,String>> getListParamResult(String interfaceId,String functionName,HashMap<String ,String> param,String[] selectTable,HashMap<String ,String[]>[] selectParam,String resultTable,String[] resultParam){
		List<HashMap<String ,String>> list=new ArrayList<HashMap<String ,String>>();;
		try {
			if("0".equals(interfaceId)){
				POOL_NAME="SAP_POOL0";
			}else if("1".equals(interfaceId)){
				POOL_NAME="SAP_POOL1";
			}
			if(functionName!=null && !"".equals(functionName)  && resultTable!=null && !"".equals(resultTable) && resultParam!=null && resultParam.length>0){
				JCO.Client connection = getConnectionInPool(POOL_NAME);
				if(connection==null){
					init(interfaceId);
					connection = getConnectionInPool(POOL_NAME);
				}
				connection.connect();
				if (connection != null && connection.isAlive()) {
					System.out.println("Connection is alive!");
					IRepository repository = JCO.createRepository("MYRepository", POOL_NAME);
					// 获得一个指定函数名的函数模板
					IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
					JCO.Function function = ft.getFunction();
					JCO.ParameterList input=function.getImportParameterList();
					if(param!=null && param.size()>0){
						Iterator<?> it = param.entrySet().iterator();
						while (it.hasNext()) {
							Entry<?, ?> entry = (Entry<?, ?>) it.next();
							String key=(String) entry.getKey();
							String value=(String) entry.getValue();
							input.setValue(value, key);
						}
					}
					if(selectTable!=null && selectTable.length>0){
						for(int i=0;i<selectTable.length;i++){
							if(selectParam!=null && selectParam.length>0 && selectParam[i]!=null){
								JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
								// 设定该行对应变量
								if(selectParam[i]!=null && selectParam[i].size()>0){
									Iterator<?> it = selectParam[i].entrySet().iterator();
									while (it.hasNext()) {
										Entry<?, ?> entry = (Entry<?, ?>) it.next();
										String key=(String) entry.getKey();
										String[] value=(String[]) entry.getValue();
										if(value!=null && value.length>0){
											for(int k=0;k<value.length;k++){
												// 新增一条空行
												tDateRange.appendRow();
												// 定位到第k行
												tDateRange.setRow(k);
												tDateRange.setValue(value[k],key);
											}
										}
									}
								}
							}
						}
					}
					Client client = JCO.getClient(POOL_NAME);
					// 执行函数
					client.execute(function);
					// 输出参数
					JCO.Table flights = function.getTableParameterList().getTable(resultTable);
					for (int i = 0; i < flights.getNumRows(); i++) {
						flights.setRow(i);
						HashMap<String,String> map =new HashMap<String,String>();
						for(int x=0;x<resultParam.length;x++){
							String key=resultParam[x];
							String value=flights.getString(resultParam[x]);
							if(key!=null){
								key=key.trim();
							}
							if(value!=null){
								value=value.trim();
							}
							map.put(key,value);
						}
						list.add(map);
					}
					releaseConnection(connection);
				}else{
					System.out.println("======error:sap连接失败");
				}
			}else{
				System.out.println("======error:参数为空");
			}
			if(list.size()==0){
				list=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public static void createConnectionPool(String poolName, int maxConnection,Properties logonProperties) {
		JCO.Pool pool = JCO.getClientPoolManager().getPool(poolName);
		if (pool == null) {
			JCO.addClientPool(poolName,         // 连接池名
					maxConnection,    // 最大连接数
					logonProperties); // 登录参数
		}
	}

	public static JCO.Client getConnectionInPool(String poolName) {
		JCO.Client connection = null;
		JCO.Pool pool = JCO.getClientPoolManager().getPool(poolName);
		if (pool != null) {
			connection = JCO.getClient(poolName);
			//pool.setAbapDebug(true);
		}
		return connection;
	}

	public static void releaseConnection(JCO.Client connection) {
		JCO.releaseClient(connection);
	}

	public static void removeConnectionPool(String poolName) {
		JCO.removeClientPool(poolName);
	}




	/**
	 * 查询客户余额
	 */
	public static void test1(){
		String functionName = "ZSDID_CREDIT2SQL";
		String[] selectTable = { "T_CUSTOM" };
		@SuppressWarnings("unchecked")
		HashMap<String, String[]>[] selectParam = new HashMap[1];
		selectParam[0] = new HashMap<String, String[]>();
		selectParam[0].put("KUNNR", new String[] { "1010700180".substring(0, 9)+ "0" });
		selectParam[0].put("KKBER", new String[] { "HT01" });
		String resultTable = "T_CUSTOM_CREDIT";
		String[] resultParam = { "KUNNR", "OBLIG" };
		List<HashMap<String, String>> resultList = SapConnectionDemo.getResult("1", functionName, selectTable, selectParam, resultTable,resultParam);
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				for (int j = 0; j < resultParam.length; j++) {
					System.out.println("======[" + resultParam[j] + "][" + i+ "]:" + resultList.get(i).get(resultParam[j]));
				}
			}
		}
	}

	/**
	 *  上传合同
	 */
	public static void test2(){
		String functionName = "ZRFC_MOSS_0001";
		HashMap<String, String> selectParam = new HashMap<String, String>();
		selectParam.put("FILE_DESC", "七车间制曲塔风管-世纪达王锐明aa1");
		selectParam.put("FILE_LINK", "http://192.168.15.55:8188/HIP/userfiles/uploadFile/G-1103.5.B03七车间制曲塔风管-世纪达-王锐明.zip");
		selectParam.put("WBS", "fsd1f");
		String[] resultParam = { "ACTION_FLAG" };
		HashMap<String, String> map = SapConnectionDemo.getParamResult("1",functionName, selectParam, resultParam);
		if (map != null && map.size() > 0) {
			System.out.println("======[" + resultParam[0] + "]:"+ map.get(resultParam[0]));
		}
	}

	/**
	 * 查询数据
	 */
	public static void test3(){
		String functionName="ZMM_TP_GETDN";
		String[] selectTable={"IT_DATE","IT_DNHB","IT_LFART","IT_VSTEL"};//{"IT_DATE","IT_DNHB","IT_LFART"};
		@SuppressWarnings("unchecked")
		HashMap<String ,String[]>[] selectParam=new HashMap[4];
		selectParam[0]=new HashMap<String ,String[]>();
		selectParam[0].put("DATE1", new String[]{"20120101"});
		selectParam[0].put("DATE2", new String[]{"20130420"});
		selectParam[1]=new HashMap<String ,String[]>();
		selectParam[1].put("VBELN", new String[]{""});
		selectParam[2]=new HashMap<String ,String[]>();
		selectParam[2].put("LFART", new String[]{"ZLF"});
		selectParam[3]=null;
		//selectParam[3].put("VSTEL", "");
		String resultTable="OT_RETURN";
		String[] resultParam={"ZDN_HB","LFART","MATNR","MAKTX","LFIMG","CHARG","ATWRT1","ATWRT2","SHPNR_L","DNLGORT","POLGORT","WERKS"};
		List<HashMap<String ,String>> resultList = SapConnectionDemo.getResult("1",functionName, selectTable, selectParam, resultTable, resultParam);
		if(resultList!=null && resultList.size()>0){
			for(int i=0;i<resultList.size();i++){
				for(int j=0;j<resultParam.length;j++){
					System.out.println("======["+resultParam[j]+"]["+i+"]:"+resultList.get(i).get(resultParam[j]));
				}
			}
		}
	}

	/**
	 * 成品半成品数据获取
	 */
	public static void test4(){
		String functionName = "ZMM_HALF_STOCK2SQL";
		String[] selectTable = { "IT_YEAR" };
		@SuppressWarnings("unchecked")
		HashMap<String, String[]>[] selectParam = new HashMap[1];
		selectParam[0] = new HashMap<String, String[]>();
		selectParam[0].put("GJAHR", new String[] { "2011" });
		String resultTable = "OT_RETURN";
		String[] resultParam = { "YEAR","MONTH","FACTORY","MATERIALNO","MATERIALNAME","VOLUME","TON","AMOUNT"};
		List<HashMap<String, String>> resultList = SapConnectionDemo.getResult("1", functionName, selectTable, selectParam, resultTable,resultParam);
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				for (int j = 0; j < resultParam.length; j++) {
					System.out.println("======[" + resultParam[j] + "][" + i+ "]:" + resultList.get(i).get(resultParam[j]));
				}
			}
		}
	}
//	List<HashMap<String, String>> resultList=new ArrayList<HashMap<String, String>>();
//	HashMap<String, String> map=new HashMap<String, String>();
//	map.put("AUFNR", "1000020");//订单号
//	map.put("MATNR", "1000020");//库存地点
//	map.put("MAKTX", "101");//移动类型
//	map.put("LGORT", "2013-01-01");//生产日期
//	map.put("BWART", "999999");//物料编码
//	map.put("BDTER", "测试物料");//物料名称
//	map.put("MEINS", "个");//单位
//	map.put("BDMNG", "100");//计划数量
//	resultList.add(map);

	/**
	 *
	 */
	public static void test5(){
		String functionName = "Z_PROPLAN_RKD_WL";
		String[] selectTable = { "AUTBL" };
		@SuppressWarnings("unchecked")
		HashMap<String, String[]>[] selectParam = new HashMap[1];
		selectParam[0] = new HashMap<String, String[]>();
		selectParam[0].put("AUFNR", new String[] { "64000004"  });
		String resultTable = "AUOUT1";
		String[] resultParam = { "AUFNR","MATNR","MAKTX","LGORT","BWART","BDTER","MEINS","BDMNG"};
		List<HashMap<String, String>> resultList = SapConnectionDemo.getResult("1", functionName, selectTable, selectParam, resultTable,resultParam);
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				for (int j = 0; j < resultParam.length; j++) {
					System.out.println("======[" + resultParam[j] + "][" + i+ "]:" + resultList.get(i).get(resultParam[j]));
				}
			}
		}
	}


	/**
	 *
	 */
	public static void test6(){
		String functionName = "ZPP_SAP2YD_GET_CPSTOCK";
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("I_DATE", "20131107");
		String[] selectTable = { "IT_MATNR" };
		@SuppressWarnings("unchecked")
		HashMap<String, String[]>[] selectParam = new HashMap[1];
		selectParam[0] = new HashMap<String, String[]>();
		selectParam[0].put("MATNR", new String[] { "000000000006000001"  });
		String resultTable = "OT_RETURN";
		String[] resultParam = { "SORTFLAG","FACTORY","MATNO","MATNAME","STOCK1","STOCK2","STOCK3","STOCK4","STOCK5","STOCK6","STOCK7"};
		List<HashMap<String, String>> resultList = SapConnectionDemo.getListParamResult("1", functionName,param,selectTable, selectParam , resultTable,resultParam);
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				for (int j = 0; j < resultParam.length; j++) {
					System.out.println("======[" + resultParam[j] + "][" + i+ "]:" + resultList.get(i).get(resultParam[j]));
				}
			}
		}

	}

	/**
	 * 新封装函数
	 */
	public static void test7(){
		String functionName = "Z_PROPLAN_TEST";
		//输入import
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("I_DATE", "20131111");
		param.put("I_DATE2", "20131122");
		//输入tables
		String[] selectTable = { "IT_BAPI","IT_BAPI1" };
		@SuppressWarnings("unchecked")
		HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][2];
		selectParam[0][0] = new HashMap<String, String>();
		selectParam[0][0].put("BUDAT","20131030");
		selectParam[0][0].put("MATNR","000000000000700001");
		selectParam[0][0].put("WERKS","1100");
		selectParam[0][0].put("LGORT","1170");
		selectParam[0][0].put("BWART","101");
		selectParam[0][0].put("ERFMG","1.000");
		selectParam[0][0].put("CHARG","0809000001");
		selectParam[0][0].put("AUFNR","000061000736");
		selectParam[0][0].put("GRUND","0000");
		selectParam[0][0].put("RSNUM","0000000000");
		selectParam[0][0].put("RSPOS","0000");
		selectParam[0][0].put("KZEAR","");

		selectParam[0][1] = new HashMap<String, String>();
		selectParam[0][1].put("BUDAT","20131022");
		selectParam[0][1].put("MATNR","000000000000700002");
		selectParam[0][1].put("WERKS","1101");
		selectParam[0][1].put("LGORT","1171");
		selectParam[0][1].put("BWART","102");
		selectParam[0][1].put("ERFMG","1.001");
		selectParam[0][1].put("CHARG","0809000002");
		selectParam[0][1].put("AUFNR","000061000737");
		selectParam[0][1].put("GRUND","0001");
		selectParam[0][1].put("RSNUM","0000000001");
		selectParam[0][1].put("RSPOS","0001");
		selectParam[0][1].put("KZEAR","X");


		selectParam[1][0] = new HashMap<String, String>();
		selectParam[1][0].put("BUDAT","20111030");
		selectParam[1][0].put("MATNR","100000000000700001");
		selectParam[1][0].put("WERKS","3100");
		selectParam[1][0].put("LGORT","3170");
		selectParam[1][0].put("BWART","101");
		selectParam[1][0].put("ERFMG","1.000");
		selectParam[1][0].put("CHARG","0809000001");
		selectParam[1][0].put("AUFNR","000061000736");
		selectParam[1][0].put("GRUND","0000");
		selectParam[1][0].put("RSNUM","0000000000");
		selectParam[1][0].put("RSPOS","0000");
		selectParam[1][0].put("KZEAR","");

		selectParam[1][1] = new HashMap<String, String>();
		selectParam[1][1].put("BUDAT","20121022");
		selectParam[1][1].put("MATNR","200000000000700002");
		selectParam[1][1].put("WERKS","2101");
		selectParam[1][1].put("LGORT","2171");
		selectParam[1][1].put("BWART","102");
		selectParam[1][1].put("ERFMG","1.001");
		selectParam[1][1].put("CHARG","0809000002");
		selectParam[1][1].put("AUFNR","000061000737");
		selectParam[1][1].put("GRUND","0001");
		selectParam[1][1].put("RSNUM","0000000001");
		selectParam[1][1].put("RSPOS","0001");
		selectParam[1][1].put("KZEAR","X");
		//selectParam[1].put("NO_MORE_GR", new String[] { NO_MORE_GR  });
		//输出tables
		String[] resultTable = {"OT_RETURN1","OT_RETURN2"};
		String[] resultSelectParam1={ "BUDAT", "MATNR", "WERKS", "LGORT", "BWART", "GRUND", "ERFMG", "CHARG", "AUFNR", "RSNUM", "RSPOS", "KZEAR"};//输出table1的结构
		String[] resultSelectParam2={ "BWART", "GRUND", "ERFMG"};//输出table2的结构
		String[][] resultSelectParam = new String[2][resultSelectParam1.length];
		resultSelectParam[0]=resultSelectParam1;
		resultSelectParam[1]=resultSelectParam2;
		//输出export
		String[] resultParam={"O_DATE","O_DATE2"};
		//String[] resultParam = { "MBLNR","MJAHR","MESSAGE"};
		//resultList = SapConnectionPool.getResult(interfaceId, functionName, selectTable, selectParam, resultTable,resultParam);

		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName,param,selectTable, selectParam , resultTable,resultSelectParam,resultParam);
		HashMap<String,List<HashMap<String ,String>>> resultList =obj.getTables();
		HashMap<String, Object> exports=obj.getExports();
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}
		if (exports != null && exports.size() > 0) {
			for (int k = 0; k < exports.size(); k++) {
				System.out.println("======[" + resultParam[k] + "]:" + exports.get(resultParam[k]));
			}
		}

	}


	public static void test9(){
		String functionName = "Z_PROPLAN_RKJH_PZ";

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("STRMP", "20140109");

		String[] selectTable = { "IT_MATNR","IT_WERKS","IT_LGORT","IT_DISPO" };
		@SuppressWarnings("unchecked")
		HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][1];
		selectParam[0][0] = new HashMap<String, String>();
		selectParam[0][0].put("MATNR","80000000001");

		selectParam[1][0] = new HashMap<String, String>();
		selectParam[1][0].put("WERKS","1100");

		String[] resultTable = {"OT_PU"};
		String[] resultSelectParam1 ={ "AUFNR","MATNR","MAKTX","LGORT","BDTER","MEINS","BDMNG","DWERK"};
		String[][] resultSelectParam = new String[2][resultSelectParam1.length];
		resultSelectParam[0]=resultSelectParam1;
		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName, param, selectTable, selectParam, resultTable, resultSelectParam, null);

		HashMap<String,List<HashMap<String ,String>>> resultList =obj.getTables();//=obj.getTables().get("OT_PU");
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}

	}
	public static void test10(){
		String functionName = "Z_PROPLAN_CKJH_BZL";
		HashMap<String, String> param = new HashMap<String, String>();
//		int len=0;
//		String[] arrWERKS = null;
//		if(CommonManage.notNull(gongchang)){
//			arrWERKS=gongchang.split(",");
//			len=arrWERKS.length;
//		}
//		String[] arrDISPO = null;
//		if(CommonManage.notNull(kongzhizhe)){
//			arrDISPO=kongzhizhe.split(",");
//			len=arrDISPO.length>len?arrDISPO.length:len;
//		}
//		String[] arrLGORT = null;
//		if(CommonManage.notNull(kucundidian)){
//			arrLGORT=kucundidian.split(",");
//			len=arrLGORT.length>len?arrLGORT.length:len;
//		}
//		String[] arrMATNR = null;
//		if(CommonManage.notNull(wuliaobianma)){
//			arrMATNR=wuliaobianma.split(",");
//			len=arrMATNR.length>len?arrMATNR.length:len;
//		}
		String[] selectTable = { "IT_WERKS","IT_DISPO","IT_LGORT","IT_MATNR" };
		HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][2];
//		if(arrWERKS!=null){
//			for(int i=0;i<arrWERKS.length;i++){
//				selectParam[0][i]=new HashMap<String, String>();
//				selectParam[0][i].put("WERKS", arrWERKS[i]);
//			}
//		}
//		if(arrDISPO!=null){
//			for(int i=0;i<arrDISPO.length;i++){
//				selectParam[1][i]=new HashMap<String, String>();
//				selectParam[1][i].put("DISPO", arrDISPO[i]);
//			}
//		}
//		if(arrLGORT!=null){
//			for(int i=0;i<arrLGORT.length;i++){
//				selectParam[2][i]=new HashMap<String, String>();
//				selectParam[2][i].put("LGORT", arrLGORT[i]);
//			}
//		}
//		if(arrMATNR!=null){
//			for(int i=0;i<arrMATNR.length;i++){
//				selectParam[3][i]=new HashMap<String, String>();
//				selectParam[3][i].put("MATNR", arrMATNR[i]);
//			}
//		}
		selectParam[3][0]=new HashMap<String, String>();
		selectParam[3][0].put("MATNR", "79100200");
		param.put("BDTER", "20100929");
//		String[] selectTable = { "IT_PU" };
		String[] resultTable = {"OT_PU"};
		String[] resultSelectParam1 ={ "LGORT","MATNR","MAKTX","MEINS","BDMNG"};
		String[][] resultSelectParam = new String[1][resultSelectParam1.length];
		resultSelectParam[0]=resultSelectParam1;
		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName, param, selectTable, selectParam, resultTable, resultSelectParam, null);

//		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName, param, selectTable, selectParam, resultTable, resultSelectParam, null);

		HashMap<String,List<HashMap<String ,String>>> resultList =obj.getTables();//=obj.getTables().get("OT_PU");
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}

	}

	/**
	 * 新封装函数
	 */
	public static void test8(){
		String functionName = "ZFI_AUFNRSKED";
		//输入tables
		String[] selectTable = { "IT_BUDAT"};
		@SuppressWarnings("unchecked")
		HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][2];
		selectParam[0][0] = new HashMap<String, String>();
		selectParam[0][0].put("BUDAT","20101001");
		selectParam[0][1] = new HashMap<String, String>();
		selectParam[0][1].put("BUDAT","20101101");
		//输出tables
		String[] resultTable = {"OT_TABLE"};
		String[] resultSelectParam1={ "STLBEZ", "GAMNG", "GMEIN", "KTEXT"};//输出table1的结构
		String[][] resultSelectParam = new String[2][resultSelectParam1.length];
		resultSelectParam[0]=resultSelectParam1;
		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName,null,selectTable, selectParam , resultTable,resultSelectParam,null);
		HashMap<String,List<HashMap<String ,String>>> resultList =obj.getTables();
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}

	}

	/**
	 * 新封装函数
	 */
	public static void test11(){
		String functionName = "Z_PROPLAN_CKJH_KCL_01";
		//输入tables
		String[] selectTable = { "IT_PUT" };
		String[] resultTable = {"OT_PU"};
		String[] resultSelectParam1 ={ "LGORT","MATNR","MAKTX","MEINS","CHARG","BDMNG"};
		@SuppressWarnings("unchecked")
		HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][2];
		selectParam[0][0] = new HashMap<String, String>();
		selectParam[0][0].put("WERKS","79100100");
		selectParam[0][0].put("LGORT","2270");
		selectParam[0][0].put("MATNR", "1210");

		selectParam[0][1] = new HashMap<String, String>();
		selectParam[0][1].put("WERKS","79100100");
		selectParam[0][1].put("LGORT","2270");
		selectParam[0][1].put("MATNR", "1210");

		String[][] resultSelectParam = new String[2][resultSelectParam1.length];
		resultSelectParam[0]=resultSelectParam1;

		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName,null,selectTable, selectParam , resultTable,resultSelectParam,null);
		HashMap<String,List<HashMap<String ,String>>> resultList =obj.getTables();
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}

	}
	/**
	 * 新封装函数
	 */
	public static void test12(){


		//SAP获取客户信用余额
		String functionName = "ZSDID_CREDIT2SQL";
		String[] selectTable = {"T_CUSTOM"};	//传入表
		String[] resultTable = {"T_CUSTOM_CREDIT"};	//返回表
		HashMap<String, String>[][] selectParam=new HashMap[selectTable.length][1];
		selectParam[0][0]=new HashMap<String, String>();
		selectParam[0][0].put("KUNNR", "101010005");	//T_CUSTOM的字段
		selectParam[0][0].put("KKBER", "HT01");	//T_CUSTOM的字段

		String[] resultParam = {"KUNNR","OBLIG"};	//抓取字段:客户编号、信用余额
		String[][] resultSelectParam = new String[1][resultParam.length];
		resultSelectParam[0]=resultParam;

		//调用SAP接口"1",functionName,null,selectTable,selectParam,resultTable,resultSelectParam,null);
		SapReturnObject obj = SapConnectionDemo.getResultNew("1", functionName,null,selectTable, selectParam , resultTable,resultSelectParam,null);
		HashMap<String,List<HashMap<String ,String>>> resultList=obj.getTables();;
//		if(obj!=null && obj.getTables()!=null){
//			resultList =obj.getTables().get("T_CUSTOM_CREDIT");
//		}
		if (resultList != null && resultList.size() > 0) {
			for (int k = 0; k < resultTable.length; k++) {
				for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
					for (int j = 0; j < resultSelectParam[k].length; j++) {
						System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k]+ "][" + k+ "][" + j+ "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		SapConnectionDemo.test12();
		SapConnectionDemo.test10();
		//SapConnectionDemo.test3();

	}
}
