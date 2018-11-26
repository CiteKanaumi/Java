class sample1{
    
    public static String change(String a){
	a += "456";
        
	return a;	
    }

    public static void main(String[] args){
	String str="ABC123";
        
	System.out.println(str);

	str = change(str);
	System.out.println("after:"+str);
    }


}
