import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class Kmean {
	public static void main (String[] args) throws IOException
	{
		CSVReader reader = new CSVReader(new FileReader("src/data.csv"),';','"');
		String [] nextLine;
		point[] Arr = new point[1200];
		int i = 0;
		while ((nextLine = reader.readNext()) != null) {
			//System.out.println("X:" + nextLine[0] + " Y:" + nextLine[1] );
			Arr[i] = new point();
			Arr[i].x = Integer.parseInt(nextLine[0]);
			Arr[i++].y = Integer.parseInt(nextLine[1]);
		}

		int numofclusters = 4;
		cluster[] c = new cluster[numofclusters];
		c = kmeans(numofclusters,Arr);
		
		for(i = 0; i < c.length; i++)
		{
			System.out.println("Cluster"+i+":");
			System.out.println(c[i].center.x);
			System.out.println(c[i].center.y);
			System.out.print('\n');
		}
	}
	
	static cluster[] kmeans(int num,point[] Arr)
	{
		//initial clusters
		cluster[] c = new cluster[num];
		for(int i=0;i<num;i++)
		{
			c[i] = new cluster();
			c[i].center.x = Arr[i * Arr.length/num].x;
			c[i].center.y = Arr[i * Arr.length/num].y;
		}
		for(int i=0;i<1000;i++)
			distribution(c,Arr);	
		
		return c;
	}
	
	static void distribution(cluster[] c, point[] Arr)
	{
		for(int i = 0; i< c.length;i++)
		{
			c[i].p = new point[1200];
			c[i].numofpoint = 0;
		}
		for(int m = 0;m<Arr.length;m++)
		{
			double min = 1000;
			int i=-1;
			double dis = -1;
			for(int n=0;n<c.length;n++)
			{
				dis = getDis(Arr[m].x,Arr[m].y,c[n].center.x,c[n].center.y);
				if(min >= dis)
				{
					min = dis;
					i = n;		
				}
			}
			
			Arr[m].c = i;//set center
		}
		
		for(int i=0;i<Arr.length;i++)
		{
			int cc = Arr[i].c;
			 
			c[cc].p[c[cc].numofpoint++]	= Arr[i];
			//System.out.println(c[cc].numofpoint);
		}
		
		for(int i=0;i<c.length;i++)
			c[i].getcenter();
	}
	
	static double getDis(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
}

class point
{
	double x;
	double y;
	int c;
}

class cluster
{
	int numofpoint;
	point center;
	point[] p;
	
	public cluster()
	{
		this.numofpoint = 0;
		this.center = new point();
		this.p = new point[1200];
	}
	
	public cluster(point[] p)
	{
		this.center = new point();
		this.p = p;
	}
	
	public void getcenter()
	{
		int i=0;
		while(this.p[i] != null)
		{
			this.center.x += this.p[i].x;
			this.center.y += this.p[i].y;
			i++;
		}
		this.center.x /= i;
		this.center.y /= i;
	}
}