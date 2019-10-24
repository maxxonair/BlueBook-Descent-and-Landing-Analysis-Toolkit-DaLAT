package GUI;

public class SplitTesterTester {
	
	 public static void main(String[] args) {
		String testString = "0 |FlightControllerElements| 12 |FlightControllerElements| |EventManagementElements 13 |EventManagementElements |EndElement| 1 150 |EndElement| ";
	       String fcSeparator="\\|FlightControllerElements\\|";
	       String eventSeparator="\\|EventManagementElements";
	       String endSeparator="\\|EndElement\\|";
	       
	       
	       	String[] initSplit = testString.split(fcSeparator);

	       	String[] head = initSplit[0].split(" ");
	       
	       	int  ID = Integer.parseInt(head[0]);
	       	int flightControllerIndex = Integer.parseInt(initSplit[1].split(" ")[1]);
	       	//System.out.println(initSplit[2]);
	       //	String eventPart = initSplit[2];
	       	String[] arr     = testString.split(eventSeparator);
	       	System.out.println(arr[1]);
	       	int eventIndex  = Integer.parseInt(arr[1].split(" ")[1]);
	       	
	       	String[] arr2   = testString.split(endSeparator);
	       	System.out.println(arr2[1]);
	       	int endIndex    = Integer.parseInt(arr2[1].split(" ")[1]);
	       	double endValue = Double.parseDouble(arr2[1].split(" ")[2]);
	       	
	       	System.out.println(ID+"%%"+flightControllerIndex+"%%"+eventIndex+"%%"+endIndex+"%%"+endValue+"%%");


	}

}
