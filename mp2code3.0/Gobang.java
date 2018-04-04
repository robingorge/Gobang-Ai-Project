import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;


class Gobang{

    public static void main(String[] args){
    	int size=11;//default size=11
    	int ai=1;//0 stands for dark, default human player color is dark




    	if (args.length==0){
    	}
    	else if (args.length==1){
    		if (args[0].equals("-l")){
    			ai=0;
    		}
    		else{
    			System.out.println("error setting");
    			System.exit(0);
    		}

    	}
    	else if(args.length==2){
    		if (args[0].equals("-n")){
    			int newsize=Integer.parseInt(args[1]);
    			if ((newsize>=5)&&(newsize<=26)){
    				size=newsize;
    			}
    			else{
    				System.out.println("error size setting");
    				System.exit(0);
    			}
    		}
    		else{
    			System.out.println("error setting");
    			System.exit(0);
    		}

    	}
    	else if(args.length==3){
    		if ((args[0].equals("-l"))||(args[2].equals("-l"))){
    			ai=0;
			}
			else{
    			System.out.println("error size setting");
    			System.exit(0);
    		}

    		if(args[0].equals("-n")){
    			int newsize=Integer.parseInt(args[1]);
    			if ((newsize>=5)&&(newsize<=26)){
    				size=newsize;
    			}
    			else{
    				System.out.println("error size setting");
    				System.exit(0);
    			}
    		}
    		else if(args[1].equals("-n")){
    			int newsize=Integer.parseInt(args[2]);
    			if ((newsize>=5)&&(newsize<=26)){
    				size=newsize;
    			}
    			else{
    				System.out.println("error size setting");
    				System.exit(0);
    			}
    		}
    		else{
    			System.out.println("error size setting");
    			System.exit(0);
    		}


    	}


    	//debug
    	System.out.println("size: "+size);
    	//System.out.println("ai color "+ai);

    	Game a=new Game(ai,size);
    	
    	//test for the board print
    	//a.print();

    	//helper function debug
    	//Point apoint=Game.stringprocess("b2");
    	//System.out.println(apoint);
    	// Point apoint=new Point(3,5);
    	// String apstring=Game.pointprocess(apoint);
    	// System.out.println(apstring);
    	
    	String complay="";
    	String humplay="";
    	boolean me=false;

    	if (ai==1){
    		me=false;
    		complay="Light player (COM) plays now\nLight player (COM) is calculating its next move... (this might take up to 30 seconds)";
    		humplay="Dark player (human) plays now";
    	}
    	else{
    		me=true;
    		complay="Dark player (COM) plays now\nDark player (COM) is calculating its next move... (this might take up to 30 seconds)";
    		humplay="Light player (human) plays now";
    	}

    	a.print();
    	System.out.println("Move played: --");


    	boolean run=true;
    	while (run){


        	if (me){
				System.out.println(complay); 
				String mymmove=a.amove();
				a.print();
				System.out.println("Move played: "+mymmove);
				me=false;       		
        	}
        	else{
        		System.out.println(humplay);
        		try {
    	        	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	            	System.out.print(">");
            		String movestep = reader.readLine();


            		Point testone=Game.stringprocess(movestep);
            		if (a.check(testone)){


            		if(a.notexist(testone)){
            			a.emove(movestep);
        				a.print();
        				System.out.println("Move played: "+movestep);
        				me=true;
        			}
        			else{
        				System.out.println("This move has been set: "+movestep);
        			}

        			}
        			else{
        				System.out.println("This move is out of range");
        			}
    	    	} catch (IOException ioe) {
	   	         	ioe.printStackTrace();
	        	}


        	}









    	}
      

        //Move played: --\nDark player (human) plays now  
    	//Move played: a1\nLight player (COM) plays now
    }




}