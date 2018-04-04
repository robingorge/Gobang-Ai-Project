import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;


class Analysis{
	public int who;//0 is ai, 1 is enemy
	public Point set;
	public Point assume;
	public ArrayList<Point> aastep;
	public ArrayList<Point> eestep;
	public Hashtable<Point,Integer> htable;


	public Analysis(ArrayList<Point> a,ArrayList<Point> b,int c,Point d,Point e){
		who=c;
		aastep=new ArrayList<Point>(a);
		eestep=new ArrayList<Point>(b);


	}
	







}