import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;

class Game{
	public char aic;//my color
	public char eic;//enemy color
	public int ssize;

	public ArrayList<Point> astep;
	public ArrayList<Point> estep;

	public Hashtable<Point,Integer> tt;

	public Game(){
		ssize=11;
		aic='L';
		eic='D';

		astep=new ArrayList<Point>();
		estep=new ArrayList<Point>();
		tt=new Hashtable<Point,Integer>();

	}

	public Game(int aicolor,int size){
		ssize=size;

		if (aicolor==1){
			aic='L';
			eic='D';
		}
		else{
			eic='L';
			aic='D';
		}

		astep=new ArrayList<Point>();
		estep=new ArrayList<Point>();
		tt=new Hashtable<Point,Integer>();
	}


	static public Point stringprocess(String go){
		char a=go.charAt(0);
		int aint= (int) a;
		aint=aint-96;
		String b=go.substring(1);
		int bint=Integer.parseInt(b);

		Point newone=new Point(aint,bint);
		
		return newone;
	}

	static public String pointprocess(Point go){
		int x=(int) go.getX();
		x=x+96;
		String xchar=Character.toString((char)x);
		int y=(int) go.getY();
		String ystr=String.valueOf(y);

		String xy=xchar+ystr;

		return xy;
	}


	//e 1
	public void emove(String thisstep){
		Point newstep=stringprocess(thisstep);
		//System.out.println("newwwwww"+newstep);
		estep.add(newstep);

		if (iswinner(1,newstep)){
				print();
				System.out.println("Move played: "+thisstep);
				System.out.println("Game over\n");
				if (eic=='L'){
					System.out.println("Light player (human) wins!");
					System.exit(0);
				}
				else{
					System.out.println("Dark player (human) wins!");
					System.exit(0);
				}
		}
	}


	//a 0
	public String amove(){
		Point nn=new Point();
		boolean find=false;


		if ((astep.size()==0)&&(estep.size()==0)){
			int x=ssize/2;
			int y=ssize/4;
			// int x= eee -2 + (int)(Math.random() * (eee+sep));
			// int y= eee -2 + (int)(Math.random() * (eee+sep));

			nn=new Point(x,y);
			astep.add(nn);
		}
		else {
			tt=new Hashtable<Point,Integer>();
			nn=minmax();
			if(nn!=null){
				astep.add(nn);
			}
		}
		



		if (nn==null){
			while (!find){
				int x= 1 + (int)(Math.random() * ssize);
				int y= 1 + (int)(Math.random() * ssize);
			
				nn=new Point(x,y);
				if (notexist(nn)){
					find=true;
					astep.add(nn);
				}
			}
		}

		if(nn!=null){
			if (iswinner(0,nn)){
				print();
				System.out.println("Move played: "+pointprocess(nn));
				System.out.println("Game over\n");
				if (aic=='L'){
					System.out.println("Light player (COM) wins!");
					System.exit(0);
				}
				else{
					System.out.println("Dark player (COM) wins!");
					System.exit(0);
				}
			}
			String aaa=pointprocess(nn);
			return aaa;
		}
		else{
			System.out.println("tie");
			System.exit(0);
		}

		return null;
	}


	public Point minmax(){
		// int astepsize=astep.size();
		// Hashtable<Point, Integer> amax=evaluate(astep.get(astepsize-1));
		// Point a=findmaxinhash(amax);

		int estepsize=estep.size();
		evaluate(estep.get(estepsize-1));
		int astepsize=astep.size();
		if (astepsize>0){
			evaluate(astep.get(astepsize-1));
		}
		//System.out.println(tt);
		processvarious();
		//System.out.println(tt);




		Point b=findmaxinhash(tt);

		return b;
	}

//System.out.println("123");

