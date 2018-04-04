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


	public Game(){
		ssize=11;
		aic='L';
		eic='D';

		astep=new ArrayList<Point>();
		estep=new ArrayList<Point>();

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
		estep.add(newstep);

		if (iswinner(1,newstep)){
			System.out.println("you win");
			System.exit(0);
		}
	}


	//a 0
	public String amove(){
		Point nn=new Point();
		boolean find=false;
		// // if ((astep.size()==0)&&(estep.size()==0)){
		// // 	int eee=ssize/2;
		// // 	int sep=ssize/4;
		// // 	int x= eee -2 + (int)(Math.random() * (eee+sep));
		// // 	int y= eee -2 + (int)(Math.random() * (eee+sep));

		// // 	nn=new Point(x,y);
		// // 	if (notexist(nn)){
		// // 		find=true;
		// // 		astep.add(nn);
		// // 	}
		// // }
		// if ((astep.size()==0)&&(estep.size()>0)){
		// 	int estepsize=estep.size();
		// 	Hashtable<Point, Integer> emax=evaluate(estep.get(estepsize-1));
		// 	nn=findmaxinhash(emax);

		// 	astep.add(nn);
		// }
		// else {
		// 	nn=minmax();
		// 	astep.add(nn);
		// }
		



		// if (nn==null){
			while (!find){
				int x= 1 + (int)(Math.random() * ssize);
				int y= 1 + (int)(Math.random() * ssize);
			
				nn=new Point(x,y);
				if (notexist(nn)){
					find=true;
					astep.add(nn);
				}
			}
		//}

		if(nn!=null){
			if (iswinner(0,nn)){
				System.out.println("com win");
				System.exit(0);
			}
		}
		String aaa=pointprocess(nn);
		return aaa;


	}


	public Point minmax(){
		int astepsize=astep.size();
		Hashtable<Point, Integer> amax=evaluate(astep.get(astepsize-1));
		Point a=findmaxinhash(amax);

		int estepsize=estep.size();
		Hashtable<Point, Integer> emax=evaluate(estep.get(estepsize-1));
		Point b=findmaxinhash(emax);


		//if (amax.get(a)>emax.get(b)){
		//	return a;
		//}
		return b;
	}


	public Hashtable<Point, Integer> evaluate(Point one){
		Hashtable<Point,Integer> tt=new Hashtable<Point,Integer>();//top node


		int x=(int)one.getX();
		int y=(int)one.getY();
		//x-4~x+4 y-4~y+4

		for (int i=x-4;i<=x+4;i++){
			for (int j=y-4;j<=y+4;j++){
				Point newone=new Point(i,j);
				if ((i>=1)&&(i<=ssize)&&(j>=1)&&(j<=ssize)&&(notexist(newone))){
					
					//assume ai set on i,j
					astep.add(newone);
					int minpay=10000;
					for (int m=x-4;i<=x+4;i++){
						for (int n=y-4;j<=y+4;j++){
							Point leaf=new Point(m,n);
							if ((i>=1)&&(i<=ssize)&&(j>=1)&&(j<=ssize)&&(notexist(leaf))){
								estep.add(leaf);
								int payy=payoff(one);
								if (payy<minpay){
									minpay=payy;
									tt.put(newone,payy);
								}
								estep.remove(estep.indexOf(leaf));


							}
						}
					}

					astep.remove(astep.indexOf(newone));
				}

			}
		}


		return tt;



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

			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y>=1)&&(y<=ssize)){
					Point dt1=new Point(x+i,y);
					if (!(existe(dt1))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
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



			for(int i=-4;i<5;i++){
				if ((x>=1)&&(x<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt2=new Point(x,y+i);
					if (!(existe(dt2))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
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




			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y-i>=1)&&(y-i<=ssize)){
					Point dt3=new Point(x+i,y-i);
					if (!(existe(dt3))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
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



			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt4=new Point(x+i,y+i);
					if (!(existe(dt4))){
						ac++;
					}
					else{
						if (ac>=5){
							aicc=aicc+ac-4;
						}
						else{
							ac=0;
						}
					}

					if (!(exista(dt4))){
						hc++;
					}
					else{
						if (hc>=5){
							humcc=humcc+hc-4;
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


		pay=aicc-humcc;
		return pay;


	}








	//check if there is a winner
	//who==1 enemy  //who==0 ai
	public boolean iswinner(int who, Point step){
		int cc=0;

		if (who==1){
			int x=(int)step.getX();
			int y=(int)step.getY();

			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y>=1)&&(y<=ssize)){
					Point dt1=new Point(x+i,y);
					if (existe(dt1)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}



			for(int i=-4;i<5;i++){
				if ((x>=1)&&(x<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt2=new Point(x,y+i);
					if (existe(dt2)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}


			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y-i>=1)&&(y-i<=ssize)){
					Point dt3=new Point(x+i,y-i);
					if (existe(dt3)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}


			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt4=new Point(x+i,y+i);
					if (existe(dt4)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}		


		}
		else{

			int x=(int)step.getX();
			int y=(int)step.getY();

			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y>=1)&&(y<=ssize)){
					Point dt1=new Point(x+i,y);
					if (exista(dt1)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}



			for(int i=-4;i<5;i++){
				if ((x>=1)&&(x<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt2=new Point(x,y+i);
					if (exista(dt2)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}


			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y-i>=1)&&(y-i<=ssize)){
					Point dt3=new Point(x+i,y-i);
					if (exista(dt3)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
			}


			for(int i=-4;i<5;i++){
				if ((x+i>=1)&&(x+i<=ssize)&&(y+i>=1)&&(y+i<=ssize)){
					Point dt4=new Point(x+i,y+i);
					if (exista(dt4)){
						cc++;
					}
				}
			}
			if(cc==5){
				return true;
			}
			else{
				cc=0;
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