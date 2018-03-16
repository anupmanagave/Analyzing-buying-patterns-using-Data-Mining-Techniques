import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DGIM {	 
	static int windowSize, currentSize=1;
	static Integer pnum;
	static String hostName;
	static List<String> window=new ArrayList<String>();
	static int index=0, k=0;
	static List<bckt> buckets=new ArrayList<bckt>();
	static boolean lock;
	public static void main(String[] args) {
		 Scanner sc=new Scanner(System.in);
		 try{
		 System.out.println("Enter window size:");
		   windowSize=sc.nextInt();
		   if(windowSize<=0){System.out.println("Invalid Window Size"); System.exit(0);}
		 }catch(Exception e){
			 System.out.println("Invalid Window Size"); System.exit(0);
		 }
		 try{
		 System.out.println("Enter IP");		 
		 String input=sc.next();
		   StringTokenizer st = new StringTokenizer(input,":"); 
		   hostName=st.nextToken();
		   String portstr=st.nextToken();
		   pnum=Integer.parseInt(portstr);
		   if(hostName.endsWith(".com")||(st.hasMoreTokens())||(input.contains("::"))){
			   System.out.println("Invalid Host:port Pair"); System.exit(0);
		   }
		 }catch(Exception e){
			 System.out.println("Invalid Host:port Pair"); System.exit(0);
		 }
		   Window obj=new Window(); 
		   output op=new output();
		     Thread tobj =new Thread(obj); 
		     Thread tobj2=new Thread(op);
		     tobj.start();
		     tobj2.start();
		     try {
				tobj.join();
				tobj2.join();
			} catch (InterruptedException e) {
				System.out.println("Thrad Exception Occurred");
			}
		     sc.close();

	}
	
	public static class Window implements Runnable{
		String str;
		@Override
		
		public void run() {
			 try{
					 @SuppressWarnings("resource")
					Socket socket = new Socket(hostName, pnum);
				 BufferedReader buf =new BufferedReader(new InputStreamReader(socket.getInputStream()));
				 while((str=buf.readLine())!= null){
					 while(lock==true){}
					 lock=true;
					 System.out.print(str+ " ");
					 updateWindow(str);index++;
					if(str.equalsIgnoreCase("1"))
					 {bucketize(index);}
					lock=false;
				 }

				}catch(Exception e){
				 System.err.println("Error Occurred");lock=false;
				 System.exit(0);
			 }
			
		}		
		private void bucketize(int ind) {			
			
			
			bckt obj=new bckt(ind);
			buckets.add(obj);
			if(buckets.size()>3){
			k=buckets.size()-1;
			check(k);}			
		}
		
		private void check(int k) {
			if((k-3>=0)&&(buckets.get(k).size==buckets.get(k-3).size)){
						int tempIndex=k-2;
						int oldSize=buckets.get(tempIndex).size;
						buckets.get(tempIndex).setSize(oldSize*2);
						buckets.remove(tempIndex-1);
						check(tempIndex-1);
				}
			
		}
		private void updateWindow(String newbit) {
			if(currentSize<=windowSize){
			window.add(newbit);
			currentSize++;
			}
			else{
				window.remove(0);
				window.add(newbit);		
			}
		}		
	}
	public static class bckt{
		int startIndx,size;
		public bckt(int sIndx){
			startIndx=sIndx;
			size=1;
			
		}
		public void setSize(int sz){
			this.size=sz;
		}
		
		public String toString(){
			return ""+startIndx;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static class output implements Runnable{
		
	@Override
	public void run() {
		System.out.println();
	
		
		while(true){
			Scanner sc2=new Scanner(System.in);
		String query=sc2.nextLine();
		 while(lock==true){}
		 lock=true;
		String stemp="What is the number of ones for last";
		try{
		if(query.startsWith(stemp)&&(query.endsWith("data?"))){
			query=query.substring(stemp.length());
		StringTokenizer st1=new StringTokenizer(query," ");
		int ip=Integer.parseInt(st1.nextToken());
		if((ip>0)&&(st1.nextToken().equalsIgnoreCase("data?"))){
		calc(ip);
		lock=false;
		}
		else{
			System.out.println("Invalid Query Please try again");lock=false;
		}
		}
		else{
			System.out.println("Invalid Query Please try again");lock=false;
		}
		}catch(Exception e){
			System.out.println("Invalid Query Please try again");lock=false;
		}
		sc2.close();
		}
		}
	private static void calc(int ip) {
		int cnt=0;
		if(ip<=windowSize){
			System.out.println("Number of ones in last "+ip+" data is exactly "+exact(ip));
		}
		else{
			System.out.println("Number of ones in last "+ip+" data is approximately "+approx(ip));
		}
		
	}

	private static int approx(int ip) {
		int count=0;
		int recent=index;
		int target=recent-ip;
		int tempIndex=buckets.size()-1;
		int firstBucket=buckets.get(0).startIndx;
		
		while((target<buckets.get(tempIndex).startIndx)&&(tempIndex>=firstBucket)){
			count=count+buckets.get(tempIndex).size;
			if(tempIndex!=firstBucket){
			tempIndex--;}
		}
		if(target==buckets.get(tempIndex).startIndx){}
		else{
			tempIndex++;
			count=(int) (count-Math.floor(((buckets.get(tempIndex).size)/2)));
		}
		return count;
		
		
	}

	private static int exact(int ip) {
		int count=0;
		for(int p=windowSize-1, q=0;q<ip;q++,p--){
			if(window.get(p).equals("1")){
				count++;
			}
			
		}
		return count;
		
	}
}
}