	public void evaluate(Point one){
		int x=(int)one.getX();
		int y=(int)one.getY();
		//x-4~x+4 y-4~y+4
		for (int i=x-4;i<=x+4;i++){
			for (int j=y-4;j<=y+4;j++){
				Point newone=new Point(i,j);
				if ((notout(newone))&&(notexist(newone))){
					
					//assume ai set on i,j
					astep.add(newone);
					int minpay=10000;

					for (int m=x-4;m<=x+4;m++){
						for (int n=y-4;n<=y+4;n++){
							Point leaf=new Point(m,n);
							if ((notout(leaf))&&(notexist(leaf))){

								//assume enemy set on this
								estep.add(leaf);
								//System.out.println("123");
								int payy=payoff(newone);
								//System.out.println(payy+"   "+minpay);
								//System.out.println(newone);
								//System.out.println(payy);
								if (payy<minpay){
									if (newone!=null){
										minpay=payy;
										Point some=newone;
										some=intt(newone);
										if (some!=null){
											tt.put(some,payy);
										}
										else{
											tt.put(newone,payy);
										}
									}
								}
								estep.remove(leaf);


							}
						}
					}

					astep.remove(newone);
				}

			}
		}



	}

	private Point findmaxinhash(Hashtable<Point, Integer> temp){
		Point maxkey=null;
		int maxvalue=-1000;

		Set<Point> keys = temp.keySet();
        for(Point key: keys){
        	if (temp.get(key)>maxvalue){
        		maxkey=key;
        		maxvalue=temp.get(maxkey);
        	}
   		}

   		return maxkey;


	}



	//caculate payoff of that point
	public int payoff(Point thisone){
		int x=(int)thisone.getX();
		int y=(int)thisone.getY();

		int pay=0;

		int aicc=0;
		int humcc=0;		
		int ac=0;
		int hc=0;



		for(int row=-4;row<5;row++){
			for(int i=-4;i<5;i++){
				Point dt1=new Point(x+i,y+row);
				if (notout(dt1)){
					if (!(existe(dt1))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt1))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}
			}

			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;
		}



		for (int column=-4;column<5;column++){
			for(int i=-4;i<5;i++){
				Point dt2=new Point(x+column,y+i);
				if (notout(dt2)){
					if (!(existe(dt2))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt2))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}
			}
			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;			
		}


		//left half from upleft to downright
		for (int incline=5;incline<=9;incline++){
			for (int i=0;i<incline;i++){
				Point dt3=new Point(x-4+i,y-5+incline-i);
				if (notout(dt3)){
					if (!(existe(dt3))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt3))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}

			}
			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;
		}

		for (int incline=5;incline<=9;incline++){
			for (int i=0;i<incline;i++){
				Point dt3=new Point(x+4-i,y-5+incline-i);
				if (notout(dt3)){
					if (!(existe(dt3))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt3))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}

			}
			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;
		}





		for (int incline=8;incline>=5;incline--){
			for (int i=0;i<incline;i++){
				Point dt3=new Point(x-incline+5+i,y+4-i);
				if (notout(dt3)){
					if (!(existe(dt3))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt3))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}

			}
			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;
		}



		for (int incline=8;incline>=5;incline--){
			for (int i=0;i<incline;i++){
				Point dt3=new Point(x+incline-5-i,y+4-i);
				if (notout(dt3)){
					if (!(existe(dt3))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
							ac=0;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt3))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
							hc=0;
						}
						else{
							hc=0;
						}
					}


				}

			}
			if (ac>=5){
				aicc=aicc+ac-4;
			}
			if (hc>=5){
				humcc=humcc+hc-4;
			}
			ac=0;
			hc=0;
		}


		pay=aicc-humcc;
		return pay;


	}

	//process some case like 3 link, 4 link, 2 link
	public void processvarious(){
		int maxs=ssize-4;
		int x=0;
		int y=0;

		for (int j=1;j<=ssize;j++){
			int ac=0;
			for (int i=1;i<=ssize;i++){
				Point a=new Point(i,j);
				while (notout(a)&&exista(a)){
					ac++;
					i++;
					a=new Point(i,j);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(i-1-ac,j);
					processhelper(1,ac,a);					
					processhelper(1,ac,ano);
				}

				ac=0;
			}

			int hc=0;
			for (int i=1;i<=ssize;i++){
				Point h=new Point(i,j);
				while (notout(h)&&existe(h)){
					hc++;
					i++;
					h=new Point(i,j);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(i-1-hc,j);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;


			}
		}




		for (int i=1;i<=ssize;i++){
			int ac=0;
			for (int j=1;j<=ssize;j++){
				Point a=new Point(i,j);
				while (notout(a)&&exista(a)){
					ac++;
					j++;
					a=new Point(i,j);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(i,j-1-ac);
					processhelper(1,ac,a);					
					processhelper(1,ac,ano);
				}

				ac=0;
			}

			int hc=0;
			for (int j=1;j<=ssize;j++){
				Point h=new Point(i,j);
				while (notout(h)&&existe(h)){
					hc++;
					j++;
					h=new Point(i,j);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(i,j-1-hc);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;


			}
		}


		for (int incline=5;incline<=ssize;incline++){
			int ac=0;
			for (int i=0;i<incline;i++){
				Point a=new Point(i+1,incline-i);
				while (notout(a)&&exista(a)){
					ac++;
					i++;
					a=new Point(i+1,incline-i);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(i+1-1-ac,incline-i+1+ac);
					processhelper(1,ac,a);
					processhelper(1,ac,ano);
				}

				ac=0;
			}



			int hc=0;
			for (int i=0;i<incline;i++){
				Point h=new Point(i+1,incline-i);
				while (notout(h)&&existe(h)){
					hc++;
					i++;
					h=new Point(i+1,incline-i);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(i+1-1-hc,incline-i+1+hc);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;
			}

		}



		for (int incline=5;incline<=ssize;incline++){
			int ac=0;
			for (int i=0;i<incline;i++){
				Point a=new Point(ssize-i,incline-i);
				while (notout(a)&&exista(a)){
					ac++;
					i++;
					a=new Point(ssize-i,incline-i);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(ssize-i+1+ac,incline-i+1+ac);
					processhelper(1,ac,a);
					processhelper(1,ac,ano);
				}

				ac=0;
			}



			int hc=0;
			for (int i=0;i<incline;i++){
				Point h=new Point(ssize-i,incline-i);
				while (notout(h)&&existe(h)){
					hc++;
					i++;
					h=new Point(ssize-i,incline-i);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(ssize-i+1+hc,incline-i+1+hc);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;
			}

		}

		//左上到右下 右上部分
		for (int incline=ssize-1;incline>=5;incline--){
			int ac=0;
			for (int i=0;i<incline;i++){
				Point a=new Point(ssize-incline+1+i,ssize-i);
				while (notout(a)&&exista(a)){
					ac++;
					i++;
					a=new Point(ssize-incline+1+i,ssize-i);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(ssize-incline+1+i-1-ac,ssize-i+1+ac);
					processhelper(1,ac,a);
					processhelper(1,ac,ano);
				}

				ac=0;
			}



			int hc=0;
			for (int i=0;i<incline;i++){
				Point h=new Point(ssize-incline+1+i,ssize-i);
				while (notout(h)&&existe(h)){
					hc++;
					i++;
					h=new Point(ssize-incline+1+i,ssize-i);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(ssize-incline+1+i-1-hc,ssize-i+1+hc);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;
			}

		}


		//右上到左下，左上部分
		for (int incline=ssize-1;incline>=5;incline--){
			int ac=0;
			for (int i=0;i<incline;i++){
				Point a=new Point(incline-i,ssize-i);
				while (notout(a)&&exista(a)){
					ac++;
					i++;
					a=new Point(incline-i,ssize-i);
				}

				if ((ac>=1)&&(ac<=4)){
					Point ano=new Point(incline-i+1+ac,ssize-i+1+ac);
					processhelper(1,ac,a);
					processhelper(1,ac,ano);
				}

				ac=0;
			}



			int hc=0;
			for (int i=0;i<incline;i++){
				Point h=new Point(incline-i,ssize-i);
				while (notout(h)&&existe(h)){
					hc++;
					i++;
					h=new Point(incline-i,ssize-i);
				}

				if ((hc>=1)&&(hc<=4)){
					Point hno=new Point(incline-i+1+hc,ssize-i+1+hc);
					processhelper(2,hc,h);					
					processhelper(2,hc,hno);
				}
				
				hc=0;
			}

		}






	}


	//2 enemy 1 ai
	public void processhelper(int who,int ccase,Point b){
		if (notexist(b)&&notout(b)){

		int bvalue=payoff(b);
		Point a=b;
		if (intt(b)!=null){
			a=intt(b);
			bvalue=tt.get(a);
			//System.out.println(bvalue);
		}

		switch(ccase){
			case 1:
				bvalue=bvalue+15;
				break;
			case 2:
				bvalue=bvalue+20;
				break;
			case 3:
				bvalue=bvalue+100*who;
				break;
			case 4:
				bvalue=bvalue+200*who;
				break;
		}
		tt.put(a,bvalue);
		//System.out.println("processhelper"+a+"   "+bvalue);///////////////
	}

	}

	public Point intt(Point a){
		Set<Point> keys = tt.keySet();
        for(Point key: keys){
        	if (a.equals(key)){
        		return key;
        	}
   		}
   		return null;

	}





	//point is not out of range
	public boolean notout(Point a){
		int x=(int)a.getX();
		int y=(int)a.getY();

		if ((x>=1)&&(x<=ssize)&&(y>=1)&&(y<=ssize)){
			return true;
		}
		return false;
	}





	//check if there is a winner
	//who==1 enemy  //who==0 ai
	public boolean iswinner(int who, Point step){
		int cc=0;

		if (who==1){
			int x=(int)step.getX();
			int y=(int)step.getY();

			for(int i=-4;i<5;i++){
				Point dt1=new Point(x+i,y);
				if (notout(dt1)&&existe(dt1)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}




			for(int i=-4;i<5;i++){
				Point dt2=new Point(x,y+i);
				if (existe(dt2)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			
			}


			for(int i=-4;i<5;i++){
				Point dt3=new Point(x+i,y-i);
				if (existe(dt3)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}


			for(int i=-4;i<5;i++){

				Point dt4=new Point(x+i,y+i);
				if (existe(dt4)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}		


		}
		else{

			int x=(int)step.getX();
			int y=(int)step.getY();

			for(int i=-4;i<5;i++){
				Point dt1=new Point(x+i,y);
				if (exista(dt1)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}




			for(int i=-4;i<5;i++){
				Point dt2=new Point(x,y+i);
				if (exista(dt2)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}


			for(int i=-4;i<5;i++){

				Point dt3=new Point(x+i,y-i);
				if (exista(dt3)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}
			}


			for(int i=-4;i<5;i++){
				Point dt4=new Point(x+i,y+i);
				if (exista(dt4)){
					cc++;
				}
				else{
					if(cc==5){
						return true;
					}
					else{
						cc=0;
					}
				}	
			}

		}

		return false;


	}




	//check in range
	public boolean check(Point ppp){
		int x=(int) ppp.getX();
		int y=(int) ppp.getY();

		if ((x>=1)&&(y>=1)&&(x<=ssize)&&(y<=ssize)){
			return true;
		}

		return false;
	}



	//detect exist help function

	public boolean notexist(Point thisone){
		if (astep.size()!=0){
		for (Point m:astep){
			if (m.equals(thisone)){
				return false;
			}
		}
		}

		if (estep.size()!=0){
		for (Point n:estep){
			if (n.equals(thisone)){
				return false;
			}

		}
		}
		return true;
	}


	public void print(){
		String firstline="   ";
		for (int ff=0;ff<ssize;ff++){
  			firstline+="  ";
  			int num=ff+97;
  			firstline+=Character.toString ((char) num);
  			firstline+=" ";
 		}
		System.out.println(firstline);


		String sep="   +";
		for (int pp=0;pp<ssize;pp++){
			sep=sep+"---+";
		}
		System.out.println(sep);
		int index=0;

		for (int i=0;i<ssize;i++){
			int yint=i+1;
			String line=" "+yint+" |";
			if (yint>=10){
				line=yint+" |";
			}

			for(int pp=0;pp<ssize;pp++){
				line=line+"   |";
			}

			char[] myline = line.toCharArray();

			double y=(double) yint;
			
			for (Point m: astep){
				if (m.getY()==y){
					int thisx=(int)m.getX();
					index=4+4*thisx-3;
					myline[index] = aic;
				}

			}
			for (Point n: estep){
				if (n.getY()==y){
					int thisx=(int)n.getX();
					index=4+4*thisx-3;
					myline[index] = eic;
				}
			}
			line = String.valueOf(myline);
			System.out.println(line);
			System.out.println(sep);
		}

	}



	private boolean exista(Point thisone){
		for (Point m:astep){
			if (m.equals(thisone)){
				return true;
			}
		}

		return false;

	}

	private boolean existe(Point thisone){
		for (Point n:estep){
			if (n.equals(thisone)){
				return true;
			}

		}
		return false;
	}
	



}